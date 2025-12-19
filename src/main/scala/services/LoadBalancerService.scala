package services

import hcloud.models.AddServiceResponse
import hcloud.models.AddTargetResponse
import hcloud.models.AttachLoadBalancerToNetworkRequest
import hcloud.models.AttachLoadBalancerToNetworkResponse
import hcloud.models.ChangeAlgorithmResponse
import hcloud.models.ChangeLoadBalancerProtectionRequest
import hcloud.models.ChangeLoadBalancerProtectionResponse
import hcloud.models.ChangeReverseDnsEntryForThisLoadBalancerRequest
import hcloud.models.ChangeReverseDnsEntryForThisLoadBalancerResponse
import hcloud.models.ChangeTypeOfLoadBalancerRequest
import hcloud.models.ChangeTypeOfLoadBalancerResponse
import hcloud.models.CreateLoadBalancerRequest
import hcloud.models.CreateLoadBalancerResponse
import hcloud.models.DeleteServiceRequest
import hcloud.models.DeleteServiceResponse
import hcloud.models.DetachLoadBalancerFromNetworkRequest
import hcloud.models.DetachLoadBalancerFromNetworkResponse
import hcloud.models.DisablePublicInterfaceOfLoadBalancerResponse
import hcloud.models.EnablePublicInterfaceOfLoadBalancerResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetLoadBalancerResponse
import hcloud.models.GetMetricsForLoadbalancerResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListLoadBalancersResponse
import hcloud.models.LoadBalancerAddTarget
import hcloud.models.LoadBalancerAlgorithm
import hcloud.models.LoadBalancerService
import hcloud.models.RemoveTargetRequest
import hcloud.models.RemoveTargetResponse
import hcloud.models.ReplaceLoadBalancerRequest
import hcloud.models.ReplaceLoadBalancerResponse
import hcloud.models.UpdateLoadBalancerService
import hcloud.models.UpdateServiceResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.LoadBalancers

abstract class LdBalancerService {

  /** Adds a service to a Load Balancer. #### Call specific error codes | Code |
    * Description | |----------------------------|---------------------------------------------------------| |
    * `source_port_already_used` | The source port you are trying to add is
    * already in use |
    *
    * Expected answers: code 201 : AddServiceResponse (The `action` key contains
    * the `add_service` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param body
    */
  def addService(
      token: String,
      id: Long,
      body: LoadBalancerService
  ): sttp.client4.Request[
    Either[ResponseException[String], AddServiceResponse]
  ] =
    LoadBalancers().withBearerTokenAuth(token).addService(id, body)

  /** Adds a target to a Load Balancer. #### Call specific error codes | Code |
    * Description | |-------------------------------------------|-------------------------------------------------------------------------------------------------------| |
    * `cloud_resource_ip_not_allowed` | The IP you are trying to add as a target
    * belongs to a Hetzner Cloud resource | | `ip_not_owned` | The IP you are
    * trying to add as a target is not owned by the Project owner | |
    * `load_balancer_public_interface_disabled` | The Load Balancer's public
    * network interface is disabled | | `load_balancer_not_attached_to_network` |
    * The Load Balancer is not attached to a network | | `robot_unavailable` |
    * Robot was not available. The caller may retry the operation after a short
    * delay. | | `server_not_attached_to_network` | The server you are trying to
    * add as a target is not attached to the same network as the Load Balancer | |
    * `missing_ipv4` | The server that you are trying to add as a public target
    * does not have a public IPv4 address | | `target_already_defined` | The
    * Load Balancer target you are trying to define is already defined |
    *
    * Expected answers: code 201 : AddTargetResponse (The `action` key contains
    * the `add_target` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param body
    */
  def addTarget(
      token: String,
      id: Long,
      body: LoadBalancerAddTarget
  ): sttp.client4.Request[
    Either[ResponseException[String], AddTargetResponse]
  ] =
    LoadBalancers().withBearerTokenAuth(token).addTarget(id, body)

  /** Attach a Load Balancer to a Network. **Call specific error codes** | Code |
    * Description | |----------------------------------|-----------------------------------------------------------------------| |
    * `load_balancer_already_attached` | The Load Balancer is already attached
    * to a network | | `ip_not_available` | The provided Network IP is not
    * available | | `no_subnet_available` | No Subnet or IP is available for the
    * Load Balancer within the network |
    *
    * Expected answers: code 201 : AttachLoadBalancerToNetworkResponse (The
    * `action` key contains the `attach_to_network` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param attachLoadBalancerToNetworkRequest
    */
  def attachLoadBalancerToNetwork(
      token: String,
      id: Long,
      attachLoadBalancerToNetworkRequest: AttachLoadBalancerToNetworkRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], AttachLoadBalancerToNetworkResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .attachLoadBalancerToNetwork(id, attachLoadBalancerToNetworkRequest)

  /** Change the algorithm that determines to which target new requests are
    * sent.
    *
    * Expected answers: code 201 : ChangeAlgorithmResponse (The `action` key
    * contains the `change_algorithm` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param body
    */
  def changeAlgorithm(
      token: String,
      id: Long,
      body: LoadBalancerAlgorithm
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeAlgorithmResponse]
  ] =
    LoadBalancers().withBearerTokenAuth(token).changeAlgorithm(id, body)

  /** Changes the protection configuration of a Load Balancer.
    *
    * Expected answers: code 201 : ChangeLoadBalancerProtectionResponse (The
    * `action` key contains the `change_protection` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param changeLoadBalancerProtectionRequest
    */
  def changeLoadBalancerProtection(
      token: String,
      id: Long,
      changeLoadBalancerProtectionRequest: ChangeLoadBalancerProtectionRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeLoadBalancerProtectionResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .changeLoadBalancerProtection(id, changeLoadBalancerProtectionRequest)

  /** Changes the hostname that will appear when getting the hostname belonging
    * to the public IPs (IPv4 and IPv6) of this Load Balancer. Floating IPs
    * assigned to the Server are not affected by this.
    *
    * Expected answers: code 201 :
    * ChangeReverseDnsEntryForThisLoadBalancerResponse (The `action` key in the
    * reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param changeReverseDnsEntryForThisLoadBalancerRequest
    *   Select the IP address for which to change the DNS entry by passing `ip`.
    *   It can be either IPv4 or IPv6. The target hostname is set by passing
    *   `dns_ptr`, which must be a fully qualified domain name (FQDN) without
    *   trailing dot.
    */
  def changeReverseDnsEntryForThisLoadBalancer(
      token: String,
      id: Long,
      changeReverseDnsEntryForThisLoadBalancerRequest: ChangeReverseDnsEntryForThisLoadBalancerRequest
  ): sttp.client4.Request[Either[ResponseException[
    String
  ], ChangeReverseDnsEntryForThisLoadBalancerResponse]] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .changeReverseDnsEntryForThisLoadBalancer(
        id,
        changeReverseDnsEntryForThisLoadBalancerRequest
      )

  /** Changes the type (Max Services, Max Targets and Max Connections) of a Load
    * Balancer. **Call specific error codes** | Code | Description | |------------------------------|-----------------------------------------------------------------| |
    * `invalid_load_balancer_type` | The Load Balancer type does not fit for the
    * given Load Balancer |
    *
    * Expected answers: code 201 : ChangeTypeOfLoadBalancerResponse (The
    * `action` key contains the `change_load_balancer_type` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param changeTypeOfLoadBalancerRequest
    */
  def changeTypeOfLoadBalancer(
      token: String,
      id: Long,
      changeTypeOfLoadBalancerRequest: ChangeTypeOfLoadBalancerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeTypeOfLoadBalancerResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .changeTypeOfLoadBalancer(id, changeTypeOfLoadBalancerRequest)

  /** Creates a Load Balancer. #### Call specific error codes | Code |
    * Description | |-----------------------------------------|-------------------------------------------------------------------------------------------------------| |
    * `cloud_resource_ip_not_allowed` | The IP you are trying to add as a target
    * belongs to a Hetzner Cloud resource | | `ip_not_owned` | The IP is not
    * owned by the owner of the project of the Load Balancer | |
    * `load_balancer_not_attached_to_network` | The Load Balancer is not
    * attached to a network | | `robot_unavailable` | Robot was not available.
    * The caller may retry the operation after a short delay. | |
    * `server_not_attached_to_network` | The server you are trying to add as a
    * target is not attached to the same network as the Load Balancer | |
    * `source_port_already_used` | The source port you are trying to add is
    * already in use | | `missing_ipv4` | The server that you are trying to add
    * as a public target does not have a public IPv4 address | |
    * `target_already_defined` | The Load Balancer target you are trying to
    * define is already defined |
    *
    * Expected answers: code 201 : CreateLoadBalancerResponse (The
    * `load_balancer` key contains the Load Balancer that was just created.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param createLoadBalancerRequest
    */
  def createLoadBalancer(
      token: String,
      createLoadBalancerRequest: CreateLoadBalancerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateLoadBalancerResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .createLoadBalancer(createLoadBalancerRequest)

  /** Deletes a Load Balancer.
    *
    * Expected answers: code 204 : (Load Balancer deleted.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    */
  def deleteLoadBalancer(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    LoadBalancers().withBearerTokenAuth(token).deleteLoadBalancer(id)

  /** Delete a service of a Load Balancer.
    *
    * Expected answers: code 201 : DeleteServiceResponse (The `action` key
    * contains the `delete_service` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param deleteServiceRequest
    */
  def deleteService(
      token: String,
      id: Long,
      deleteServiceRequest: DeleteServiceRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], DeleteServiceResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .deleteService(id, deleteServiceRequest)

  /** Detaches a Load Balancer from a network.
    *
    * Expected answers: code 201 : DetachLoadBalancerFromNetworkResponse (The
    * `action` key contains the `detach_from_network` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param detachLoadBalancerFromNetworkRequest
    */
  def detachLoadBalancerFromNetwork(
      token: String,
      id: Long,
      detachLoadBalancerFromNetworkRequest: DetachLoadBalancerFromNetworkRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], DetachLoadBalancerFromNetworkResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .detachLoadBalancerFromNetwork(id, detachLoadBalancerFromNetworkRequest)

  /** Disable the public interface of a Load Balancer. The Load Balancer will be
    * not accessible from the internet via its public IPs. #### Call specific
    * error codes | Code | Description | |-------------------------------------------|--------------------------------------------------------------------------------| |
    * `load_balancer_not_attached_to_network` | The Load Balancer is not
    * attached to a network | | `targets_without_use_private_ip` | The Load
    * Balancer has targets that use the public IP instead of the private IP |
    *
    * Expected answers: code 201 : DisablePublicInterfaceOfLoadBalancerResponse
    * (The `action` key contains the `disable_public_interface` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    */
  def disablePublicInterfaceOfLoadBalancer(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[
    String
  ], DisablePublicInterfaceOfLoadBalancerResponse]] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .disablePublicInterfaceOfLoadBalancer(id)

  /** Enable the public interface of a Load Balancer. The Load Balancer will be
    * accessible from the internet via its public IPs.
    *
    * Expected answers: code 201 : EnablePublicInterfaceOfLoadBalancerResponse
    * (The `action` key contains the `enable_public_interface` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    */
  def enablePublicInterfaceOfLoadBalancer(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[
    String
  ], EnablePublicInterfaceOfLoadBalancerResponse]] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .enablePublicInterfaceOfLoadBalancer(id)

  /** Returns a specific Action for a Load Balancer.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key contains
    * the Load Balancer Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForLoadBalancer(
      token: String,
      id: Long,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .getActionForLoadBalancer(id, actionId)

  /** Gets a specific Load Balancer object.
    *
    * Expected answers: code 200 : GetLoadBalancerResponse (The `load_balancer`
    * key contains the Load Balancer.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    */
  def getLoadBalancer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetLoadBalancerResponse]
  ] =
    LoadBalancers().withBearerTokenAuth(token).getLoadBalancer(id)

  /** Returns a specific Action object.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key in the
    * reply has this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Action.
    */
  def getLoadBalancerAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    LoadBalancers().withBearerTokenAuth(token).getLoadBalancerAction(id)

  /** You must specify the type of metric to get: `open_connections`,
    * `connections_per_second`, `requests_per_second` or `bandwidth`. You can
    * also specify more than one type by comma separation, e.g.
    * `requests_per_second,bandwidth`. Depending on the type you will get
    * different time series data: |Type | Timeseries | Unit | Description | |---- |------------|------|-------------| |
    * open_connections | open_connections | number | Open connections | |
    * connections_per_second | connections_per_second | connections/s |
    * Connections per second | | requests_per_second | requests_per_second |
    * requests/s | Requests per second | | bandwidth | bandwidth.in | bytes/s |
    * Ingress bandwidth | || bandwidth.out | bytes/s | Egress bandwidth |
    * Metrics are available for the last 30 days only. If you do not provide the
    * step argument we will automatically adjust it so that 200 samples are
    * returned. We limit the number of samples to a maximum of 500 and will
    * adjust the step parameter accordingly.
    *
    * Expected answers: code 200 : GetMetricsForLoadbalancerResponse (The
    * `metrics` key in the reply contains a metrics object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param `type`
    *   Type of metrics to get.
    * @param start
    *   Start of period to get Metrics for (in ISO-8601 format).
    * @param end
    *   End of period to get Metrics for (in ISO-8601 format).
    * @param step
    *   Resolution of results in seconds.
    */
  def getMetricsForLoadbalancer(
      token: String,
      id: Long,
      `type`: Seq[String],
      start: String,
      end: String,
      step: Option[String] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], GetMetricsForLoadbalancerResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .getMetricsForLoadbalancer(id, `type`, start, end, step)

  /** Returns all Action objects for a Load Balancer. You can sort the results
    * by using the `sort` URI parameter, and filter them with the `status`
    * parameter.
    *
    * Expected answers: code 200 : ListActionsResponse (The `actions` key
    * contains a list of Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
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
  def listActionsForLoadBalancer(
      token: String,
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .listActionsForLoadBalancer(id, sort, status, page, perPage)

  /** Returns all Action objects. You can `sort` the results by using the sort
    * URI parameter, and filter them with the `status` and `id` parameter.
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
  def listLoadBalancerActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .listLoadBalancerActions(id, sort, status, page, perPage)

  /** Gets all existing Load Balancers that you have available.
    *
    * Expected answers: code 200 : ListLoadBalancersResponse (The
    * `load_balancers` key contains a list of Load Balancers.)
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
  def listLoadBalancers(
      token: String,
      sort: Seq[String],
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListLoadBalancersResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .listLoadBalancers(sort, name, labelSelector, page, perPage)

  /** Removes a target from a Load Balancer.
    *
    * Expected answers: code 201 : RemoveTargetResponse (The `action` key
    * contains the `remove_target` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param removeTargetRequest
    */
  def removeTarget(
      token: String,
      id: Long,
      removeTargetRequest: RemoveTargetRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], RemoveTargetResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .removeTarget(id, removeTargetRequest)

  /** Updates a Load Balancer. You can update a Load Balancer’s name and a Load
    * Balancer’s labels. Note: if the Load Balancer object changes during the
    * request, the response will be a “conflict” error.
    *
    * Expected answers: code 200 : ReplaceLoadBalancerResponse (The
    * `load_balancer` key contains the updated Load Balancer.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param replaceLoadBalancerRequest
    */
  def replaceLoadBalancer(
      token: String,
      id: Long,
      replaceLoadBalancerRequest: ReplaceLoadBalancerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceLoadBalancerResponse]
  ] =
    LoadBalancers()
      .withBearerTokenAuth(token)
      .replaceLoadBalancer(id, replaceLoadBalancerRequest)

  /** Updates a Load Balancer Service. #### Call specific error codes | Code |
    * Description | |----------------------------|---------------------------------------------------------| |
    * `source_port_already_used` | The source port you are trying to add is
    * already in use |
    *
    * Expected answers: code 201 : UpdateServiceResponse (The `action` key
    * contains the `update_service` Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Load Balancer.
    * @param body
    */
  def updateService(
      token: String,
      id: Long,
      body: UpdateLoadBalancerService
  ): sttp.client4.Request[
    Either[ResponseException[String], UpdateServiceResponse]
  ] =
    LoadBalancers().withBearerTokenAuth(token).updateService(id, body)

}
