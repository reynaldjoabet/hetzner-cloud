package services

import hcloud.models.ApplyToResourcesRequest
import hcloud.models.ApplyToResourcesResponse
import hcloud.models.CreateFirewallRequest
import hcloud.models.CreateFirewallResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetFirewallResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListFirewallsResponse
import hcloud.models.RemoveFromResourcesRequest
import hcloud.models.RemoveFromResourcesResponse
import hcloud.models.ReplaceFirewallRequest
import hcloud.models.ReplaceFirewallResponse
import hcloud.models.SetRulesRequest
import hcloud.models.SetRulesResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Firewalls

abstract class FirewallService {

  /** Applies a Firewall to multiple resources. Supported resources: - Servers
    * (with a public network interface) - Label Selectors A Server can be
    * applied to [a maximum of 5
    * Firewalls](https://docs.hetzner.com/cloud/firewalls/overview#limits). This
    * limit applies to Servers applied via a matching Label Selector as well.
    * Updates to resources matching or no longer matching a Label Selector can
    * take up to a few seconds to be processed. A Firewall is applied to a
    * resource once the related Action with command `apply_firewall`
    * successfully finished. #### Error Codes specific to this Call | Code |
    * Description | |-------------------------------|-------------------------------------------------------------------------------------------------| |
    * `firewall_already_applied` | Firewall is already applied to resource | |
    * `incompatible_network_type` | The network type of the resource is not
    * supported by Firewalls | | `firewall_resource_not_found` | The resource
    * the Firewall should be applied to was not found | |
    * `private_net_only_server` | The Server the Firewall should be applied to
    * has no public interface |
    *
    * Expected answers: code 201 : ApplyToResourcesResponse (The `actions` key
    * contains multiple Actions with the `apply_firewall` command.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Firewall.
    * @param applyToResourcesRequest
    */
  def applyToResources(
      token: String,
      id: Long,
      applyToResourcesRequest: ApplyToResourcesRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ApplyToResourcesResponse]
  ] =
    Firewalls()
      .withBearerTokenAuth(token)
      .applyToResources(id, applyToResourcesRequest)

  /** Create a Firewall. #### Error Codes specific to this Call | Code |
    * Description | |------------------------------ |-----------------------------------------------------------------------------| |
    * `server_already_added` | Server applied more than once | |
    * `incompatible_network_type` | The resources network type is not supported
    * by Firewalls | | `firewall_resource_not_found` | The resource the Firewall
    * should be attached to was not found |
    *
    * Expected answers: code 201 : CreateFirewallResponse (The `firewall` key
    * contains the created Firewall.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param createFirewallRequest
    */
  def createFirewall(
      token: String,
      createFirewallRequest: CreateFirewallRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateFirewallResponse]
  ] =
    Firewalls()
      .withBearerTokenAuth(token)
      .createFirewall(createFirewallRequest)

  /** Deletes the Firewall. #### Error Codes specific to this Call | Code |
    * Description | |--------------------- |----------------------------------------------------| |
    * `resource_in_use` | Firewall still applied to a resource |
    *
    * Expected answers: code 204 : (Firewall deleted.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Firewall.
    */
  def deleteFirewall(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    Firewalls().withBearerTokenAuth(token).deleteFirewall(id)

  /** Returns a specific Action for a Firewall.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key contains
    * the Firewall Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Firewall.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForFirewall(
      token: String,
      id: Long,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Firewalls().withBearerTokenAuth(token).getActionForFirewall(id, actionId)

  /** Returns a single Firewall.
    *
    * Expected answers: code 200 : GetFirewallResponse (The `firewall` key
    * contains the Firewall.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Firewall.
    */
  def getFirewall(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetFirewallResponse]
  ] =
    Firewalls().withBearerTokenAuth(token).getFirewall(id)

  /** Returns the specific Action.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key contains
    * the Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Action.
    */
  def getFirewallAction(id: Long, token: String): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Firewalls().withBearerTokenAuth(token).getFirewallAction(id)

  /** Returns all Actions for the Firewall. Use the provided URI parameters to
    * modify the result.
    *
    * Expected answers: code 200 : ListActionsResponse (The `actions` key
    * contains a list of Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Firewall.
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
  def listActionsForFirewall(
      token: String,
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Firewalls()
      .withBearerTokenAuth(token)
      .listActionsForFirewall(id, sort, status, page, perPage)

  /** Returns all Actions for Firewalls. Use the provided URI parameters to
    * modify the result.
    *
    * Expected answers: code 200 : ListActionsResponse (The `actions` key
    * contains a list of Actions.)
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
  def listFirewallActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Firewalls()
      .withBearerTokenAuth(token)
      .listFirewallActions(id, sort, status, page, perPage)

  /** Returns all Firewalls. Use the provided URI parameters to modify the
    * result.
    *
    * Expected answers: code 200 : ListFirewallsResponse (The `firewalls` key
    * contains the Firewalls.)
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
  def listFirewalls(
      token: String,
      sort: Seq[String],
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListFirewallsResponse]
  ] =
    Firewalls()
      .withBearerTokenAuth(token)
      .listFirewalls(sort, name, labelSelector, page, perPage)

  /** Removes a Firewall from multiple resources. Supported resources: - Servers
    * (with a public network interface) A Firewall is removed from a resource
    * once the related Action with command `remove_firewall` successfully
    * finished. #### Error Codes specific to this Call | Code | Description | |---------------------------------------|---------------------------------------------------------------------------------------------------------| |
    * `firewall_resource_not_found` | The resource the Firewall should be
    * removed from was not found | | `firewall_managed_by_label_selector` |
    * Firewall is applied via a Label Selector and cannot be removed manually |
    *
    * Expected answers: code 201 : RemoveFromResourcesResponse (The `actions`
    * key contains multiple Actions with the `remove_firewall` command.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Firewall.
    * @param removeFromResourcesRequest
    */
  def removeFromResources(
      token: String,
      id: Long,
      removeFromResourcesRequest: RemoveFromResourcesRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], RemoveFromResourcesResponse]
  ] =
    Firewalls()
      .withBearerTokenAuth(token)
      .removeFromResources(id, removeFromResourcesRequest)

  /** Update a Firewall. In case of a parallel running change on the Firewall a
    * `conflict` error will be returned.
    *
    * Expected answers: code 200 : ReplaceFirewallResponse (The `firewall` key
    * contains the updated Firewall.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Firewall.
    * @param replaceFirewallRequest
    */
  def replaceFirewall(
      token: String,
      id: Long,
      replaceFirewallRequest: ReplaceFirewallRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceFirewallResponse]
  ] =
    Firewalls()
      .withBearerTokenAuth(token)
      .replaceFirewall(id, replaceFirewallRequest)

  /** Set the rules of a Firewall. Overwrites the existing rules with the given
    * ones. Pass an empty array to remove all rules. Rules are limited to 50
    * entries per Firewall and [500 effective
    * rules](https://docs.hetzner.com/cloud/firewalls/overview#limits).
    *
    * Expected answers: code 201 : SetRulesResponse (The `action` key contains
    * an Action with the `set_firewall_rules` command and additionally one with
    * the `apply_firewall` command per resource of the Firewall.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Firewall.
    * @param setRulesRequest
    */
  def setRules(
      token: String,
      id: Long,
      setRulesRequest: SetRulesRequest
  ): sttp.client4.Request[Either[ResponseException[String], SetRulesResponse]] =
    Firewalls().withBearerTokenAuth(token).setRules(id, setRulesRequest)

}
