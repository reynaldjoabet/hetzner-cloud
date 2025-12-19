package services

import hcloud.models.GetServerTypeResponse
import hcloud.models.ListServerTypesResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.ServerTypes

abstract class ServerTypeService {

  /** Gets a specific Server type object.
    *
    * Expected answers: code 200 : GetServerTypeResponse (The `server_type` key
    * in the reply contains a Server type object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server Type.
    */
  def getServerType(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetServerTypeResponse]
  ] =
    ServerTypes().withBearerTokenAuth(token).getServerType(id)

  /** Gets all Server type objects.
    *
    * Expected answers: code 200 : ListServerTypesResponse (The `server_types`
    * key in the reply contains an array of Server type objects with this
    * structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param name
    *   Filter resources by their name. The response will only contain the
    *   resources matching exactly the specified name.
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listServerTypes(
      token: String,
      name: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListServerTypesResponse]
  ] =
    ServerTypes()
      .withBearerTokenAuth(token)
      .listServerTypes(name, page, perPage)

}
