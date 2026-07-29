package clients

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.CodecMakerConfig
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import sttp.client4.ResponseException

import java.time.Instant
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.SECONDS
import scala.util.Try
import scala.util.control.NoStackTrace

/** Domain error raised by [[HetznerClient]]. Every case is a translation of either a non-2xx HTTP response (see
  * [[HetznerError.fromResponseException]]) or a failure that occurred before any response was received.
  *
  * Extends `RuntimeException` (rather than being a plain ADT) so callers who want exception semantics can
  * `.liftTo[F]`/`.rethrow` it; mixes in `NoStackTrace` since these are expected, frequent control-flow outcomes (a 404
  * while polling, a transient 429) and capturing a JVM stack trace for each one would be pure overhead.
  */
sealed trait HetznerError extends RuntimeException with NoStackTrace {
  def message: String
  override def getMessage: String = message
}

object HetznerError {

  final case class Unauthorized(message: String) extends HetznerError
  final case class Forbidden(message: String) extends HetznerError
  final case class NotFound(message: String) extends HetznerError
  final case class Conflict(message: String) extends HetznerError
  final case class Locked(message: String) extends HetznerError
  final case class ValidationError(code: String, message: String) extends HetznerError
  final case class RateLimited(
      retryAfter: Option[FiniteDuration],
      message: String
  ) extends HetznerError
  final case class ServerError(status: Int, message: String) extends HetznerError
  final case class Unexpected(status: Int, message: String) extends HetznerError
  final case class Decoding(message: String, cause: Throwable) extends HetznerError
  final case class Network(cause: Throwable) extends HetznerError {
    def message: String =
      s"Network error while calling the Hetzner API: ${cause.getMessage}"
  }
  final case class ActionFailed(actionId: Long, code: String, message: String) extends HetznerError
  final case class ActionTimedOut(actionId: Long, message: String) extends HetznerError

  /** Maps an sttp `ResponseException` -- the `Left` side of every generated hcloud client call -- onto a stable domain
    * error, keyed off the HTTP status code per Hetzner's documented error codes. The response body is
    * `{"error": {"code": ..., "message": ...}}` on every endpoint, but each generated operation has its own
    * nominally-distinct 4xx/5xx response type for that same shape, so rather than depend on ~40 near-identical
    * generated types this decodes the shared envelope directly.
    */
  def fromResponseException(e: ResponseException[String]): HetznerError =
    e match {
      case ResponseException.UnexpectedStatusCode(body, meta) =>
        val parsed = parseErrorBody(body)
        val code = parsed.map(_.code).getOrElse("unknown")
        val msg = parsed.map(_.message).getOrElse(body.take(500))
        meta.code.code match {
          case 401                     => Unauthorized(msg)
          case 403                     => Forbidden(msg)
          case 404                     => NotFound(msg)
          case 409                     => Conflict(msg)
          case 422                     => ValidationError(code, msg)
          case 423                     => Locked(msg)
          case 429                     => RateLimited(parseRateLimitReset(meta.header("RateLimit-Reset")), msg)
          case status if status >= 500 => ServerError(status, msg)
          case status                  => Unexpected(status, msg)
        }
      case ResponseException.DeserializationException(body, cause, _) =>
        Decoding(s"Failed to decode Hetzner API response: ${cause.getMessage}", cause)
    }

  private case class ErrorEnvelope(error: ErrorBody)
  private case class ErrorBody(code: String, message: String)
  private given errorEnvelopeCodec: JsonValueCodec[ErrorEnvelope] =
    JsonCodecMaker.make(CodecMakerConfig.withFieldNameMapper(JsonCodecMaker.enforceCamelCase))

  private def parseErrorBody(body: String): Option[ErrorBody] =
    Try(readFromString[ErrorEnvelope](body)).toOption.map(_.error)

  /** `RateLimit-Reset` is a Unix timestamp (seconds) of when the limit recovers; convert it to a relative delay,
    * clamped to non-negative since the header can already be in the past by the time we read it.
    */
  private def parseRateLimitReset(header: Option[String]): Option[FiniteDuration] =
    header.flatMap(_.toLongOption).map { resetEpochSeconds =>
      val secondsLeft = resetEpochSeconds - Instant.now().getEpochSecond
      FiniteDuration(math.max(0L, secondsLeft), SECONDS)
    }

}
