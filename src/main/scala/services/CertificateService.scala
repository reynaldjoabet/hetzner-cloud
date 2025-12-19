package services

import hcloud.models.CreateCertificateRequest
import hcloud.models.CreateCertificateResponse
import hcloud.models.GetActionResponse
import hcloud.models.GetCertificateResponse
import hcloud.models.ListActionsResponse
import hcloud.models.ListCertificatesResponse
import hcloud.models.ReplaceCertificateRequest
import hcloud.models.ReplaceCertificateResponse
import hcloud.models.RetryIssuanceOrRenewalResponse
import hcloud.JsonSupport.{*, given}
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method
import hcloud.api.Certificates

abstract class CertificateService {

  /** Creates a new Certificate. The default type **uploaded** allows for
    * uploading your existing `certificate` and `private_key` in PEM format. You
    * have to monitor its expiration date and handle renewal yourself. In
    * contrast, type **managed** requests a new Certificate from *Let's Encrypt*
    * for the specified `domain_names`. Only domains managed by *Hetzner DNS*
    * are supported. We handle renewal and timely alert the project owner via
    * email if problems occur. For type `managed` Certificates the `action` key
    * of the response contains the Action that allows for tracking the issuance
    * process. For type `uploaded` Certificates the `action` is always null.
    *
    * Expected answers: code 201 : CreateCertificateResponse (The `certificate`
    * key contains the Certificate that was just created. For type `managed`
    * Certificates the `action` key contains the Action that allows for tracking
    * the issuance process. For type `uploaded` Certificates the `action` is
    * always null.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param createCertificateRequest
    */
  def createCertificate(
      token: String,
      createCertificateRequest: CreateCertificateRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateCertificateResponse]
  ] =
    Certificates()
      .withBearerTokenAuth(token)
      .createCertificate(createCertificateRequest)

  /** Deletes a Certificate.
    *
    * Expected answers: code 204 : (Certificate deleted.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Certificate.
    */
  def deleteCertificate(
      token: String,
      id: Long
  ): sttp.client4.Request[Either[ResponseException[String], Unit]] =
    Certificates().withBearerTokenAuth(token).deleteCertificate(id)

  /** Returns a specific Action for a Certificate. Only type `managed`
    * Certificates have Actions.
    *
    * Expected answers: code 200 : GetActionResponse (The `action` key contains
    * the Certificate Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Certificate.
    * @param actionId
    *   ID of the Action.
    */
  def getActionForCertificate(
      token: String,
      id: Long,
      actionId: Long
  ): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Certificates()
      .withBearerTokenAuth(token)
      .getActionForCertificate(id, actionId)

  /** Gets a specific Certificate object.
    *
    * Expected answers: code 200 : GetCertificateResponse (The `certificate` key
    * contains a Certificate object.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Certificate.
    */
  def getCertificate(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetCertificateResponse]
  ] =
    Certificates().withBearerTokenAuth(token).getCertificate(id)

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
  def getCertificateAction(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], GetActionResponse]
  ] =
    Certificates().withBearerTokenAuth(token).getCertificateAction(id)

  /** Returns all Action objects for a Certificate. You can sort the results by
    * using the `sort` URI parameter, and filter them with the `status`
    * parameter. Only type `managed` Certificates can have Actions. For type
    * `uploaded` Certificates the `actions` key will always contain an empty
    * array.
    *
    * Expected answers: code 200 : ListActionsResponse (The `actions` key
    * contains a list of Actions.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Certificate.
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
  def listActionsForCertificate(
      token: String,
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Certificates()
      .withBearerTokenAuth(token)
      .listActionsForCertificate(id, sort, status, page, perPage)

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
  def listCertificateActions(
      token: String,
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListActionsResponse]
  ] =
    Certificates()
      .withBearerTokenAuth(token)
      .listCertificateActions(id, sort, status, page, perPage)

  /** Returns all Certificate objects.
    *
    * Expected answers: code 200 : ListCertificatesResponse (The `certificates`
    * key contains an array of Certificate objects.)
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
    * @param `type`
    *   Filter resources by type. Can be used multiple times. The response will
    *   only contain the resources with the specified type.
    * @param page
    *   Page number to return. For more information, see \"Pagination\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"Pagination\".
    */
  def listCertificates(
      token: String,
      sort: Seq[String],
      name: Option[String] = scala.None,
      labelSelector: Option[String] = scala.None,
      `type`: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  ): sttp.client4.Request[
    Either[ResponseException[String], ListCertificatesResponse]
  ] =
    Certificates()
      .withBearerTokenAuth(token)
      .listCertificates(sort, name, labelSelector, `type`, page, perPage)

  /** Updates the Certificate properties. Note: if the Certificate object
    * changes during the request, the response will be a “conflict” error.
    *
    * Expected answers: code 200 : ReplaceCertificateResponse (The `certificate`
    * key contains the Certificate that was just updated.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Certificate.
    * @param replaceCertificateRequest
    */
  def replaceCertificate(
      token: String,
      id: Long,
      replaceCertificateRequest: ReplaceCertificateRequest
  ): sttp.client4.Request[
    Either[ResponseException[String], ReplaceCertificateResponse]
  ] =
    Certificates()
      .withBearerTokenAuth("your_token")
      .replaceCertificate(id, replaceCertificateRequest)

  /** Retry a failed Certificate issuance or renewal. Only applicable if the
    * type of the Certificate is `managed` and the issuance or renewal status is
    * `failed`. #### Call specific error codes | Code | Description | |---------------------------------------------------------|---------------------------------------------------------------------------| |
    * `caa_record_does_not_allow_ca` | CAA record does not allow certificate
    * authority | | `ca_dns_validation_failed` | Certificate Authority: DNS
    * validation failed | | `ca_too_many_authorizations_failed_recently` |
    * Certificate Authority: Too many authorizations failed recently | |
    * `ca_too_many_certificates_issued_for_registered_domain` | Certificate
    * Authority: Too many certificates issued for registered domain | |
    * `ca_too_many_duplicate_certificates` | Certificate Authority: Too many
    * duplicate certificates | | `could_not_verify_domain_delegated_to_zone` |
    * Could not verify domain delegated to zone | | `dns_zone_not_found` | DNS
    * zone not found | | `dns_zone_is_secondary_zone` | DNS zone is a secondary
    * zone |
    *
    * Expected answers: code 201 : RetryIssuanceOrRenewalResponse (The `action`
    * key contains the resulting Action.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Certificate.
    */
  def retryIssuanceOrRenewal(token: String, id: Long): sttp.client4.Request[
    Either[ResponseException[String], RetryIssuanceOrRenewalResponse]
  ] =
    Certificates().withBearerTokenAuth(token).retryIssuanceOrRenewal(id)

}
