package services

import hcloud.models.GetLoadBalancerTypeResponse
import hcloud.models.ListLoadBalancerTypesResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.LoadBalancerTypes

abstract class LoadBalancerTypeService {

  /** Gets a specific Load Balancer type object.
    *
    * Expected answers: code 200 : GetLoadBalancerTypeResponse (The
    * `load_balancer_type` key in the reply contains a Load Balancer type object
    * with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer Type.
    */
  def getLoadBalancerType(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetLoadBalancerTypeResponse]
  ] =
    LoadBalancerTypes().withBearerTokenAuth(token).getLoadBalancerType(id)

  /** Gets all Load Balancer type objects.
    *
    * Expected answers: code 200 : ListLoadBalancerTypesResponse (The
    * `load_balancer_types` key in the reply contains an array of Load Balancer
    * type objects with this structure.)
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
  def listLoadBalancerTypes(
      token: String,
      name: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListLoadBalancerTypesResponse]
  ] =
    LoadBalancerTypes()
      .withBearerTokenAuth(token)
      .listLoadBalancerTypes(name, page, perPage)

}
