package services

import hcloud.models.AddServerToPlacementGroupRequest
import hcloud.models.AddServerToPlacementGroupResponse
import hcloud.models.AttachIsoToServerRequest
import hcloud.models.AttachIsoToServerResponse
import hcloud.models.AttachServerToNetworkRequest
import hcloud.models.AttachServerToNetworkResponse
import hcloud.models.ChangeAliasIpsOfNetworkRequest
import hcloud.models.ChangeAliasIpsOfNetworkResponse
import hcloud.models.ChangeReverseDnsEntryForThisServerRequest
import hcloud.models.ChangeReverseDnsEntryForThisServerResponse
import hcloud.models.ChangeServerProtectionRequest
import hcloud.models.ChangeServerProtectionResponse
import hcloud.models.ChangeTypeOfServerRequest
import hcloud.models.ChangeTypeOfServerResponse
import hcloud.models.CreateImageFromServerRequest
import hcloud.models.CreateImageFromServerResponse
import hcloud.models.CreateServerRequest
import hcloud.models.CreateServerResponse
import hcloud.models.DeleteServerResponse
import hcloud.models.DetachIsoFromServerResponse
import hcloud.models.DetachServerFromNetworkRequest
import hcloud.models.DetachServerFromNetworkResponse
import hcloud.models.DisableBackupsForServerResponse
import hcloud.models.DisableRescueModeForServerResponse
import hcloud.models.EnableAndConfigureBackupsForServerResponse
import hcloud.models.EnableRescueModeForServerRequest
import hcloud.models.EnableRescueModeForServerResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetMetricsForServerResponse
import hcloud.models.GetServerResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListServersResponse
import hcloud.models.PowerOffServerResponse
import hcloud.models.PowerOnServerResponse
import hcloud.models.RebuildServerFromImageRequest
import hcloud.models.RebuildServerFromImageResponse
import hcloud.models.RemoveFromPlacementGroupResponse
import hcloud.models.ReplaceServerRequest
import hcloud.models.ReplaceServerResponse
import hcloud.models.RequestConsoleForServerResponse
import hcloud.models.ResetRootPasswordOfServerResponse
import hcloud.models.ResetServerResponse
import hcloud.models.ShutdownServerResponse
import hcloud.models.SoftRebootServerResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Servers

abstract class ServerService {

  /** Adds a Server to a Placement Group. Server must be powered off for this
    * command to succeed. #### Call specific error codes | Code | Description | |-------------------------------|----------------------------------------------------------------------| |
    * `server_not_stopped` | The action requires a stopped server |
    *
    * Expected answers: code 201 : AddServerToPlacementGroupResponse (The
    * `action` key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param addServerToPlacementGroupRequest
    */
  def addServerToPlacementGroup(
      token: String,
      id: Long,
      addServerToPlacementGroupRequest: AddServerToPlacementGroupRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], AddServerToPlacementGroupResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .addServerToPlacementGroup(id, addServerToPlacementGroupRequest)

  /** Attaches an ISO to a Server. The Server will immediately see it as a new
    * disk. An already attached ISO will automatically be detached before the
    * new ISO is attached. Servers with attached ISOs have a modified boot
    * order: They will try to boot from the ISO first before falling back to
    * hard disk.
    *
    * Expected answers: code 201 : AttachIsoToServerResponse (The `action` key
    * in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param attachIsoToServerRequest
    */
  def attachIsoToServer(
      token: String,
      id: Long,
      attachIsoToServerRequest: AttachIsoToServerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], AttachIsoToServerResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .attachIsoToServer(id, attachIsoToServerRequest)

  /** Attaches a Server to a network. This will complement the fixed public
    * Server interface by adding an additional ethernet interface to the Server
    * which is connected to the specified network. The Server will get an IP
    * auto assigned from a subnet of type `server` in the same `network_zone`.
    * Using the `alias_ips` attribute you can also define one or more additional
    * IPs to the Servers. Please note that you will have to configure these IPs
    * by hand on your Server since only the primary IP will be given out by
    * DHCP. **Call specific error codes** | Code | Description | |----------------------------------|-----------------------------------------------------------------------| |
    * `server_already_attached` | The server is already attached to the network | |
    * `ip_not_available` | The provided Network IP is not available | |
    * `no_subnet_available` | No Subnet or IP is available for the Server within
    * the network | | `networks_overlap` | The network IP range overlaps with
    * one of the server networks |
    *
    * Expected answers: code 201 : AttachServerToNetworkResponse (The `action`
    * key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param attachServerToNetworkRequest
    */
  def attachServerToNetwork(
      token: String,
      id: Long,
      attachServerToNetworkRequest: AttachServerToNetworkRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], AttachServerToNetworkResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .attachServerToNetwork(id, attachServerToNetworkRequest)

  /** Changes the alias IPs of an already attached Network. Note that the
    * existing aliases for the specified Network will be replaced with these
    * provided in the request body. So if you want to add an alias IP, you have
    * to provide the existing ones from the Network plus the new alias IP in the
    * request body.
    *
    * Expected answers: code 201 : ChangeAliasIpsOfNetworkResponse (The `action`
    * key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param changeAliasIpsOfNetworkRequest
    */
  def changeAliasIpsOfNetwork(
      token: String,
      id: Long,
      changeAliasIpsOfNetworkRequest: ChangeAliasIpsOfNetworkRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeAliasIpsOfNetworkResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .changeAliasIpsOfNetwork(id, changeAliasIpsOfNetworkRequest)

  /** Changes the hostname that will appear when getting the hostname belonging
    * to the primary IPs (IPv4 and IPv6) of this Server. Floating IPs assigned
    * to the Server are not affected by this.
    *
    * Expected answers: code 201 : ChangeReverseDnsEntryForThisServerResponse
    * (The `action` key in the reply contains an Action object with this
    * structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param changeReverseDnsEntryForThisServerRequest
    *   Select the IP address for which to change the DNS entry by passing `ip`.
    *   It can be either IPv4 or IPv6. The target hostname is set by passing
    *   `dns_ptr`, which must be a fully qualified domain name (FQDN) without
    *   trailing dot.
    */
  def changeReverseDnsEntryForThisServer(
      token: String,
      id: Long,
      changeReverseDnsEntryForThisServerRequest: ChangeReverseDnsEntryForThisServerRequest
  ): sttp.client4.Request[Either[ResponseException[
    String
  ], ChangeReverseDnsEntryForThisServerResponse]] =
    Servers()
      .withBearerTokenAuth(token)
      .changeReverseDnsEntryForThisServer(
        id,
        changeReverseDnsEntryForThisServerRequest
      )

  /** Changes the protection configuration of the Server.
    *
    * Expected answers: code 201 : ChangeServerProtectionResponse (The `action`
    * key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param changeServerProtectionRequest
    */
  def changeServerProtection(
      token: String,
      id: Long,
      changeServerProtectionRequest: ChangeServerProtectionRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeServerProtectionResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .changeServerProtection(id, changeServerProtectionRequest)

  /** Changes the type (Cores, RAM and disk sizes) of a Server. Server must be
    * powered off for this command to succeed. This copies the content of its
    * disk, and starts it again. You can only migrate to Server types with the
    * same `storage_type` and equal or bigger disks. Shrinking disks is not
    * possible as it might destroy data. If the disk gets upgraded, the Server
    * type can not be downgraded any more. If you plan to downgrade the Server
    * type, set `upgrade_disk` to `false`. #### Call specific error codes | Code |
    * Description | |-------------------------------|----------------------------------------------------------------------| |
    * `invalid_server_type` | The server type does not fit for the given server
    * or is deprecated | | `server_not_stopped` | The action requires a stopped
    * server |
    *
    * Expected answers: code 201 : ChangeTypeOfServerResponse (The `action` key
    * in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param changeTypeOfServerRequest
    */
  def changeTypeOfServer(
      token: String,
      id: Long,
      changeTypeOfServerRequest: ChangeTypeOfServerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeTypeOfServerResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .changeTypeOfServer(id, changeTypeOfServerRequest)

  /** Creates an Image (snapshot) from a Server by copying the contents of its
    * disks. This creates a snapshot of the current state of the disk and copies
    * it into an Image. If the Server is currently running you must make sure
    * that its disk content is consistent. Otherwise, the created Image may not
    * be readable. To make sure disk content is consistent, we recommend to shut
    * down the Server prior to creating an Image. You can either create a
    * `backup` Image that is bound to the Server and therefore will be deleted
    * when the Server is deleted, or you can create a `snapshot` Image which is
    * completely independent of the Server it was created from and will survive
    * Server deletion. Backup Images are only available when the backup option
    * is enabled for the Server. Snapshot Images are billed on a per GB basis.
    *
    * Expected answers: code 201 : CreateImageFromServerResponse (The `image`
    * key in the reply contains an the created Image, which is an object with
    * this structure. The `action` key in the reply contains an Action object
    * with this structure. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param createImageFromServerRequest
    */
  def createImageFromServer(
      token: String,
      id: Long,
      createImageFromServerRequest: CreateImageFromServerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateImageFromServerResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .createImageFromServer(id, createImageFromServerRequest)

  /** Creates a new Server. Returns preliminary information about the Server as
    * well as an Action that covers progress of creation.
    *
    * Expected answers: code 201 : CreateServerResponse (The `server` key in the
    * reply contains a Server object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param createServerRequest
    *   Please note that Server names must be unique per Project and valid
    *   hostnames as per RFC 1123 (i.e. may only contain letters, digits,
    *   periods, and dashes). For `server_type` you can either use the ID as
    *   listed in `/server_types` or its name. For `image` you can either use
    *   the ID as listed in `/images` or its name. If you want to create the
    *   Server in a Location, you must set `location` to the ID or name as
    *   listed in `/locations`. This is the recommended way. You can be even
    *   more specific by setting `datacenter` to the ID or name as listed in
    *   `/datacenters`. However we only recommend this if you want to assign a
    *   specific Primary IP to the Server which is located in the specified Data
    *   Center. Some properties like `start_after_create` or `automount` will
    *   trigger Actions after the Server is created. Those Actions are listed in
    *   the `next_actions` field in the response. For accessing your Server we
    *   strongly recommend to use SSH keys by passing the respective key IDs in
    *   `ssh_keys`. If you do not specify any `ssh_keys` we will generate a root
    *   password for you and return it in the response. Please note that
    *   provided user-data is stored in our systems. While we take measures to
    *   protect it we highly recommend that you don’t use it to store passwords
    *   or other sensitive information. #### Call specific error codes | Code |
    *   Description | |----------------------------------|------------------------------------------------------------| |
    *   `placement_error` | An error during the placement occurred | |
    *   `primary_ip_assigned` | The specified Primary IP is already assigned to
    *   a server | | `primary_ip_datacenter_mismatch` | The specified Primary IP
    *   is in a different datacenter | | `primary_ip_version_mismatch` | The
    *   specified Primary IP has the wrong IP Version |
    */
  def createServer(
      token: String,
      createServerRequest: CreateServerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateServerResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .createServer(createServerRequest)

  /** Deletes a Server. This immediately removes the Server from your account,
    * and it is no longer accessible. Any resources attached to the server (like
    * Volumes, Primary IPs, Floating IPs, Firewalls, Placement Groups) are
    * detached while the server is deleted.
    *
    * Expected answers: code 200 : DeleteServerResponse (The `action` key in the
    * reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def deleteServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], DeleteServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).deleteServer(id)

  /** Detaches an ISO from a Server. In case no ISO Image is attached to the
    * Server, the status of the returned Action is immediately set to `success`.
    *
    * Expected answers: code 201 : DetachIsoFromServerResponse (The `action` key
    * in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def detachIsoFromServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], DetachIsoFromServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).detachIsoFromServer(id)

  /** Detaches a Server from a network. The interface for this network will
    * vanish.
    *
    * Expected answers: code 201 : DetachServerFromNetworkResponse (The `action`
    * key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param detachServerFromNetworkRequest
    */
  def detachServerFromNetwork(
      token: String,
      id: Long,
      detachServerFromNetworkRequest: DetachServerFromNetworkRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], DetachServerFromNetworkResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .detachServerFromNetwork(id, detachServerFromNetworkRequest)

  /** Disables the automatic backup option and deletes all existing Backups for
    * a Server. No more additional charges for backups will be made. Caution:
    * This immediately removes all existing backups for the Server!
    *
    * Expected answers: code 201 : DisableBackupsForServerResponse (The `action`
    * key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def disableBackupsForServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], DisableBackupsForServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).disableBackupsForServer(id)

  /** Disables the Hetzner Rescue System for a Server. This makes a Server start
    * from its disks on next reboot. Rescue Mode is automatically disabled when
    * you first boot into it or if you do not use it for 60 minutes. Disabling
    * rescue mode will not reboot your Server — you will have to do this
    * yourself.
    *
    * Expected answers: code 201 : DisableRescueModeForServerResponse (The
    * `action` key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def disableRescueModeForServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], DisableRescueModeForServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).disableRescueModeForServer(id)

  /** Enables and configures the automatic daily backup option for the Server.
    * Enabling automatic backups will increase the price of the Server by 20%.
    * In return, you will get seven slots where Images of type backup can be
    * stored. Backups are automatically created daily.
    *
    * Expected answers: code 201 : EnableAndConfigureBackupsForServerResponse
    * (The `action` key in the reply contains an Action object with this
    * structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def enableAndConfigureBackupsForServer(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[
    String
  ], EnableAndConfigureBackupsForServerResponse]] =
    Servers()
      .withBearerTokenAuth(token)
      .enableAndConfigureBackupsForServer(id)

  /** Enable the Hetzner Rescue System for this Server. The next time a Server
    * with enabled rescue mode boots it will start a special minimal Linux
    * distribution designed for repair and reinstall. In case a Server cannot
    * boot on its own you can use this to access a Server’s disks. Rescue Mode
    * is automatically disabled when you first boot into it or if you do not use
    * it for 60 minutes. Enabling rescue mode will not
    * [reboot](https://docs.hetzner.cloud/#server-actions-soft-reboot-a-server)
    * your Server — you will have to do this yourself.
    *
    * Expected answers: code 201 : EnableRescueModeForServerResponse (The
    * `root_password` key in the reply contains the root password that can be
    * used to access the booted rescue system. The `action` key in the reply
    * contains an Action object with this structure. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param enableRescueModeForServerRequest
    */
  def enableRescueModeForServer(
      token: String,
      id: Long,
      enableRescueModeForServerRequest: EnableRescueModeForServerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], EnableRescueModeForServerResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .enableRescueModeForServer(id, enableRescueModeForServerRequest)

  /** Returns a specific Action object for a Server.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key in the
    * reply has this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForServer(
      token: String,
      id: Long,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Servers().withBearerTokenAuth(token).getActionForServer(id, actionId)

  /** Get Metrics for specified Server. You must specify the type of metric to
    * get: cpu, disk or network. You can also specify more than one type by
    * comma separation, e.g. cpu,disk. Depending on the type you will get
    * different time series data | Type | Timeseries | Unit | Description | |---------|-------------------------|-----------|------------------------------------------------------| |
    * cpu | cpu | percent | Percent CPU usage | | disk | disk.0.iops.read |
    * iop/s | Number of read IO operations per second | | | disk.0.iops.write |
    * iop/s | Number of write IO operations per second | | |
    * disk.0.bandwidth.read | bytes/s | Bytes read per second | | |
    * disk.0.bandwidth.write | bytes/s | Bytes written per second | | network |
    * network.0.pps.in | packets/s | Public Network interface packets per second
    * received | | | network.0.pps.out | packets/s | Public Network interface
    * packets per second sent | | | network.0.bandwidth.in | bytes/s | Public
    * Network interface bytes/s received | | | network.0.bandwidth.out | bytes/s |
    * Public Network interface bytes/s sent | Metrics are available for the last
    * 30 days only. If you do not provide the step argument we will
    * automatically adjust it so that a maximum of 200 samples are returned. We
    * limit the number of samples returned to a maximum of 500 and will adjust
    * the step parameter accordingly.
    *
    * Expected answers: code 200 : GetMetricsForServerResponse (The `metrics`
    * key in the reply contains a metrics object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param `type`
    *   Type of metrics to get.
    * @param start
    *   Start of period to get Metrics for (in ISO-8601 format).
    * @param end
    *   End of period to get Metrics for (in ISO-8601 format).
    * @param step
    *   Resolution of results in seconds.
    */
  def getMetricsForServer(
      token: String,
      id: Long,
      `type`: Seq[String],
      start: String,
      end: String,
      step: Option[String] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], GetMetricsForServerResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .getMetricsForServer(id, `type`, start, end, step)

  /** Returns a specific Server object. The Server must exist inside the
    * Project.
    *
    * Expected answers: code 200 : GetServerResponse (The `server` key in the
    * reply contains a Server object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def getServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).getServer(id)

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
  def getServerAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Servers().withBearerTokenAuth(token).getServerAction(id)

  /** Returns all Action objects for a Server. You can `sort` the results by
    * using the sort URI parameter, and filter them with the `status` parameter.
    *
    * Expected answers: code 200 : ListActionsResponse (The `actions` key
    * contains a list of Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
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
  def listActionsForServer(
      token: String,
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .listActionsForServer(id, sort, status, page, perPage)

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
  def listServerActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .listServerActions(id, sort, status, page, perPage)

  /** Returns all existing Server objects.
    *
    * Expected answers: code 200 : ListServersResponse (A paged array of
    * servers.) Headers : x-next - A link to the next page of responses.
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
    * @param status
    *   Filter resources by status. Can be used multiple times. The response
    *   will only contain the resources with the specified status.
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listServers(
      token: String,
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListServersResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .listServers(name, labelSelector, sort, status, page, perPage)

  /** Cuts power to the Server. This forcefully stops it without giving the
    * Server operating system time to gracefully stop. May lead to data loss,
    * equivalent to pulling the power cord. Power off should only be used when
    * shutdown does not work.
    *
    * Expected answers: code 201 : PowerOffServerResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def powerOffServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], PowerOffServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).powerOffServer(id)

  /** Starts a Server by turning its power on.
    *
    * Expected answers: code 201 : PowerOnServerResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def powerOnServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], PowerOnServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).powerOnServer(id)

  /** Rebuilds a Server overwriting its disk with the content of an Image,
    * thereby **destroying all data** on the target Server The Image can either
    * be one you have created earlier (`backup` or `snapshot` Image) or it can
    * be a completely fresh `system` Image provided by us. You can get a list of
    * all available Images with `GET /images`. Your Server will automatically be
    * powered off before the rebuild command executes.
    *
    * Expected answers: code 201 : RebuildServerFromImageResponse (The `action`
    * key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param rebuildServerFromImageRequest
    *   To select which Image to rebuild from you can either pass an ID or a
    *   name as the `image` argument. Passing a name only works for `system`
    *   Images since the other Image types do not have a name set.
    */
  def rebuildServerFromImage(
      token: String,
      id: Long,
      rebuildServerFromImageRequest: RebuildServerFromImageRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], RebuildServerFromImageResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .rebuildServerFromImage(id, rebuildServerFromImageRequest)

  /** Removes a Server from a Placement Group.
    *
    * Expected answers: code 201 : RemoveFromPlacementGroupResponse (The
    * `action` key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def removeFromPlacementGroup(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], RemoveFromPlacementGroupResponse]
  ] =
    Servers().withBearerTokenAuth(token).removeFromPlacementGroup(id)

  /** Updates a Server. You can update a Server’s name and a Server’s labels.
    * Please note that Server names must be unique per Project and valid
    * hostnames as per RFC 1123 (i.e. may only contain letters, digits, periods,
    * and dashes).
    *
    * Expected answers: code 200 : ReplaceServerResponse (The `server` key in
    * the reply contains the updated Server.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param replaceServerRequest
    */
  def replaceServer(
      token: String,
      id: Long,
      replaceServerRequest: ReplaceServerRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceServerResponse]
  ] =
    Servers()
      .withBearerTokenAuth(token)
      .replaceServer(id, replaceServerRequest)

  /** Requests credentials for remote access via VNC over websocket to keyboard,
    * monitor, and mouse for a Server. The provided URL is valid for 1 minute,
    * after this period a new url needs to be created to connect to the Server.
    * How long the connection is open after the initial connect is not subject
    * to this timeout.
    *
    * Expected answers: code 201 : RequestConsoleForServerResponse (The `action`
    * key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def requestConsoleForServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], RequestConsoleForServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).requestConsoleForServer(id)

  /** Resets the root password. Only works for Linux systems that are running
    * the qemu guest agent. Server must be powered on (status `running`) in
    * order for this operation to succeed. This will generate a new password for
    * this Server and return it. If this does not succeed you can use the rescue
    * system to netboot the Server and manually change your Server password by
    * hand.
    *
    * Expected answers: code 201 : ResetRootPasswordOfServerResponse (The
    * `root_password` key in the reply contains the new root password that will
    * be active if the Action succeeds. The `action` key in the reply contains
    * an Action object with this structure. )
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def resetRootPasswordOfServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], ResetRootPasswordOfServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).resetRootPasswordOfServer(id)

  /** Cuts power to a Server and starts it again. This forcefully stops it
    * without giving the Server operating system time to gracefully stop. This
    * may lead to data loss, it’s equivalent to pulling the power cord and
    * plugging it in again. Reset should only be used when reboot does not work.
    *
    * Expected answers: code 201 : ResetServerResponse (The `action` key in the
    * reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def resetServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], ResetServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).resetServer(id)

  /** Shuts down a Server gracefully by sending an ACPI shutdown request. The
    * Server operating system must support ACPI and react to the request,
    * otherwise the Server will not shut down. Please note that the `action`
    * status in this case only reflects whether the action was sent to the
    * server. It does not mean that the server actually shut down successfully.
    * If you need to ensure that the server is off, use the `poweroff` action.
    *
    * Expected answers: code 201 : ShutdownServerResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def shutdownServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], ShutdownServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).shutdownServer(id)

  /** Reboots a Server gracefully by sending an ACPI request. The Server
    * operating system must support ACPI and react to the request, otherwise the
    * Server will not reboot.
    *
    * Expected answers: code 201 : SoftRebootServerResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def softRebootServer(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], SoftRebootServerResponse]
  ] =
    Servers().withBearerTokenAuth(token).softRebootServer(id)

}
