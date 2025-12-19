package services

import hcloud.models.ChangeImageProtectionRequest
import hcloud.models.ChangeImageProtectionResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetImageResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListImagesResponse
import hcloud.models.ReplaceImageRequest
import hcloud.models.ReplaceImageResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Images

abstract class ImageService {

  /** Changes the protection configuration of the Image. Can only be used on
    * snapshots.
    *
    * Expected answers: code 201 : ChangeImageProtectionResponse (The `action`
    * key contains the `change_protection` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Image.
    * @param changeImageProtectionRequest
    */
  def changeImageProtection(
      token: String,
      id: Long,
      changeImageProtectionRequest: ChangeImageProtectionRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeImageProtectionResponse]
  ] =
    Images()
      .withBearerTokenAuth(token)
      .changeImageProtection(id, changeImageProtectionRequest)

  /** Deletes an Image. Only Images of type `snapshot` and `backup` can be
    * deleted.
    *
    * Expected answers: code 204 : (Image deleted.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Image.
    */
  def deleteImage(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    Images().withBearerTokenAuth(token).deleteImage(id)

  /** Returns a specific Action for an Image.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key contains
    * the Image Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Image.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForImage(
      token: String,
      id: Long,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Images().withBearerTokenAuth(token).getActionForImage(id, actionId)

  /** Returns a specific Image object.
    *
    * Expected answers: code 200 : GetImageResponse (The `image` key in the
    * reply contains an Image object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Image.
    */
  def getImage(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], GetImageResponse]] =
    Images().withBearerTokenAuth(token).getImage(id)

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
  def getImageAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Images().withBearerTokenAuth(token).getImageAction(id)

  /** Returns all Action objects for an Image. You can sort the results by using
    * the `sort` URI parameter, and filter them with the `status` parameter.
    *
    * Expected answers: code 200 : ListActionsResponse (The `actions` key
    * contains a list of Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Image.
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
  def listActionsForImage(
      token: String,
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Images()
      .withBearerTokenAuth(token)
      .listActionsForImage(id, sort, status, page, perPage)

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
  def listImageActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Images()
      .withBearerTokenAuth(token)
      .listImageActions(id, sort, status, page, perPage)

  /** Returns all Image objects. You can select specific Image types only and
    * sort the results by using URI parameters.
    *
    * Expected answers: code 200 : ListImagesResponse (The `images` key in the
    * reply contains an array of Image objects with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param sort
    *   Sort resources by field and direction. Can be used multiple times. For
    *   more information, see \"Sorting\".
    * @param `type`
    *   Filter resources by type. Can be used multiple times. The response will
    *   only contain the resources with the specified type.
    * @param status
    *   Filter resources by status. Can be used multiple times. The response
    *   will only contain the resources with the specified status.
    * @param boundTo
    *   Can be used multiple times. Server ID linked to the Image. Only
    *   available for Images of type `backup`.
    * @param includeDeprecated
    *   Can be used multiple times.
    * @param name
    *   Filter resources by their name. The response will only contain the
    *   resources matching exactly the specified name.
    * @param labelSelector
    *   Filter resources by labels. The response will only contain resources
    *   matching the label selector. For more information, see \"Label
    *   Selector\".
    * @param architecture
    *   Filter resources by cpu architecture. The response will only contain the
    *   resources with the specified cpu architecture.
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listImages(
      token: String,
      sort: Seq[String],
      `type`: Seq[String],
      status: Seq[String],
      boundTo: Seq[String],
      includeDeprecated: Option[Boolean] = scala.None,
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      architecture: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListImagesResponse]
  ] =
    Images()
      .withBearerTokenAuth(token)
      .listImages(
        sort,
        `type`,
        status,
        boundTo,
        includeDeprecated,
        name,
        labelSelector,
        architecture,
        page,
        perPage
      )

  /** Updates the Image. You may change the description, convert a Backup Image
    * to a Snapshot Image or change the Image labels. Only Images of type
    * `snapshot` and `backup` can be updated.
    *
    * Expected answers: code 200 : ReplaceImageResponse (The image key in the
    * reply contains the modified Image object.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Image.
    * @param replaceImageRequest
    */
  def replaceImage(
      token: String,
      id: Long,
      replaceImageRequest: ReplaceImageRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceImageResponse]
  ] =
    Images().withBearerTokenAuth(token).replaceImage(id, replaceImageRequest)

}
