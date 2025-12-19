package services

import hcloud.models.GetLocationResponse
import hcloud.models.ListLocationsResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Locations

abstract class LocationService {

  /** Returns a Location.
    *
    * Expected answers: code 200 : GetLocationResponse (Response with the
    * Location.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Location.
    */
  def getLocation(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetLocationResponse]
  ] =
    Locations().withBearerTokenAuth(token).getLocation(id)

  /** Returns all Locations. Use the provided URI parameters to modify the
    * result.
    *
    * Expected answers: code 200 : ListLocationsResponse (Response with the
    * Locations.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param name
    *   Filter resources by their name. The response will only contain the
    *   resources matching exactly the specified name.
    * @param sort
    *   Sort resources by field and direction. Can be used multiple times. For
    *   more information, see \"Sorting\".
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listLocations(
      token: String,
      name: Option[String] = scala.None,
      sort: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListLocationsResponse]
  ] =
    Locations()
      .withBearerTokenAuth(token)
      .listLocations(name, sort, page, perPage)

}
