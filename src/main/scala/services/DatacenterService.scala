package services

import hcloud.models.GetDataCenterResponse
import hcloud.models.ListDataCentersResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Datacenters

abstract class DatacenterService {

  /** Returns a single Data Center.
    *
    * Expected answers: code 200 : GetDataCenterResponse (Contains the queried
    * Data Center.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Data Center.
    */
  def getDataCenter(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetDataCenterResponse]
  ] =
    Datacenters().withBearerTokenAuth(token).getDataCenter(id)

  /** Returns all Data Centers.
    *
    * Expected answers: code 200 : ListDataCentersResponse (Contains the queried
    * Data Centers.)
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
  def listDataCenters(
      token: String,
      name: Option[String] = scala.None,
      sort: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListDataCentersResponse]
  ] =
    Datacenters()
      .withBearerTokenAuth("token")
      .listDataCenters(name, sort, page, perPage)

}
