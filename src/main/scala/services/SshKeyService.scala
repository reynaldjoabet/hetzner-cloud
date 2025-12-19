package services

import hcloud.models.CreateSshKeyRequest
import hcloud.models.CreateSshKeyResponse
import hcloud.models.GetSshKeyResponse
import hcloud.models.ListSshKeysResponse
import hcloud.models.ReplaceSshKeyRequest
import hcloud.models.ReplaceSshKeyResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.SshKeys

abstract class SshKeyService {

  /** Creates a new SSH key with the given `name` and `public_key`. Once an SSH
    * key is created, it can be used in other calls such as creating Servers.
    *
    * Expected answers: code 201 : CreateSshKeyResponse (The `ssh_key` key in
    * the reply contains the object that was just created.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param createSshKeyRequest
    */
  def createSshKey(
      token: String,
      createSshKeyRequest: CreateSshKeyRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateSshKeyResponse]
  ] =
    SshKeys().withBearerTokenAuth(token).createSshKey(createSshKeyRequest)

  /** Deletes an SSH key. It cannot be used anymore.
    *
    * Expected answers: code 204 : (SSH key deleted.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the SSH Key.
    */
  def deleteSshKey(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    SshKeys().withBearerTokenAuth(token).deleteSshKey(id)

  /** Returns a specific SSH key object.
    *
    * Expected answers: code 200 : GetSshKeyResponse (The `ssh_key` key in the
    * reply contains an SSH key object with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the SSH Key.
    */
  def getSshKey(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetSshKeyResponse]
  ] =
    SshKeys().withBearerTokenAuth(token).getSshKey(id)

  /** Returns all SSH key objects.
    *
    * Expected answers: code 200 : ListSshKeysResponse (The `ssh_keys` key in
    * the reply contains an array of SSH key objects with this structure.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param sort
    *   Sort resources by field and direction. Can be used multiple times. For
    *   more information, see \"Sorting\".
    * @param name
    *   Filter resources by their name. The response will only contain the
    *   resources matching exactly the specified name.
    * @param fingerprint
    *   Can be used to filter SSH keys by their fingerprint. The response will
    *   only contain the SSH key matching the specified fingerprint.
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
  def listSshKeys(
      token: String,
      sort: Seq[String],
      name: Option[String] = scala.None,
      fingerprint: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListSshKeysResponse]
  ] =
    SshKeys()
      .withBearerTokenAuth(token)
      .listSshKeys(sort, name, fingerprint, labelSelector, page, perPage)

  /** Updates an SSH key. You can update an SSH key name and an SSH key labels.
    *
    * Expected answers: code 200 : ReplaceSshKeyResponse (The `ssh_key` key in
    * the reply contains the modified SSH key object with the new description.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the SSH Key.
    * @param replaceSshKeyRequest
    */
  def replaceSshKey(
      token: String,
      id: Long,
      replaceSshKeyRequest: ReplaceSshKeyRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceSshKeyResponse]
  ] =
    SshKeys()
      .withBearerTokenAuth(token)
      .replaceSshKey(id, replaceSshKeyRequest)

}
