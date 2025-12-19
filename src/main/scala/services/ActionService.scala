package services

import hcloud.models.GetActionResponse
import hcloud.models.GetMultipleActionsResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Actions

abstract class ActionService {

  /** Returns a specific Action object.
    *
    * Expected answers: code 200 : GetActionResponse (Response for getting an
    * Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Action.
    */
  def getAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Actions().withBearerTokenAuth(token).getAction(id)

  /** Returns multiple Action objects specified by the `id` parameter. **Note**:
    * This endpoint previously allowed listing all actions in the project. This
    * functionality was deprecated in July 2023 and removed on 30 January 2025. -
    * Announcement:
    * https://docs.hetzner.cloud/changelog#2023-07-20-actions-list-endpoint-is-deprecated -
    * Removal:
    * https://docs.hetzner.cloud/changelog#2025-01-30-listing-arbitrary-actions-in-the-actions-list-endpoint-is-removed
    *
    * Expected answers: code 200 : GetMultipleActionsResponse (Response for
    * listing Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   Filter the actions by ID. Can be used multiple times. The response will
    *   only contain actions matching the specified IDs.
    */
  def getMultipleActions(token: String, id: Seq[Long]): sttp.client4.Request[
    Either[ResponseException[String], GetMultipleActionsResponse]
  ] =
    Actions().withBearerTokenAuth(token).getMultipleActions(id)

}
