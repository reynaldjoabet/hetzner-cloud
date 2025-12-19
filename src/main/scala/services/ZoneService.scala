package services

import hcloud.models.AddRecordsToRrsetRequest
import hcloud.models.AddRecordsToRrsetResponse
import hcloud.models.ChangeRrsetsProtectionRequest
import hcloud.models.ChangeRrsetsProtectionResponse
import hcloud.models.ChangeRrsetsTtlRequest
import hcloud.models.ChangeRrsetsTtlResponse
import hcloud.models.ChangeZonesDefaultTtlRequest
import hcloud.models.ChangeZonesDefaultTtlResponse
import hcloud.models.ChangeZonesPrimaryNameserversRequest
import hcloud.models.ChangeZonesPrimaryNameserversResponse
import hcloud.models.ChangeZonesProtectionRequest
import hcloud.models.ChangeZonesProtectionResponse
import hcloud.models.CreateRrsetResponse
import hcloud.models.CreateZoneRequest
import hcloud.models.CreateZoneResponse
import hcloud.models.DeleteRrsetResponse
import hcloud.models.DeleteZoneResponse
import hcloud.models.ExportZoneFileResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetRrsetResponse
import hcloud.models.GetZoneResponse
import hcloud.models.ImportZoneFileRequest
import hcloud.models.ImportZoneFileResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListRrsetsResponse
import hcloud.models.ListZonesResponse
import hcloud.models.RemoveRecordsFromRrsetRequest
import hcloud.models.RemoveRecordsFromRrsetResponse
import hcloud.models.ReplaceRrsetRequest
import hcloud.models.ReplaceRrsetResponse
import hcloud.models.ReplaceZoneRequest
import hcloud.models.ReplaceZoneResponse
import hcloud.models.ResourceRecordSetToCreate
import hcloud.models.SetRecordsOfRrsetRequest
import hcloud.models.SetRecordsOfRrsetResponse
import hcloud.models.UpdateRecordsOfRrsetRequest
import hcloud.models.UpdateRecordsOfRrsetResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Zones

abstract class ZoneService {

  /** Adds resource records (RRs) to an RRSet in the Zone. For convenience, the
    * RRSet will be automatically created if it doesn't exist. Otherwise, the
    * new records are appended to the existing RRSet. Only applicable for Zones
    * in primary mode. #### Call specific error codes | Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 201 : AddRecordsToRrsetResponse (Request
    * succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param rrName
    * @param rrType
    * @param addRecordsToRrsetRequest
    */
  def addRecordsToRrset(
      token: String,
      idOrName: String,
      rrName: String,
      rrType: String,
      addRecordsToRrsetRequest: AddRecordsToRrsetRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], AddRecordsToRrsetResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .addRecordsToRrset(idOrName, rrName, rrType, addRecordsToRrsetRequest)

  /** Changes the protection of an RRSet in the Zone. Only applicable for Zones
    * in primary mode. #### Call specific error codes | Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 201 : ChangeRrsetsProtectionResponse (Request
    * succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param rrName
    * @param rrType
    * @param changeRrsetsProtectionRequest
    */
  def changeRrsetsProtection(
      token: String,
      idOrName: String,
      rrName: String,
      rrType: String,
      changeRrsetsProtectionRequest: ChangeRrsetsProtectionRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeRrsetsProtectionResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .changeRrsetsProtection(
        idOrName,
        rrName,
        rrType,
        changeRrsetsProtectionRequest
      )

  /** Changes the Time To Live (TTL) of an RRSet in the Zone. Only applicable
    * for Zones in primary mode. #### Call specific error codes | Code |
    * Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 201 : ChangeRrsetsTtlResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param rrName
    * @param rrType
    * @param changeRrsetsTtlRequest
    */
  def changeRrsetsTtl(
      token: String,
      idOrName: String,
      rrName: String,
      rrType: String,
      changeRrsetsTtlRequest: ChangeRrsetsTtlRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeRrsetsTtlResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .changeRrsetsTtl(idOrName, rrName, rrType, changeRrsetsTtlRequest)

  /** Changes the default Time To Live (TTL) of a Zone. This TTL is used for
    * RRSets that do not explicitly define a TTL. Only applicable for Zones in
    * primary mode. #### Call specific error codes | Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 201 : ChangeZonesDefaultTtlResponse (Request
    * succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param changeZonesDefaultTtlRequest
    */
  def changeZonesDefaultTtl(
      token: String,
      idOrName: String,
      changeZonesDefaultTtlRequest: ChangeZonesDefaultTtlRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeZonesDefaultTtlResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .changeZonesDefaultTtl(idOrName, changeZonesDefaultTtlRequest)

  /** Overwrites the primary nameservers of a Zone. Only applicable for Zones in
    * secondary mode. #### Call specific error codes | Code | Description | |-----------------------|------------------------------------| |
    * `incorrect_zone_mode` | The zone is not in secondary mode. |
    *
    * Expected answers: code 201 : ChangeZonesPrimaryNameserversResponse
    * (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param changeZonesPrimaryNameserversRequest
    */
  def changeZonesPrimaryNameservers(
      token: String,
      idOrName: String,
      changeZonesPrimaryNameserversRequest: ChangeZonesPrimaryNameserversRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeZonesPrimaryNameserversResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .changeZonesPrimaryNameservers(
        idOrName,
        changeZonesPrimaryNameserversRequest
      )

  /** Changes the protection configuration of a Zone.
    *
    * Expected answers: code 201 : ChangeZonesProtectionResponse (Request
    * succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param changeZonesProtectionRequest
    */
  def changeZonesProtection(
      token: String,
      idOrName: String,
      changeZonesProtectionRequest: ChangeZonesProtectionRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeZonesProtectionResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .changeZonesProtection(idOrName, changeZonesProtectionRequest)

  /** Create an RRSet in the Zone. Only applicable for Zones in primary mode. |
    * Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 201 : CreateRrsetResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param body
    */
  def createRrset(
      token: String,
      idOrName: String,
      body: ResourceRecordSetToCreate
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateRrsetResponse]
  ] =
    Zones().withBearerTokenAuth(token).createRrset(idOrName, body)

  /** Creates a Zone. A default `SOA` and three `NS` resource records with the
    * assigned Hetzner nameservers are created automatically.
    *
    * Expected answers: code 201 : CreateZoneResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param createZoneRequest
    */
  def createZone(
      token: String,
      createZoneRequest: CreateZoneRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateZoneResponse]
  ] =
    Zones().withBearerTokenAuth(token).createZone(createZoneRequest)

  /** Deletes an RRSet from the Zone. Only applicable for Zones in primary mode. |
    * Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 201 : DeleteRrsetResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param rrName
    * @param rrType
    */
  def deleteRrset(
      token: String,
      idOrName: String,
      rrName: String,
      rrType: String
  ): sttp.client4.Request[
    Either[ResponseException[String], DeleteRrsetResponse]
  ] =
    Zones().withBearerTokenAuth(token).deleteRrset(idOrName, rrName, rrType)

  /** Deletes a Zone.
    *
    * Expected answers: code 200 : DeleteZoneResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    */
  def deleteZone(token: String, idOrName: String): sttp.client4.Request[
    Either[ResponseException[String], DeleteZoneResponse]
  ] =
    Zones().withBearerTokenAuth(token).deleteZone(idOrName)

  /** Returns a generated Zone file in BIND (RFC
    * [1034](https://datatracker.ietf.org/doc/html/rfc1034)/[1035](https://datatracker.ietf.org/doc/html/rfc1035))
    * format. Only applicable for Zones in primary mode. #### Call specific
    * error codes | Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 200 : ExportZoneFileResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    */
  def exportZoneFile(token: String, idOrName: String): sttp.client4.Request[
    Either[ResponseException[String], ExportZoneFileResponse]
  ] =
    Zones().withBearerTokenAuth(token).exportZoneFile(idOrName)

  /** Returns a specific Action for a Zone.
    *
    * Expected answers: code 200 : GetActionResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForZone(
      token: String,
      idOrName: String,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Zones().withBearerTokenAuth(token).getActionForZone(idOrName, actionId)

  /** Returns a single RRSet from the Zone. Only applicable for Zones in primary
    * mode. #### Call specific error codes | Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 200 : GetRrsetResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param rrName
    * @param rrType
    */
  def getRrset(
      token: String,
      idOrName: String,
      rrName: String,
      rrType: String
  ): sttp.client4.Request[Either[ResponseException[String], GetRrsetResponse]] =
    Zones().withBearerTokenAuth(token).getRrset(idOrName, rrName, rrType)

  /** Returns a single Zone.
    *
    * Expected answers: code 200 : GetZoneResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    */
  def getZone(
      token: String,
      idOrName: String
  ): sttp.client4.Request[Either[ResponseException[String], GetZoneResponse]] =
    Zones().withBearerTokenAuth(token).getZone(idOrName)

  /** Returns a specific Action.
    *
    * Expected answers: code 200 : GetActionResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Action.
    */
  def getZoneAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Zones().withBearerTokenAuth(token).getZoneAction(id)

  /** Imports a zone file, replacing all resource record sets (RRSets). The
    * import will fail if existing RRSet are `change` protected. See Zone file
    * import for more details. Only applicable for Zones in primary mode. ####
    * Call specific error codes | Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 201 : ImportZoneFileResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param importZoneFileRequest
    */
  def importZoneFile(
      token: String,
      idOrName: String,
      importZoneFileRequest: ImportZoneFileRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ImportZoneFileResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .importZoneFile(idOrName, importZoneFileRequest)

  /** Returns all Actions for a Zone. Use the provided URI parameters to modify
    * the result.
    *
    * Expected answers: code 200 : ListActionsResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
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
  def listActionsForZone(
      token: String,
      idOrName: String,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .listActionsForZone(idOrName, sort, status, page, perPage)

  /** Returns all RRSets in the Zone. Use the provided URI parameters to modify
    * the result. The maximum value for `per_page` on this endpoint is `100`
    * instead of `50`. Only applicable for Zones in primary mode. #### Call
    * specific error codes | Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 200 : ListRrsetsResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param name
    *   Filter resources by their name. The response will only contain the
    *   resources matching exactly the specified name.
    * @param `type`
    *   Filter resources by their type. Can be used multiple times. The response
    *   will only contain resources matching the specified types.
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
  def listRrsets(
      token: String,
      idOrName: String,
      name: Option[String] = scala.None,
      `type`: Seq[String],
      labelSelector: Option[String] = scala.None,
      sort: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListRrsetsResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .listRrsets(idOrName, name, `type`, labelSelector, sort, page, perPage)

  /** Returns all Zone Actions. Use the provided URI parameters to modify the
    * result.
    *
    * Expected answers: code 200 : ListActionsResponse (Request succeeded.)
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
  def listZoneActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .listZoneActions(id, sort, status, page, perPage)

  /** Returns all Zones. Use the provided URI parameters to modify the result.
    *
    * Expected answers: code 200 : ListZonesResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param name
    *   Filter resources by their name. The response will only contain the
    *   resources matching exactly the specified name.
    * @param mode
    *   Filter resources by their mode. The response will only contain the
    *   resources matching exactly the specified mode.
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
  def listZones(
      token: String,
      name: Option[String] = scala.None,
      mode: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      sort: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListZonesResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .listZones(name, mode, labelSelector, sort, page, perPage)

  /** Removes resource records (RRs) from an existing RRSet in the Zone. For
    * convenience, the RRSet will be automatically deleted if it doesn't contain
    * any RRs afterwards. Only applicable for Zones in primary mode. #### Call
    * specific error codes | Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 201 : RemoveRecordsFromRrsetResponse (Request
    * succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param rrName
    * @param rrType
    * @param removeRecordsFromRrsetRequest
    */
  def removeRecordsFromRrset(
      token: String,
      idOrName: String,
      rrName: String,
      rrType: String,
      removeRecordsFromRrsetRequest: RemoveRecordsFromRrsetRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], RemoveRecordsFromRrsetResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .removeRecordsFromRrset(
        idOrName,
        rrName,
        rrType,
        removeRecordsFromRrsetRequest
      )

  /** Updates an RRSet in the Zone. Only applicable for Zones in primary mode. |
    * Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 200 : ReplaceRrsetResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param rrName
    * @param rrType
    * @param replaceRrsetRequest
    */
  def replaceRrset(
      token: String,
      idOrName: String,
      rrName: String,
      rrType: String,
      replaceRrsetRequest: ReplaceRrsetRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceRrsetResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .replaceRrset(idOrName, rrName, rrType, replaceRrsetRequest)

  /** Updates a Zone. To modify resource record sets (RRSets), use the RRSet
    * Actions endpoints.
    *
    * Expected answers: code 200 : ReplaceZoneResponse (Request succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param replaceZoneRequest
    */
  def replaceZone(
      token: String,
      idOrName: String,
      replaceZoneRequest: ReplaceZoneRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceZoneResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .replaceZone(idOrName, replaceZoneRequest)

  /** Overwrites the resource records (RRs) of an existing RRSet in the Zone.
    * Only applicable for Zones in primary mode. #### Call specific error codes |
    * Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 201 : SetRecordsOfRrsetResponse (Request
    * succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param rrName
    * @param rrType
    * @param setRecordsOfRrsetRequest
    */
  def setRecordsOfRrset(
      token: String,
      idOrName: String,
      rrName: String,
      rrType: String,
      setRecordsOfRrsetRequest: SetRecordsOfRrsetRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], SetRecordsOfRrsetResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .setRecordsOfRrset(idOrName, rrName, rrType, setRecordsOfRrsetRequest)

  /** Updates resource records' (RRs) comments of an existing RRSet in the Zone.
    * Only applicable for Zones in primary mode. #### Call specific error codes |
    * Code | Description | |-----------------------|----------------------------------| |
    * `incorrect_zone_mode` | The zone is not in primary mode. |
    *
    * Expected answers: code 200 : UpdateRecordsOfRrsetResponse (Request
    * succeeded.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param idOrName
    *   ID or Name of the Zone.
    * @param rrName
    * @param rrType
    * @param updateRecordsOfRrsetRequest
    */
  def updateRecordsOfRrset(
      token: String,
      idOrName: String,
      rrName: String,
      rrType: String,
      updateRecordsOfRrsetRequest: UpdateRecordsOfRrsetRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], UpdateRecordsOfRrsetResponse]
  ] =
    Zones()
      .withBearerTokenAuth(token)
      .updateRecordsOfRrset(
        idOrName,
        rrName,
        rrType,
        updateRecordsOfRrsetRequest
      )

}
