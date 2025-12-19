package services

import hcloud.models.AssignFloatingIpToServerRequest
import hcloud.models.AssignFloatingIpToServerResponse
import hcloud.models.ChangeFloatingIpProtectionResponse
import hcloud.models.ChangeReverseDnsRecordsForFloatingIpResponse
import hcloud.models.CreateFloatingIpRequest
import hcloud.models.CreateFloatingIpResponse
import hcloud.models.DnsPtr
import hcloud.models.GetActionResponse
import hcloud.models.GetFloatingIpResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListFloatingIpsResponse
import hcloud.models.Protection
import hcloud.models.ReplaceFloatingIpRequest
import hcloud.models.ReplaceFloatingIpResponse
import hcloud.models.UnassignFloatingIpResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.FloatingIps

abstract class FloatingIpService {

  /** Assigns a Floating IP to a Server.
    *
    * Expected answers: code 201 : AssignFloatingIpToServerResponse (Response
    * for assigning a Floating IP. Contains an Action of type
    * `assign_floating_ip`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Floating IP.
    * @param assignFloatingIpToServerRequest
    */
  def assignFloatingIpToServer(
      token: String,
      id: Long,
      assignFloatingIpToServerRequest: AssignFloatingIpToServerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], AssignFloatingIpToServerResponse]
  ] =
    FloatingIps()
      .withBearerTokenAuth(token)
      .assignFloatingIpToServer(id, assignFloatingIpToServerRequest)

  /** Changes the protection settings configured for the Floating IP.
    *
    * Expected answers: code 201 : ChangeFloatingIpProtectionResponse (Response
    * for changing a Floating IPs protection settings. Contains an Action of
    * type `change_protection`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Floating IP.
    * @param body
    */
  def changeFloatingIpProtection(
      token: String,
      id: Long,
      body: Protection
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeFloatingIpProtectionResponse]
  ] =
    FloatingIps()
      .withBearerTokenAuth(token)
      .changeFloatingIpProtection(id, body)

  /** Change the reverse DNS records for this Floating IP. Allows to modify the
    * PTR records set for the IP address.
    *
    * Expected answers: code 201 : ChangeReverseDnsRecordsForFloatingIpResponse
    * (Response for changing a Floating IPs DNS pointer. Contains an Action of
    * type `change_dns_ptr`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Floating IP.
    * @param body
    *   The `ip` attributes specifies for which IP address the record is set.
    *   For IPv4 addresses this must be the exact address of the Floating IP.
    *   For IPv6 addresses this must be a single address within the `/64` subnet
    *   of the Floating IP. The `dns_ptr` attribute specifies the hostname used
    *   for the IP address. Must be a fully qualified domain name (FQDN) without
    *   trailing dot. For IPv6 Floating IPs up to 100 entries can be created.
    */
  def changeReverseDnsRecordsForFloatingIp(
      token: String,
      id: Long,
      body: DnsPtr
  ): sttp.client4.Request[Either[ResponseException[
    String
  ], ChangeReverseDnsRecordsForFloatingIpResponse]] =
    FloatingIps()
      .withBearerTokenAuth(token)
      .changeReverseDnsRecordsForFloatingIp(id, body)

  /** Create a Floating IP. Provide the `server` attribute to assign the
    * Floating IP to that server or provide a `home_location` to locate the
    * Floating IP at. Note that the Floating IP can be assigned to a Server in
    * any Location later on. For optimal routing it is advised to use the
    * Floating IP in the same Location it was created in.
    *
    * Expected answers: code 201 : CreateFloatingIpResponse (Response for
    * creating a Floating IP. Contains the created IP. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param createFloatingIpRequest
    *   The `type` argument is required while `home_location` and `server` are
    *   mutually exclusive.
    */
  def createFloatingIp(
      token: String,
      createFloatingIpRequest: CreateFloatingIpRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateFloatingIpResponse]
  ] =
    FloatingIps()
      .withBearerTokenAuth(token)
      .createFloatingIp(createFloatingIpRequest)

  /** Deletes a Floating IP. If the IP is assigned to a resource it will be
    * unassigned.
    *
    * Expected answers: code 204 : (Floating IP deleted.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Floating IP.
    */
  def deleteFloatingIp(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    FloatingIps().withBearerTokenAuth(token).deleteFloatingIp(id)

  /** Returns a specific Action for a Floating IP.
    *
    * Expected answers: code 200 : GetActionResponse (Response for getting an
    * Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Floating IP.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForFloatingIp(
      token: String,
      id: Long,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    FloatingIps()
      .withBearerTokenAuth(token)
      .getActionForFloatingIp(id, actionId)

  /** Returns a single Floating IP.
    *
    * Expected answers: code 200 : GetFloatingIpResponse (Response for getting a
    * single Floating IP.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Floating IP.
    */
  def getFloatingIp(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetFloatingIpResponse]
  ] =
    FloatingIps().withBearerTokenAuth(token).getFloatingIp(id)

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
  def getFloatingIpAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    FloatingIps().withBearerTokenAuth(token).getFloatingIpAction(id)

  /** Lists Actions for a Floating IP. Use the provided URI parameters to modify
    * the result.
    *
    * Expected answers: code 200 : ListActionsResponse (Response for listing
    * Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Floating IP.
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
  def listActionsForFloatingIp(
      token: String,
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    FloatingIps()
      .withBearerTokenAuth(token)
      .listActionsForFloatingIp(id, sort, status, page, perPage)

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
  def listFloatingIpActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    FloatingIps()
      .withBearerTokenAuth(token)
      .listFloatingIpActions(id, sort, status, page, perPage)

  /** List multiple Floating IPs. Use the provided URI parameters to modify the
    * result.
    *
    * Expected answers: code 200 : ListFloatingIpsResponse (Response for listing
    * Floating IPs.)
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
    * @param sort
    *   Sort resources by field and direction. Can be used multiple times. For
    *   more information, see \"Sorting\".
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listFloatingIps(
      token: String,
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      sort: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListFloatingIpsResponse]
  ] =
    FloatingIps()
      .withBearerTokenAuth(token)
      .listFloatingIps(name, labelSelector, sort, page, perPage)

  /** Update a Floating IP.
    *
    * Expected answers: code 200 : ReplaceFloatingIpResponse (Response for
    * updating a Floating IP. Contains the updated Floating IP. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Floating IP.
    * @param replaceFloatingIpRequest
    */
  def replaceFloatingIp(
      token: String,
      id: Long,
      replaceFloatingIpRequest: ReplaceFloatingIpRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceFloatingIpResponse]
  ] =
    FloatingIps()
      .withBearerTokenAuth(token)
      .replaceFloatingIp(id, replaceFloatingIpRequest)

  /** Unassigns a Floating IP. Results in the IP being unreachable. Can be
    * assigned to another resource again.
    *
    * Expected answers: code 201 : UnassignFloatingIpResponse (Response for
    * unassigning a Floating IP. Contains an Action of type
    * `unassign_floating_ip`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Floating IP.
    */
  def unassignFloatingIp(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], UnassignFloatingIpResponse]
  ] =
    FloatingIps().withBearerTokenAuth(token).unassignFloatingIp(id)

}
