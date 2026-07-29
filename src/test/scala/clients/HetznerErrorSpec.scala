package clients

import sttp.client4.ResponseException
import sttp.model.Header
import sttp.model.ResponseMetadata
import sttp.model.StatusCode

import scala.concurrent.duration.*

class HetznerErrorSpec extends munit.FunSuite {

  private def statusOnly(code: Int, headers: Seq[Header] = Seq.empty): ResponseException[String] =
    ResponseException.UnexpectedStatusCode(
      body = """{"error":{"code":"some_code","message":"some message"}}""",
      response = ResponseMetadata(StatusCode(code), "", headers)
    )

  test("maps well-known status codes to their dedicated error case") {
    assert(HetznerError.fromResponseException(statusOnly(401)).isInstanceOf[HetznerError.Unauthorized])
    assert(HetznerError.fromResponseException(statusOnly(403)).isInstanceOf[HetznerError.Forbidden])
    assert(HetznerError.fromResponseException(statusOnly(404)).isInstanceOf[HetznerError.NotFound])
    assert(HetznerError.fromResponseException(statusOnly(409)).isInstanceOf[HetznerError.Conflict])
    assert(HetznerError.fromResponseException(statusOnly(423)).isInstanceOf[HetznerError.Locked])
    assert(HetznerError.fromResponseException(statusOnly(500)).isInstanceOf[HetznerError.ServerError])
    assert(HetznerError.fromResponseException(statusOnly(503)).isInstanceOf[HetznerError.ServerError])
  }

  test("extracts the error code and message from a well-formed error body") {
    HetznerError.fromResponseException(statusOnly(422)) match {
      case HetznerError.ValidationError(code, message) =>
        assertEquals(code, "some_code")
        assertEquals(message, "some message")
      case other => fail(s"expected ValidationError, got $other")
    }
  }

  test("falls back to the raw body when it isn't the documented error envelope") {
    val e = ResponseException.UnexpectedStatusCode(
      body = "not json",
      response = ResponseMetadata(StatusCode(400), "", Seq.empty)
    )
    HetznerError.fromResponseException(e) match {
      case HetznerError.Unexpected(status, message) =>
        assertEquals(status, 400)
        assertEquals(message, "not json")
      case other => fail(s"expected Unexpected, got $other")
    }
  }

  test("429 without a RateLimit-Reset header has no retryAfter") {
    HetznerError.fromResponseException(statusOnly(429)) match {
      case HetznerError.RateLimited(retryAfter, _) => assertEquals(retryAfter, None)
      case other                                   => fail(s"expected RateLimited, got $other")
    }
  }

  test("429 with a future RateLimit-Reset header yields a positive retryAfter") {
    val resetAt = (System.currentTimeMillis() / 1000) + 120
    val e = statusOnly(429, Seq(Header("RateLimit-Reset", resetAt.toString)))
    HetznerError.fromResponseException(e) match {
      case HetznerError.RateLimited(Some(retryAfter), _) =>
        assert(retryAfter > 0.seconds && retryAfter <= 120.seconds, s"got $retryAfter")
      case other => fail(s"expected RateLimited with a retryAfter, got $other")
    }
  }

  test("429 with a past RateLimit-Reset header clamps retryAfter to zero, not negative") {
    val e = statusOnly(429, Seq(Header("RateLimit-Reset", "1")))
    HetznerError.fromResponseException(e) match {
      case HetznerError.RateLimited(Some(retryAfter), _) => assertEquals(retryAfter, 0.seconds)
      case other                                         => fail(s"expected RateLimited(Some(0s)), got $other")
    }
  }

  test("a deserialization failure (malformed 2xx body) maps to Decoding") {
    val cause = new RuntimeException("boom")
    val e =
      ResponseException.DeserializationException("{bad json", cause, ResponseMetadata(StatusCode(200), "", Seq.empty))
    HetznerError.fromResponseException(e) match {
      case HetznerError.Decoding(_, c) => assertEquals(c, cause)
      case other                       => fail(s"expected Decoding, got $other")
    }
  }

}
