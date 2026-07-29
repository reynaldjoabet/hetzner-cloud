package clients

import cats.effect.IO
import cats.effect.Resource
import configs.HetznerConfig
import hcloud.api.Actions
import hcloud.models.Action
import sttp.client4.Backend
import sttp.client4.Request
import sttp.client4.ResponseException
import sttp.client4.http4s.Http4sBackend

/** The production entry point for talking to the Hetzner Cloud API: owns a connection-pooled HTTP backend (wrapped with
  * [[RetryingBackend]]) and the configured bearer token, and exposes [[send]] to run any generated `hcloud.api.*`
  * request through both, mapping failures onto [[HetznerError]] instead of a raw `ResponseException`.
  *
  * Deliberately does not wrap every generated `hcloud.api.*` class -- those are already fine entry points
  * (`Actions().withBearerTokenAuth(client.token)`). What was missing was the plumbing around them: a real backend,
  * retries, error mapping, and action-awaiting, which is what this provides.
  */
final class HetznerClient private (config: HetznerConfig, backend: Backend[IO]) {

  val token: String = config.token
  val baseUrl: String = config.baseUrl

  /** Runs a generated hcloud request, mapping a `Left(ResponseException)` onto [[HetznerError]] and a failure that
    * occurred before any response was received (connection failure, etc.) onto [[HetznerError.Network]].
    */
  def send[A](request: Request[Either[ResponseException[String], A]]): IO[Either[HetznerError, A]] =
    IO.defer(request.send(backend)).attempt.flatMap {
      case Right(response) =>
        response.body match {
          case Right(value) => IO.pure(Right(value))
          case Left(e)      =>
            val mapped = HetznerError.fromResponseException(e)
            IO(scribe.warn(s"Hetzner API request failed: $mapped")).as(Left(mapped))
        }
      case Left(throwable) =>
        IO(scribe.error("Hetzner API request errored before a response was received", throwable))
          .as(Left(HetznerError.Network(throwable)))
    }

  /** Polls Action `actionId` until it reaches `success` or `error` (or [[configs.ActionPollConfig.timeout]] elapses),
    * per [[configs.HetznerConfig.actionPoll]]. Needed because Hetzner's mutating endpoints return a `running` Action
    * immediately -- the resource they describe isn't actually ready until this resolves to `Right`.
    */
  def awaitAction(actionId: Long): IO[Either[HetznerError, Action]] =
    ActionPoller.poll(config.actionPoll) { id =>
      send(Actions(baseUrl).withBearerTokenAuth(token).getAction(id)).map(_.map(_.action))
    }(actionId)

}

object HetznerClient {

  /** Builds a client with a connection-pooled Ember backend, retries applied. A `Resource` because the underlying HTTP
    * connection pool must be shut down on release -- a client is not meant to be constructed per-request.
    */
  def resource(config: HetznerConfig): Resource[IO, HetznerClient] =
    Http4sBackend
      .usingDefaultEmberClientBuilder[IO]()
      .map(ember => new HetznerClient(config, RetryingBackend(ember, config.retry)))

}
