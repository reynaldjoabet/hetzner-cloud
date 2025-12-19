package services

import hcloud.models.AttachVolumeToServerRequest
import hcloud.models.AttachVolumeToServerResponse
import hcloud.models.ChangeVolumeProtectionRequest
import hcloud.models.ChangeVolumeProtectionResponse
import hcloud.models.CreateVolumeRequest
import hcloud.models.CreateVolumeResponse
import hcloud.models.DetachVolumeResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetVolumeResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListVolumesResponse
import hcloud.models.ReplaceVolumeRequest
import hcloud.models.ReplaceVolumeResponse
import hcloud.models.ResizeVolumeRequest
import hcloud.models.ResizeVolumeResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Volumes

abstract class VolumeService {

  /** Attaches a Volume to a Server. Works only if the Server is in the same
    * Location as the Volume.
    *
    * Expected answers: code 201 : AttachVolumeToServerResponse (The `action`
    * key contains the `attach_volume` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Volume.
    * @param attachVolumeToServerRequest
    */
  def attachVolumeToServer(
      token: String,
      id: Long,
      attachVolumeToServerRequest: AttachVolumeToServerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], AttachVolumeToServerResponse]
  ] =
    Volumes()
      .withBearerTokenAuth(token)
      .attachVolumeToServer(id, attachVolumeToServerRequest)

  /** Changes the protection configuration of a Volume.
    *
    * Expected answers: code 201 : ChangeVolumeProtectionResponse (The `action`
    * key contains the `change_protection` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Volume.
    * @param changeVolumeProtectionRequest
    */
  def changeVolumeProtection(
      token: String,
      id: Long,
      changeVolumeProtectionRequest: ChangeVolumeProtectionRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeVolumeProtectionResponse]
  ] =
    Volumes()
      .withBearerTokenAuth(token)
      .changeVolumeProtection(id, changeVolumeProtectionRequest)

  /** Creates a new Volume attached to a Server. If you want to create a Volume
    * that is not attached to a Server, you need to provide the `location` key
    * instead of `server`. This can be either the ID or the name of the Location
    * this Volume will be created in. Note that a Volume can be attached to a
    * Server only in the same Location as the Volume itself. Specifying the
    * Server during Volume creation will automatically attach the Volume to that
    * Server after it has been initialized. In that case, the `next_actions` key
    * in the response is an array which contains a single `attach_volume`
    * action. The minimum Volume size is 10GB and the maximum size is 10TB
    * (10240GB). A volume’s name can consist of alphanumeric characters, dashes,
    * underscores, and dots, but has to start and end with an alphanumeric
    * character. The total length is limited to 64 characters. Volume names must
    * be unique per Project. #### Call specific error codes | Code | Description | |-------------------------------------|-----------------------------------------------------| |
    * `no_space_left_in_location` | There is no volume space left in the given
    * location |
    *
    * Expected answers: code 201 : CreateVolumeResponse (The `volume` key
    * contains the Volume that was just created. The `action` key contains the
    * Action tracking Volume creation. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param createVolumeRequest
    */
  def createVolume(
      token: String,
      createVolumeRequest: CreateVolumeRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateVolumeResponse]
  ] =
    Volumes().withBearerTokenAuth(token).createVolume(createVolumeRequest)

  /** Deletes a volume. All Volume data is irreversibly destroyed. The Volume
    * must not be attached to a Server and it must not have delete protection
    * enabled.
    *
    * Expected answers: code 204 : (Volume deleted.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Volume.
    */
  def deleteVolume(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    Volumes().withBearerTokenAuth(token).deleteVolume(id)

  /** Detaches a Volume from the Server it’s attached to. You may attach it to a
    * Server again at a later time.
    *
    * Expected answers: code 201 : DetachVolumeResponse (The `action` key
    * contains the `detach_volume` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Volume.
    */
  def detachVolume(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], DetachVolumeResponse]
  ] =
    Volumes().withBearerTokenAuth(token).detachVolume(id)

  /** Returns a specific Action for a Volume.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key contains
    * the Volume Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Volume.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForVolume(
      token: String,
      id: Long,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Volumes().withBearerTokenAuth(token).getActionForVolume(id, actionId)

  /** Gets a specific Volume object.
    *
    * Expected answers: code 200 : GetVolumeResponse (The `volume` key contains
    * the volume.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Volume.
    */
  def getVolume(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetVolumeResponse]
  ] =
    Volumes().withBearerTokenAuth(token).getVolume(id)

  /** Returns a specific Action object.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key in the
    * reply has this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Action.
    */
  def getVolumeAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Volumes().withBearerTokenAuth(token).getVolumeAction(id)

  /** Returns all Action objects for a Volume. You can `sort` the results by
    * using the sort URI parameter, and filter them with the `status` parameter.
    *
    * Expected answers: code 200 : ListActionsResponse (The `actions` key
    * contains a list of Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Volume.
    * @param sort
    *   Sort actions by field and direction. Can be used multiple times. For
    *   more information, see \"Sorting\".
    * @param status
    *   Filter the actions by status. Can be used multiple times. The response
    *   will only contain actions matching the specified statuses.
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listActionsForVolume(
      token: String,
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Volumes()
      .withBearerTokenAuth(token)
      .listActionsForVolume(id, sort, status, page, perPage)

  /** Returns all Action objects. You can `sort` the results by using the sort
    * URI parameter, and filter them with the `status` and `id` parameter.
    *
    * Expected answers: code 200 : ListActionsResponse (The `actions` key
    * contains a list of Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   Filter the actions by ID. Can be used multiple times. The response will
    *   only contain actions matching the specified IDs.
    * @param sort
    *   Sort actions by field and direction. Can be used multiple times. For
    *   more information, see \"Sorting\".
    * @param status
    *   Filter the actions by status. Can be used multiple times. The response
    *   will only contain actions matching the specified statuses.
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listVolumeActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Volumes()
      .withBearerTokenAuth(token)
      .listVolumeActions(id, sort, status, page, perPage)

  /** Gets all existing Volumes that you have available.
    *
    * Expected answers: code 200 : ListVolumesResponse (The `volumes` key
    * contains a list of volumes.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param status
    *   Filter resources by status. Can be used multiple times. The response
    *   will only contain the resources with the specified status.
    * @param sort
    *   Sort resources by field and direction. Can be used multiple times. For
    *   more information, see \"Sorting\".
    * @param name
    *   Filter resources by their name. The response will only contain the
    *   resources matching exactly the specified name.
    * @param labelSelector
    *   Filter resources by labels. The response will only contain resources
    *   matching the label selector. For more information, see \"Label
    *   Selector\".
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listVolumes(
      token: String,
      status: Seq[String],
      sort: Seq[String],
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListVolumesResponse]
  ] =
    Volumes()
      .withBearerTokenAuth(token)
      .listVolumes(status, sort, name, labelSelector, page, perPage)

  /** Updates the Volume properties.
    *
    * Expected answers: code 200 : ReplaceVolumeResponse (The `volume` key
    * contains the updated volume.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Volume.
    * @param replaceVolumeRequest
    */
  def replaceVolume(
      token: String,
      id: Long,
      replaceVolumeRequest: ReplaceVolumeRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceVolumeResponse]
  ] =
    Volumes()
      .withBearerTokenAuth(token)
      .replaceVolume(id, replaceVolumeRequest)

  /** Changes the size of a Volume. Note that downsizing a Volume is not
    * possible.
    *
    * Expected answers: code 201 : ResizeVolumeResponse (The `action` key
    * contains the `resize_volume` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Volume.
    * @param resizeVolumeRequest
    */
  def resizeVolume(
      token: String,
      id: Long,
      resizeVolumeRequest: ResizeVolumeRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ResizeVolumeResponse]
  ] =
    Volumes().withBearerTokenAuth(token).resizeVolume(id, resizeVolumeRequest)

}
