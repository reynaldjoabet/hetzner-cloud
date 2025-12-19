package services

import hcloud.models.AddRouteToNetworkResponse
import hcloud.models.AddSubnetToNetworkResponse
import hcloud.models.ChangeIpRangeOfNetworkRequest
import hcloud.models.ChangeIpRangeOfNetworkResponse
import hcloud.models.ChangeNetworkProtectionRequest
import hcloud.models.ChangeNetworkProtectionResponse
import hcloud.models.CreateNetworkRequest
import hcloud.models.CreateNetworkResponse
import hcloud.models.DeleteRouteFromNetworkResponse
import hcloud.models.DeleteSubnetFromNetworkRequest
import hcloud.models.DeleteSubnetFromNetworkResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetNetworkResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListNetworksResponse
import hcloud.models.ReplaceNetworkRequest
import hcloud.models.ReplaceNetworkResponse
import hcloud.models.Route
import hcloud.models.Subnet
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Networks

abstract class NetworkService {

  /** Adds a route entry to a Network. If a change is currently being performed
    * on this Network, a error response with code `conflict` will be returned.
    *
    * Expected answers: code 201 : AddRouteToNetworkResponse (Response for
    * adding a route to a Network. The `action` key contains an Action with
    * command `add_route`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    * @param body
    */
  def addRouteToNetwork(
      token: String,
      id: Long,
      body: Route
  ): sttp.client4.Request[
    Either[ResponseException[String], AddRouteToNetworkResponse]
  ] =
    Networks().withBearerTokenAuth(token).addRouteToNetwork(id, body)

  /** Adds a new subnet to the Network. If the subnet `ip_range` is not
    * provided, the first available `/24` IP range will be used. If a change is
    * currently being performed on this Network, a error response with code
    * `conflict` will be returned.
    *
    * Expected answers: code 201 : AddSubnetToNetworkResponse (Response for
    * adding a subnet to a Network. The `action` key contains an Action with
    * command `add_subnet`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    * @param body
    */
  def addSubnetToNetwork(
      token: String,
      id: Long,
      body: Subnet
  ): sttp.client4.Request[
    Either[ResponseException[String], AddSubnetToNetworkResponse]
  ] =
    Networks().withBearerTokenAuth(token).addSubnetToNetwork(id, body)

  /** Changes the IP range of a Network. The following restrictions apply to
    * changing the IP range: - IP ranges can only be extended and never shrunk. -
    * IPs can only be added to the end of the existing range, therefore only the
    * netmask is allowed to be changed. To update the routes on the connected
    * Servers, they need to be rebooted or the routes to be updated manually.
    * For example if the Network has a range of `10.0.0.0/16` to extend it the
    * new range has to start with the IP `10.0.0.0` as well. The netmask `/16`
    * can be changed to a smaller one then `16` therefore increasing the IP
    * range. A valid entry would be `10.0.0.0/15`, `10.0.0.0/14` or
    * `10.0.0.0/13` and so on. If a change is currently being performed on this
    * Network, a error response with code `conflict` will be returned.
    *
    * Expected answers: code 201 : ChangeIpRangeOfNetworkResponse (Response for
    * changing the Networks IP range. The `action` key contains an Action with
    * command `change_ip_range`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    * @param changeIpRangeOfNetworkRequest
    */
  def changeIpRangeOfNetwork(
      token: String,
      id: Long,
      changeIpRangeOfNetworkRequest: ChangeIpRangeOfNetworkRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeIpRangeOfNetworkResponse]
  ] =
    Networks()
      .withBearerTokenAuth(token)
      .changeIpRangeOfNetwork(id, changeIpRangeOfNetworkRequest)

  /** Changes the protection settings of a Network. If a change is currently
    * being performed on this Network, a error response with code `conflict`
    * will be returned.
    *
    * Expected answers: code 201 : ChangeNetworkProtectionResponse (Response for
    * changing the Networks protection. The `action` key contains an Action with
    * command `change_protection`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    * @param changeNetworkProtectionRequest
    */
  def changeNetworkProtection(
      token: String,
      id: Long,
      changeNetworkProtectionRequest: ChangeNetworkProtectionRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeNetworkProtectionResponse]
  ] =
    Networks()
      .withBearerTokenAuth(token)
      .changeNetworkProtection(id, changeNetworkProtectionRequest)

  /** Creates a Network. The provided `ip_range` can only be extended later on,
    * but not reduced. Subnets can be added now or later on using the add subnet
    * action. If you do not specify an `ip_range` for the subnet the first
    * available /24 range will be used. Routes can be added now or later by
    * using the add route action.
    *
    * Expected answers: code 201 : CreateNetworkResponse (Response for creating
    * a Network. Contains the newly created Network. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param createNetworkRequest
    */
  def createNetwork(
      token: String,
      createNetworkRequest: CreateNetworkRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateNetworkResponse]
  ] =
    Networks().withBearerTokenAuth(token).createNetwork(createNetworkRequest)

  /** Deletes a Network. Attached resources will be detached automatically.
    *
    * Expected answers: code 204 : (Response for deleting a Network.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    */
  def deleteNetwork(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    Networks().withBearerTokenAuth(token).deleteNetwork(id)

  /** Delete a route entry from a Network. If a change is currently being
    * performed on this Network, a error response with code `conflict` will be
    * returned.
    *
    * Expected answers: code 201 : DeleteRouteFromNetworkResponse (Response for
    * deleting a route from a Network. The `action` key contains an Action with
    * command `delete_route`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    * @param body
    */
  def deleteRouteFromNetwork(
      token: String,
      id: Long,
      body: Route
  ): sttp.client4.Request[
    Either[ResponseException[String], DeleteRouteFromNetworkResponse]
  ] =
    Networks().withBearerTokenAuth(token).deleteRouteFromNetwork(id, body)

  /** Deletes a single subnet entry from a Network. Subnets containing attached
    * resources can not be deleted, they must be detached beforehand. If a
    * change is currently being performed on this Network, a error response with
    * code `conflict` will be returned.
    *
    * Expected answers: code 201 : DeleteSubnetFromNetworkResponse (Response for
    * deleting a subnet from a Network. The `action` key contains an Action with
    * command `delete_subnet`. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    * @param deleteSubnetFromNetworkRequest
    */
  def deleteSubnetFromNetwork(
      token: String,
      id: Long,
      deleteSubnetFromNetworkRequest: DeleteSubnetFromNetworkRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], DeleteSubnetFromNetworkResponse]
  ] =
    Networks()
      .withBearerTokenAuth(token)
      .deleteSubnetFromNetwork(id, deleteSubnetFromNetworkRequest)

  /** Returns a specific Action for a Network.
    *
    * Expected answers: code 200 : GetActionResponse (Response for getting an
    * Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForNetwork(
      token: String,
      id: Long,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Networks().withBearerTokenAuth(token).getActionForNetwork(id, actionId)

  /** Get a specific Network.
    *
    * Expected answers: code 200 : GetNetworkResponse (The `network` key
    * contains the network.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    */
  def getNetwork(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetNetworkResponse]
  ] =
    Networks().withBearerTokenAuth(token).getNetwork(id)

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
  def getNetworkAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Networks().withBearerTokenAuth(token).getNetworkAction(id)

  /** Lists Actions for a Network. Use the provided URI parameters to modify the
    * result.
    *
    * Expected answers: code 200 : ListActionsResponse (Response for listing
    * Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
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
  def listActionsForNetwork(
      token: String,
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Networks()
      .withBearerTokenAuth(token)
      .listActionsForNetwork(id, sort, status, page, perPage)

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
  def listNetworkActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Networks()
      .withBearerTokenAuth(token)
      .listNetworkActions(id, sort, status, page, perPage)

  /** List multiple Networks. Use the provided URI parameters to modify the
    * result.
    *
    * Expected answers: code 200 : ListNetworksResponse (Response for listing
    * Networks.)
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
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listNetworks(
      token: String,
      sort: Seq[String],
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListNetworksResponse]
  ] =
    Networks()
      .withBearerTokenAuth(token)
      .listNetworks(sort, name, labelSelector, page, perPage)

  /** Update a Network. If a change is currently being performed on this
    * Network, a error response with code `conflict` will be returned.
    *
    * Expected answers: code 200 : ReplaceNetworkResponse (Response for updating
    * a Network. Contains the updated Network. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Network.
    * @param replaceNetworkRequest
    */
  def replaceNetwork(
      token: String,
      id: Long,
      replaceNetworkRequest: ReplaceNetworkRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceNetworkResponse]
  ] =
    Networks()
      .withBearerTokenAuth(token)
      .replaceNetwork(id, replaceNetworkRequest)

}
