package clients

import configs.HetznerConfig
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
import configs.HetznerConfig
import org.http4s.headers.Authorization
import org.http4s.headers.`Content-Type`
import org.http4s.AuthScheme
import scala.concurrent.duration.*
import clients.JsoniterSyntaticSugar.*
import clients.unitCodec
import hcloud.api.*
import hcloud.models.*
//import services.{ActionService,CertificateService,DatacenterService,FirewallService,FloatingIpService,ImageService,IsoService,LoadBalancerService as LBalancerService,LoadBalancerTypeService,LocationService,NetworkService,PlacementGroupService,PricingService,PrimaryIpService,ServerService,ServerTypeService,SshKeyService,StorageBoxService,StorageBoxTypeService,VolumeService,ZoneService}
import services.*
abstract class HetznerClient[F[*]] {

  /** Returns multiple Action objects specified by the `id` parameter. **Note**:
    * This endpoint previously allowed listing all actions in the project. This
    * functionality was deprecated in July 2023 and removed on 30 January 2025. -
    * Announcement:
    * https://docs.hetzner.cloud/changelog#2023-07-20-actions-list-endpoint-is-deprecated -
    * Removal:
    * https://docs.hetzner.cloud/changelog#2025-01-30-listing-arbitrary-actions-in-the-actions-list-endpoint-is-removed
    *
    * Expected answers: code 200 : GetMultipleActionsResponse (Response for
    * listing Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   Filter the actions by ID. Can be used multiple times. The response will
    *   only contain actions matching the specified IDs.
    */

  def getActions(
      id: Seq[Long]
  ): F[Either[ResponseException[String], GetMultipleActionsResponse]]

  /** Returns a specific Action object.
    *
    * Expected answers: code 200 : GetActionResponse (Response for getting an
    * Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Action.
    */
  def getAction(
      id: Long
  ): F[Either[ResponseException[String], GetActionResponse]]
}

private class HetznerClientImpl[F[*]: Concurrent](
    logger: Logger[F],
    config: HetznerConfig,
    backend: Backend[F]
) { // extends HetznerClient[F] {

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

object HetznerClient {
  def make[F[*]: Concurrent](
      logger: Logger[F],
      config: HetznerConfig,
      backend: Backend[F] = DefaultFutureBackend()
  ): HetznerClient[F] = ???
  // new HetznerClientImpl[F](logger, config, backend)

  object actions extends ActionService
  object certificates extends CertificateService
  object datacenters extends DatacenterService
  object firewalls extends FirewallService
  object floatingIPs extends FloatingIpService
  object images extends ImageService
  object isos extends IsoService
  object loadBalancers extends LdBalancerService
  object loadBalancerTypes extends LoadBalancerTypeService
  object locations extends LocationService
  object networks extends NetworkService
  object placementsGroups extends PlacementGroupService
  object pricing extends PricingService
  object primaryIPs extends PrimaryIpService
  object servers extends ServerService
  object serverTypes extends ServerTypeService
  object sshKeys extends SshKeyService
  object storageBoxes extends StorageBoxService
  object storageBoxTypes extends StorageBoxTypeService
  object volumes extends VolumeService
  object zones extends ZoneService
}
