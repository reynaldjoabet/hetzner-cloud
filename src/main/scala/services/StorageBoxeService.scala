package services

import hcloud.models.ChangeHomeDirectoryRequest
import hcloud.models.ChangeHomeDirectoryResponse
import hcloud.models.ChangeProtectionRequest
import hcloud.models.ChangeProtectionResponse
import hcloud.models.ChangeTypeRequest
import hcloud.models.ChangeTypeResponse
import hcloud.models.CreateSnapshotRequest
import hcloud.models.CreateSnapshotResponse
import hcloud.models.CreateStorageBoxRequest
import hcloud.models.CreateStorageBoxResponse
import hcloud.models.CreateSubaccountRequest
import hcloud.models.CreateSubaccountResponse
import hcloud.models.DeleteSnapshotResponse
import hcloud.models.DeleteStorageBoxResponse
import hcloud.models.DeleteSubaccountResponse
import hcloud.models.DisableSnapshotPlanResponse
import hcloud.models.EnableSnapshotPlanRequest
import hcloud.models.EnableSnapshotPlanResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetSnapshotResponse
import hcloud.models.GetStorageBoxResponse
import hcloud.models.GetSubaccountResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListFoldersOfStorageBoxResponse
import hcloud.models.ListSnapshotsResponse
import hcloud.models.ListStorageBoxesResponse
import hcloud.models.ListSubaccountsResponse
import hcloud.models.ReplaceSnapshotRequest
import hcloud.models.ReplaceSnapshotResponse
import hcloud.models.ReplaceStorageBoxRequest
import hcloud.models.ReplaceStorageBoxResponse
import hcloud.models.ReplaceSubaccountRequest
import hcloud.models.ReplaceSubaccountResponse
import hcloud.models.ResetPasswordRequest
import hcloud.models.ResetPasswordResponse
import hcloud.models.RollbackSnapshotRequest
import hcloud.models.RollbackSnapshotResponse
import hcloud.models.UpdateAccessSettingsRequest
import hcloud.models.UpdateAccessSettingsResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.StorageBoxes

abstract class StorageBoxService {

  /** Change the home directory of a Storage Box Subaccount.
    *
    * Expected answers: code 201 : ChangeHomeDirectoryResponse (The `action` key
    * in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param subaccountId
    *   ID of the Storage Box Subaccount.
    * @param changeHomeDirectoryRequest
    */
  def changeHomeDirectory(
      token: String,
      id: Long,
      subaccountId: Long,
      changeHomeDirectoryRequest: ChangeHomeDirectoryRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeHomeDirectoryResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .changeHomeDirectory(id, subaccountId, changeHomeDirectoryRequest)

  /** Changes the protection configuration of a Storage Box.
    *
    * Expected answers: code 201 : ChangeProtectionResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param changeProtectionRequest
    */
  def changeProtection(
      token: String,
      id: Long,
      changeProtectionRequest: ChangeProtectionRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeProtectionResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .changeProtection(id, changeProtectionRequest)

  /** Changes the type of a Storage Box. Upgrades or downgrades a Storage Box to
    * another Storage Box Type. It is not possible to downgrade to a Storage Box
    * Type that offers less disk space than you are currently using.
    *
    * Expected answers: code 201 : ChangeTypeResponse (The `action` key in the
    * reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param changeTypeRequest
    */
  def changeType(
      token: String,
      id: Long,
      changeTypeRequest: ChangeTypeRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ChangeTypeResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .changeType(id, changeTypeRequest)

  /** Creates a Storage Box Snapshot.
    *
    * Expected answers: code 201 : CreateSnapshotResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param createSnapshotRequest
    */
  def createSnapshot(
      token: String,
      id: Long,
      createSnapshotRequest: CreateSnapshotRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateSnapshotResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .createSnapshot(id, createSnapshotRequest)

  /** Creates a Storage Box.
    *
    * Expected answers: code 201 : CreateStorageBoxResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param createStorageBoxRequest
    */
  def createStorageBox(
      token: String,
      createStorageBoxRequest: CreateStorageBoxRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateStorageBoxResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .createStorageBox(createStorageBoxRequest)

  /** Creates a Storage Box Subaccount. A Storage Box Subaccount will use a
    * separate home directory.
    *
    * Expected answers: code 201 : CreateSubaccountResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param createSubaccountRequest
    */
  def createSubaccount(
      token: String,
      id: Long,
      createSubaccountRequest: CreateSubaccountRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateSubaccountResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .createSubaccount(id, createSubaccountRequest)

  /** Deletes a Storage Box Snapshot.
    *
    * Expected answers: code 201 : DeleteSnapshotResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param snapshotId
    *   ID of the Storage Box Snapshot.
    */
  def deleteSnapshot(
      token: String,
      id: Long,
      snapshotId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], DeleteSnapshotResponse]
  ] =
    StorageBoxes().withBearerTokenAuth(token).deleteSnapshot(id, snapshotId)

  /** Deletes a Storage Box.
    *
    * Expected answers: code 201 : DeleteStorageBoxResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    */
  def deleteStorageBox(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], DeleteStorageBoxResponse]
  ] =
    StorageBoxes().withBearerTokenAuth(token).deleteStorageBox(id)

  /** Deletes a Storage Box Subaccount.
    *
    * Expected answers: code 201 : DeleteSubaccountResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param subaccountId
    *   ID of the Storage Box Subaccount.
    */
  def deleteSubaccount(
      token: String,
      id: Long,
      subaccountId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], DeleteSubaccountResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .deleteSubaccount(id, subaccountId)

  /** Disables the active Snapshot Plan of a Storage Box. Existing Storage Box
    * Snapshots created by the Snapshot Plan will not be delete, they must be
    * removed manually.
    *
    * Expected answers: code 201 : DisableSnapshotPlanResponse (The `action` key
    * in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    */
  def disableSnapshotPlan(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], DisableSnapshotPlanResponse]
  ] =
    StorageBoxes().withBearerTokenAuth(token).disableSnapshotPlan(id)

  /** Enables a Snapshot Plan for a Storage Box. Once enabled, a Snapshot Plan
    * will create Snapshots at predefined intervals. There can only ever be one
    * Snapshot Plan. The existing Snapshot Plan will be deleted before a new one
    * is set up. Automatic Snapshots are retained until explicitly deleted by
    * the user or the maximum snapshot count for the plan (\"max_snapshots\") is
    * exceeded. You can choose the specific time (UTC timezone), day of the
    * week, and day of the month. The time-related options are cron like. Some
    * typical use cases include: | Interval | Request body | |
    * ------------------------------------------ |
    * ------------------------------------------------------------- | | Every
    * day at 3 o'clock | `{\"max_snapshots\":10,\"minute\":0,\"hour\":3}` | |
    * Every Friday at 3 o'clock |
    * `{\"max_snapshots\":10,\"minute\":0,\"hour\":3,\"day_of_week\": 5}` | | On
    * the first of every month at half past 6 |
    * `{\"max_snapshots\":10,\"minute\":30,\"hour\":6,\"day_of_month\": 1}` |
    *
    * Expected answers: code 201 : EnableSnapshotPlanResponse (The `action` key
    * in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param enableSnapshotPlanRequest
    */
  def enableSnapshotPlan(
      token: String,
      id: Long,
      enableSnapshotPlanRequest: EnableSnapshotPlanRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], EnableSnapshotPlanResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .enableSnapshotPlan(id, enableSnapshotPlanRequest)

  /** Returns a specific Action object for a Storage Box.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key in the
    * reply has this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForStorageBox(
      token: String,
      id: Long,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .getActionForStorageBox(id, actionId)

  /** Returns a specific Storage Box Snapshot.
    *
    * Expected answers: code 200 : GetSnapshotResponse (The `snapshot` key in
    * the reply contains a Snapshot object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param snapshotId
    *   ID of the Storage Box Snapshot.
    */
  def getSnapshot(
      token: String,
      id: Long,
      snapshotId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetSnapshotResponse]
  ] =
    StorageBoxes().withBearerTokenAuth(token).getSnapshot(id, snapshotId)

  /** Returns a specific Storage Box.
    *
    * Expected answers: code 200 : GetStorageBoxResponse (The `storage_box` key
    * in the reply contains a Storage Box object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    */
  def getStorageBox(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetStorageBoxResponse]
  ] =
    StorageBoxes().withBearerTokenAuth(token).getStorageBox(id)

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
  def getStorageBoxAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    StorageBoxes().withBearerTokenAuth(token).getStorageBoxAction(id)

  /** Returns a specific Storage Box Subaccount.
    *
    * Expected answers: code 200 : GetSubaccountResponse (The `subaccount` key
    * in the reply contains a Subaccount object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param subaccountId
    *   ID of the Storage Box Subaccount.
    */
  def getSubaccount(
      token: String,
      id: Long,
      subaccountId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetSubaccountResponse]
  ] =
    StorageBoxes().withBearerTokenAuth(token).getSubaccount(id, subaccountId)

  /** List all Actions related to a specific Storage Box.
    *
    * Expected answers: code 200 : ListActionsResponse (The `actions` key
    * contains a list of Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
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
  def listActionsForStorageBox(
      token: String,
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .listActionsForStorageBox(id, sort, status, page, perPage)

  /** Returns a list of (sub)folders in a Storage Box. The folder location is
    * specified by the `path` query parameter.
    *
    * Expected answers: code 200 : ListFoldersOfStorageBoxResponse (The
    * `folders` key lists the directories the Storage Box contains at the
    * specified path.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param path
    *   Relative path for which the listing is to be made.
    */
  def listFoldersOfStorageBox(
      token: String,
      id: Long,
      path: Option[String] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListFoldersOfStorageBoxResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .listFoldersOfStorageBox(id, path)

  /** Returns a list of Storage Box Snapshot. Both snapshots created manually
    * and by the snapshot plan are returned.
    *
    * Expected answers: code 200 : ListSnapshotsResponse (The `snapshots` key
    * contains a list of Snapshots.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
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
    * @param isAutomatic
    *   Filter wether a Storage Box Snapshot is automatic.
    */
  def listSnapshots(
      token: String,
      id: Long,
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      sort: Seq[String],
      isAutomatic: Option[Boolean] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListSnapshotsResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .listSnapshots(id, name, labelSelector, sort, isAutomatic)

  /** Returns all Action objects.
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
  def listStorageBoxActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .listStorageBoxActions(id, sort, status, page, perPage)

  /** Returns a paginated list of Storage Boxes.
    *
    * Expected answers: code 200 : ListStorageBoxesResponse (The `storage_box`
    * key in the reply contains a Storage Box object with this structure.)
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
  def listStorageBoxes(
      token: String,
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      sort: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListStorageBoxesResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .listStorageBoxes(name, labelSelector, sort, page, perPage)

  /** Returns a list of Storage Box Subaccount.
    *
    * Expected answers: code 200 : ListSubaccountsResponse (The `subaccounts`
    * key contains a list of Subaccounts.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param labelSelector
    *   Filter resources by labels. The response will only contain resources
    *   matching the label selector. For more information, see \"Label
    *   Selector\".
    * @param sort
    *   Sort resources by field and direction. Can be used multiple times. For
    *   more information, see \"Sorting\".
    * @param username
    *   Filter Storage Box Subaccounts by username. The response will only
    *   contain the resources matching exactly the specified username.
    */
  def listSubaccounts(
      token: String,
      id: Long,
      labelSelector: Option[String] = scala.None,
      sort: Seq[String],
      username: Option[String] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListSubaccountsResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .listSubaccounts(id, labelSelector, sort, username)

  /** Updates a Storage Box Snapshot.
    *
    * Expected answers: code 200 : ReplaceSnapshotResponse (The `snapshot` key
    * contains the updated Snapshot.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param snapshotId
    *   ID of the Storage Box Snapshot.
    * @param replaceSnapshotRequest
    */
  def replaceSnapshot(
      token: String,
      id: Long,
      snapshotId: Long,
      replaceSnapshotRequest: ReplaceSnapshotRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceSnapshotResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .replaceSnapshot(id, snapshotId, replaceSnapshotRequest)

  /** Updates a Storage Box.
    *
    * Expected answers: code 200 : ReplaceStorageBoxResponse (The `storage_box`
    * key in the reply contains the updated Storage Box object with this
    * structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param replaceStorageBoxRequest
    */
  def replaceStorageBox(
      token: String,
      id: Long,
      replaceStorageBoxRequest: ReplaceStorageBoxRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceStorageBoxResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .replaceStorageBox(id, replaceStorageBoxRequest)

  /** Updates a Storage Box Subaccount.
    *
    * Expected answers: code 200 : ReplaceSubaccountResponse (The `subaccount`
    * key contains the updated Subaccount.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param subaccountId
    *   ID of the Storage Box Subaccount.
    * @param replaceSubaccountRequest
    */
  def replaceSubaccount(
      token: String,
      id: Long,
      subaccountId: Long,
      replaceSubaccountRequest: ReplaceSubaccountRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceSubaccountResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .replaceSubaccount(id, subaccountId, replaceSubaccountRequest)

  /** Reset the password of a Storage Box.
    *
    * Expected answers: code 201 : ResetPasswordResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param resetPasswordRequest
    */
  def resetStorageBoxPassword(
      token: String,
      id: Long,
      resetPasswordRequest: ResetPasswordRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ResetPasswordResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .resetStorageBoxPassword(id, resetPasswordRequest)

  /** Reset the password of a Storage Box Subaccount.
    *
    * Expected answers: code 201 : ResetPasswordResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param subaccountId
    *   ID of the Storage Box Subaccount.
    * @param resetPasswordRequest
    */
  def resetStorageBoxSubaccountPassword(
      token: String,
      id: Long,
      subaccountId: Long,
      resetPasswordRequest: ResetPasswordRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ResetPasswordResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .resetStorageBoxSubaccountPassword(id, subaccountId, resetPasswordRequest)

  /** Rolls back a Storage Box to the given Storage Box Snapshot. This will
    * remove all newer Storage Box Snapshot and irrevocably delete all data that
    * was since written to the Storage Box.
    *
    * Expected answers: code 201 : RollbackSnapshotResponse (The `action` key in
    * the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param rollbackSnapshotRequest
    */
  def rollbackSnapshot(
      token: String,
      id: Long,
      rollbackSnapshotRequest: RollbackSnapshotRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], RollbackSnapshotResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .rollbackSnapshot(id, rollbackSnapshotRequest)

  /** Update access settings of a primary Storage Box account. This endpoints
    * supports partial updates. Omitted optional parameters do not result in any
    * changes to the respective properties.
    *
    * Expected answers: code 201 : UpdateAccessSettingsResponse (The `action`
    * key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param updateAccessSettingsRequest
    */
  def updateStorageBoxAccessSettings(
      token: String,
      id: Long,
      updateAccessSettingsRequest: UpdateAccessSettingsRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], UpdateAccessSettingsResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .updateStorageBoxAccessSettings(id, updateAccessSettingsRequest)

  /** Updates the access settings of a Storage Box Subaccount. This endpoints
    * supports partial updates. Omitted optional parameters do not result in any
    * changes to the respective properties.
    *
    * Expected answers: code 201 : UpdateAccessSettingsResponse (The `action`
    * key in the reply contains an Action object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Storage Box.
    * @param subaccountId
    *   ID of the Storage Box Subaccount.
    * @param updateAccessSettingsRequest
    */
  def updateStorageBoxSubaccountAccessSettings(
      token: String,
      id: Long,
      subaccountId: Long,
      updateAccessSettingsRequest: UpdateAccessSettingsRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], UpdateAccessSettingsResponse]
  ] =
    StorageBoxes()
      .withBearerTokenAuth(token)
      .updateStorageBoxSubaccountAccessSettings(
        id,
        subaccountId,
        updateAccessSettingsRequest
      )

}
