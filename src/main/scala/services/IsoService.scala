package services

import hcloud.models.GetIsoResponse
import hcloud.models.ListIsosResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Isos

abstract class IsoService {

  /** Returns a specific ISO object.
    *
    * Expected answers: code 200 : GetIsoResponse (The `iso` key in the reply
    * contains an array of ISO objects with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the ISO.
    */
  def getIso(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], GetIsoResponse]] =
    Isos().withBearerTokenAuth(token).getIso(id)

  /** Returns all available ISO objects.
    *
    * Expected answers: code 200 : ListIsosResponse (The `isos` key in the reply
    * contains an array of iso objects with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param name
    *   Filter resources by their name. The response will only contain the
    *   resources matching exactly the specified name.
    * @param architecture
    *   Filter resources by cpu architecture. The response will only contain the
    *   resources with the specified cpu architecture.
    * @param includeArchitectureWildcard
    *   Include Images with wildcard architecture (architecture is null). Works
    *   only if architecture filter is specified.
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listIsos(
      token: String,
      name: Option[String] = scala.None,
      architecture: Option[String] = scala.None,
      includeArchitectureWildcard: Option[Boolean] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[Either[ResponseException[String], ListIsosResponse]] =
    Isos()
      .withBearerTokenAuth(token)
      .listIsos(name, architecture, includeArchitectureWildcard, page, perPage)

}
