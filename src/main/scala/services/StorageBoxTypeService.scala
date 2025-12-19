package services

import hcloud.models.GetStorageBoxTypeResponse
import hcloud.models.ListStorageBoxTypesResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.StorageBoxTypes

abstract class StorageBoxTypeService {

  /** Returns a specific Storage Box Type.
    *
    * Expected answers: code 200 : GetStorageBoxTypeResponse (Request
    * succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box Type.
    */
  def getStorageBoxType(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetStorageBoxTypeResponse]
  ] =
    StorageBoxTypes().withBearerTokenAuth(token).getStorageBoxType(id)

  /** Returns a paginated list of Storage Box Types.
    *
    * Expected answers: code 200 : ListStorageBoxTypesResponse (Request
    * succeeded.)
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
  def listStorageBoxTypes(
      token: String,
      name: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListStorageBoxTypesResponse]
  ] =
    StorageBoxTypes()
      .withBearerTokenAuth(token)
      .listStorageBoxTypes(name, page, perPage)

}
