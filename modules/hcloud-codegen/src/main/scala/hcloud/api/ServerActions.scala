/** Hetzner Cloud API # Overview This is the official documentation for the
  * Hetzner Cloud API. ## Introduction The Hetzner Cloud API operates over HTTPS
  * and uses JSON as its data format. The API is a RESTful API and utilizes HTTP
  * methods and HTTP status codes to specify requests and responses. As an
  * alternative to working directly with our API you may also consider to use: -
  * Our CLI program [hcloud](https://github.com/hetznercloud/cli) - Our [library
  * for Go](https://github.com/hetznercloud/hcloud-go) - Our [library for
  * Python](https://github.com/hetznercloud/hcloud-python) You can find even
  * more libraries, tools and integrations on our [Awesome List on
  * GitHub](https://github.com/hetznercloud/awesome-hcloud). ### Open source
  * credits If you are developing an open-source project that supports or
  * intends to add support for Hetzner APIs, you may be eligible for a free
  * one-time credit of up to € 50 / $ 50 on your account. Please contact us via
  * the support page on your [Hetzner
  * Console](https://console.hetzner.cloud/support) and let us know the
  * following: - The name of the project you are working on - A short
  * description of the project - Link to the project website or repo where the
  * project is hosted - Affiliation with / role in the project (e.g. project
  * maintainer) - Link to some other open-source work you have already done (if
  * you have done so) **Note:** We only consider rewards for projects that
  * provide Hetzner-specific functionality or integrations. For example, our
  * Object Storage exposes a standard S3 API without any Hetzner-specific
  * extensions. Projects that focus solely on generic S3 capabilities (e.g.,
  * general S3 clients or SDKs) are not Hetzner-specific and are therefore not
  * eligible for Hetzner Rewards. ## Getting Started To get started using the
  * API you first need an API token. Sign in into the [Hetzner
  * Console](https://console.hetzner.com/) choose a Project, go to `Security` →
  * `API Tokens`, and generate a new token. Make sure to copy the token because
  * it won’t be shown to you again. A token is bound to a Project, to interact
  * with the API of another Project you have to create a new token inside the
  * Project. Let’s say your new token is
  * `LRK9DAWQ1ZAEFSrCNEEzLCUwhYX1U3g7wMg4dTlkkDC96fyDuyJ39nVbVjCKSDfj`. You’re
  * now ready to do your first request against the API. To get a list of all
  * Servers in your Project, issue the example request on the right side using
  * [curl](https://curl.se/). Make sure to replace the token in the example
  * command with the token you have just created. Since your Project probably
  * does not contain any Servers yet, the example response will look like the
  * response on the right side. We will almost always provide a resource root
  * like `servers` inside the example response. A response can also contain a
  * `meta` object with information like [Pagination](#description/pagination).
  * **Example Request** ```shell curl -H \"Authorization: Bearer LRK9DAWQ1ZAEFSrCNEEzLCUwhYX1U3g7wMg4dTlkkDC96fyDuyJ39nVbVjCKSDfj\" \\   https://api.hetzner.cloud/v1/servers ```
  * **Example Response** ```json {   \"servers\": [],   \"meta\": {     \"pagination\": {       \"page\": 1,       \"per_page\": 25,       \"previous_page\": null,       \"next_page\": null,       \"last_page\": 1,       \"total_entries\": 0     }   } } ```
  * ## Authentication All requests to the Hetzner Cloud API must be
  * authenticated via a API token. Include your secret API token in every
  * request you send to the API with the `Authorization` HTTP header. To create
  * a new API token for your Project, switch into the [Hetzner
  * Console](https://console.hetzner.com/) choose a Project, go to `Security` →
  * `API Tokens`, and generate a new token. **Example Authorization header** ```http Authorization: Bearer LRK9DAWQ1ZAEFSrCNEEzLCUwhYX1U3g7wMg4dTlkkDC96fyDuyJ39nVbVjCKSDfj ```
  * ## Query Parameters The API makes use of query parameters to sort and filter
  * responses. The parameter names and values must be URI encoded according to
  * [RFC2396](https://datatracker.ietf.org/doc/html/rfc2396). Query parameters
  * of type `array` can be used multiple times: **Example query parameters for
  * pagination** ``` https://api.hetzner.cloud/v1/certificates?page=1&page_size=12 ```
  * **Example use of multiple values for a parameter** ``` https://api.hetzner.cloud/v1/certificates?type=uploaded&type=managed ```
  * **Example use of an encoded parameter** ``` https://api.hetzner.cloud/v1/certificates?label_selector=key%3Dvalue ```
  * ## Errors Errors are indicated by HTTP status codes. Further, the response
  * of the request which generated the error contains an error code, an error
  * message, and, optionally, error details. The schema of the error details
  * object depends on the error code. The error response contains the following
  * keys: | Keys | Meaning | | --------- |
  * --------------------------------------------------------------------- | |
  * `code` | Short string indicating the type of error (machine-parsable) | |
  * `message` | Textual description on what has gone wrong | | `details` | An
  * object providing for details on the error (schema depends on code) |
  * **Example response** ```json {   \"error\": {     \"code\": \"invalid_input\",     \"message\": \"invalid input in field 'broken_field': is too long\",     \"details\": {       \"fields\": [         {           \"name\": \"broken_field\",           \"messages\": [\"is too long\"]         }       ]     }   } } ```
  * ### Error Codes | Status | Code | Description | | --- | --- | --- | | `400` |
  * `json_error` | Invalid JSON input in your request. | | `401` |
  * `unauthorized` | Request was made with an invalid or unknown token. | |
  * `401` | `token_readonly` | The token is only allowed to perform GET
  * requests. | | `403` | `forbidden` | Insufficient permissions for this
  * request. | | `403` | `maintenance` | Cannot perform operation due to
  * maintenance. | | `403` | `resource_limit_exceeded` | Error when exceeding
  * the maximum quantity of a resource for an account. | | `404` | `not_found` |
  * Entity not found. | | `405` | `method_not_allowed` | The request method is
  * not allowed | | `409` | `uniqueness_error` | One or more of the objects
  * fields must be unique. | | `409` | `conflict` | The resource has changed
  * during the request, please retry. | | `410` | `deprecated_api_endpoint` |
  * The API endpoint functionality was removed. | | `412` |
  * `resource_unavailable` | The requested resource is currently unavailable
  * (e.g. not available for order). | | `422` | `invalid_input` | Error while
  * parsing or processing the input. | | `422` | `service_error` | Error within
  * a service. | | `422` | `unsupported_error` | The corresponding resource does
  * not support the Action. | | `423` | `locked` | The item you are trying to
  * access is locked (there is already an Action running). | | `423` |
  * `protected` | The Action you are trying to start is protected for this
  * resource. | | `429` | `rate_limit_exceeded` | Error when sending too many
  * requests. | | `500` | `server_error` | Error within the API backend. | |
  * `503` | `unavailable` | A service or product is currently not available. | |
  * `504` | `timeout` | The request could not be answered in time, please retry. |
  * **invalid_input** ```json {   \"error\": {     \"code\": \"invalid_input\",     \"message\": \"invalid input in field 'broken_field': is too long\",     \"details\": {       \"fields\": [         {           \"name\": \"broken_field\",           \"messages\": [\"is too long\"]         }       ]     }   } } ```
  * **uniqueness_error** ```json {   \"error\": {     \"code\": \"uniqueness_error\",     \"message\": \"SSH key with the same fingerprint already exists\",     \"details\": {       \"fields\": [         {           \"name\": \"public_key\"         }       ]     }   } } ```
  * **resource_limit_exceeded** ```json {   \"error\": {     \"code\": \"resource_limit_exceeded\",     \"message\": \"project limit exceeded\",     \"details\": {       \"limits\": [         {           \"name\": \"project_limit\"         }       ]     }   } } ```
  * **deprecated_api_endpoint** ```json {   \"error\": {     \"code\": \"deprecated_api_endpoint\",     \"message\": \"API functionality was removed\",     \"details\": {       \"announcement\": \"https://docs.hetzner.cloud/changelog#2023-07-20-foo-endpoint-is-deprecated\"     }   } } ```
  * ## Actions Actions represent asynchronous tasks within the API, targeting
  * one or more resources. Triggering changes in the API may return a `running`
  * action. An action should be waited upon, until it reaches either the
  * `success` or `error` state. Avoid polling the action's state too frequently
  * to reduce the risk of exhausting your API requests and hitting the [rate
  * limit](#description/rate-limiting). If an action fails, it will contain
  * details about the underlying error. Once the asynchronous tasks have
  * completed and the targeted resources are in a consistent state, the action
  * is marked as succeeded. In some cases, you may trigger multiple changes at
  * once, and only wait for the returned actions at a later stage. ## Labels
  * Labels are `key/value` pairs that can be attached to all resources. Valid
  * label keys have two segments: an optional prefix and name, separated by a
  * slash (`/`). The name segment is required and must be a string of 63
  * characters or less, beginning and ending with an alphanumeric character
  * (`[a-z0-9A-Z]`) with dashes (`-`), underscores (`_`), dots (`.`), and
  * alphanumerics between. The prefix is optional. If specified, the prefix must
  * be a DNS subdomain: a series of DNS labels separated by dots (`.`), not
  * longer than 253 characters in total, followed by a slash (`/`). Valid label
  * values must be a string of 63 characters or less and must be empty or begin
  * and end with an alphanumeric character (`[a-z0-9A-Z]`) with dashes (`-`),
  * underscores (`_`), dots (`.`), and alphanumerics between. The
  * `hetzner.cloud/` prefix is reserved and cannot be used. **Example Labels** ```json {   \"labels\": {     \"environment\": \"development\",     \"service\": \"backend\",     \"example.com/my\": \"label\",     \"just-a-key\": \"\"   } } ```
  * ## Label Selector For resources with labels, you can filter resources by
  * their labels using the label selector query language. | Expression | Meaning | |
  * -------------------- | ---------------------------------------------------- | |
  * `k==v` / `k=v` | Value of key `k` does equal value `v` | | `k!=v` | Value of
  * key `k` does not equal value `v` | | `k` | Key `k` is present | | `!k` | Key
  * `k` is not present | | `k in (v1,v2,v3)` | Value of key `k` is `v1`, `v2`,
  * or `v3` | | `k notin (v1,v2,v3)` | Value of key `k` is neither `v1`, nor
  * `v2`, nor `v3` | | `k1==v,!k2` | Value of key `k1` is `v` and key `k2` is
  * not present | ### Examples - Returns all resources that have a
  * `env=production` label and that don't have a `type=database` label:
  * `env=production,type!=database` - Returns all resources that have a
  * `env=testing` or `env=staging` label: `env in (testing,staging)` - Returns
  * all resources that don't have a `type` label: `!type` ## Pagination
  * Responses which return multiple items support pagination. If they do support
  * pagination, it can be controlled with following query string parameters: - A
  * `page` parameter specifies the page to fetch. The number of the first page
  * is 1. - A `per_page` parameter specifies the number of items returned per
  * page. The default value is 25, the maximum value is 50 except otherwise
  * specified in the documentation. Responses contain a `Link` header with
  * pagination information. Additionally, if the response body is JSON and the
  * root object is an object, that object has a `pagination` object inside the
  * `meta` object with pagination information: **Example Pagination** ```json {     \"servers\": [...],     \"meta\": {         \"pagination\": {             \"page\": 2,             \"per_page\": 25,             \"previous_page\": 1,             \"next_page\": 3,             \"last_page\": 4,             \"total_entries\": 100         }     } } ```
  * The keys `previous_page`, `next_page`, `last_page`, and `total_entries` may
  * be `null` when on the first page, last page, or when the total number of
  * entries is unknown. **Example Pagination Link header** ```http Link: <https://api.hetzner.cloud/v1/actions?page=2&per_page=5>; rel=\"prev\",       <https://api.hetzner.cloud/v1/actions?page=4&per_page=5>; rel=\"next\",       <https://api.hetzner.cloud/v1/actions?page=6&per_page=5>; rel=\"last\" ```
  * Line breaks have been added for display purposes only and responses may only
  * contain some of the above `rel` values. ## Rate Limiting All requests,
  * whether they are authenticated or not, are subject to rate limiting. If you
  * have reached your limit, your requests will be handled with a
  * `429 Too Many Requests` error. Burst requests are allowed. Responses contain
  * several headers which provide information about your current rate limit
  * status. - The `RateLimit-Limit` header contains the total number of requests
  * you can perform per hour. - The `RateLimit-Remaining` header contains the
  * number of requests remaining in the current rate limit time frame. - The
  * `RateLimit-Reset` header contains a UNIX timestamp of the point in time when
  * your rate limit will have recovered, and you will have the full number of
  * requests available again. The default limit is 3600 requests per hour and
  * per Project. The number of remaining requests increases gradually. For
  * example, when your limit is 3600 requests per hour, the number of remaining
  * requests will increase by 1 every second. ## Server Metadata Your Server can
  * discover metadata about itself by doing a HTTP request to specific URLs. The
  * following data is available: | Data | Format | Contents | |
  * ----------------- | ------ |
  * ------------------------------------------------------------ | | hostname |
  * text | Name of the Server as set in the api | | instance-id | number | ID of
  * the server | | public-ipv4 | text | Primary public IPv4 address | |
  * private-networks | yaml | Details about the private networks the Server is
  * attached to | | availability-zone | text | Name of the availability zone
  * that Server runs in | | region | text | Network zone, e.g. eu-central |
  * **Example: Summary** ```shell $ curl http://169.254.169.254/hetzner/v1/metadata ``` ```yaml availability-zone: hel1-dc2 hostname: my-server instance-id: 42 public-ipv4: 1.2.3.4 region: eu-central ```
  * **Example: Hostname** ```shell $ curl http://169.254.169.254/hetzner/v1/metadata/hostname my-server ```
  * **Example: Instance ID** ```shell $ curl http://169.254.169.254/hetzner/v1/metadata/instance-id 42 ```
  * **Example: Public IPv4** ```shell $ curl http://169.254.169.254/hetzner/v1/metadata/public-ipv4 1.2.3.4 ```
  * **Example: Private Networks** ```shell $ curl http://169.254.169.254/hetzner/v1/metadata/private-networks ``` ```yaml - ip: 10.0.0.2   alias_ips: [10.0.0.3, 10.0.0.4]   interface_num: 1   mac_address: 86:00:00:2a:7d:e0   network_id: 1234   network_name: nw-test1   network: 10.0.0.0/8   subnet: 10.0.0.0/24   gateway: 10.0.0.1 - ip: 192.168.0.2   alias_ips: []   interface_num: 2   mac_address: 86:00:00:2a:7d:e1   network_id: 4321   network_name: nw-test2   network: 192.168.0.0/16   subnet: 192.168.0.0/24   gateway: 192.168.0.1 ```
  * **Example: Availability Zone** ```shell $ curl http://169.254.169.254/hetzner/v1/metadata/availability-zone hel1-dc2 ```
  * **Example: Region** ```shell $ curl http://169.254.169.254/hetzner/v1/metadata/region eu-central ```
  * ## Sorting Some responses which return multiple items support sorting. If
  * they do support sorting the documentation states which fields can be used
  * for sorting. You specify sorting with the `sort` query string parameter. You
  * can sort by multiple fields. You can set the sort direction by appending
  * `:asc` or `:desc` to the field name. By default, ascending sorting is used.
  * **Example: Sorting** ``` https://api.hetzner.cloud/v1/actions?sort=status https://api.hetzner.cloud/v1/actions?sort=status:asc https://api.hetzner.cloud/v1/actions?sort=status:desc https://api.hetzner.cloud/v1/actions?sort=status:asc&sort=command:desc ```
  * ## Deprecation Notices You can find all announced deprecations in our
  * [Changelog](/changelog).
  *
  * The version of the OpenAPI document: 1.0.0
  *
  * NOTE: This class is auto generated by OpenAPI Generator
  * (https://openapi-generator.tech). https://openapi-generator.tech Do not edit
  * the class manually.
  */
package hcloud.api

import hcloud.models.ActionListResponseWithMeta
import hcloud.models.ActionResponse
import hcloud.models.ActionResponse1
import hcloud.models.AddToPlacementGroupRequest
import hcloud.models.AttachServerIsoRequest
import hcloud.models.AttachToNetworkRequest
import hcloud.models.ChangeServerAliasIpsRequest
import hcloud.models.ChangeServerDnsPtrRequest
import hcloud.models.ChangeServerProtectionRequest
import hcloud.models.ChangeServerTypeRequest
import hcloud.models.CreateImageRequest
import hcloud.models.CreateServerImage201Response
import hcloud.models.DetachFromNetworkRequest
import hcloud.models.EnableServerRescue201Response
import hcloud.models.EnableServerRescueRequest
import hcloud.models.GetActions4xxResponse
import hcloud.models.GetActions5xxResponse
import hcloud.models.RebuildServer201Response
import hcloud.models.RebuildServerRequest
import hcloud.models.RequestServerConsole201Response
import hcloud.models.ResetServerPassword201Response
import hcloud.JsonSupport.{*, given}
import hcloud.FormSerializable
import hcloud.FormStyleFormat
import hcloud.HeaderSerializable
import hcloud.ApiKeyLocation
import hcloud.PathStyleFormat
import hcloud.PathSerializable
import hcloud.CookieSerializable
import hcloud.Helpers.*
import sttp.client4.jsoniter.*
import sttp.client4.*
import sttp.model.Method

object ServerActions:
  def apply(
      baseUrl: String = "https://api.hetzner.cloud/v1"
  ): ServerActions[hcloud.Authorization.NoAuthorization.type] =
    ServerActions(baseUrl, hcloud.Authorization.NoAuthorization)
  def withBasicAuth(
      baseUrl: String,
      username: String,
      password: String
  ): ServerActions[hcloud.Authorization.BasicAuth] =
    ServerActions(baseUrl, hcloud.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(
      baseUrl: String,
      apiKey: String
  ): ServerActions[hcloud.Authorization.ApiKey] =
    ServerActions(baseUrl, hcloud.Authorization.ApiKey(apiKey))

  def withBearerTokenAuth(
      baseUrl: String,
      token: String
  ): ServerActions[hcloud.Authorization.BearerToken] =
    ServerActions(baseUrl, hcloud.Authorization.BearerToken(token))

case class ServerActions[Auth <: hcloud.Authorization] private (
    baseUrl: String,
    authConfig: hcloud.Authorization
):
  def withBasicAuth(
      username: String,
      password: String
  ): ServerActions[hcloud.Authorization.BasicAuth] =
    copy(authConfig = hcloud.Authorization.BasicAuth(username, password))

  def withApiKeyAuth(
      apiKey: String
  ): ServerActions[hcloud.Authorization.ApiKey] =
    copy(authConfig = hcloud.Authorization.ApiKey(apiKey))

  def withNoAuth: ServerActions[hcloud.Authorization.NoAuthorization.type] =
    copy(authConfig = hcloud.Authorization.NoAuthorization)

  def withBearerTokenAuth(
      token: String
  ): ServerActions[hcloud.Authorization.BearerToken] =
    copy(authConfig = hcloud.Authorization.BearerToken(token))

  /** Adds a Server to a Placement Group. Server must be powered off for this
    * command to succeed. #### Operation specific errors | Status | Code |
    * Description | | --- | --- | --- | | `422` | `server_not_stopped` | The
    * action requires a stopped server | | `422` | `already_in_placement_group` |
    * The server is already part of a placement group |
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param addToPlacementGroupRequest
    */
  def addServerToPlacementGroup(
      id: Long,
      addToPlacementGroupRequest: AddToPlacementGroupRequest
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/add_to_placement_group"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(addToPlacementGroupRequest))
      .response(asJson[ActionResponse1])

  /** Attaches an ISO to a Server. The Server will immediately see it as a new
    * disk. An already attached ISO will automatically be detached before the
    * new ISO is attached. Servers with attached ISOs have a modified boot
    * order: They will try to boot from the ISO first before falling back to
    * hard disk.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param attachServerIsoRequest
    */
  def attachServerIso(id: Long, attachServerIsoRequest: AttachServerIsoRequest)(
      using Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/attach_iso"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(attachServerIsoRequest))
      .response(asJson[ActionResponse1])

  /** Attaches a Server to a network. This will complement the fixed public
    * Server interface by adding an additional ethernet interface to the Server
    * which is connected to the specified network. The Server will get an IP
    * auto assigned from a subnet of type `server` in the same `network_zone`.
    * Using the `alias_ips` attribute you can also define one or more additional
    * IPs to the Servers. Please note that you will have to configure these IPs
    * by hand on your Server since only the primary IP will be given out by
    * DHCP. #### Operation specific errors | Status | Code | Description | | --- |
    * --- | --- | | `422` | `server_already_attached` | The server is already
    * attached to the network | | `422` | `ip_not_available` | The provided
    * Network IP is not available | | `422` | `no_subnet_available` | No Subnet
    * or IP is available for the Server within the network | | `422` |
    * `networks_overlap` | The network IP range overlaps with one of the server
    * networks |
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param attachToNetworkRequest
    */
  def attachServerToNetwork(
      id: Long,
      attachToNetworkRequest: AttachToNetworkRequest
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/attach_to_network"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(attachToNetworkRequest))
      .response(asJson[ActionResponse1])

  /** Changes the alias IPs of an already attached Network. Note that the
    * existing aliases for the specified Network will be replaced with these
    * provided in the request body. So if you want to add an alias IP, you have
    * to provide the existing ones from the Network plus the new alias IP in the
    * request body.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param changeServerAliasIpsRequest
    */
  def changeServerAliasIps(
      id: Long,
      changeServerAliasIpsRequest: ChangeServerAliasIpsRequest
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/change_alias_ips"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(changeServerAliasIpsRequest))
      .response(asJson[ActionResponse1])

  /** Changes the hostname that will appear when getting the hostname belonging
    * to the primary IPs (IPv4 and IPv6) of this Server. Floating IPs assigned
    * to the Server are not affected by this.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param changeServerDnsPtrRequest
    *   Select the IP address for which to change the DNS entry by passing `ip`.
    *   It can be either IPv4 or IPv6. The target hostname is set by passing
    *   `dns_ptr`, which must be a fully qualified domain name (FQDN) without
    *   trailing dot.
    */
  def changeServerDnsPtr(
      id: Long,
      changeServerDnsPtrRequest: ChangeServerDnsPtrRequest
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/change_dns_ptr"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(changeServerDnsPtrRequest))
      .response(asJson[ActionResponse1])

  /** Changes the protection configuration of the Server.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param changeServerProtectionRequest
    */
  def changeServerProtection(
      id: Long,
      changeServerProtectionRequest: ChangeServerProtectionRequest
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/change_protection"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(changeServerProtectionRequest))
      .response(asJson[ActionResponse1])

  /** Changes the type (Cores, RAM and disk sizes) of a Server. Server must be
    * powered off for this command to succeed. This copies the content of its
    * disk, and starts it again. You can only migrate to Server types with the
    * same `storage_type` and equal or bigger disks. Shrinking disks is not
    * possible as it might destroy data. If the disk gets upgraded, the Server
    * type can not be downgraded any more. If you plan to downgrade the Server
    * type, set `upgrade_disk` to `false`. #### Operation specific errors |
    * Status | Code | Description | | --- | --- | --- | | `422` |
    * `invalid_server_type` | The server type does not fit for the given server
    * or is deprecated | | `422` | `server_not_stopped` | The action requires a
    * stopped server |
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param changeServerTypeRequest
    */
  def changeServerType(
      id: Long,
      changeServerTypeRequest: ChangeServerTypeRequest
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/change_type"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(changeServerTypeRequest))
      .response(asJson[ActionResponse1])

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
    * Expected answers: code 201 : CreateServerImage201Response (Request
    * succeeded.) code 4xx : GetActions4xxResponse (Request failed with a user
    * error.) code 5xx : GetActions5xxResponse (Request failed with a server
    * error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param createImageRequest
    */
  def createServerImage(id: Long, createImageRequest: CreateImageRequest)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[
    Either[ResponseException[String], CreateServerImage201Response]
  ] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/create_image"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(createImageRequest))
      .response(asJson[CreateServerImage201Response])

  /** Detaches a Server from a network. The interface for this network will
    * vanish.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param detachFromNetworkRequest
    */
  def detachServerFromNetwork(
      id: Long,
      detachFromNetworkRequest: DetachFromNetworkRequest
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/detach_from_network"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(detachFromNetworkRequest))
      .response(asJson[ActionResponse1])

  /** Detaches an ISO from a Server. In case no ISO Image is attached to the
    * Server, the status of the returned Action is immediately set to `success`.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def detachServerIso(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/detach_iso"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

  /** Disables the automatic backup option and deletes all existing Backups for
    * a Server. No more additional charges for backups will be made. Caution:
    * This immediately removes all existing backups for the Server!
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def disableServerBackup(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/disable_backup"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

  /** Disables the Hetzner Rescue System for a Server. This makes a Server start
    * from its disks on next reboot. Rescue Mode is automatically disabled when
    * you first boot into it or if you do not use it for 60 minutes. Disabling
    * rescue mode will not reboot your Server — you will have to do this
    * yourself.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def disableServerRescue(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/disable_rescue"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

  /** Enables and configures the automatic daily backup option for the Server.
    * Enabling automatic backups will increase the price of the Server by 20%.
    * In return, you will get seven slots where Images of type backup can be
    * stored. Backups are automatically created daily.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def enableServerBackup(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/enable_backup"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

  /** Enable the Hetzner Rescue System for this Server. The next time a Server
    * with enabled rescue mode boots it will start a special minimal Linux
    * distribution designed for repair and reinstall. In case a Server cannot
    * boot on its own you can use this to access a Server’s disks. Rescue Mode
    * is automatically disabled when you first boot into it or if you do not use
    * it for 60 minutes. Enabling rescue mode will not
    * [reboot](https://docs.hetzner.cloud/#server-actions-soft-reboot-a-server)
    * your Server — you will have to do this yourself.
    *
    * Expected answers: code 201 : EnableServerRescue201Response (Request
    * succeeded.) code 4xx : GetActions4xxResponse (Request failed with a user
    * error.) code 5xx : GetActions5xxResponse (Request failed with a server
    * error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param enableServerRescueRequest
    */
  def enableServerRescue(
      id: Long,
      enableServerRescueRequest: EnableServerRescueRequest
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[
    Either[ResponseException[String], EnableServerRescue201Response]
  ] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/enable_rescue"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(enableServerRescueRequest))
      .response(asJson[EnableServerRescue201Response])

  /** Returns a specific Action object for a Server.
    *
    * Expected answers: code 200 : ActionResponse (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param actionId
    *   ID of the Action.
    */
  def getServerAction(id: Long, actionId: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val actionIdPathParam = PathSerializable.serialize(
      "action_id",
      actionId,
      PathStyleFormat.SIMPLE,
      false
    )
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/${actionIdPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse])

  /** Returns a specific Action object.
    *
    * Expected answers: code 200 : ActionResponse (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Action.
    */
  def getServersAction(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/actions/${idPathParam}"

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse])

  /** Returns all Action objects for a Server. You can `sort` the results by
    * using the sort URI parameter, and filter them with the `status` parameter.
    *
    * Expected answers: code 200 : ActionListResponseWithMeta (Request
    * succeeded.) code 4xx : GetActions4xxResponse (Request failed with a user
    * error.) code 5xx : GetActions5xxResponse (Request failed with a server
    * error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param sort
    *   Sort actions by field and direction. May be used multiple times. For
    *   more information, see \"[Sorting](#description/sorting)\".
    * @param status
    *   Filter the actions by status. May be used multiple times. The response
    *   will only contain actions matching the specified statuses.
    * @param page
    *   Page number to return. For more information, see
    *   \"[Pagination](#description/pagination)\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"[Pagination](#description/pagination)\".
    */
  def listServerActions(
      id: Long,
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[
    Either[ResponseException[String], ActionListResponseWithMeta]
  ] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions"
        .addParams(
          FormSerializable
            .serialize("sort", sort, FormStyleFormat.FORM, true): _*
        )
        .addParams(
          FormSerializable
            .serialize("status", status, FormStyleFormat.FORM, true): _*
        )
        .addParams(
          FormSerializable
            .serialize("page", page, FormStyleFormat.FORM, true): _*
        )
        .addParams(
          FormSerializable
            .serialize("per_page", perPage, FormStyleFormat.FORM, true): _*
        )

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionListResponseWithMeta])

  /** Returns all Action objects. You can `sort` the results by using the sort
    * URI parameter, and filter them with the `status` and `id` parameter.
    *
    * Expected answers: code 200 : ActionListResponseWithMeta (Request
    * succeeded.) code 4xx : GetActions4xxResponse (Request failed with a user
    * error.) code 5xx : GetActions5xxResponse (Request failed with a server
    * error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   Filter the actions by ID. May be used multiple times. The response will
    *   only contain actions matching the specified IDs.
    * @param sort
    *   Sort actions by field and direction. May be used multiple times. For
    *   more information, see \"[Sorting](#description/sorting)\".
    * @param status
    *   Filter the actions by status. May be used multiple times. The response
    *   will only contain actions matching the specified statuses.
    * @param page
    *   Page number to return. For more information, see
    *   \"[Pagination](#description/pagination)\".
    * @param perPage
    *   Maximum number of entries returned per page. For more information, see
    *   \"[Pagination](#description/pagination)\".
    */
  def listServersActions(
      id: Seq[Long],
      sort: Seq[String],
      status: Seq[String],
      page: Option[Long] = scala.None,
      perPage: Option[Long] = scala.None
  )(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[
    Either[ResponseException[String], ActionListResponseWithMeta]
  ] =
    val requestURL =
      uri"$baseUrl/servers/actions"
        .addParams(
          FormSerializable.serialize("id", id, FormStyleFormat.FORM, true): _*
        )
        .addParams(
          FormSerializable
            .serialize("sort", sort, FormStyleFormat.FORM, true): _*
        )
        .addParams(
          FormSerializable
            .serialize("status", status, FormStyleFormat.FORM, true): _*
        )
        .addParams(
          FormSerializable
            .serialize("page", page, FormStyleFormat.FORM, true): _*
        )
        .addParams(
          FormSerializable
            .serialize("per_page", perPage, FormStyleFormat.FORM, true): _*
        )

    basicRequest
      .method(Method.GET, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionListResponseWithMeta])

  /** Cuts power to the Server. This forcefully stops it without giving the
    * Server operating system time to gracefully stop. May lead to data loss,
    * equivalent to pulling the power cord. Power off should only be used when
    * shutdown does not work.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def poweroffServer(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/poweroff"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

  /** Starts a Server by turning its power on.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def poweronServer(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/poweron"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

  /** Reboots a Server gracefully by sending an ACPI request. The Server
    * operating system must support ACPI and react to the request, otherwise the
    * Server will not reboot.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def rebootServer(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/reboot"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

  /** Rebuilds a Server overwriting its disk with the content of an Image,
    * thereby **destroying all data** on the target Server The Image can either
    * be one you have created earlier (`backup` or `snapshot` Image) or it can
    * be a completely fresh `system` Image provided by us. You can get a list of
    * all available Images with `GET /images`. Your Server will automatically be
    * powered off before the rebuild command executes.
    *
    * Expected answers: code 201 : RebuildServer201Response (Request succeeded.)
    * code 4xx : GetActions4xxResponse (Request failed with a user error.) code
    * 5xx : GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    * @param rebuildServerRequest
    *   To select which Image to rebuild from you can either pass an ID or a
    *   name as the `image` argument. Passing a name only works for `system`
    *   Images since the other Image types do not have a name set.
    */
  def rebuildServer(id: Long, rebuildServerRequest: RebuildServerRequest)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[
    Either[ResponseException[String], RebuildServer201Response]
  ] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/rebuild"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .body(asJson(rebuildServerRequest))
      .response(asJson[RebuildServer201Response])

  /** Removes a Server from a Placement Group.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def removeServerFromPlacementGroup(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/remove_from_placement_group"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

  /** Requests credentials for remote access via VNC over websocket to keyboard,
    * monitor, and mouse for a Server. The provided URL is valid for 1 minute,
    * after this period a new url needs to be created to connect to the Server.
    * How long the connection is open after the initial connect is not subject
    * to this timeout.
    *
    * Expected answers: code 201 : RequestServerConsole201Response (Request
    * succeeded.) code 4xx : GetActions4xxResponse (Request failed with a user
    * error.) code 5xx : GetActions5xxResponse (Request failed with a server
    * error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def requestServerConsole(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[
    Either[ResponseException[String], RequestServerConsole201Response]
  ] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/request_console"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[RequestServerConsole201Response])

  /** Cuts power to a Server and starts it again. This forcefully stops it
    * without giving the Server operating system time to gracefully stop. This
    * may lead to data loss, it’s equivalent to pulling the power cord and
    * plugging it in again. Reset should only be used when reboot does not work.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def resetServer(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/reset"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

  /** Resets the root password. Only works for Linux systems that are running
    * the qemu guest agent. Server must be powered on (status `running`) in
    * order for this operation to succeed. This will generate a new password for
    * this Server and return it. If this does not succeed you can use the rescue
    * system to netboot the Server and manually change your Server password by
    * hand.
    *
    * Expected answers: code 201 : ResetServerPassword201Response (Request
    * succeeded.) code 4xx : GetActions4xxResponse (Request failed with a user
    * error.) code 5xx : GetActions5xxResponse (Request failed with a server
    * error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def resetServerPassword(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[
    Either[ResponseException[String], ResetServerPassword201Response]
  ] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/reset_password"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ResetServerPassword201Response])

  /** Shuts down a Server gracefully by sending an ACPI shutdown request. The
    * Server operating system must support ACPI and react to the request,
    * otherwise the Server will not shut down. Please note that the `action`
    * status in this case only reflects whether the action was sent to the
    * server. It does not mean that the server actually shut down successfully.
    * If you need to ensure that the server is off, use the `poweroff` action.
    *
    * Expected answers: code 201 : ActionResponse1 (Request succeeded.) code 4xx
    * : GetActions4xxResponse (Request failed with a user error.) code 5xx :
    * GetActions5xxResponse (Request failed with a server error.)
    *
    * Available security schemes: APIToken (http)
    *
    * @param id
    *   ID of the Server.
    */
  def shutdownServer(id: Long)(using
      Auth <:< hcloud.Authorization.BearerToken
  ): sttp.client4.Request[Either[ResponseException[String], ActionResponse1]] =
    val idPathParam =
      PathSerializable.serialize("id", id, PathStyleFormat.SIMPLE, false)
    val requestURL =
      uri"$baseUrl/servers/${idPathParam}/actions/shutdown"

    basicRequest
      .method(Method.POST, requestURL)
      .contentType("application/json")
      .auth(authConfig)
      .response(asJson[ActionResponse1])

end ServerActions
