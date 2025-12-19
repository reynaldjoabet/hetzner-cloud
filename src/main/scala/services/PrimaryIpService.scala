package services
import hcloud.models.AssignPrimaryIpToResourceRequest
import hcloud.models.AssignPrimaryIpToResourceResponse
import hcloud.models.ChangePrimaryIpProtectionResponse
import hcloud.models.ChangeReverseDnsRecordsForPrimaryIpResponse
import hcloud.models.CreatePrimaryIpRequest
import hcloud.models.CreatePrimaryIpResponse
import hcloud.models.DnsPtr
import hcloud.models.GetActionResponse
import hcloud.models.GetPrimaryIpResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListPrimaryIpsResponse
import hcloud.models.Protection
import hcloud.models.ReplacePrimaryIpRequest
import hcloud.models.ReplacePrimaryIpResponse
import hcloud.models.UnassignPrimaryIpFromResourceResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.PrimaryIps

abstract class PrimaryIpService {

  /** Assign a Primary IP to a resource. A Server can only have one Primary IP
    * of type `ipv4` and one of type `ipv6` assigned. If you need more IPs use
    * Floating IPs. A Server must be powered off (status `off`) in order for
    * this operation to succeed. #### Error Codes specific to this Call | Code |
    * Description | |------------------------------ |--------------------------------------------------------------------------------- | |
    * `server_not_stopped` | The Server is running, but needs to be powered off | |
    * `primary_ip_already_assigned` | Primary IP is already assigned to a
    * different Server | | `server_has_ipv4` | The Server already has an IPv4
    * address | | `server_has_ipv6` | The Server already has an IPv6 address |
    *
    * Expected answers: code 201 : AssignPrimaryIpToResourceResponse (Response
    * for assigning a Primary IP. Contains an Action of type
    * `assign_primary_ip`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Primary IP.
    * @param assignPrimaryIpToResourceRequest
    */
  def assignPrimaryIpToResource(
      token: String,
      id: Long,
      assignPrimaryIpToResourceRequest: AssignPrimaryIpToResourceRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], AssignPrimaryIpToResourceResponse]
  ] =
    PrimaryIps()
      .withBearerTokenAuth(token)
      .assignPrimaryIpToResource(id, assignPrimaryIpToResourceRequest)

  /** Changes the protection configuration of a Primary IP. A Primary IPs
    * deletion protection can only be enabled if its `auto_delete` property is
    * set to `false`.
    *
    * Expected answers: code 201 : ChangePrimaryIpProtectionResponse (Response
    * for changing a Primary IPs protection settings. Contains an Action of type
    * `change_protection`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Primary IP.
    * @param body
    */
  def changePrimaryIpProtection(
      token: String,
      id: Long,
      body: Protection
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangePrimaryIpProtectionResponse]
  ] =
    PrimaryIps()
      .withBearerTokenAuth(token)
      .changePrimaryIpProtection(id, body)

  /** Change the reverse DNS records for this Primary IP. Allows to modify the
    * PTR records set for the IP address.
    *
    * Expected answers: code 201 : ChangeReverseDnsRecordsForPrimaryIpResponse
    * (Response for changing a Primary IPs DNS pointer. Contains an Action of
    * type `change_dns_ptr`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Primary IP.
    * @param body
    *   The `ip` attributes specifies for which IP address the record is set.
    *   For IPv4 addresses this must be the exact address of the Primary IP. For
    *   IPv6 addresses this must be a single address within the `/64` subnet of
    *   the Primary IP. The `dns_ptr` attribute specifies the hostname used for
    *   the IP address. Must be a fully qualified domain name (FQDN) without
    *   trailing dot. For IPv6 Primary IPs up to 100 entries can be created.
    */
  def changeReverseDnsRecordsForPrimaryIp(
      token: String,
      id: Long,
      body: DnsPtr
  ): sttp.client4.Request[Either[ResponseException[
    String
  ], ChangeReverseDnsRecordsForPrimaryIpResponse]] =
    PrimaryIps()
      .withBearerTokenAuth(token)
      .changeReverseDnsRecordsForPrimaryIp(id, body)

  /** Create a new Primary IP. Can optionally be assigned to a resource by
    * providing an `assignee_id` and `assignee_type`. If not assigned to a
    * resource the `datacenter` key needs to be provided. This can be either the
    * ID or the name of the Data Center this Primary IP shall be created in. A
    * Primary IP can only be assigned to resource in the same Data Center later
    * on. #### Call specific error codes | Code | Description | |------------------------------ |------------------------------------------------------------------------- | |
    * `server_not_stopped` | The specified Server is running, but needs to be
    * powered off | | `server_has_ipv4` | The Server already has an ipv4 address | |
    * `server_has_ipv6` | The Server already has an ipv6 address |
    *
    * Expected answers: code 201 : CreatePrimaryIpResponse (Response for
    * creating a Primary IP. Contains the newly created Primary IP. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param createPrimaryIpRequest
    *   Request Body for creating a new Primary IP. The `datacenter` and
    *   `assignee_id`/`assignee_type` attributes are mutually exclusive.
    */
  def createPrimaryIp(
      token: String,
      createPrimaryIpRequest: CreatePrimaryIpRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreatePrimaryIpResponse]
  ] =
    PrimaryIps()
      .withBearerTokenAuth(token)
      .createPrimaryIp(createPrimaryIpRequest)

  /** Deletes a Primary IP. If assigned to a Server the Primary IP will be
    * unassigned automatically. The Server must be powered off (status `off`) in
    * order for this operation to succeed.
    *
    * Expected answers: code 204 : (Primary IP deletion succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Primary IP.
    */
  def deletePrimaryIp(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    PrimaryIps().withBearerTokenAuth(token).deletePrimaryIp(id)

  /** Returns a Primary IP.
    *
    * Expected answers: code 200 : GetPrimaryIpResponse (The `primary_ip` key
    * contains the Primary IP.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Primary IP.
    */
  def getPrimaryIp(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetPrimaryIpResponse]
  ] =
    PrimaryIps().withBearerTokenAuth(token).getPrimaryIp(id)

  /** Returns a single Action.
    *
    * Expected answers: code 200 : GetActionResponse (Response for getting a
    * single Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Action.
    */
  def getPrimaryIpAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    PrimaryIps().withBearerTokenAuth(token).getPrimaryIpAction(id)

  /** Lists multiple Actions. Use the provided URI parameters to modify the
    * result.
    *
    * Expected answers: code 200 : ListActionsResponse (Response for listing
    * Actions.)
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
  def listPrimaryIpActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    PrimaryIps()
      .withBearerTokenAuth(token)
      .listPrimaryIpActions(id, sort, status, page, perPage)

  /** List multiple Primary IPs. Use the provided URI parameters to modify the
    * result.
    *
    * Expected answers: code 200 : ListPrimaryIpsResponse (Response for listing
    * Primary IPs.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param name
    *   Filter resources by their name. The response will only contain the
    *   resources matching exactly the specified name.
    * @param labelSelector
    *   Filter resources by labels. The response will only contain resources
    *   matching the label selector. For more information, see \"Label
    *   Selector\".
    * @param ip
    *   Filter results by IP address.
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    * @param sort
    *   Sort resources by field and direction. Can be used multiple times. For
    *   more information, see \"Sorting\".
    */
  def listPrimaryIps(
      token: String,
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      ip: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None,
      sort: Seq[String]
  ): sttp.client4.Request[
    Either[ResponseException[String], ListPrimaryIpsResponse]
  ] =
    PrimaryIps()
      .withBearerTokenAuth(token)
      .listPrimaryIps(name, labelSelector, ip, page, perPage, sort)

  /** Update a Primary IP. If another change is concurrently performed on this
    * Primary IP, a error response with code `conflict` will be returned.
    *
    * Expected answers: code 200 : ReplacePrimaryIpResponse (The `primary_ip`
    * key contains the updated Primary IP.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Primary IP.
    * @param replacePrimaryIpRequest
    */
  def replacePrimaryIp(
      token: String,
      id: Long,
      replacePrimaryIpRequest: ReplacePrimaryIpRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplacePrimaryIpResponse]
  ] =
    PrimaryIps()
      .withBearerTokenAuth(token)
      .replacePrimaryIp(id, replacePrimaryIpRequest)

  /** Unassign a Primary IP from a resource. A Server must be powered off
    * (status `off`) in order for this operation to succeed. A Server requires
    * at least one network interface (public or private) to be powered on. ####
    * Error Codes specific to this Call | Code | Description | |---------------------------------- |-------------------------------------------------------------- | |
    * `server_not_stopped` | The Server is running, but needs to be powered off | |
    * `server_is_load_balancer_target` | The Server IPv4 address is a
    * loadbalancer target |
    *
    * Expected answers: code 201 : UnassignPrimaryIpFromResourceResponse
    * (Response for unassigning a Primary IP. Contains an Action of type
    * `unassign_primary_ip`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Primary IP.
    */
  def unassignPrimaryIpFromResource(
      token: String,
      id: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], UnassignPrimaryIpFromResourceResponse]
  ] =
    PrimaryIps().withBearerTokenAuth(token).unassignPrimaryIpFromResource(id)

}
