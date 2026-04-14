# openapi-client

Hetzner Cloud API
- API version: 1.0.0
    - Generator version: 7.18.0

# Overview

This is the official documentation for the Hetzner Cloud API.

## Introduction

The Hetzner Cloud API operates over HTTPS and uses JSON as its data format. The API is a RESTful API and utilizes HTTP methods and HTTP status codes to specify requests and responses.

As an alternative to working directly with our API you may also consider to use:

- Our CLI program [hcloud](https://github.com/hetznercloud/cli)
- Our [library for Go](https://github.com/hetznercloud/hcloud-go)
- Our [library for Python](https://github.com/hetznercloud/hcloud-python)

You can find even more libraries, tools and integrations on our [Awesome List on GitHub](https://github.com/hetznercloud/awesome-hcloud).

### Open source credits

If you are developing an open-source project that supports or intends to add support for Hetzner APIs, you may be eligible for a free one-time credit of up to € 50 / $ 50 on your account. Please contact us via the support page on your [Hetzner Console](https://console.hetzner.cloud/support) and let us know the following:

- The name of the project you are working on
- A short description of the project
- Link to the project website or repo where the project is hosted
- Affiliation with / role in the project (e.g. project maintainer)
- Link to some other open-source work you have already done (if you have done so)

**Note:** We only consider rewards for projects that provide Hetzner-specific functionality or integrations. For example, our Object Storage exposes a standard S3 API without any Hetzner-specific extensions. Projects that focus solely on generic S3 capabilities (e.g., general S3 clients or SDKs) are not Hetzner-specific and are therefore not eligible for Hetzner Rewards.

## Getting Started

To get started using the API you first need an API token. Sign in into the [Hetzner Console](https://console.hetzner.com/) choose a Project, go to `Security` → `API Tokens`, and generate a new token. Make sure to copy the token because it won’t be shown to you again. A token is bound to a Project, to interact with the API of another Project you have to create a new token inside the Project. Let’s say your new token is `LRK9DAWQ1ZAEFSrCNEEzLCUwhYX1U3g7wMg4dTlkkDC96fyDuyJ39nVbVjCKSDfj`.

You’re now ready to do your first request against the API. To get a list of all Servers in your Project, issue the example request on the right side using [curl](https://curl.se/).

Make sure to replace the token in the example command with the token you have just created. Since your Project probably does not contain any Servers yet, the example response will look like the response on the right side. We will almost always provide a resource root like `servers` inside the example response. A response can also contain a `meta` object with information like [Pagination](#description/pagination).

**Example Request**

```shell
curl -H \"Authorization: Bearer LRK9DAWQ1ZAEFSrCNEEzLCUwhYX1U3g7wMg4dTlkkDC96fyDuyJ39nVbVjCKSDfj\" \\
  https://api.hetzner.cloud/v1/servers
```

**Example Response**

```json
{
  \"servers\": [],
  \"meta\": {
    \"pagination\": {
      \"page\": 1,
      \"per_page\": 25,
      \"previous_page\": null,
      \"next_page\": null,
      \"last_page\": 1,
      \"total_entries\": 0
    }
  }
}
```

## Authentication

All requests to the Hetzner Cloud API must be authenticated via a API token. Include your secret API token in every request you send to the API with the `Authorization` HTTP header.

To create a new API token for your Project, switch into the [Hetzner Console](https://console.hetzner.com/) choose a Project, go to `Security` → `API Tokens`, and generate a new token.

**Example Authorization header**

```http
Authorization: Bearer LRK9DAWQ1ZAEFSrCNEEzLCUwhYX1U3g7wMg4dTlkkDC96fyDuyJ39nVbVjCKSDfj
```

## Query Parameters

The API makes use of query parameters to sort and filter responses. The parameter names and values must be URI encoded according to [RFC2396](https://datatracker.ietf.org/doc/html/rfc2396). Query parameters of type `array` can be used multiple times:

**Example query parameters for pagination**

```
https://api.hetzner.cloud/v1/certificates?page=1&page_size=12
```

**Example use of multiple values for a parameter**

```
https://api.hetzner.cloud/v1/certificates?type=uploaded&type=managed
```

**Example use of an encoded parameter**

```
https://api.hetzner.cloud/v1/certificates?label_selector=key%3Dvalue
```

## Errors

Errors are indicated by HTTP status codes. Further, the response of the request which generated the error contains an error code, an error message, and, optionally, error details. The schema of the error details object depends on the error code.

The error response contains the following keys:

| Keys      | Meaning                                                               |
| --------- | --------------------------------------------------------------------- |
| `code`    | Short string indicating the type of error (machine-parsable)          |
| `message` | Textual description on what has gone wrong                            |
| `details` | An object providing for details on the error (schema depends on code) |

**Example response**

```json
{
  \"error\": {
    \"code\": \"invalid_input\",
    \"message\": \"invalid input in field 'broken_field': is too long\",
    \"details\": {
      \"fields\": [
        {
          \"name\": \"broken_field\",
          \"messages\": [\"is too long\"]
        }
      ]
    }
  }
}
```

### Error Codes

| Status | Code | Description |
| --- | --- | --- |
| `400` | `json_error` | Invalid JSON input in your request. |
| `401` | `unauthorized` | Request was made with an invalid or unknown token. |
| `401` | `token_readonly` | The token is only allowed to perform GET requests. |
| `403` | `forbidden` | Insufficient permissions for this request. |
| `403` | `maintenance` | Cannot perform operation due to maintenance. |
| `403` | `resource_limit_exceeded` | Error when exceeding the maximum quantity of a resource for an account. |
| `404` | `not_found` | Entity not found. |
| `405` | `method_not_allowed` | The request method is not allowed |
| `409` | `uniqueness_error` | One or more of the objects fields must be unique. |
| `409` | `conflict` | The resource has changed during the request, please retry. |
| `410` | `deprecated_api_endpoint` | The API endpoint functionality was removed. |
| `412` | `resource_unavailable` | The requested resource is currently unavailable (e.g. not available for order). |
| `422` | `invalid_input` | Error while parsing or processing the input. |
| `422` | `service_error` | Error within a service. |
| `422` | `unsupported_error` | The corresponding resource does not support the Action. |
| `423` | `locked` | The item you are trying to access is locked (there is already an Action running). |
| `423` | `protected` | The Action you are trying to start is protected for this resource. |
| `429` | `rate_limit_exceeded` | Error when sending too many requests. |
| `500` | `server_error` | Error within the API backend. |
| `503` | `unavailable` | A service or product is currently not available. |
| `504` | `timeout` | The request could not be answered in time, please retry. |

**invalid_input**

```json
{
  \"error\": {
    \"code\": \"invalid_input\",
    \"message\": \"invalid input in field 'broken_field': is too long\",
    \"details\": {
      \"fields\": [
        {
          \"name\": \"broken_field\",
          \"messages\": [\"is too long\"]
        }
      ]
    }
  }
}
```

**uniqueness_error**

```json
{
  \"error\": {
    \"code\": \"uniqueness_error\",
    \"message\": \"SSH key with the same fingerprint already exists\",
    \"details\": {
      \"fields\": [
        {
          \"name\": \"public_key\"
        }
      ]
    }
  }
}
```

**resource_limit_exceeded**

```json
{
  \"error\": {
    \"code\": \"resource_limit_exceeded\",
    \"message\": \"project limit exceeded\",
    \"details\": {
      \"limits\": [
        {
          \"name\": \"project_limit\"
        }
      ]
    }
  }
}
```

**deprecated_api_endpoint**

```json
{
  \"error\": {
    \"code\": \"deprecated_api_endpoint\",
    \"message\": \"API functionality was removed\",
    \"details\": {
      \"announcement\": \"https://docs.hetzner.cloud/changelog#2023-07-20-foo-endpoint-is-deprecated\"
    }
  }
}
```

## Actions

Actions represent asynchronous tasks within the API, targeting one or more resources. Triggering changes in the API may return a `running` action.

An action should be waited upon, until it reaches either the `success` or `error` state. Avoid polling the action's state too frequently to reduce the risk of exhausting your API requests and hitting the [rate limit](#description/rate-limiting).

If an action fails, it will contain details about the underlying error.

Once the asynchronous tasks have completed and the targeted resources are in a consistent state, the action is marked as succeeded.

In some cases, you may trigger multiple changes at once, and only wait for the returned actions at a later stage.

## Labels

Labels are `key/value` pairs that can be attached to all resources.

Valid label keys have two segments: an optional prefix and name, separated by a slash (`/`). The name segment is required and must be a string of 63 characters or less, beginning and ending with an alphanumeric character (`[a-z0-9A-Z]`) with dashes (`-`), underscores (`_`), dots (`.`), and alphanumerics between. The prefix is optional. If specified, the prefix must be a DNS subdomain: a series of DNS labels separated by dots (`.`), not longer than 253 characters in total, followed by a slash (`/`).

Valid label values must be a string of 63 characters or less and must be empty or begin and end with an alphanumeric character (`[a-z0-9A-Z]`) with dashes (`-`), underscores (`_`), dots (`.`), and alphanumerics between.

The `hetzner.cloud/` prefix is reserved and cannot be used.

**Example Labels**

```json
{
  \"labels\": {
    \"environment\": \"development\",
    \"service\": \"backend\",
    \"example.com/my\": \"label\",
    \"just-a-key\": \"\"
  }
}
```

## Label Selector

For resources with labels, you can filter resources by their labels using the label selector query language.

| Expression           | Meaning                                              |
| -------------------- | ---------------------------------------------------- |
| `k==v` / `k=v`       | Value of key `k` does equal value `v`                |
| `k!=v`               | Value of key `k` does not equal value `v`            |
| `k`                  | Key `k` is present                                   |
| `!k`                 | Key `k` is not present                               |
| `k in (v1,v2,v3)`    | Value of key `k` is `v1`, `v2`, or `v3`              |
| `k notin (v1,v2,v3)` | Value of key `k` is neither `v1`, nor `v2`, nor `v3` |
| `k1==v,!k2`          | Value of key `k1` is `v` and key `k2` is not present |

### Examples

- Returns all resources that have a `env=production` label and that don't have a `type=database` label:

  `env=production,type!=database`

- Returns all resources that have a `env=testing` or `env=staging` label:

  `env in (testing,staging)`

- Returns all resources that don't have a `type` label:

  `!type`

## Pagination

Responses which return multiple items support pagination. If they do support pagination, it can be controlled with following query string parameters:

- A `page` parameter specifies the page to fetch. The number of the first page is 1.
- A `per_page` parameter specifies the number of items returned per page. The default value is 25, the maximum value is 50 except otherwise specified in the documentation.

Responses contain a `Link` header with pagination information.

Additionally, if the response body is JSON and the root object is an object, that object has a `pagination` object inside the `meta` object with pagination information:

**Example Pagination**

```json
{
    \"servers\": [...],
    \"meta\": {
        \"pagination\": {
            \"page\": 2,
            \"per_page\": 25,
            \"previous_page\": 1,
            \"next_page\": 3,
            \"last_page\": 4,
            \"total_entries\": 100
        }
    }
}
```

The keys `previous_page`, `next_page`, `last_page`, and `total_entries` may be `null` when on the first page, last page, or when the total number of entries is unknown.

**Example Pagination Link header**

```http
Link: <https://api.hetzner.cloud/v1/actions?page=2&per_page=5>; rel=\"prev\",
      <https://api.hetzner.cloud/v1/actions?page=4&per_page=5>; rel=\"next\",
      <https://api.hetzner.cloud/v1/actions?page=6&per_page=5>; rel=\"last\"
```

Line breaks have been added for display purposes only and responses may only contain some of the above `rel` values.

## Rate Limiting

All requests, whether they are authenticated or not, are subject to rate limiting. If you have reached your limit, your requests will be handled with a `429 Too Many Requests` error. Burst requests are allowed. Responses contain several headers which provide information about your current rate limit status.

- The `RateLimit-Limit` header contains the total number of requests you can perform per hour.
- The `RateLimit-Remaining` header contains the number of requests remaining in the current rate limit time frame.
- The `RateLimit-Reset` header contains a UNIX timestamp of the point in time when your rate limit will have recovered, and you will have the full number of requests available again.

The default limit is 3600 requests per hour and per Project. The number of remaining requests increases gradually. For example, when your limit is 3600 requests per hour, the number of remaining requests will increase by 1 every second.

## Server Metadata

Your Server can discover metadata about itself by doing a HTTP request to specific URLs. The following data is available:

| Data              | Format | Contents                                                     |
| ----------------- | ------ | ------------------------------------------------------------ |
| hostname          | text   | Name of the Server as set in the api                         |
| instance-id       | number | ID of the server                                             |
| public-ipv4       | text   | Primary public IPv4 address                                  |
| private-networks  | yaml   | Details about the private networks the Server is attached to |
| availability-zone | text   | Name of the availability zone that Server runs in            |
| region            | text   | Network zone, e.g. eu-central                                |

**Example: Summary**

```shell
$ curl http://169.254.169.254/hetzner/v1/metadata
```

```yaml
availability-zone: hel1-dc2
hostname: my-server
instance-id: 42
public-ipv4: 1.2.3.4
region: eu-central
```

**Example: Hostname**

```shell
$ curl http://169.254.169.254/hetzner/v1/metadata/hostname
my-server
```

**Example: Instance ID**

```shell
$ curl http://169.254.169.254/hetzner/v1/metadata/instance-id
42
```

**Example: Public IPv4**

```shell
$ curl http://169.254.169.254/hetzner/v1/metadata/public-ipv4
1.2.3.4
```

**Example: Private Networks**

```shell
$ curl http://169.254.169.254/hetzner/v1/metadata/private-networks
```

```yaml
- ip: 10.0.0.2
  alias_ips: [10.0.0.3, 10.0.0.4]
  interface_num: 1
  mac_address: 86:00:00:2a:7d:e0
  network_id: 1234
  network_name: nw-test1
  network: 10.0.0.0/8
  subnet: 10.0.0.0/24
  gateway: 10.0.0.1
- ip: 192.168.0.2
  alias_ips: []
  interface_num: 2
  mac_address: 86:00:00:2a:7d:e1
  network_id: 4321
  network_name: nw-test2
  network: 192.168.0.0/16
  subnet: 192.168.0.0/24
  gateway: 192.168.0.1
```

**Example: Availability Zone**

```shell
$ curl http://169.254.169.254/hetzner/v1/metadata/availability-zone
hel1-dc2
```

**Example: Region**

```shell
$ curl http://169.254.169.254/hetzner/v1/metadata/region
eu-central
```

## Sorting

Some responses which return multiple items support sorting. If they do support sorting the documentation states which fields can be used for sorting. You specify sorting with the `sort` query string parameter. You can sort by multiple fields. You can set the sort direction by appending `:asc` or `:desc` to the field name. By default, ascending sorting is used.

**Example: Sorting**

```
https://api.hetzner.cloud/v1/actions?sort=status
https://api.hetzner.cloud/v1/actions?sort=status:asc
https://api.hetzner.cloud/v1/actions?sort=status:desc
https://api.hetzner.cloud/v1/actions?sort=status:asc&sort=command:desc
```

## Deprecation Notices

You can find all announced deprecations in our [Changelog](/changelog).



*Automatically generated by the [OpenAPI Generator](https://openapi-generator.tech)*

## Requirements

Building the API client library requires:
1. Java 1.7+
2. Maven/Gradle/SBT

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean deploy
```

Refer to the [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-client</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "org.openapitools:openapi-client:0.1.0-SNAPSHOT"
```

### SBT users

```scala
libraryDependencies += "org.openapitools" % "openapi-client" % "0.1.0-SNAPSHOT"
```

## Getting Started

## Documentation for API Endpoints

All URIs are relative to *https://api.hetzner.cloud/v1*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*Actions* | **getAction** | **GET** /actions/${idPathParam} | Get an Action
*Actions* | **getActions** | **GET** /actions | Get multiple Actions
*CertificateActions* | **getCertificateAction** | **GET** /certificates/${idPathParam}/actions/${actionIdPathParam} | Get an Action for a Certificate
*CertificateActions* | **getCertificatesAction** | **GET** /certificates/actions/${idPathParam} | Get an Action
*CertificateActions* | **listCertificateActions** | **GET** /certificates/${idPathParam}/actions | List Actions for a Certificate
*CertificateActions* | **listCertificatesActions** | **GET** /certificates/actions | List Actions
*CertificateActions* | **retryCertificate** | **POST** /certificates/${idPathParam}/actions/retry | Retry Issuance or Renewal
*Certificates* | **createCertificate** | **POST** /certificates | Create a Certificate
*Certificates* | **deleteCertificate** | **DELETE** /certificates/${idPathParam} | Delete a Certificate
*Certificates* | **getCertificate** | **GET** /certificates/${idPathParam} | Get a Certificate
*Certificates* | **listCertificates** | **GET** /certificates | List Certificates
*Certificates* | **updateCertificate** | **PUT** /certificates/${idPathParam} | Update a Certificate
*DataCenters* | **getDatacenter** | **GET** /datacenters/${idPathParam} | Get a Data Center
*DataCenters* | **listDatacenters** | **GET** /datacenters | List Data Centers
*FirewallActions* | **applyFirewallToResources** | **POST** /firewalls/${idPathParam}/actions/apply_to_resources | Apply to Resources
*FirewallActions* | **getFirewallAction** | **GET** /firewalls/${idPathParam}/actions/${actionIdPathParam} | Get an Action for a Firewall
*FirewallActions* | **getFirewallsAction** | **GET** /firewalls/actions/${idPathParam} | Get an Action
*FirewallActions* | **listFirewallActions** | **GET** /firewalls/${idPathParam}/actions | List Actions for a Firewall
*FirewallActions* | **listFirewallsActions** | **GET** /firewalls/actions | List Actions
*FirewallActions* | **removeFirewallFromResources** | **POST** /firewalls/${idPathParam}/actions/remove_from_resources | Remove from Resources
*FirewallActions* | **setFirewallRules** | **POST** /firewalls/${idPathParam}/actions/set_rules | Set Rules
*Firewalls* | **createFirewall** | **POST** /firewalls | Create a Firewall
*Firewalls* | **deleteFirewall** | **DELETE** /firewalls/${idPathParam} | Delete a Firewall
*Firewalls* | **getFirewall** | **GET** /firewalls/${idPathParam} | Get a Firewall
*Firewalls* | **listFirewalls** | **GET** /firewalls | List Firewalls
*Firewalls* | **updateFirewall** | **PUT** /firewalls/${idPathParam} | Update a Firewall
*FloatingIPActions* | **assignFloatingIp** | **POST** /floating_ips/${idPathParam}/actions/assign | Assign a Floating IP to a Server
*FloatingIPActions* | **changeFloatingIpDnsPtr** | **POST** /floating_ips/${idPathParam}/actions/change_dns_ptr | Change reverse DNS records for a Floating IP
*FloatingIPActions* | **changeFloatingIpProtection** | **POST** /floating_ips/${idPathParam}/actions/change_protection | Change Floating IP Protection
*FloatingIPActions* | **getFloatingIpAction** | **GET** /floating_ips/${idPathParam}/actions/${actionIdPathParam} | Get an Action for a Floating IP
*FloatingIPActions* | **getFloatingIpsAction** | **GET** /floating_ips/actions/${idPathParam} | Get an Action
*FloatingIPActions* | **listFloatingIpActions** | **GET** /floating_ips/${idPathParam}/actions | List Actions for a Floating IP
*FloatingIPActions* | **listFloatingIpsActions** | **GET** /floating_ips/actions | List Actions
*FloatingIPActions* | **unassignFloatingIp** | **POST** /floating_ips/${idPathParam}/actions/unassign | Unassign a Floating IP
*FloatingIPs* | **createFloatingIp** | **POST** /floating_ips | Create a Floating IP
*FloatingIPs* | **deleteFloatingIp** | **DELETE** /floating_ips/${idPathParam} | Delete a Floating IP
*FloatingIPs* | **getFloatingIp** | **GET** /floating_ips/${idPathParam} | Get a Floating IP
*FloatingIPs* | **listFloatingIps** | **GET** /floating_ips | List Floating IPs
*FloatingIPs* | **updateFloatingIp** | **PUT** /floating_ips/${idPathParam} | Update a Floating IP
*ISOs* | **getIso** | **GET** /isos/${idPathParam} | Get an ISO
*ISOs* | **listIsos** | **GET** /isos | List ISOs
*ImageActions* | **changeImageProtection** | **POST** /images/${idPathParam}/actions/change_protection | Change Image Protection
*ImageActions* | **getImageAction** | **GET** /images/${idPathParam}/actions/${actionIdPathParam} | Get an Action for an Image
*ImageActions* | **getImagesAction** | **GET** /images/actions/${idPathParam} | Get an Action
*ImageActions* | **listImageActions** | **GET** /images/${idPathParam}/actions | List Actions for an Image
*ImageActions* | **listImagesActions** | **GET** /images/actions | List Actions
*Images* | **deleteImage** | **DELETE** /images/${idPathParam} | Delete an Image
*Images* | **getImage** | **GET** /images/${idPathParam} | Get an Image
*Images* | **listImages** | **GET** /images | List Images
*Images* | **updateImage** | **PUT** /images/${idPathParam} | Update an Image
*LoadBalancerActions* | **addLoadBalancerService** | **POST** /load_balancers/${idPathParam}/actions/add_service | Add Service
*LoadBalancerActions* | **addLoadBalancerTarget** | **POST** /load_balancers/${idPathParam}/actions/add_target | Add Target
*LoadBalancerActions* | **attachLoadBalancerToNetwork** | **POST** /load_balancers/${idPathParam}/actions/attach_to_network | Attach a Load Balancer to a Network
*LoadBalancerActions* | **changeLoadBalancerAlgorithm** | **POST** /load_balancers/${idPathParam}/actions/change_algorithm | Change Algorithm
*LoadBalancerActions* | **changeLoadBalancerDnsPtr** | **POST** /load_balancers/${idPathParam}/actions/change_dns_ptr | Change reverse DNS entry for this Load Balancer
*LoadBalancerActions* | **changeLoadBalancerProtection** | **POST** /load_balancers/${idPathParam}/actions/change_protection | Change Load Balancer Protection
*LoadBalancerActions* | **changeLoadBalancerType** | **POST** /load_balancers/${idPathParam}/actions/change_type | Change the Type of a Load Balancer
*LoadBalancerActions* | **deleteLoadBalancerService** | **POST** /load_balancers/${idPathParam}/actions/delete_service | Delete Service
*LoadBalancerActions* | **detachLoadBalancerFromNetwork** | **POST** /load_balancers/${idPathParam}/actions/detach_from_network | Detach a Load Balancer from a Network
*LoadBalancerActions* | **disableLoadBalancerPublicInterface** | **POST** /load_balancers/${idPathParam}/actions/disable_public_interface | Disable the public interface of a Load Balancer
*LoadBalancerActions* | **enableLoadBalancerPublicInterface** | **POST** /load_balancers/${idPathParam}/actions/enable_public_interface | Enable the public interface of a Load Balancer
*LoadBalancerActions* | **getLoadBalancerAction** | **GET** /load_balancers/${idPathParam}/actions/${actionIdPathParam} | Get an Action for a Load Balancer
*LoadBalancerActions* | **getLoadBalancersAction** | **GET** /load_balancers/actions/${idPathParam} | Get an Action
*LoadBalancerActions* | **listLoadBalancerActions** | **GET** /load_balancers/${idPathParam}/actions | List Actions for a Load Balancer
*LoadBalancerActions* | **listLoadBalancersActions** | **GET** /load_balancers/actions | List Actions
*LoadBalancerActions* | **removeLoadBalancerTarget** | **POST** /load_balancers/${idPathParam}/actions/remove_target | Remove Target
*LoadBalancerActions* | **updateLoadBalancerService** | **POST** /load_balancers/${idPathParam}/actions/update_service | Update Service
*LoadBalancerTypes* | **getLoadBalancerType** | **GET** /load_balancer_types/${idPathParam} | Get a Load Balancer Type
*LoadBalancerTypes* | **listLoadBalancerTypes** | **GET** /load_balancer_types | List Load Balancer Types
*LoadBalancers* | **createLoadBalancer** | **POST** /load_balancers | Create a Load Balancer
*LoadBalancers* | **deleteLoadBalancer** | **DELETE** /load_balancers/${idPathParam} | Delete a Load Balancer
*LoadBalancers* | **getLoadBalancer** | **GET** /load_balancers/${idPathParam} | Get a Load Balancer
*LoadBalancers* | **getLoadBalancerMetrics** | **GET** /load_balancers/${idPathParam}/metrics | Get Metrics for a LoadBalancer
*LoadBalancers* | **listLoadBalancers** | **GET** /load_balancers | List Load Balancers
*LoadBalancers* | **updateLoadBalancer** | **PUT** /load_balancers/${idPathParam} | Update a Load Balancer
*Locations* | **getLocation** | **GET** /locations/${idPathParam} | Get a Location
*Locations* | **listLocations** | **GET** /locations | List Locations
*NetworkActions* | **addNetworkRoute** | **POST** /networks/${idPathParam}/actions/add_route | Add a route to a Network
*NetworkActions* | **addNetworkSubnet** | **POST** /networks/${idPathParam}/actions/add_subnet | Add a subnet to a Network
*NetworkActions* | **changeNetworkIpRange** | **POST** /networks/${idPathParam}/actions/change_ip_range | Change IP range of a Network
*NetworkActions* | **changeNetworkProtection** | **POST** /networks/${idPathParam}/actions/change_protection | Change Network Protection
*NetworkActions* | **deleteNetworkRoute** | **POST** /networks/${idPathParam}/actions/delete_route | Delete a route from a Network
*NetworkActions* | **deleteNetworkSubnet** | **POST** /networks/${idPathParam}/actions/delete_subnet | Delete a subnet from a Network
*NetworkActions* | **getNetworkAction** | **GET** /networks/${idPathParam}/actions/${actionIdPathParam} | Get an Action for a Network
*NetworkActions* | **getNetworksAction** | **GET** /networks/actions/${idPathParam} | Get an Action
*NetworkActions* | **listNetworkActions** | **GET** /networks/${idPathParam}/actions | List Actions for a Network
*NetworkActions* | **listNetworksActions** | **GET** /networks/actions | List Actions
*Networks* | **createNetwork** | **POST** /networks | Create a Network
*Networks* | **deleteNetwork** | **DELETE** /networks/${idPathParam} | Delete a Network
*Networks* | **getNetwork** | **GET** /networks/${idPathParam} | Get a Network
*Networks* | **listNetworks** | **GET** /networks | List Networks
*Networks* | **updateNetwork** | **PUT** /networks/${idPathParam} | Update a Network
*PlacementGroups* | **createPlacementGroup** | **POST** /placement_groups | Create a PlacementGroup
*PlacementGroups* | **deletePlacementGroup** | **DELETE** /placement_groups/${idPathParam} | Delete a PlacementGroup
*PlacementGroups* | **getPlacementGroup** | **GET** /placement_groups/${idPathParam} | Get a PlacementGroup
*PlacementGroups* | **listPlacementGroups** | **GET** /placement_groups | List Placement Groups
*PlacementGroups* | **updatePlacementGroup** | **PUT** /placement_groups/${idPathParam} | Update a PlacementGroup
*Pricing* | **getPricing** | **GET** /pricing | Get all prices
*PrimaryIPActions* | **assignPrimaryIp** | **POST** /primary_ips/${idPathParam}/actions/assign | Assign a Primary IP to a resource
*PrimaryIPActions* | **changePrimaryIpDnsPtr** | **POST** /primary_ips/${idPathParam}/actions/change_dns_ptr | Change reverse DNS records for a Primary IP
*PrimaryIPActions* | **changePrimaryIpProtection** | **POST** /primary_ips/${idPathParam}/actions/change_protection | Change Primary IP Protection
*PrimaryIPActions* | **getPrimaryIpAction** | **GET** /primary_ips/${idPathParam}/actions/${actionIdPathParam} | Get an Action for a Primary IP
*PrimaryIPActions* | **getPrimaryIpsAction** | **GET** /primary_ips/actions/${idPathParam} | Get an Action
*PrimaryIPActions* | **listPrimaryIpActions** | **GET** /primary_ips/${idPathParam}/actions | List Actions for a Primary IP
*PrimaryIPActions* | **listPrimaryIpsActions** | **GET** /primary_ips/actions | List Actions
*PrimaryIPActions* | **unassignPrimaryIp** | **POST** /primary_ips/${idPathParam}/actions/unassign | Unassign a Primary IP from a resource
*PrimaryIPs* | **createPrimaryIp** | **POST** /primary_ips | Create a Primary IP
*PrimaryIPs* | **deletePrimaryIp** | **DELETE** /primary_ips/${idPathParam} | Delete a Primary IP
*PrimaryIPs* | **getPrimaryIp** | **GET** /primary_ips/${idPathParam} | Get a Primary IP
*PrimaryIPs* | **listPrimaryIps** | **GET** /primary_ips | List Primary IPs
*PrimaryIPs* | **updatePrimaryIp** | **PUT** /primary_ips/${idPathParam} | Update a Primary IP
*SSHKeys* | **createSshKey** | **POST** /ssh_keys | Create an SSH key
*SSHKeys* | **deleteSshKey** | **DELETE** /ssh_keys/${idPathParam} | Delete an SSH key
*SSHKeys* | **getSshKey** | **GET** /ssh_keys/${idPathParam} | Get a SSH key
*SSHKeys* | **listSshKeys** | **GET** /ssh_keys | List SSH keys
*SSHKeys* | **updateSshKey** | **PUT** /ssh_keys/${idPathParam} | Update an SSH key
*ServerActions* | **addServerToPlacementGroup** | **POST** /servers/${idPathParam}/actions/add_to_placement_group | Add a Server to a Placement Group
*ServerActions* | **attachServerIso** | **POST** /servers/${idPathParam}/actions/attach_iso | Attach an ISO to a Server
*ServerActions* | **attachServerToNetwork** | **POST** /servers/${idPathParam}/actions/attach_to_network | Attach a Server to a Network
*ServerActions* | **changeServerAliasIps** | **POST** /servers/${idPathParam}/actions/change_alias_ips | Change alias IPs of a Network
*ServerActions* | **changeServerDnsPtr** | **POST** /servers/${idPathParam}/actions/change_dns_ptr | Change reverse DNS entry for this Server
*ServerActions* | **changeServerProtection** | **POST** /servers/${idPathParam}/actions/change_protection | Change Server Protection
*ServerActions* | **changeServerType** | **POST** /servers/${idPathParam}/actions/change_type | Change the Type of a Server
*ServerActions* | **createServerImage** | **POST** /servers/${idPathParam}/actions/create_image | Create Image from a Server
*ServerActions* | **detachServerFromNetwork** | **POST** /servers/${idPathParam}/actions/detach_from_network | Detach a Server from a Network
*ServerActions* | **detachServerIso** | **POST** /servers/${idPathParam}/actions/detach_iso | Detach an ISO from a Server
*ServerActions* | **disableServerBackup** | **POST** /servers/${idPathParam}/actions/disable_backup | Disable Backups for a Server
*ServerActions* | **disableServerRescue** | **POST** /servers/${idPathParam}/actions/disable_rescue | Disable Rescue Mode for a Server
*ServerActions* | **enableServerBackup** | **POST** /servers/${idPathParam}/actions/enable_backup | Enable and Configure Backups for a Server
*ServerActions* | **enableServerRescue** | **POST** /servers/${idPathParam}/actions/enable_rescue | Enable Rescue Mode for a Server
*ServerActions* | **getServerAction** | **GET** /servers/${idPathParam}/actions/${actionIdPathParam} | Get an Action for a Server
*ServerActions* | **getServersAction** | **GET** /servers/actions/${idPathParam} | Get an Action
*ServerActions* | **listServerActions** | **GET** /servers/${idPathParam}/actions | List Actions for a Server
*ServerActions* | **listServersActions** | **GET** /servers/actions | List Actions
*ServerActions* | **poweroffServer** | **POST** /servers/${idPathParam}/actions/poweroff | Power off a Server
*ServerActions* | **poweronServer** | **POST** /servers/${idPathParam}/actions/poweron | Power on a Server
*ServerActions* | **rebootServer** | **POST** /servers/${idPathParam}/actions/reboot | Soft-reboot a Server
*ServerActions* | **rebuildServer** | **POST** /servers/${idPathParam}/actions/rebuild | Rebuild a Server from an Image
*ServerActions* | **removeServerFromPlacementGroup** | **POST** /servers/${idPathParam}/actions/remove_from_placement_group | Remove from Placement Group
*ServerActions* | **requestServerConsole** | **POST** /servers/${idPathParam}/actions/request_console | Request Console for a Server
*ServerActions* | **resetServer** | **POST** /servers/${idPathParam}/actions/reset | Reset a Server
*ServerActions* | **resetServerPassword** | **POST** /servers/${idPathParam}/actions/reset_password | Reset root Password of a Server
*ServerActions* | **shutdownServer** | **POST** /servers/${idPathParam}/actions/shutdown | Shutdown a Server
*ServerTypes* | **getServerType** | **GET** /server_types/${idPathParam} | Get a Server Type
*ServerTypes* | **listServerTypes** | **GET** /server_types | List Server Types
*Servers* | **createServer** | **POST** /servers | Create a Server
*Servers* | **deleteServer** | **DELETE** /servers/${idPathParam} | Delete a Server
*Servers* | **getServer** | **GET** /servers/${idPathParam} | Get a Server
*Servers* | **getServerMetrics** | **GET** /servers/${idPathParam}/metrics | Get Metrics for a Server
*Servers* | **listServers** | **GET** /servers | List Servers
*Servers* | **updateServer** | **PUT** /servers/${idPathParam} | Update a Server
*VolumeActions* | **attachVolume** | **POST** /volumes/${idPathParam}/actions/attach | Attach Volume to a Server
*VolumeActions* | **changeVolumeProtection** | **POST** /volumes/${idPathParam}/actions/change_protection | Change Volume Protection
*VolumeActions* | **detachVolume** | **POST** /volumes/${idPathParam}/actions/detach | Detach Volume
*VolumeActions* | **getVolumeAction** | **GET** /volumes/${idPathParam}/actions/${actionIdPathParam} | Get an Action for a Volume
*VolumeActions* | **getVolumesAction** | **GET** /volumes/actions/${idPathParam} | Get an Action
*VolumeActions* | **listVolumeActions** | **GET** /volumes/${idPathParam}/actions | List Actions for a Volume
*VolumeActions* | **listVolumesActions** | **GET** /volumes/actions | List Actions
*VolumeActions* | **resizeVolume** | **POST** /volumes/${idPathParam}/actions/resize | Resize Volume
*Volumes* | **createVolume** | **POST** /volumes | Create a Volume
*Volumes* | **deleteVolume** | **DELETE** /volumes/${idPathParam} | Delete a Volume
*Volumes* | **getVolume** | **GET** /volumes/${idPathParam} | Get a Volume
*Volumes* | **listVolumes** | **GET** /volumes | List Volumes
*Volumes* | **updateVolume** | **PUT** /volumes/${idPathParam} | Update a Volume
*ZoneActions* | **changeZonePrimaryNameservers** | **POST** /zones/${idOrNamePathParam}/actions/change_primary_nameservers | Change a Zone&#39;s Primary Nameservers
*ZoneActions* | **changeZoneProtection** | **POST** /zones/${idOrNamePathParam}/actions/change_protection | Change a Zone&#39;s Protection
*ZoneActions* | **changeZoneTtl** | **POST** /zones/${idOrNamePathParam}/actions/change_ttl | Change a Zone&#39;s Default TTL
*ZoneActions* | **getZoneAction** | **GET** /zones/${idOrNamePathParam}/actions/${actionIdPathParam} | Get an Action for a Zone
*ZoneActions* | **getZonesAction** | **GET** /zones/actions/${idPathParam} | Get an Action
*ZoneActions* | **importZoneZonefile** | **POST** /zones/${idOrNamePathParam}/actions/import_zonefile | Import a Zone file
*ZoneActions* | **listZoneActions** | **GET** /zones/${idOrNamePathParam}/actions | List Actions for a Zone
*ZoneActions* | **listZonesActions** | **GET** /zones/actions | List Actions
*ZoneRRSetActions* | **addZoneRrsetRecords** | **POST** /zones/${idOrNamePathParam}/rrsets/${rrNamePathParam}/${rrTypePathParam}/actions/add_records | Add Records to an RRSet
*ZoneRRSetActions* | **changeZoneRrsetProtection** | **POST** /zones/${idOrNamePathParam}/rrsets/${rrNamePathParam}/${rrTypePathParam}/actions/change_protection | Change an RRSet&#39;s Protection
*ZoneRRSetActions* | **changeZoneRrsetTtl** | **POST** /zones/${idOrNamePathParam}/rrsets/${rrNamePathParam}/${rrTypePathParam}/actions/change_ttl | Change an RRSet&#39;s TTL
*ZoneRRSetActions* | **removeZoneRrsetRecords** | **POST** /zones/${idOrNamePathParam}/rrsets/${rrNamePathParam}/${rrTypePathParam}/actions/remove_records | Remove Records from an RRSet
*ZoneRRSetActions* | **setZoneRrsetRecords** | **POST** /zones/${idOrNamePathParam}/rrsets/${rrNamePathParam}/${rrTypePathParam}/actions/set_records | Set Records of an RRSet
*ZoneRRSetActions* | **updateZoneRrsetRecords** | **POST** /zones/${idOrNamePathParam}/rrsets/${rrNamePathParam}/${rrTypePathParam}/actions/update_records | Update Records of an RRSet
*ZoneRRSets* | **createZoneRrset** | **POST** /zones/${idOrNamePathParam}/rrsets | Create an RRSet
*ZoneRRSets* | **deleteZoneRrset** | **DELETE** /zones/${idOrNamePathParam}/rrsets/${rrNamePathParam}/${rrTypePathParam} | Delete an RRSet
*ZoneRRSets* | **getZoneRrset** | **GET** /zones/${idOrNamePathParam}/rrsets/${rrNamePathParam}/${rrTypePathParam} | Get an RRSet
*ZoneRRSets* | **listZoneRrsets** | **GET** /zones/${idOrNamePathParam}/rrsets | List RRSets
*ZoneRRSets* | **updateZoneRrset** | **PUT** /zones/${idOrNamePathParam}/rrsets/${rrNamePathParam}/${rrTypePathParam} | Update an RRSet
*Zones* | **createZone** | **POST** /zones | Create a Zone
*Zones* | **deleteZone** | **DELETE** /zones/${idOrNamePathParam} | Delete a Zone
*Zones* | **getZone** | **GET** /zones/${idOrNamePathParam} | Get a Zone
*Zones* | **getZoneZonefile** | **GET** /zones/${idOrNamePathParam}/zonefile | Export a Zone file
*Zones* | **listZones** | **GET** /zones | List Zones
*Zones* | **updateZone** | **PUT** /zones/${idOrNamePathParam} | Update a Zone


## Documentation for Models

 - [Action](Action.md)
 - [ActionError](ActionError.md)
 - [ActionListResponse](ActionListResponse.md)
 - [ActionListResponseWithMeta](ActionListResponseWithMeta.md)
 - [ActionNullable](ActionNullable.md)
 - [ActionResourcesInner](ActionResourcesInner.md)
 - [ActionResponse](ActionResponse.md)
 - [ActionResponse1](ActionResponse1.md)
 - [AddRouteRequest](AddRouteRequest.md)
 - [AddSubnetRequest](AddSubnetRequest.md)
 - [AddToPlacementGroupRequest](AddToPlacementGroupRequest.md)
 - [AddZoneRrsetRecordsRequest](AddZoneRrsetRecordsRequest.md)
 - [ApplyToResourcesRequest](ApplyToResourcesRequest.md)
 - [AttachLoadBalancerToNetworkRequest](AttachLoadBalancerToNetworkRequest.md)
 - [AttachServerIsoRequest](AttachServerIsoRequest.md)
 - [AttachToNetworkRequest](AttachToNetworkRequest.md)
 - [AttachVolumeRequest](AttachVolumeRequest.md)
 - [Certificate](Certificate.md)
 - [CertificateResponse](CertificateResponse.md)
 - [CertificateStatus](CertificateStatus.md)
 - [CertificateStatusError](CertificateStatusError.md)
 - [CertificateUsedByInner](CertificateUsedByInner.md)
 - [CertificatesResponse](CertificatesResponse.md)
 - [ChangeIPRangeRequest](ChangeIPRangeRequest.md)
 - [ChangeImageProtectionRequest](ChangeImageProtectionRequest.md)
 - [ChangeLoadBalancerAlgorithmRequest](ChangeLoadBalancerAlgorithmRequest.md)
 - [ChangeLoadBalancerProtectionRequest](ChangeLoadBalancerProtectionRequest.md)
 - [ChangeLoadbalancerDnsPtrRequest](ChangeLoadbalancerDnsPtrRequest.md)
 - [ChangeProtectionRequest](ChangeProtectionRequest.md)
 - [ChangeServerAliasIpsRequest](ChangeServerAliasIpsRequest.md)
 - [ChangeServerDnsPtrRequest](ChangeServerDnsPtrRequest.md)
 - [ChangeServerProtectionRequest](ChangeServerProtectionRequest.md)
 - [ChangeServerTypeRequest](ChangeServerTypeRequest.md)
 - [ChangeTypeRequest](ChangeTypeRequest.md)
 - [ChangeVolumeProtectionRequest](ChangeVolumeProtectionRequest.md)
 - [ChangeZonePrimaryNameserversRequest](ChangeZonePrimaryNameserversRequest.md)
 - [ChangeZoneProtectionRequest](ChangeZoneProtectionRequest.md)
 - [ChangeZoneRrsetTtlRequest](ChangeZoneRrsetTtlRequest.md)
 - [ChangeZoneTtlRequest](ChangeZoneTtlRequest.md)
 - [CreateCertificateRequest](CreateCertificateRequest.md)
 - [CreateCertificateResponse](CreateCertificateResponse.md)
 - [CreateFirewallRequest](CreateFirewallRequest.md)
 - [CreateFirewallResponse](CreateFirewallResponse.md)
 - [CreateFloatingIp201Response](CreateFloatingIp201Response.md)
 - [CreateImageRequest](CreateImageRequest.md)
 - [CreateLoadBalancer201Response](CreateLoadBalancer201Response.md)
 - [CreateLoadBalancerRequest](CreateLoadBalancerRequest.md)
 - [CreateNetwork201Response](CreateNetwork201Response.md)
 - [CreatePlacementGroupRequest](CreatePlacementGroupRequest.md)
 - [CreatePlacementGroupResponse](CreatePlacementGroupResponse.md)
 - [CreatePrimaryIPResponse](CreatePrimaryIPResponse.md)
 - [CreateServerImage201Response](CreateServerImage201Response.md)
 - [CreateServerRequest](CreateServerRequest.md)
 - [CreateServerRequestFirewallsInner](CreateServerRequestFirewallsInner.md)
 - [CreateServerRequestPublicNet](CreateServerRequestPublicNet.md)
 - [CreateServerResponse](CreateServerResponse.md)
 - [CreateSshKey201Response](CreateSshKey201Response.md)
 - [CreateSshKeyRequest](CreateSshKeyRequest.md)
 - [CreateVolume201Response](CreateVolume201Response.md)
 - [CreateVolumeRequest](CreateVolumeRequest.md)
 - [CreateZone201Response](CreateZone201Response.md)
 - [CreateZoneRequest](CreateZoneRequest.md)
 - [CreateZoneRequestRrsetsInner](CreateZoneRequestRrsetsInner.md)
 - [CreateZoneRrset201Response](CreateZoneRrset201Response.md)
 - [DeleteLoadBalancerServiceRequest](DeleteLoadBalancerServiceRequest.md)
 - [DeleteRouteRequest](DeleteRouteRequest.md)
 - [DeleteServer200Response](DeleteServer200Response.md)
 - [DeleteSubnetRequest](DeleteSubnetRequest.md)
 - [DeprecationInfo](DeprecationInfo.md)
 - [DeprecationInfo1](DeprecationInfo1.md)
 - [DetachFromNetworkRequest](DetachFromNetworkRequest.md)
 - [DetachLoadBalancerFromNetworkRequest](DetachLoadBalancerFromNetworkRequest.md)
 - [EnableServerRescue201Response](EnableServerRescue201Response.md)
 - [EnableServerRescueRequest](EnableServerRescueRequest.md)
 - [FirewallResource](FirewallResource.md)
 - [FirewallResourceServer](FirewallResourceServer.md)
 - [FirewallResponse](FirewallResponse.md)
 - [FirewallResponse1](FirewallResponse1.md)
 - [FirewallResponseAppliedToInner](FirewallResponseAppliedToInner.md)
 - [FirewallResponseAppliedToInnerAppliedToResourcesInner](FirewallResponseAppliedToInnerAppliedToResourcesInner.md)
 - [FirewallResponseAppliedToInnerLabelSelector](FirewallResponseAppliedToInnerLabelSelector.md)
 - [FirewallResponseAppliedToInnerServer](FirewallResponseAppliedToInnerServer.md)
 - [FirewallsResponse](FirewallsResponse.md)
 - [FloatingIPActionsAssignRequest](FloatingIPActionsAssignRequest.md)
 - [FloatingIPCreateRequest](FloatingIPCreateRequest.md)
 - [FloatingIPCreateRequestHomeLocation](FloatingIPCreateRequestHomeLocation.md)
 - [FloatingIPUpdateRequest](FloatingIPUpdateRequest.md)
 - [GetActions4xxResponse](GetActions4xxResponse.md)
 - [GetActions4xxResponseError](GetActions4xxResponseError.md)
 - [GetActions5xxResponse](GetActions5xxResponse.md)
 - [GetDatacenter200Response](GetDatacenter200Response.md)
 - [GetFloatingIp200Response](GetFloatingIp200Response.md)
 - [GetImage200Response](GetImage200Response.md)
 - [GetIso200Response](GetIso200Response.md)
 - [GetLoadBalancer200Response](GetLoadBalancer200Response.md)
 - [GetLoadBalancerMetrics200Response](GetLoadBalancerMetrics200Response.md)
 - [GetLoadBalancerMetrics200ResponseMetrics](GetLoadBalancerMetrics200ResponseMetrics.md)
 - [GetLoadBalancerMetrics200ResponseMetricsTimeSeriesValue](GetLoadBalancerMetrics200ResponseMetricsTimeSeriesValue.md)
 - [GetLoadBalancerMetrics200ResponseMetricsTimeSeriesValueValuesInnerInner](GetLoadBalancerMetrics200ResponseMetricsTimeSeriesValueValuesInnerInner.md)
 - [GetLoadBalancerType200Response](GetLoadBalancerType200Response.md)
 - [GetLocation200Response](GetLocation200Response.md)
 - [GetPricing200Response](GetPricing200Response.md)
 - [GetPricing200ResponsePricing](GetPricing200ResponsePricing.md)
 - [GetPricing200ResponsePricingFloatingIp](GetPricing200ResponsePricingFloatingIp.md)
 - [GetPricing200ResponsePricingFloatingIpPriceMonthly](GetPricing200ResponsePricingFloatingIpPriceMonthly.md)
 - [GetPricing200ResponsePricingFloatingIpsInner](GetPricing200ResponsePricingFloatingIpsInner.md)
 - [GetPricing200ResponsePricingFloatingIpsInnerPricesInner](GetPricing200ResponsePricingFloatingIpsInnerPricesInner.md)
 - [GetPricing200ResponsePricingImage](GetPricing200ResponsePricingImage.md)
 - [GetPricing200ResponsePricingImagePricePerGbMonth](GetPricing200ResponsePricingImagePricePerGbMonth.md)
 - [GetPricing200ResponsePricingLoadBalancerTypesInner](GetPricing200ResponsePricingLoadBalancerTypesInner.md)
 - [GetPricing200ResponsePricingPrimaryIpsInner](GetPricing200ResponsePricingPrimaryIpsInner.md)
 - [GetPricing200ResponsePricingPrimaryIpsInnerPricesInner](GetPricing200ResponsePricingPrimaryIpsInnerPricesInner.md)
 - [GetPricing200ResponsePricingServerBackup](GetPricing200ResponsePricingServerBackup.md)
 - [GetPricing200ResponsePricingServerTypesInner](GetPricing200ResponsePricingServerTypesInner.md)
 - [GetPricing200ResponsePricingVolume](GetPricing200ResponsePricingVolume.md)
 - [GetPricing200ResponsePricingVolumePricePerGbMonth](GetPricing200ResponsePricingVolumePricePerGbMonth.md)
 - [GetServer200Response](GetServer200Response.md)
 - [GetServerType200Response](GetServerType200Response.md)
 - [GetVolume200Response](GetVolume200Response.md)
 - [GetZone200Response](GetZone200Response.md)
 - [GetZoneRrset200Response](GetZoneRrset200Response.md)
 - [GetZoneZonefile200Response](GetZoneZonefile200Response.md)
 - [ImportZoneZonefileRequest](ImportZoneZonefileRequest.md)
 - [ListDatacenters200Response](ListDatacenters200Response.md)
 - [ListDatacenters200ResponseDatacentersInner](ListDatacenters200ResponseDatacentersInner.md)
 - [ListDatacenters200ResponseDatacentersInnerLocation](ListDatacenters200ResponseDatacentersInnerLocation.md)
 - [ListDatacenters200ResponseDatacentersInnerServerTypes](ListDatacenters200ResponseDatacentersInnerServerTypes.md)
 - [ListFloatingIpActions200Response](ListFloatingIpActions200Response.md)
 - [ListFloatingIps200Response](ListFloatingIps200Response.md)
 - [ListFloatingIps200ResponseFloatingIpsInner](ListFloatingIps200ResponseFloatingIpsInner.md)
 - [ListFloatingIps200ResponseFloatingIpsInnerDnsPtrInner](ListFloatingIps200ResponseFloatingIpsInnerDnsPtrInner.md)
 - [ListFloatingIps200ResponseFloatingIpsInnerHomeLocation](ListFloatingIps200ResponseFloatingIpsInnerHomeLocation.md)
 - [ListFloatingIps200ResponseFloatingIpsInnerProtection](ListFloatingIps200ResponseFloatingIpsInnerProtection.md)
 - [ListImages200Response](ListImages200Response.md)
 - [ListImages200ResponseImagesInner](ListImages200ResponseImagesInner.md)
 - [ListImages200ResponseImagesInnerCreatedFrom](ListImages200ResponseImagesInnerCreatedFrom.md)
 - [ListIsos200Response](ListIsos200Response.md)
 - [ListIsos200ResponseIsosInner](ListIsos200ResponseIsosInner.md)
 - [ListLoadBalancerTypes200Response](ListLoadBalancerTypes200Response.md)
 - [ListLoadBalancerTypes200ResponseLoadBalancerTypesInner](ListLoadBalancerTypes200ResponseLoadBalancerTypesInner.md)
 - [ListLoadBalancerTypes200ResponseLoadBalancerTypesInnerPricesInner](ListLoadBalancerTypes200ResponseLoadBalancerTypesInnerPricesInner.md)
 - [ListLoadBalancerTypes200ResponseLoadBalancerTypesInnerPricesInnerPriceHourly](ListLoadBalancerTypes200ResponseLoadBalancerTypesInnerPricesInnerPriceHourly.md)
 - [ListLoadBalancerTypes200ResponseLoadBalancerTypesInnerPricesInnerPriceMonthly](ListLoadBalancerTypes200ResponseLoadBalancerTypesInnerPricesInnerPriceMonthly.md)
 - [ListLoadBalancerTypes200ResponseLoadBalancerTypesInnerPricesInnerPricePerTbTraffic](ListLoadBalancerTypes200ResponseLoadBalancerTypesInnerPricesInnerPricePerTbTraffic.md)
 - [ListLoadBalancers200Response](ListLoadBalancers200Response.md)
 - [ListLoadBalancers200ResponseLoadBalancersInner](ListLoadBalancers200ResponseLoadBalancersInner.md)
 - [ListLoadBalancers200ResponseLoadBalancersInnerAlgorithm](ListLoadBalancers200ResponseLoadBalancersInnerAlgorithm.md)
 - [ListLoadBalancers200ResponseLoadBalancersInnerPrivateNetInner](ListLoadBalancers200ResponseLoadBalancersInnerPrivateNetInner.md)
 - [ListLoadBalancers200ResponseLoadBalancersInnerPublicNet](ListLoadBalancers200ResponseLoadBalancersInnerPublicNet.md)
 - [ListLoadBalancers200ResponseLoadBalancersInnerPublicNetIpv4](ListLoadBalancers200ResponseLoadBalancersInnerPublicNetIpv4.md)
 - [ListLoadBalancers200ResponseLoadBalancersInnerPublicNetIpv6](ListLoadBalancers200ResponseLoadBalancersInnerPublicNetIpv6.md)
 - [ListLocations200Response](ListLocations200Response.md)
 - [ListMeta](ListMeta.md)
 - [ListMetaPagination](ListMetaPagination.md)
 - [ListNetworks200Response](ListNetworks200Response.md)
 - [ListNetworks200ResponseNetworksInner](ListNetworks200ResponseNetworksInner.md)
 - [ListNetworks200ResponseNetworksInnerRoutesInner](ListNetworks200ResponseNetworksInnerRoutesInner.md)
 - [ListNetworks200ResponseNetworksInnerSubnetsInner](ListNetworks200ResponseNetworksInnerSubnetsInner.md)
 - [ListServerTypes200Response](ListServerTypes200Response.md)
 - [ListServerTypes200ResponseServerTypesInner](ListServerTypes200ResponseServerTypesInner.md)
 - [ListServerTypes200ResponseServerTypesInnerLocationsInner](ListServerTypes200ResponseServerTypesInnerLocationsInner.md)
 - [ListServers200Response](ListServers200Response.md)
 - [ListServers200ResponseServersInner](ListServers200ResponseServersInner.md)
 - [ListServers200ResponseServersInnerDatacenter](ListServers200ResponseServersInnerDatacenter.md)
 - [ListServers200ResponseServersInnerImage](ListServers200ResponseServersInnerImage.md)
 - [ListServers200ResponseServersInnerIso](ListServers200ResponseServersInnerIso.md)
 - [ListServers200ResponseServersInnerLocation](ListServers200ResponseServersInnerLocation.md)
 - [ListServers200ResponseServersInnerPrivateNetInner](ListServers200ResponseServersInnerPrivateNetInner.md)
 - [ListServers200ResponseServersInnerProtection](ListServers200ResponseServersInnerProtection.md)
 - [ListServers200ResponseServersInnerPublicNet](ListServers200ResponseServersInnerPublicNet.md)
 - [ListServers200ResponseServersInnerPublicNetIpv4](ListServers200ResponseServersInnerPublicNetIpv4.md)
 - [ListServers200ResponseServersInnerPublicNetIpv6](ListServers200ResponseServersInnerPublicNetIpv6.md)
 - [ListServers200ResponseServersInnerPublicNetIpv6DnsPtrInner](ListServers200ResponseServersInnerPublicNetIpv6DnsPtrInner.md)
 - [ListSshKeys200Response](ListSshKeys200Response.md)
 - [ListSshKeys200ResponseSshKeysInner](ListSshKeys200ResponseSshKeysInner.md)
 - [ListVolumes200Response](ListVolumes200Response.md)
 - [ListVolumes200ResponseVolumesInner](ListVolumes200ResponseVolumesInner.md)
 - [ListVolumes200ResponseVolumesInnerLocation](ListVolumes200ResponseVolumesInnerLocation.md)
 - [ListZoneRrsets200Response](ListZoneRrsets200Response.md)
 - [ListZones200Response](ListZones200Response.md)
 - [LoadBalancerAlgorithm](LoadBalancerAlgorithm.md)
 - [LoadBalancerService](LoadBalancerService.md)
 - [LoadBalancerServiceHTTP](LoadBalancerServiceHTTP.md)
 - [LoadBalancerServiceHTTP1](LoadBalancerServiceHTTP1.md)
 - [LoadBalancerServiceHealthCheck](LoadBalancerServiceHealthCheck.md)
 - [LoadBalancerServiceHealthCheckHttp](LoadBalancerServiceHealthCheckHttp.md)
 - [LoadBalancerTarget](LoadBalancerTarget.md)
 - [LoadBalancerTarget1](LoadBalancerTarget1.md)
 - [LoadBalancerTargetHealthStatusInner](LoadBalancerTargetHealthStatusInner.md)
 - [LoadBalancerTargetIP](LoadBalancerTargetIP.md)
 - [LoadBalancerTargetIP1](LoadBalancerTargetIP1.md)
 - [LoadBalancerTargetLabelSelector](LoadBalancerTargetLabelSelector.md)
 - [LoadBalancerTargetLabelSelector1](LoadBalancerTargetLabelSelector1.md)
 - [LoadBalancerTargetServer](LoadBalancerTargetServer.md)
 - [LoadBalancerTargetServer1](LoadBalancerTargetServer1.md)
 - [LoadBalancerTargetTarget](LoadBalancerTargetTarget.md)
 - [NetworkCreateRequest](NetworkCreateRequest.md)
 - [NetworkCreateRequestSubnetsInner](NetworkCreateRequestSubnetsInner.md)
 - [NetworkUpdateRequest](NetworkUpdateRequest.md)
 - [PlacementGroup](PlacementGroup.md)
 - [PlacementGroupNullable](PlacementGroupNullable.md)
 - [PlacementGroupResponse](PlacementGroupResponse.md)
 - [PlacementGroupsResponse](PlacementGroupsResponse.md)
 - [PrimaryIP](PrimaryIP.md)
 - [PrimaryIPActionsAssignRequest](PrimaryIPActionsAssignRequest.md)
 - [PrimaryIPCreateRequest](PrimaryIPCreateRequest.md)
 - [PrimaryIPCreateRequestDatacenter](PrimaryIPCreateRequestDatacenter.md)
 - [PrimaryIPCreateRequestLocation](PrimaryIPCreateRequestLocation.md)
 - [PrimaryIPDatacenter](PrimaryIPDatacenter.md)
 - [PrimaryIPLocation](PrimaryIPLocation.md)
 - [PrimaryIPResponse](PrimaryIPResponse.md)
 - [PrimaryIPUpdateRequest](PrimaryIPUpdateRequest.md)
 - [PrimaryIPsResponse](PrimaryIPsResponse.md)
 - [PrimaryZone](PrimaryZone.md)
 - [PrimaryZoneAllOfAuthoritativeNameservers](PrimaryZoneAllOfAuthoritativeNameservers.md)
 - [PrimaryZoneAllOfPrimaryNameserversInner](PrimaryZoneAllOfPrimaryNameserversInner.md)
 - [RRSet](RRSet.md)
 - [RRSetProtection](RRSetProtection.md)
 - [RebuildServer201Response](RebuildServer201Response.md)
 - [RebuildServerRequest](RebuildServerRequest.md)
 - [Record](Record.md)
 - [Record1](Record1.md)
 - [RemoveFromResourcesRequest](RemoveFromResourcesRequest.md)
 - [RemoveTargetRequest](RemoveTargetRequest.md)
 - [RemoveTargetRequestLabelSelector](RemoveTargetRequestLabelSelector.md)
 - [RemoveTargetRequestServer](RemoveTargetRequestServer.md)
 - [RemoveZoneRrsetRecordsRequest](RemoveZoneRrsetRecordsRequest.md)
 - [RequestServerConsole201Response](RequestServerConsole201Response.md)
 - [ResetServerPassword201Response](ResetServerPassword201Response.md)
 - [ResizeVolumeRequest](ResizeVolumeRequest.md)
 - [Rule](Rule.md)
 - [RuleResponse](RuleResponse.md)
 - [SecondaryZone](SecondaryZone.md)
 - [ServerPublicNetFirewall](ServerPublicNetFirewall.md)
 - [SetRulesRequest](SetRulesRequest.md)
 - [SetZoneRrsetRecordsRequest](SetZoneRrsetRecordsRequest.md)
 - [UpdateCertificateRequest](UpdateCertificateRequest.md)
 - [UpdateFirewallRequest](UpdateFirewallRequest.md)
 - [UpdateImageRequest](UpdateImageRequest.md)
 - [UpdateLoadBalancerRequest](UpdateLoadBalancerRequest.md)
 - [UpdateLoadBalancerService](UpdateLoadBalancerService.md)
 - [UpdateLoadBalancerServiceHealthCheck](UpdateLoadBalancerServiceHealthCheck.md)
 - [UpdateLoadBalancerServiceHealthCheckHttp](UpdateLoadBalancerServiceHealthCheckHttp.md)
 - [UpdatePlacementGroupRequest](UpdatePlacementGroupRequest.md)
 - [UpdateServerRequest](UpdateServerRequest.md)
 - [UpdateSshKeyRequest](UpdateSshKeyRequest.md)
 - [UpdateVolumeRequest](UpdateVolumeRequest.md)
 - [UpdateZoneRrsetRecordsRequest](UpdateZoneRrsetRecordsRequest.md)
 - [UpdateZoneRrsetRequest](UpdateZoneRrsetRequest.md)
 - [Zone](Zone.md)
 - [ZonePrimary](ZonePrimary.md)
 - [ZoneSecondary](ZoneSecondary.md)
 - [ZoneUpdateRequest](ZoneUpdateRequest.md)


<a id="documentation-for-authorization"></a>
## Documentation for Authorization


Authentication schemes defined for the API:
    <a id="APIToken"></a>
    ### APIToken

            - **Type**: HTTP Bearer Token authentication
        

## Author



