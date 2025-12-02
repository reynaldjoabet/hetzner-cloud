package client

import config.HetznerConfig
import sttp.client4.*
import sttp.client4._
import sttp.client4.jsoniter.*
import sttp.client4.Backend
import sttp.client4.DefaultFutureBackend
import sttp.model.MediaType
import sttp.model.Method
import sttp.model.StatusCode
import sttp.model.Uri
import org.typelevel.log4cats.Logger
import cats.syntax.all.*
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import cats.Monad
import cats.effect.kernel.Concurrent
import cats.effect.syntax.all.*
import cats.effect.kernel.syntax.resource
import com.github.plokhotnyuk.jsoniter_scala.core.*
import org.http4s.Response
import org.http4s.Request
import org.http4s.Method
import config.HetznerConfig
import org.http4s.headers.Authorization
import org.http4s.headers.`Content-Type`
import org.http4s.AuthScheme
import scala.concurrent.duration.*
import client.JsoniterSyntaticSugar.*
import client.unitCodec
import hcloud.api.*



//object HetznerClient extends Actions with Certificates with Datacenters  with Firewalls with FloatingIPs with Images with IsOs with  LoadBalancers  with LoadBalancerTypes with Locations with Networks  with PlacementGroups  with Pricing with PrimaryIps with Servers  with ServerTypes with SSHKeys with  StorageBoxes with StorageBoxTpes with Volumes  with Zones

abstract class HetznerClient[F[*]] {}
private class HetznerClientImpl[F[*]: Concurrent](
    logger: Logger[F],
    config: HetznerConfig,
    backend: Backend[F]
) extends HetznerClient[F] {

  private val basicReq = basicRequest.auth
    .bearer(config.token)
    // .contentType(MediaType.ApplicationJson)
    .readTimeout(config.requestTimeout.second)

  private val baseUri = Uri(config.baseUrl)

  private def postRequest[B: JsonValueCodec, R: JsonValueCodec](
      uri: Uri,
      body: B
  ): sttp.client4.Request[Either[ResponseException[String], R]] =
    basicReq
      .post(uri)
      .body(body.toJson)
      .contentType(MediaType.ApplicationJson)
      .response(asJson[R])

  private def postRequest[B: JsonValueCodec](uri: Uri, body: B) =
    basicReq.post(uri).body(body.toJson).contentType(MediaType.ApplicationJson)

  private def postRequestWithUnitResponse[B: JsonValueCodec](
      uri: Uri,
      body: B
  ): sttp.client4.Request[Unit] =
    postRequest[B](uri, body).response(ignore)

  private def postRequest[R: JsonValueCodec](
      uri: Uri
  ): sttp.client4.Request[Either[ResponseException[String], R]] =
    basicReq.post(uri).response(asJson[R])

  private def putRequest[B: JsonValueCodec, R: JsonValueCodec](
      uri: Uri,
      body: B
  ): sttp.client4.Request[Either[ResponseException[String], R]] =
    basicReq
      .put(uri)
      .body(body.toJson)
      .contentType(MediaType.ApplicationJson)
      .response(asJson[R])

// If you set the body as a Map[String, String] or Seq[(String, String)], it will be encoded as form-data (as if a web form with the given values was submitted)
  private def postRequestForm[R: JsonValueCodec](
      uri: Uri,
      body: Map[String, String]
  ): sttp.client4.Request[Either[ResponseException[String], R]] =
    basicReq
      .post(uri)
      .body(body)
      .response(asJson[R])
      .contentType(MediaType.ApplicationXWwwFormUrlencoded)

  private def getRequest[R: JsonValueCodec](
      uri: Uri
  ): sttp.client4.Request[Either[ResponseException[String], R]] =
    basicReq.get(uri).response(asJson[R])

  private def deleteRequest[R: JsonValueCodec](uri: Uri) =
    basicReq
      .delete(uri)
      .response(asJson[R])

}

