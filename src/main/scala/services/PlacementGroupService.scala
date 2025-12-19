package services

import hcloud.models.CreatePlacementgroupRequest
import hcloud.models.CreatePlacementgroupResponse
import hcloud.models.GetPlacementgroupResponse
import hcloud.models.ListPlacementGroupsResponse
import hcloud.models.ReplacePlacementgroupRequest
import hcloud.models.ReplacePlacementgroupResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.PlacementGroups

abstract class PlacementGroupService {

  /** Creates a new Placement Group.
    *
    * Expected answers: code 201 : CreatePlacementgroupResponse (The
    * `PlacementGroup` key contains the Placement Group that was just created.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param createPlacementgroupRequest
    */
  def createPlacementgroup(
      token: String,
      createPlacementgroupRequest: CreatePlacementgroupRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreatePlacementgroupResponse]
  ] =
    PlacementGroups()
      .withBearerTokenAuth(token)
      .createPlacementgroup(createPlacementgroupRequest)

  /** Deletes a Placement Group.
    *
    * Expected answers: code 204 : (Placement Group deleted.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Placement Group.
    */
  def deletePlacementgroup(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    PlacementGroups().withBearerTokenAuth(token).deletePlacementgroup(id)

  /** Gets a specific Placement Group object.
    *
    * Expected answers: code 200 : GetPlacementgroupResponse (The
    * `placement_group` key contains a Placement Group object.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Placement Group.
    */
  def getPlacementgroup(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetPlacementgroupResponse]
  ] =
    PlacementGroups().withBearerTokenAuth(token).getPlacementgroup(id)

  /** Returns all Placement Group objects.
    *
    * Expected answers: code 200 : ListPlacementGroupsResponse (The
    * `placement_groups` key contains an array of Placement Group objects.)
    *
    * Available security schemes: APIToken (http)
    *
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
    * @param `type`
    *   Filter resources by type. Can be used multiple times. The response will
    *   only contain the resources with the specified type.
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listPlacementGroups(
      token: String,
      sort: Seq[String],
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      `type`: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListPlacementGroupsResponse]
  ] =
    PlacementGroups()
      .withBearerTokenAuth(token)
      .listPlacementGroups(sort, name, labelSelector, `type`, page, perPage)

  /** Updates the Placement Group properties. Note: if the Placement Group
    * object changes during the request, the response will be a “conflict”
    * error.
    *
    * Expected answers: code 200 : ReplacePlacementgroupResponse (The
    * `certificate` key contains the Placement Group that was just updated.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Placement Group.
    * @param replacePlacementgroupRequest
    */
  def replacePlacementgroup(
      token: String,
      id: Long,
      replacePlacementgroupRequest: ReplacePlacementgroupRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplacePlacementgroupResponse]
  ] =
    PlacementGroups()
      .withBearerTokenAuth(token)
      .replacePlacementgroup(id, replacePlacementgroupRequest)

}
