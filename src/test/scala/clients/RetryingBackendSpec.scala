package clients

import cats.effect.IO
import cats.effect.Ref
import configs.RetryConfig
import sttp.client4.*
import sttp.client4.impl.cats.implicits.monadError
import sttp.model.Header
import sttp.model.StatusCode

import scala.concurrent.duration.*

class RetryingBackendSpec extends munit.CatsEffectSuite {

  private val fastRetryConfig =
    RetryConfig(maxAttempts = 4, initialBackoff = 1.millis, maxBackoff = 5.millis)

  /** A minimal `Backend[IO]` that ignores the request entirely and returns the next status code (and headers) from
    * `responses`, in order, so the retry behaviour can be observed without any real network I/O.
    */
  private def fakeBackend(responses: List[(Int, Seq[Header])], calls: Ref[IO, Int]): Backend[IO] =
    new Backend[IO] {
      def monad: sttp.monad.MonadError[IO] = monadError[IO]
      def close(): IO[Unit] = IO.unit
      def send[T](request: GenericRequest[T, Any & sttp.capabilities.Effect[IO]]): IO[Response[T]] =
        calls.getAndUpdate(_ + 1).map { attemptIndex =>
          val (code, headers) = responses(math.min(attemptIndex, responses.length - 1))
          Response(
            "".asInstanceOf[T],
            StatusCode(code),
            "",
            headers,
            Nil,
            request.onlyMetadata
          )
        }
    }

  private val testRequest = basicRequest.get(uri"http://example.test/x").response(asStringAlways)

  test("does not retry a successful response") {
    for {
      calls <- Ref.of[IO, Int](0)
      backend = new RetryingBackend(fakeBackend(List((200, Seq.empty)), calls), fastRetryConfig)
      response <- testRequest.send(backend)
      n <- calls.get
    } yield {
      assertEquals(response.code.code, 200)
      assertEquals(n, 1)
    }
  }

  test("does not retry a plain 4xx client error") {
    for {
      calls <- Ref.of[IO, Int](0)
      backend = new RetryingBackend(fakeBackend(List((404, Seq.empty)), calls), fastRetryConfig)
      response <- testRequest.send(backend)
      n <- calls.get
    } yield {
      assertEquals(response.code.code, 404)
      assertEquals(n, 1)
    }
  }

  test("retries a 500 until it succeeds, within the attempt budget") {
    for {
      calls <- Ref.of[IO, Int](0)
      backend = new RetryingBackend(
        fakeBackend(List((500, Seq.empty), (500, Seq.empty), (200, Seq.empty)), calls),
        fastRetryConfig
      )
      response <- testRequest.send(backend)
      n <- calls.get
    } yield {
      assertEquals(response.code.code, 200)
      assertEquals(n, 3)
    }
  }

  test("gives up after maxAttempts and returns the last failing response") {
    for {
      calls <- Ref.of[IO, Int](0)
      backend = new RetryingBackend(fakeBackend(List((503, Seq.empty)), calls), fastRetryConfig)
      response <- testRequest.send(backend)
      n <- calls.get
    } yield {
      assertEquals(response.code.code, 503)
      assertEquals(n, fastRetryConfig.maxAttempts)
    }
  }

  test("retries a 429 and honours a RateLimit-Reset header in the past as an immediate retry") {
    for {
      calls <- Ref.of[IO, Int](0)
      backend = new RetryingBackend(
        fakeBackend(List((429, Seq(Header("RateLimit-Reset", "1"))), (200, Seq.empty)), calls),
        fastRetryConfig
      )
      response <- testRequest.send(backend)
      n <- calls.get
    } yield {
      assertEquals(response.code.code, 200)
      assertEquals(n, 2)
    }
  }

}
