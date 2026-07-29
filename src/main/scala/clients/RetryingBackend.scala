package clients

import cats.effect.Temporal
import cats.syntax.all.*
import configs.RetryConfig
import sttp.capabilities.Effect
import sttp.client4.Backend
import sttp.client4.GenericRequest
import sttp.client4.Response
import sttp.monad.MonadError as SttpMonadError

import java.time.Instant
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.MILLISECONDS
import scala.util.Random

/** Wraps a [[Backend]] with retries for the two failure modes Hetzner's API documents as transient:
  * `429 rate_limit_exceeded` and `5xx` server errors. Also retries on exceptions raised before any response is received
  * (connection refused, timeout, etc.) under the same budget. Everything else -- 4xx client errors -- is never retried,
  * since retrying a malformed request just repeats the same failure.
  */
final class RetryingBackend[F[_]](delegate: Backend[F], config: RetryConfig)(using
    F: Temporal[F]
) extends Backend[F] {

  def monad: SttpMonadError[F] = delegate.monad

  def close(): F[Unit] = delegate.close()

  def send[T](request: GenericRequest[T, Any & Effect[F]]): F[Response[T]] =
    attempt(request, attemptNo = 1)

  private def attempt[T](
      request: GenericRequest[T, Any & Effect[F]],
      attemptNo: Int
  ): F[Response[T]] =
    delegate.send(request).attempt.flatMap {
      case Right(response) if isRetryable(response.code.code) && attemptNo < config.maxAttempts =>
        F.sleep(delayFor(attemptNo, response.header("RateLimit-Reset"))) *>
          attempt(request, attemptNo + 1)
      case Right(response) =>
        F.pure(response)
      case Left(throwable) if attemptNo < config.maxAttempts =>
        F.sleep(delayFor(attemptNo, None)) *> attempt(request, attemptNo + 1)
      case Left(throwable) =>
        F.raiseError(throwable)
    }

  private def isRetryable(statusCode: Int): Boolean =
    statusCode == 429 || statusCode >= 500

  private def delayFor(attemptNo: Int, rateLimitReset: Option[String]): FiniteDuration =
    rateLimitReset
      .flatMap(_.toLongOption)
      .map { resetEpochSeconds =>
        val secondsLeft = resetEpochSeconds - Instant.now().getEpochSecond
        if (secondsLeft > 0) FiniteDuration(secondsLeft, scala.concurrent.duration.SECONDS)
        else exponentialBackoff(attemptNo)
      }
      .getOrElse(exponentialBackoff(attemptNo))

  /** Exponential backoff with +/-25% jitter, capped at `config.maxBackoff`. Computed in raw milliseconds throughout to
    * avoid FiniteDuration's `Duration`-returning arithmetic ops, whose result type doesn't statically guarantee
    * finiteness.
    */
  private def exponentialBackoff(attemptNo: Int): FiniteDuration = {
    val shift = math.min(attemptNo - 1, 20) // cap to keep 1L << shift from overflowing
    val rawMillis = config.initialBackoff.toMillis * (1L << shift)
    val cappedMillis = math.min(rawMillis, config.maxBackoff.toMillis)
    val jitterBound = math.min(cappedMillis / 4 + 1, Int.MaxValue.toLong).toInt
    val jitterMillis = Random.nextInt(jitterBound).toLong
    FiniteDuration(cappedMillis + jitterMillis, MILLISECONDS)
  }

}

object RetryingBackend {
  def apply[F[_]: Temporal](delegate: Backend[F], config: RetryConfig): RetryingBackend[F] =
    new RetryingBackend(delegate, config)
}
