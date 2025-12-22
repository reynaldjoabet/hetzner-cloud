import hcloud.api.*
import hcloud.models.*
import hcloud.models.ZoneEnums.Mode
//import sttp.client4.logging.slf4j.Slf4jLoggingBackend
//import sttp.client4.prometheus.*
//import sttp.client4.scribe.ScribeLoggingBackend
import java.util.UUID
import hcloud.models.*
import clients.*
object HetznerCloud extends App {

  println("Hello, Hetzner Cloud!")

  val webserverrules = List(
    Rule(
      direction = RuleEnums.Direction.in,
      protocol = RuleEnums.Protocol.tcp,
      description = Some("Allow http traffic"),
      destinationIps = None,
      port = Some("80"),
      sourceIps = None
    ),
    Rule(
      direction = RuleEnums.Direction.out,
      protocol = RuleEnums.Protocol.tcp,
      description = Some("Allow https traffic"),
      destinationIps = None,
      port = Some("443"),
      sourceIps = Some(List("0.0.0.0/0"))
    )
  )

  val createFirewallRequest =
    CreateFirewallRequest(name = "web server", None, None, Some(webserverrules))

  val sshrules = Rule(
    direction = RuleEnums.Direction.in,
    protocol = RuleEnums.Protocol.tcp,
    description = Some("Allow ssh traffic"),
    destinationIps = None,
    port = Some("22"),
    sourceIps = Some(List("0.0.0.0/0"))
  )

  val ssh =
    CreateFirewallRequest(name = "ssh", None, None, Some(List(sshrules)))
  val postgresrules = List(
    Rule(
      direction = RuleEnums.Direction.in,
      protocol = RuleEnums.Protocol.tcp,
      description = Some("Allow postgresql traffic"),
      destinationIps = None,
      port = Some("5432"),
      sourceIps = Some(List("0.0.0.0/0"))
    )
  )

  val postgres =
    CreateFirewallRequest(name = "postgres", None, None, Some((postgresrules)))

  val redisrules = List(
    Rule(
      direction = RuleEnums.Direction.in,
      protocol = RuleEnums.Protocol.tcp,
      description = Some("Allow redis traffic"),
      destinationIps = None,
      port = Some("6379"),
      sourceIps = Some(List("0.0.0.0/0"))
    )
  )
  val redis =
    CreateFirewallRequest(name = "redis", None, None, Some((redisrules)))

  val privateNetwork = CreateNetworkRequest(
    ipRange = "10.0.0.0/16",
    name = "Airport Gap Private Network",
    exposeRoutesToVswitch = None,
    labels = None,
    routes = None,
    subnets = None
  )

  val privateNetworkSubnet = Subnet(
    networkZone = "us-west",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("10.0.1.0/24"),
    vswitchId = None
  )

  val myfirewall = CreateFirewallRequest(
    "my-firewall",
    None,
    None,
    Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.icmp,
          Some("Allow icmp traffic"),
          None,
          None,
          Some(List("0.0.0.0/0", "::/0"))
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          Some("Allow http traffic"),
          None,
          Some("80-85"),
          Some(List("0.0.0.0/0", "::/0"))
        )
      )
    )
  )

  val server = CreateServerRequest(
    image = "debian-12",
    name = "node1",
    serverType = "cx23",
    automount = None,
    datacenter = None,
    firewalls = Some(List(CreateServerRequestFirewalls(12))),
    labels = None,
    location = None,
    networks = None,
    placementGroup = None,
    publicNet = None,
    sshKeys = None,
    startAfterCreate = None,
    userData = None,
    volumes = Some(List(1234))
  )

  val attachLoadBalancerToNetworkRequest = AttachLoadBalancerToNetworkRequest(
    network = 1234,
    ip = Some("192.168.8.90"),
    ipRange = Some("192.168.8.0/24")
  )

  val attachServerToNetworkRequest = AttachServerToNetworkRequest(
    network = 1234,
    aliasIps = Some(Seq("10.8.6.7/24")),
    ip = Some("192.168.4.7"),
    ipRange = Some("192.168.4.0/24")
  )

  val certificate = Certificate(
    certificate =
      "-----BEGIN CERTIFICATE-----\nMIIDdzCCAl+gAwIBAgIEUz5b6DANBgkqhkiG9w0BAQsFADBoMQswCQYDVQQGEwJV\nUzELMAkGA1UECAwCTkMxEDAOBgNVBAcMB0R1cmhhbTEMMAoGA1UECgwDSGV0ejEM\nMAoGA1UECwwDSGV0ejEUMBIGA1UEAwwLKi5oZXR6bmVyLmNvbTAeFw0yMTA1MTkx\nNzI0MDBaFw0yMjA1MTkxNzI0MDBaMGgxCzAJBgNVBAYTAlVTMQswCQYDVQQIDAJO\nQzEQMA4GA1UEBwwHRHVyaGFtMQwwCgYDVQQKDANIZXR6MQwwCgYDVQQLDANIZXR6\nMRQwEgYDVQQDDAsqLmhldHpuZXIuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A\nMIIBCgKCAQEAzV6j6u8G7b3p6k9i2n5k3Z2K1rOqvXh5j3Z2K1rOqvXh5j3Z2K1rO\n",
    created = "2021-05-19T17:24:00+00:00",
    domainNames = Seq("example.com", "www.example.com"),
    fingerprint =
      "AB:CD:EF:12:34:56:78:90:AB:CD:EF:12:34:56:78:90:AB:CD:EF:12:34:56:78:90",
    id = 123456,
    labels = Map("env" -> "production", "team" -> "devops"),
    name = "example-certificate",
    notValidAfter = "2023-05-19T17:24:00+00:00",
    notValidBefore = "2021-05-19T17:24:00+00:00",
    usedBy = Seq(),
    status = None,
    `type` = Some(CertificateEnums.Type.`uploaded`)
  )

  val createCertificateRequest = CreateCertificateRequest(
    name = "example-certificate",
    certificate = Some(
      "-----BEGIN CERTIFICATE-----\nMIIDdzCCAl+gAwIBAgIEUz5b6DANBgkqhkiG9w0BAQsFADBoMQswCQYDVQQGEwJV\nUzELMAkGA1UECAwCTkMxEDAOBgNVBAcMB0R1cmhhbTEMMAoGA1UECgwDSGV0ejEM\nMAoGA1UECwwDSGV0ejEUMBIGA1UEAwwLKi5oZXR6bmVyLmNvbTAeFw0yMTA1MTkx\nNzI0MDBaFw0yMjA1MTkxNzI0MDBaMGgxCzAJBgNVBAYTAlVTMQswCQYDVQQIDAJO\nQzEQMA4GA1UEBwwHRHVyaGFtMQwwCgYDVQQKDANIZXR6MQwwCgYDVQQLDANIZXR6\nMRQwEgYDVQQDDAsqLmhldHpuZXIuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8A\nMIIBCgKCAQEAzV6j6u8G7b3p6k9i2n5k3Z2K1rOqvXh5j3Z2K1rOqvXh5j3Z2K1rO\n"
    ),
    domainNames = Some(Seq("example.com", "www.example.com")),
    labels = Some(Map("env" -> "production", "team" -> "devops")),
    privateKey = Some(
      "-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDNXqPq7wbtvenq\nT2Lafxk3Z2K1rOqvXh5j3Z2K1rOqvXh5j3Z2K1rOqvXh5j3"
    ),
    `type` = Some(CreateCertificateRequestEnums.Type.`uploaded`)
  )

  val createFirewall = CreateFirewallRequest(
    name = "my-firewall",
    applyTo = None,
    labels = None,
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.icmp,
          Some("Allow icmp traffic"),
          None,
          None,
          Some(List("0.0.0.0/0", "::/0"))
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          Some("Allow http traffic"),
          None,
          Some("80-85"),
          Some(List("0.0.0.0/0", "::/0"))
        )
      )
    )
  )

  val createFloatingIpRequest = CreateFloatingIpRequest(
    `type` = IpType.ipv4,
    description = Some("My Floating IP"),
    homeLocation = Some("fsn1"),
    labels = Some(Map("env" -> "production")),
    name = Some("my-floating-ip"),
    server = Some(123456)
  )

  val createImageFromServerRequest = CreateImageFromServerRequest(
    description = Some("My server image"),
    labels = Some(Map("env" -> "production", "team" -> "devops")),
    `type` = Some(CreateImageFromServerRequestEnums.Type.`snapshot`)
  )

  val loadBalancerAlgorithm = LoadBalancerAlgorithm(
    `type` = LoadBalancerAlgorithmEnums.Type.`round_robin`
  )

  val loadBalancerAlgorithm2 = LoadBalancerAlgorithm(
    `type` = LoadBalancerAlgorithmEnums.Type.`least_connections`
  )

  val http = Http(
    certificates = Some(Seq(123456, 789012)),
    cookieLifetime = Some(300),
    cookieName = Some("MYSESSIONID"),
    redirectHttp = Some(true),
    stickySessions = Some(true)
  )

  val loadBalancerServiceHealthCheckHttp = LoadBalancerServiceHealthCheckHttp(
    domain = "example.com",
    path = "/healthz",
    response = Some("OK"),
    statusCodes = Some(Seq("200", "201", "202")),
    tls = Some(false)
  )

  val loadBalancerHealthCheck = LoadBalancerServiceHealthCheck(
    interval = 10,
    port = 80,
    protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
    retries = 3,
    timeout = 5,
    http = Some(loadBalancerServiceHealthCheckHttp)
  )
  val loadBalancerService = LoadBalancerService(
    destinationPort = 80,
    healthCheck = loadBalancerHealthCheck,
    listenPort = 80,
    protocol = LoadBalancerServiceEnums.Protocol.http,
    proxyprotocol = false,
    http = Some(http)
  )

  val loadBalancerTargetIp = LoadBalancerTargetIp(
    ip = "192.168.0.1"
  )
  val resourceId = ResourceId(id = 123456)

  val selector = LabelSelector(
    selector = "env=production"
  )

  val loadBalancerAddTarget = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.`ip`,
    ip = Some(loadBalancerTargetIp),
    labelSelector = None,
    server = Some(resourceId),
    usePrivateIp = Some(false)
  )

  val createLoadBalancerRequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "my-load-balancer",
    algorithm = Some(loadBalancerAlgorithm),
    labels = Some(Map("env" -> "production")),
    location = Some("fsn1"),
    network = None,
    networkZone = None,
    publicInterface = Some(true),
    services = Some(Seq(loadBalancerService)),
    targets = Some(
      Seq(
        loadBalancerAddTarget
      )
    )
  )

  val createLoadBalancerRequest2 = CreateLoadBalancerRequest(
    loadBalancerType = "lb21",
    name = "my-load-balancer",
    algorithm = Some(loadBalancerAlgorithm),
    labels = Some(Map("env" -> "production")),
    location = Some("hel1"),
    network = None,
    networkZone = None,
    publicInterface = Some(true),
    services = Some(Seq(loadBalancerService)),
    targets = Some(
      Seq(
        loadBalancerAddTarget
      )
    )
  )

  val createLoadBalancerRequest3 = CreateLoadBalancerRequest(
    loadBalancerType = "lb31",
    name = "my-load-balancer",
    algorithm = Some(loadBalancerAlgorithm),
    labels = Some(Map("env" -> "production")),
    location = Some("nbg1"),
    network = None,
    networkZone = None,
    publicInterface = Some(true),
    services = Some(Seq(loadBalancerService)),
    targets = Some(
      Seq(
        loadBalancerAddTarget
      )
    )
  )
  val loadBalancerPrivateNet = LoadBalancerPrivateNet(
    ip = Some("192.168.0.1"),
    network = Some(123456)
  )

  val loadBalancerPublicNet = LoadBalancerPublicNet(
    enabled = true,
    ipv4 = LoadBalancerPublicNetIpv4(
      dnsPtr = None,
      ip = Some("192.168.2.8")
    ),
    ipv6 = LoadBalancerPublicNetIpv6(
      dnsPtr = None,
      ip = Some("2001:db8::1")
    )
  )

  val pricePerTime = PricePerTime(
    includedTraffic = 107374182,
    location = "fsn1",
    priceHourly = Price(
      net = 0.012,
      gross = 0.01428
    ),
    priceMonthly = Price(
      net = 8.0,
      gross = 9.52
    ),
    pricePerTbTraffic = Price(
      net = 1.0,
      gross = 1.19
    )
  )

  val loadBalancerType = LoadBalancerType(
    deprecated = "2023-12-31T23:59:59+00:00",
    description = "Basic Load Balancer",
    id = 1,
    maxAssignedCertificates = 5,
    maxConnections = 100000,
    maxServices = 10,
    maxTargets = 50,
    name = "lb11",
    prices = Seq(
      pricePerTime
    )
  )

  val sshKey = SshKey(
    created = "2021-05-19T17:24:00+00:00",
    fingerprint =
      "AB:CD:EF:12:34:56:78:90:AB:CD:EF:12:34:56:78:90:AB:CD:EF:12:34:56:78:90",
    id = 123456,
    labels = Map("env" -> "production", "team" -> "devops"),
    name = "example-ssh-key",
    publicKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCz..."
  )
  val authoritativeNameservers = ZoneAuthoritativeNameservers(
    assigned = Seq("ns1.hetzner.com", "ns2.hetzner.com", "ns3.hetzner.com"),
    delegated = Seq("ns1.hetzner.com", "ns2.hetzner.com", "ns3.hetzner.com"),
    delegationLastCheck = "2024-06-01T12:00:00+00:00",
    delegationStatus =
      Some(ZoneAuthoritativeNameserversEnums.DelegationStatus.`valid`)
  )

  val zone = Zone(
    authoritativeNameservers = authoritativeNameservers,
    created = "2024-06-01T12:00:00+00:00",
    id = 123456,
    labels = Map("env" -> "production", "team" -> "devops"),
    mode = Mode.primary,
    name = "example.com",
    protection = Protection(delete = false),
    recordCount = 42,
    registrar = ZoneEnums.Registrar.`hetzner`,
    status = ZoneEnums.Status.`ok`,
    ttl = 3600,
    primaryNameservers = None
  )

  val createCertificateRequest2 = CreateServerRequest(
    image = "debian-12",
    name = "node1",
    serverType = "cx23",
    automount = None,
    datacenter = None,
    firewalls = Some(List(CreateServerRequestFirewalls(12))),
    labels = None,
    location = None,
    networks = None,
    placementGroup = None,
    publicNet = None,
    sshKeys = None,
    startAfterCreate = None,
    userData = None,
    volumes = Some(List(1234))
  )

  val createServerRequest3 = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "my-server",
    serverType = "cpx22",
    automount = Some(false),
    datacenter = Some("nbg1-dc3"),
    firewalls = Some(List(CreateServerRequestFirewalls(38))),
    labels = Some(
      Map(
        "environment" -> "prod",
        "example.com/my" -> "label",
        "just-a-key" -> ""
      )
    ),
    location = Some("nbg1"),
    networks = Some(List(456)),
    placementGroup = Some(1),
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(false),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData =
      Some("#cloud-config\nruncmd:\n- [touch, /root/cloud-init-worked]\n"),
    volumes = Some(List(123))
  )

  val loadBalancerTarget = LoadBalancerTarget(
    `type` = LoadBalancerTargetEnums.Type.`server`,
    healthStatus = Some(
      Seq(
        LoadBalancerTargetHealthStatus(
          listenPort = Some(80),
          status = Some(LoadBalancerTargetHealthStatusEnums.Status.`healthy`)
        )
      )
    ),
    ip = None,
    labelSelector = None,
    server = Some(ResourceId(123456)),
    targets = None,
    usePrivateIp = Some(true)
  )

  val loadBalancerTarget2 = LoadBalancerTarget(
    `type` = LoadBalancerTargetEnums.Type.`label_selector`,
    healthStatus = None,
    ip = None,
    labelSelector = Some(LabelSelector("env=production")),
    server = None,
    targets = Some(
      Seq(
        LoadBalancerSelectedTarget(
          healthStatus = Some(
            Seq(
              LoadBalancerTargetHealthStatus(
                listenPort = Some(80),
                status =
                  Some(LoadBalancerTargetHealthStatusEnums.Status.`healthy`)
              )
            )
          ),
          server = Some(ResourceId(234567)),
          `type` = Some("server"),
          usePrivateIp = Some(false)
        ),
        LoadBalancerSelectedTarget(
          healthStatus = Some(
            Seq(
              LoadBalancerTargetHealthStatus(
                listenPort = Some(80),
                status =
                  Some(LoadBalancerTargetHealthStatusEnums.Status.`unhealthy`)
              )
            )
          ),
          server = Some(ResourceId(345678)),
          `type` = Some("server"),
          usePrivateIp = Some(true)
        )
      )
    ),
    usePrivateIp = Some(false)
  )

  val loadBalancerTarget3 = LoadBalancerTarget(
    `type` = LoadBalancerTargetEnums.Type.`ip`,
    healthStatus = Some(
      Seq(
        LoadBalancerTargetHealthStatus(
          listenPort = Some(80),
          status = Some(LoadBalancerTargetHealthStatusEnums.Status.`healthy`)
        )
      )
    ),
    ip = Some(LoadBalancerTargetIp("203.0.113.1")),
    labelSelector = None,
    server = None,
    targets = None,
    usePrivateIp = Some(false)
  )

  val cp = LoadBalancerTarget(
    `type` = LoadBalancerTargetEnums.Type.`label_selector`,
    labelSelector = Some(LabelSelector("vm-type=cp")),
    usePrivateIp = Some(true)
  )

  val k8sLoadBalancer = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "k8s-lb-" + scala.util.Random.alphanumeric.take(8).mkString,
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`round_robin`)
    ),
    location = Some("fsn1"),
    publicInterface = Some(true)
  )

  val healthCheck = LoadBalancerServiceHealthCheck(
    interval = 10,
    port = 6443,
    protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
    retries = 3,
    timeout = 5,
    http = None
  )

  val healthCheck2 = LoadBalancerServiceHealthCheck(
    interval = 10,
    port = 80,
    protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
    retries = 3,
    timeout = 5,
    http = None
  )
  val apiService = LoadBalancerService(
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    listenPort = 6443,
    destinationPort = 6443,
    healthCheck = healthCheck,
    proxyprotocol = false,
    http = None
  )
  val httpService = LoadBalancerService(
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    listenPort = 80,
    destinationPort = 30080,
    healthCheck = healthCheck2,
    proxyprotocol = false,
    http = None
  )
  val httpsService = LoadBalancerService(
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    listenPort = 443,
    destinationPort = 30443,
    healthCheck = healthCheck2,
    proxyprotocol = false,
    http = None
  )

  val labelSelectorTarget = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.`label_selector`,
    labelSelector = Some(LabelSelector("vm-type=cp")),
    usePrivateIp = Some(true)
  )

  val k8sLoadBalancerRequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "k8s-lb-" + scala.util.Random.alphanumeric.take(8).mkString,
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`round_robin`)
    ),
    location = Some("fsn1"),
    publicInterface = Some(true),
    services = Some(Seq(apiService, httpService, httpsService)),
    targets = Some(Seq(labelSelectorTarget))
  )

//Create Private network
//All our servers will be connected to this private network, and external access to internal resources will only be possible through VPN or Load Balancers.
  val createPrivateNetworkRequest = CreateNetworkRequest(
    ipRange = "10.10.0.0/16",
    name = "k8s-private-network",
    exposeRoutesToVswitch = Some(true),
    labels = Some(Map("environment" -> "production", "project" -> "k8s")),
    routes = Some(
      List(
        Route(
          destination = "10.10.3.0/24",
          gateway = "10.10.3.1"
        ),
        Route(
          destination = "10.10.4.0/24",
          gateway = "10.10.4.1"
        )
      )
    ),
    subnets = Some(
      List(
        Subnet(
          networkZone = "fsn1",
          `type` = SubnetEnums.Type.`cloud`,
          ipRange = Some("10.10.3.0/24"),
          vswitchId = None
        )
      )
    )
  )

  val createVpnServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "vpn-server",
    serverType = "cpx11",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "vpn-server"
      )
    ),
    location = Some("fsn1"),
    networks =
      Some(List( /* ID of the private network created earlier */ 123456)),
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, openvpn]\n"
    ),
    volumes = None
  )

  // We need two Load Balancers: Public Load Balancer distributes incoming traffic from external sources across internal services, Internal Load Balancer will be entry point for private services.

  val createPublicLoadBalancerRequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "public-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`round_robin`)
    ),
    labels = Some(Map("environment" -> "production", "role" -> "public-lb")),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(true),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 80,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 80,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
            retries = 3,
            timeout = 5,
            http = Some(
              LoadBalancerServiceHealthCheckHttp(
                domain = "example.com",
                path = "/healthz",
                response = Some("OK"),
                statusCodes = Some(Seq("200")),
                tls = Some(false)
              )
            )
          ),
          listenPort = 80,
          protocol = LoadBalancerServiceEnums.Protocol.http,
          proxyprotocol = false,
          http = Some(
            Http(
              certificates = None,
              cookieLifetime = None,
              cookieName = None,
              redirectHttp = Some(true),
              stickySessions = Some(false)
            )
          )
        )
      )
    )
  )

  val createInternalLoadBalancerRequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "internal-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels = Some(Map("environment" -> "production", "role" -> "internal-lb")),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 5432,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 5432,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 5432,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    )
  )

//Hetzner Cloud CPX41 server with 8 vCPUs, 16GB RAM and 240GB NVMe SSD is a good option for main Kubernetes node. Select the same location, Ubuntu 24.04, Shared CPX41 instance

  val createK8sMasterNodeRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "k8s-master-node",
    serverType = "cpx41",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "k8s-master"
      )
    ),
    location = Some("fsn1"),
    networks =
      Some(List( /* ID of the private network created earlier */ 123456)),
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(false),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, docker.io]\n"
    ),
    volumes = None
  )

  val mywebsite = CreateServerRequest(
    image = "ubuntu-22.04",
    name = "my-personal-website",
    serverType = "cx21",
    automount = Some(true),
    datacenter = Some("nbg1-dc3"),
    firewalls = Some(
      List(
        CreateServerRequestFirewalls(
          /* ID of the website firewall created earlier */ 123456
        )
      )
    ),
    labels = None,
    location = Some("nbg1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val mywebsitefirewall = CreateFirewallRequest(
    name = "website-firewall",
    applyTo = None,
    labels = None,
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow ssh traffic"),
          destinationIps = None,
          port = Some("22"),
          sourceIps = Some(List("0.0.0.0/0", "::/0"))
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow http traffic"),
          destinationIps = None,
          port = Some("80"),
          sourceIps = Some(List("0.0.0.0/0", "::/0"))
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow https traffic"),
          destinationIps = None,
          port = Some("443"),
          sourceIps = Some(List("0.0.0.0/0", "::/0"))
        )
      )
    )
  )

  val mywebsitelb = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "website-lb",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`round_robin`)
    ),
    labels = None,
    location = Some("nbg1"),
    network = None,
    networkZone = None,
    publicInterface = Some(true),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 80,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 80,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
            retries = 3,
            timeout = 5,
            http = Some(
              LoadBalancerServiceHealthCheckHttp(
                domain = "my-personal-website.com",
                path = "/",
                response = Some(""),
                statusCodes = Some(Seq("200")),
                tls = Some(false)
              )
            )
          ),
          listenPort = 80,
          protocol = LoadBalancerServiceEnums.Protocol.http,
          proxyprotocol = false,
          http = Some(
            Http(
              certificates = None,
              cookieLifetime = None,
              cookieName = None,
              redirectHttp = Some(true),
              stickySessions = Some(false)
            )
          )
        )
      )
    )
  )

  val mywebsitelbtarget = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.`server`,
    ip = None,
    labelSelector = None,
    server =
      Some(ResourceId( /* ID of the website server created earlier */ 123456)),
    usePrivateIp = Some(false)
  )

  val mywebsitelbrequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "website-lb",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`round_robin`)
    ),
    labels = None,
    location = Some("nbg1"),
    network = None,
    networkZone = None,
    publicInterface = Some(true),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 80,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 80,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
            retries = 3,
            timeout = 5,
            http = Some(
              LoadBalancerServiceHealthCheckHttp(
                domain = "my-personal-website.com",
                path = "/",
                response = Some(""),
                statusCodes = Some(Seq("200")),
                tls = Some(false)
              )
            )
          ),
          listenPort = 80,
          protocol = LoadBalancerServiceEnums.Protocol.http,
          proxyprotocol = false,
          http = Some(
            Http(
              certificates = None,
              cookieLifetime = None,
              cookieName = None,
              redirectHttp = Some(true),
              stickySessions = Some(false)
            )
          )
        )
      )
    ),
    targets = Some(
      Seq(
        mywebsitelbtarget
      )
    )
  )

  val createSshKeyRequest = CreateSshKeyRequest(
    name = "example-ssh-key",
    publicKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCz...",
    labels = Some(Map("env" -> "production", "team" -> "devops"))
  )

  val cx23ServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "cx23-server",
    serverType = "cx23",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "web-server"
      )
    ),
    location = Some("fsn1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val cax11ServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "cax11-server",
    serverType = "cax11",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "web-server"
      )
    ),
    location = Some("fsn1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val cx33ServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "cx33-server",
    serverType = "cx33",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "web-server"
      )
    ),
    location = Some("fsn1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val cax21ServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "cax21-server",
    serverType = "cax21",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "web-server"
      )
    ),
    location = Some("fsn1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val cx43ServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "cx43-server",
    serverType = "cx43",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "web-server"
      )
    ),
    location = Some("fsn1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val cax31ServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "cax31-server",
    serverType = "cax31",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "web-server"
      )
    ),
    location = Some("fsn1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val cax41ServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "cax41-server",
    serverType = "cax41",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "web-server"
      )
    ),
    location = Some("fsn1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val cx53ServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "cx53-server",
    serverType = "cx53",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "web-server"
      )
    ),
    location = Some("fsn1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val keyId = UUID.randomUUID().toString()
//java key pair generation
  val keyPair =
    java.security.KeyPairGenerator.getInstance("RSA").generateKeyPair()
  val publicKeyPEM =
    java.util.Base64.getEncoder.encodeToString(keyPair.getPublic.getEncoded)
  val privateKeyPEM =
    java.util.Base64.getEncoder.encodeToString(keyPair.getPrivate.getEncoded)

  val createSshKeyRequest2 = CreateSshKeyRequest(
    name = s"auto-generated-key-$keyId",
    publicKey = publicKeyPEM,
    labels = Some(Map("env" -> "automation", "key-id" -> keyId))
  )
  val rules = Seq(
    // Allow HTTPS from anywhere
    Rule(
      direction = RuleEnums.Direction.in,
      protocol = RuleEnums.Protocol.tcp,
      port = Some("443"),
      sourceIps = Some(Seq("0.0.0.0/0", "::/0"))
    ),
    // Allow SSH only from a specific Management IP
    Rule(
      direction = RuleEnums.Direction.in,
      protocol = RuleEnums.Protocol.tcp,
      port = Some("22"),
      sourceIps = Some(Seq("1.2.3.4/32"))
    )
  )

  // your Load Balancer should distribute traffic across multiple redundant targets (Servers or IP addresses).

  // Private Networking: Use attachLoadBalancerToNetwork to connect the Load Balancer to a Cloud Network. This allows the Load Balancer to communicate with backend servers via their private IPs.

  val createLoadBalancerWithNetworkRequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "redundant-lb",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`round_robin`)
    ),
    labels = Some(Map("environment" -> "production", "role" -> "redundant-lb")),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(true),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 80,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 80,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
            retries = 3,
            timeout = 5,
            http = Some(
              LoadBalancerServiceHealthCheckHttp(
                domain = "redundant.example.com",
                path = "/healthz",
                response = Some("OK"),
                statusCodes = Some(Seq("200")),
                tls = Some(false)
              )
            )
          ),
          listenPort = 80,
          protocol = LoadBalancerServiceEnums.Protocol.http,
          proxyprotocol = false,
          http = Some(
            Http(
              certificates = None,
              cookieLifetime = None,
              cookieName = None,
              redirectHttp = Some(true),
              stickySessions = Some(false)
            )
          )
        )
      )
    )
  )

  /// Delete Protection: Immediately after creation, call changeLoadBalancerProtection with delete = true. This prevents the deleteLoadBalancer method from executing unless protection is manually removed first
  val protectLoadBalancerRequest = ChangeLoadBalancerProtectionRequest(
    delete = Some(true)
  )

//// Attach to Private Network

  val attachLoadBalancerToNetworkRequest2 = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("192.168.8.5"),
    ipRange = Some("192.168.0.0/16")
  )

// val changeFloatingIpProtectionRequest = ChangeFloatingIpProtectionRequest(
//     delete = Some(true)
//   )

  val changeImageProtectionRequest = ChangeImageProtectionRequest(
    delete = Some(true)
  )

  val changeLoadBalancerProtectionRequest = ChangeLoadBalancerProtectionRequest(
    delete = Some(true)
  )

  val changeNetworkProtectionRequest = ChangeNetworkProtectionRequest(
    delete = Some(true)
  )

  val changeNetworkProtectionRequest2 = ChangeNetworkProtectionRequest(
    delete = Some(true)
  )

  // val changePrmaryIpProtectionRequest = ChangePrimaryIpProtectionRequest(
  //   delete = Some(true)
  // )

  val changeProtectionRequest = ChangeProtectionRequest(
    delete = Some(true)
  )

  val changeRrsetsProtectionRequest = ChangeRrsetsProtectionRequest(
    change = true
  )

  val changeServerProtectionRequest = ChangeServerProtectionRequest(
    delete = Some(true),
    rebuild = Some(true)
  )

  val changeVolumeProtectionRequest = ChangeVolumeProtectionRequest(
    delete = Some(true)
  )

  val changeZoneProtectionRequest = ChangeZonesProtectionRequest(
    delete = Some(true)
  )

  val subnet1 = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("192.168.0.0/24"),
    vswitchId = None
  )

  val subnet2 = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.`vswitch`,
    ipRange = Some("192.168.1.0/24"),
    vswitchId = None
  )

  val subnet3 = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("192.168.2.0/24"),
    vswitchId = None
  )

  val subnet4 = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.`vswitch`,
    ipRange = Some("192.168.3.0/24"),
    vswitchId = None
  )

  val subnet5 = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.`server`,
    ipRange = Some("192.168.4.0/24"),
    vswitchId = None
  )

  val subnet6 = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("192.168.5.0/24"),
    vswitchId = None
  )

  val addRouteToNetwork = HetznerClient.networks.addRouteToNetwork(
    token = "token",
    id = 123456,
    body = Route(
      destination = "10.10.0.0/16",
      gateway = "10.10.0.1"
    )
  )

  val addSubnetToNetwork = HetznerClient.networks.addSubnetToNetwork(
    token = "token",
    id = 123456,
    body = Subnet(
      networkZone = "fsn1",
      `type` = SubnetEnums.Type.`cloud`,
      ipRange = Some("10.8.0.0/18"),
      vswitchId = None
    )
  )

  val changeIpRangeOfNetwork =
    HetznerClient.networks.changeIpRangeOfNetwork(
      "token",
      id = 123456,
      changeIpRangeOfNetworkRequest = ChangeIpRangeOfNetworkRequest(
        ipRange = "10.10.0.0/15"
      )
    )

  val changeNetworkProtection =
    HetznerClient.networks.changeNetworkProtection(
      "token",
      id = 123456,
      changeNetworkProtectionRequest = ChangeNetworkProtectionRequest(
        delete = Some(true)
      )
    )

  val createNetwork = HetznerClient.networks.createNetwork(
    "token",
    createNetworkRequest = CreateNetworkRequest(
      name = "my-network",
      ipRange = "10.200.0.0/16",
      subnets = Some(
        Seq(
          Subnet(
            networkZone = "fsn1",
            `type` = SubnetEnums.Type.`cloud`,
            ipRange = Some("10.200.0.0/18"),
            vswitchId = None
          ),
          Subnet(
            networkZone = "fsn1",
            `type` = SubnetEnums.Type.`vswitch`,
            ipRange = Some("10.200.64.0/18"),
            vswitchId = Some(87654321L)
          )
        )
      ),
      exposeRoutesToVswitch = Some(true),
      labels = Some(
        Map(
          "environment" -> "production",
          "project" -> "alpha"
        )
      ),
      routes = Some(
        Seq(
          Route(
            destination = "10.200.0.0/16",
            gateway = "10.200.0.1"
          )
        )
      )
    )
  )

  val deleteNetwork = HetznerClient.networks.deleteNetwork(
    "token",
    id = 123456
  )

  val deleteRouteFromNetwork =
    HetznerClient.networks.deleteRouteFromNetwork(
      "token",
      id = 123456,
      body = Route(
        destination = "10.10.0.0/16",
        gateway = "10.10.0.1"
      )
    )

  val deleteSubnetFromNetwork =
    HetznerClient.networks.deleteSubnetFromNetwork(
      "token",
      id = 123456,
      deleteSubnetFromNetworkRequest = DeleteSubnetFromNetworkRequest(
        ipRange = "10.10.0.0/16"
      )
    )

  val network = HetznerClient.networks.getNetwork(
    "token",
    id = 123456
  )

  val networks = HetznerClient.networks.listNetworks(
    "token",
    sort = Seq("name:asc"),
    name = Some("my-network"),
    labelSelector = Some("environment=production"),
    page = Some(1),
    perPage = Some(20)
  )

  val controlPlaneFirewall = CreateFirewallRequest(
    name = "controlplane",
    applyTo = None,
    labels = Some(
      Map(
        "type" -> "infra",
        "label" -> "controlplane"
      )
    ),
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.icmp,
          description = Some("Allow ICMP traffic"),
          destinationIps = None,
          port = None,
          sourceIps = Some(
            List("10.10.0.0/16", "::/0")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          description = Some("Allow UDP traffic from VPC"),
          destinationIps = None,
          port = Some("any"),
          sourceIps = Some(
            List("10.10.0.0/16")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow TCP traffic from VPC"),
          destinationIps = None,
          port = Some("any"),
          sourceIps = Some(
            List("10.10.0.0/16")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow HTTP traffic from admins"),
          destinationIps = None,
          port = Some("80"),
          sourceIps = Some(
            List("0.0.0.0/0")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow HTTPS traffic from admins"),
          destinationIps = None,
          port = Some("443"),
          sourceIps = Some(
            List("0.0.0.0/0")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow kube-apiserver traffic"),
          destinationIps = None,
          port = Some("6443"),
          sourceIps = Some(
            List("10.10.0.0/16", "0.0.0.0/0")
          )
        )
      )
    )
  )

  val webFirewall = CreateFirewallRequest(
    name = "web",
    applyTo = None,
    labels = Some(
      Map(
        "type" -> "infra",
        "label" -> "web"
      )
    ),
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.icmp,
          description = Some("Allow ICMP traffic"),
          destinationIps = None,
          port = None,
          sourceIps = Some(
            List("10.10.0.0/16", "::/0")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          description = Some("Allow UDP traffic from VPC"),
          destinationIps = None,
          port = Some("any"),
          sourceIps = Some(
            List("10.10.0.0/16")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow TCP traffic from VPC"),
          destinationIps = None,
          port = Some("any"),
          sourceIps = Some(
            List("10.10.0.0/16")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow HTTP traffic from web whitelist"),
          destinationIps = None,
          port = Some("80"),
          sourceIps = Some(
            List("0.0.0.0/0")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow HTTPS traffic from web whitelist"),
          destinationIps = None,
          port = Some("443"),
          sourceIps = Some(
            List("0.0.0.0/0")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow cilium health traffic"),
          destinationIps = None,
          port = Some("4240"),
          sourceIps = Some(
            List("::/0")
          )
        )
      )
    )
  )

  val workerFirewall = CreateFirewallRequest(
    name = "worker",
    applyTo = None,
    labels = Some(
      Map(
        "type" -> "infra",
        "label" -> "worker"
      )
    ),
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.icmp,
          description = Some("Allow ICMP traffic"),
          destinationIps = None,
          port = None,
          sourceIps = Some(
            List("10.10.0.0/16", "::/0")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          description = Some("Allow UDP traffic from VPC"),
          destinationIps = None,
          port = Some("any"),
          sourceIps = Some(
            List("10.10.0.0/16")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow TCP traffic from VPC"),
          destinationIps = None,
          port = Some("any"),
          sourceIps = Some(
            List("10.10.0.0/16")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow cilium health traffic"),
          destinationIps = None,
          port = Some("4240"),
          sourceIps = Some(
            List("::/0")
          )
        )
      )
    )
  )

  val workerAutoScaleFirewall = CreateFirewallRequest(
    name = "worker-auto-scale",
    applyTo = None,
    labels = Some(
      Map(
        "type" -> "infra",
        "label" -> "worker-auto-scale"
      )
    ),
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.icmp,
          description = Some("Allow ICMP traffic"),
          destinationIps = None,
          port = None,
          sourceIps = Some(
            List("10.10.0.0/16", "::/0")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          description = Some("Allow UDP traffic from VPC"),
          destinationIps = None,
          port = Some("any"),
          sourceIps = Some(
            List("10.10.0.0/16")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow TCP traffic from VPC"),
          destinationIps = None,
          port = Some("any"),
          sourceIps = Some(
            List("10.10.0.0/16")
          )
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          description = Some("Allow cilium health traffic"),
          destinationIps = None,
          port = Some("4240"),
          sourceIps = Some(
            List("::/0")
          )
        )
      )
    )
  )

  val mmanagementlbrequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "management-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels =
      Some(Map("environment" -> "production", "role" -> "management-lb")),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 6443,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 6443,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 6443,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        ),
        LoadBalancerService(
          destinationPort = 22,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 22,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 22,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    )
  )

  val managementlbNetworkRegistration = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("10.20.3.2"),
    ipRange = Some("10.20.3.0/24")
  )

  val managementlbTarget = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.`server`,
    ip = None,
    labelSelector = None,
    server = Some(
      ResourceId( /* ID of the management server created earlier */ 123456)
    ),
    usePrivateIp = Some(true)
  )
  val managementlbrequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "management-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels =
      Some(Map("environment" -> "production", "role" -> "management-lb")),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 6443,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 6443,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 6443,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        ),
        LoadBalancerService(
          destinationPort = 22,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 22,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 22,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    ),
    targets = Some(
      Seq(
        managementlbTarget
      )
    )
  )

  val mywebsiteserver = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "website-server",
    serverType = "cx11",
    automount = Some(true),
    datacenter = Some("nbg1-dc3"),
    firewalls = Some(List(CreateServerRequestFirewalls(123456))),
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "website-server"
      )
    ),
    location = Some("nbg1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(true),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

  val managementlbk8sservice = LoadBalancerService(
    destinationPort = 6443,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 6443,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 6443,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val managementlbsshservice = LoadBalancerService(
    destinationPort = 22,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 22,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 22,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val managementlbregisterservice = LoadBalancerService(
    destinationPort = 9345,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 9345,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 9345,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val managementlbservice = LoadBalancerService(
    destinationPort = 8080,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 8080,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 8080,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val kubernetesInternalNetwork = CreateNetworkRequest(
    name = "kubernetes-internal-network",
    ipRange = "172.16.0.0/12",
    labels = Some(
      Map("automated" -> "true")
    )
  )

  val rancherManagementSshKey = CreateSshKeyRequest(
    name = "rancher-management-key",
    publicKey = "publicKeyPEM",
    labels = Some(
      Map("automated" -> "true")
    )
  )

  val rancherManagementServer = CreateServerRequest(
    image = "ubuntu-20.04",
    name = "rancher-management-node",
    serverType = "cx11",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "rancher-management-node"
      )
    ),
    location = Some("fsn1"),
    networks = Some(List(78678L)),
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(true),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("rancher-management-key")),
    startAfterCreate = Some(true),
    userData = Some(
      """#cloud-config
runcmd:
- [apt-get, update]
- [apt-get, install, -y, docker.io]
- [curl, -fsSL, https://releases.rancher.com/install-docker/20.10.sh, |, sh]
- [docker, run, -d, --restart=unless-stopped, -p, 80:80, -p, 443:443, rancher/rancher]
"""
    ),
    volumes = None
  )

  val cx11ServerRequest = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "cx11-server",
    serverType = "cx11",
    automount = Some(true),
    datacenter = Some("fsn1-dc3"),
    firewalls = None,
    labels = Some(
      Map(
        "environment" -> "production",
        "role" -> "web-server"
      )
    ),
    location = Some("fsn1"),
    networks = None,
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = Some(
      "#cloud-config\nruncmd:\n- [apt-get, update]\n- [apt-get, install, -y, nginx]\n"
    ),
    volumes = None
  )

//This is the actual IP range the servers and Load Balancer will use for their private communication.
  val rancherManagementSubnet = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("172.16.0.0/24"),
    vswitchId = None
  )
  val rancherManagementSubnet2 = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.cloud,
    ipRange = Some("172.16.10.0/24"),
    vswitchId = None
  )

  val rancherManagementSubnet3 = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("172.16.20.0/24"),
    vswitchId = None
  )

  val rancherManagementSubnet4 = Subnet(
    networkZone = "fsn1",
    `type` = SubnetEnums.Type.server,
    ipRange = Some("172.16.29.0/24"),
    vswitchId = None
  )

//This connects the Load Balancer to the private network/subnet d
  val rancherManagementServerSubnetRegistration =
    AttachLoadBalancerToNetworkRequest(
      network = 123456,
      /* IP to request to be assigned to this Load Balancer; if you do not provide this then you will be auto assigned an IP address */
      ip = Some("172.16.10.7"),
      /* IP range in CIDR block notation of the subnet to attach to.  This allows for auto assigning an IP address for a specific subnet. Providing `ip` that is not part of `ip_range` will result in an error.  */
      ipRange = Some("172.16.10.0/24")
    )

  val rancherManagementTarget = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.`server`,
    ip = None,
    labelSelector = None,
    server = Some(
      ResourceId( /* ID of the management server created earlier */ 123456)
    ),
    usePrivateIp = Some(true)
  )

  val rancherManagementlbk8sService = LoadBalancerService(
    destinationPort = 6443,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 6443,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 6443,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val rancherManagementlbhttpService = LoadBalancerService(
    destinationPort = 80,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 80,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 80,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val rancherManagementlbhttpsService = LoadBalancerService(
    destinationPort = 443,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 443,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 443,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val rancherManagementlbrequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "rancher-management-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels = Some(
      Map("environment" -> "production", "role" -> "rancher-management-lb")
    ),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        rancherManagementlbk8sService,
        rancherManagementlbhttpService,
        rancherManagementlbhttpsService
      )
    ),
    targets = Some(
      Seq(
        rancherManagementTarget
      )
    )
  )

  val attachIsoToServerRequest = AttachIsoToServerRequest(
    iso = "ubuntu-24.04"
  )

  val attachServerToNetworkRequest3 = AttachServerToNetworkRequest(
    network = 123456,
    aliasIps = Some(Seq("172.16.10.8", "192.168.1.1")),
    ip = Some("10.10.4.10"),
    ipRange = Some("10.10.4.0/24")
  )

  val attachVolumeToServerRequest = AttachVolumeToServerRequest(
    server = 123456,
    automount = Some(true)
  )

  val attachLoadBalancerToNetworkRequest4 = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("172.16.10.7"),
    ipRange = Some("172.16.10.0/24")
  )

// 1. Create a Placement Group to ensure hardware diversity
  val k8sPlacementGroup = CreatePlacementgroupRequest(
    name = "k8s-compute-spread",
    `type` = CreatePlacementgroupRequestEnums.Type.spread, // Critical for HA
    labels = Some(Map("stack" -> "production", "tier" -> "control-plane"))
  )

// 2. Production Worker Node Template
  def createWorkerNode(index: Int, networkId: Int, placementGroupId: Int) =
    CreateServerRequest(
      name = s"prod-worker-$index",
      serverType = "cpx31", // Higher performance for production
      image = "ubuntu-24.04",
      location = Some("fsn1"),
      placementGroup = Some(placementGroupId), // Link to placement group
      networks = Some(List(networkId)),
      publicNet = Some(
        CreateServerRequestPublicNet(
          enableIpv4 = Some(false), // No Public IPv4 (Private Only)
          enableIpv6 = Some(false) // No Public IPv6
        )
      ),
      labels = Some(
        Map(
          "role" -> "worker",
          "environment" -> "production",
          "managed-by" -> "scala-iac"
        )
      ),
      userData = Some(
        """#cloud-config
			|package_update: true
			|packages: [open-iscsi, nfs-common]
			|runcmd:
			|  - [systemctl, enable, iscsid]
		""".stripMargin
      )
    )

// 1. Production Health Check (Active monitoring)
  val prodHealthCheck = LoadBalancerServiceHealthCheck(
    protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
    port = 80,
    interval = 15,
    timeout = 10,
    retries = 3,
    http = Some(
      LoadBalancerServiceHealthCheckHttp(
        domain = "example.com",
        path = "/healthz",
        response = None,
        statusCodes = Some(Seq("200")),
        tls = Some(false)
      )
    )
  )

// 2. HTTPS Service with SSL and Auto-Redirect
  val productionHttpsService = LoadBalancerService(
    protocol = LoadBalancerServiceEnums.Protocol.https,
    listenPort = 443,
    destinationPort = 80, // Internal traffic stays on 80
    healthCheck = prodHealthCheck,
    proxyprotocol = true, // Pass client IP to the server
    http = Some(
      Http(
        /* IDs of the Certificates to use for TLS/SSL termination by the Load Balancer; empty for TLS/SSL passthrough or if `protocol` is `http`. */
        certificates = Some(Seq(987654)), // Your Managed Certificate ID
        /* Lifetime of the cookie used for sticky sessions (in seconds). */
        cookieLifetime = Some(3600),
        /* Name of the cookie used for sticky sessions. */
        cookieName = Some("PRODSESSIONID"),
        /* Lifetime of the cookie in seconds. Set to `0` for session cookies. */
        stickySessions = Some(true),
        /* Redirect HTTP requests to HTTPS. Only available if `protocol` is `https`. */
        redirectHttp = Some(true) // Force all HTTP traffic to HTTPS
      )
    )
  )

  val prodLoadBalancer = CreateLoadBalancerRequest(
    name = "prod-ingress-lb",
    loadBalancerType = "lb21", // Higher capacity
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.least_connections)
    ),
    /* ID of the network the Load Balancer should be attached to on creation. */
    network = Some(123456),
    publicInterface = Some(true),
    location = Some("nbg1"),
    services = Some(Seq(productionHttpsService)),
    targets = Some(
      Seq(
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.label_selector,
          ip = None,
          server = Some(ResourceId(123456)),
          labelSelector =
            Some(LabelSelector("role=worker,environment=production")),
          usePrivateIp = Some(true)
        )
      )
    )
  )

  // In production, your databases and application servers should never have public IP addresses. You reach them via a Bastion/VPN host

  // 1. The Secure Private Subnet
  val secureSubnet = Subnet(
    /* Type of subnet.  - `cloud` - Used to connect cloud Servers and Load Balancers. - `server` - Same as the `cloud` type. **Deprecated**, use the `cloud` type instead. - `vswitch` - Used to [connect cloud Servers and Load Balancers with dedicated Servers](https://docs.hetzner.com/cloud/networks/connect-dedi-vswitch).  */
    `type` = SubnetEnums.Type.cloud,
    networkZone = "eu-central",
    ipRange = Some("10.0.1.0/24") // Dedicated subnet for databases
  )

  // 2. The Database Firewall (Isolated to Private Network)
  val databaseFirewall = CreateFirewallRequest(
    name = "db-internal-only",
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("5432"),
          // ONLY allow traffic from the web-tier subnet, not the whole world
          sourceIps = Some(List("10.0.0.0/24")),
          description = Some("Allow PG access from web tier only")
        ),
        Rule(
          direction = RuleEnums.Direction.out,
          protocol = RuleEnums.Protocol.tcp,
          destinationIps = Some(List("0.0.0.0/0")),
          description = Some("Allow updates")
        )
      )
    )
  )

  // Define subnets for different regions within the same Private Network
  val networkRegions = List(
    Subnet(
      networkZone = "eu-central",
      `type` = SubnetEnums.Type.cloud,
      ipRange = Some("10.0.1.0/24")
    ), // Germany
    Subnet(
      networkZone = "us-east",
      `type` = SubnetEnums.Type.cloud,
      ipRange = Some("10.0.2.0/24")
    ) // USA (Ashburn)
  )

  val globalVPC = CreateNetworkRequest(
    name = "global-production-backbone",
    ipRange = "10.0.0.0/16",
    subnets = Some(networkRegions)
  )

  val databaseVolume = CreateVolumeRequest(
    name = "pg-data-production",
    size = 100, // 100GB NVMe
    location = Some("fsn1"),
    format = Some("ext4"),
    labels = Some(Map("db-cluster" -> "primary"))
  )

  // In your Server Request, reference the volume
  val dbServer = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "db-node-01",
    serverType = "cx31",
    volumes = Some(List(1234567)) // The ID of the volume created above
  )

  // Example: High-performance TCP with Proxy Protocol

  val apiService2 = LoadBalancerService(
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    listenPort = 443,
    destinationPort = 443,
    proxyprotocol =
      true, // Passes original Client IP to the backend. Without this, your web server logs will show every single visitor as having the same IP address (the LB's private IP).
    healthCheck = LoadBalancerServiceHealthCheck(
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      port = 443,
      interval = 10,
      timeout = 5,
      retries = 3
    )
  )

  val ipsecVpnFirewall = CreateFirewallRequest(
    name = "vpn-gateway-firewall",
    rules = Some(
      List(
        // 1. IKE (Internet Key Exchange) - Initial Handshake
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          port = Some("500"),
          sourceIps = Some(List("0.0.0.0/0", "::/0")),
          description = Some("Allow IKE for VPN handshake")
        ),

        // 2. NAT-T (NAT Traversal) - Essential for clients behind routers
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          port = Some("4500"),
          sourceIps = Some(List("0.0.0.0/0", "::/0")),
          description = Some("Allow IPsec NAT-Traversal")
        ),

        // 3. ESP (Encapsulating Security Payload) - The Encrypted Data
        // Note: No port is specified because ESP is its own protocol (Layer 3)
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.esp,
          port = None,
          sourceIps = Some(List("0.0.0.0/0", "::/0")),
          description = Some("Allow encrypted ESP traffic")
        ),

        // 4. SSH Management - Restricted to your specific IP for security
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("22"),
          sourceIps =
            Some(List("203.0.113.1/32")), // Use your ACTUAL office/home IP
          description = Some("Secure Admin SSH access")
        )
      )
    )
  )

  // 1. Define the Route
  val officeRoute = Route(
    destination = "192.168.1.0/24", // Your remote office network
    gateway = "10.10.0.5" // The Private IP of your VPN server
  )

  // 2. Create the Network with the Route
  val networkWithGateway = CreateNetworkRequest(
    name = "prod-network-with-vpn",
    ipRange = "10.10.0.0/16",
    subnets = Some(
      List(
        Subnet(
          networkZone = "eu-central",
          `type` = SubnetEnums.Type.cloud,
          ipRange = Some("10.10.0.0/24")
        )
      )
    ),
    routes = Some(List(officeRoute)),
    labels = Some(Map("environment" -> "production"))
  )
  // Production VPC with Segmented Subnets
  val productionVPC = CreateNetworkRequest(
    name = "production-vpc",
    ipRange = "10.0.0.0/16", // The "Supernet"
    subnets = Some(
      List(
        // 1. PUBLIC TIER
        Subnet(
          `type` = SubnetEnums.Type.cloud,
          networkZone = "eu-central",
          ipRange = Some("10.0.1.0/24")
        ),
        // 2. PRIVATE TIER (Larger range for scaling app nodes)
        Subnet(
          `type` = SubnetEnums.Type.cloud,
          networkZone = "eu-central",
          ipRange = Some("10.0.10.0/22")
        ),
        // 3. DATABASE TIER
        Subnet(
          `type` = SubnetEnums.Type.cloud,
          networkZone = "eu-central",
          ipRange = Some("10.0.20.0/24")
        ),
        // 4. MANAGEMENT TIER (Isolated admin access)
        Subnet(
          `type` = SubnetEnums.Type.cloud,
          networkZone = "eu-central",
          ipRange = Some("10.0.30.0/24")
        )
      )
    )
  )

  val databaseFirewall2 = CreateFirewallRequest(
    name = "db-tier-security",
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("5432"), // Postgres
          sourceIps = Some(List("10.0.10.0/22")), // ONLY App Tier
          description = Some("Allow PG access from App Tier only")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("6379"), // Redis
          sourceIps = Some(List("10.0.10.0/22")),
          description = Some("Allow Redis access from App Tier only")
        )
      )
    )
  )

// A single production Load Balancer often runs multiple services simultaneously. For example:
// 		Service A (HTTP Redirect): Listens on port 80 just to tell users to go to port 443.
// 		Service B (HTTPS App): Listens on port 443 for the actual website traffic.
// 		Service C (API): Listens on port 8443 for a specific mobile API.

  val prodHttpRedirectService = LoadBalancerService(
    protocol = LoadBalancerServiceEnums.Protocol.http,
    listenPort = 80,
    destinationPort = 80,
    healthCheck = LoadBalancerServiceHealthCheck(
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
      port = 80,
      interval = 10,
      timeout = 5,
      retries = 3,
      http = Some(
        LoadBalancerServiceHealthCheckHttp(
          domain = "example.com",
          path = "/",
          response = None,
          statusCodes = Some(Seq("200")),
          tls = Some(false)
        )
      )
    ),
    proxyprotocol = false,
    http = Some(
      Http(
        redirectHttp = Some(true) // Redirect all HTTP to HTTPS
      )
    )
  )

  val prodHttpsAppService = LoadBalancerService(
    protocol = LoadBalancerServiceEnums.Protocol.https,
    listenPort = 443,
    destinationPort = 80,
    healthCheck = LoadBalancerServiceHealthCheck(
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
      port = 80,
      interval = 10,
      timeout = 5,
      retries = 3,
      http = Some(
        LoadBalancerServiceHealthCheckHttp(
          domain = "example.com",
          path = "/",
          response = None,
          statusCodes = Some(Seq("200")),
          tls = Some(false)
        )
      )
    ),
    proxyprotocol = true,
    http = Some(
      Http(
        certificates = Some(Seq(987654)), // Your Managed Certificate ID
        stickySessions = Some(true),
        cookieName = Some("APPSESSIONID"),
        cookieLifetime = Some(3600)
      )
    )
  )

  val prodApiService = LoadBalancerService(
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    listenPort = 8443,
    destinationPort = 8443,
    healthCheck = LoadBalancerServiceHealthCheck(
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      port = 8443,
      interval = 10,
      timeout = 5,
      retries = 3
    ),
    proxyprotocol = true
  )

  val prodLoadBalancer2 = CreateLoadBalancerRequest(
    name = "prod-multi-service-lb",
    loadBalancerType = "lb21",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.least_connections)
    ),
    network = Some(123456),
    publicInterface = Some(true),
    location = Some("nbg1"),
    services = Some(
      Seq(
        prodHttpRedirectService,
        prodHttpsAppService,
        prodApiService
      )
    ),
    targets = Some(
      Seq(
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.label_selector,
          ip = None,
          server = Some(ResourceId(123456)),
          labelSelector =
            Some(LabelSelector("role=web,environment=production")),
          usePrivateIp = Some(true)
        )
      )
    )
  )

  val kubelbTarget = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.`server`,
    ip = None,
    labelSelector = None,
    server = Some(
      ResourceId( /* ID of one of your Kubernetes servers */ 123456)
    ),
    usePrivateIp = Some(true)
  )
  val kubeNetworkRequest = CreateNetworkRequest(
    name = "kube-network",
    ipRange = "10.0.0.0/16",
    labels = Some(
      Map("environment" -> "production", "role" -> "kube-network")
    )
  )

  val kubeLoadBalancerRequest = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "kube-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels = Some(
      Map("environment" -> "production", "role" -> "kube-load-balancer")
    ),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 6443,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 6443,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 6443,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    ),
    targets = Some(
      Seq(
        kubelbTarget
      )
    )
  )

  val kubeSubnetCommon = Subnet(
    networkZone = "eu-central",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("10.0.7.0/24"),
    vswitchId = None
  )

  val kubeSubnetServers = Subnet(
    networkZone = "eu-central",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("10.0.8.0/24"),
    vswitchId = None
  )

  val kubeSubnetWorkers = Subnet(
    networkZone = "eu-central",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("10.0.9.0/24"),
    vswitchId = None
  )

  val kubeFirewall = CreateFirewallRequest(
    name = "kube-firewall",
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.icmp,
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow ICMP within the VPC")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("443"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow secure web traffic")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("80"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow insecure web traffic")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("22"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow SSH within the VPC")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("6443"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow Kubernetes cluster internal communication")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          port = Some("8472"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow Kubernetes VXLAN communication")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("10250"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow Kubernetes kubelet API communication")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          port = Some("2379-2380"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow Kubernetes etcd server communication")
        )
      )
    )
  )

  val kubelb = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "kube-lb",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels = Some(
      Map("environment" -> "production", "role" -> "kube-lb")
    ),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 6443,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 6443,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 6443,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    ),
    targets = Some(
      Seq(
        kubelbTarget
      )
    )
  )

  val kubelbNetwork = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("10.0.0.90"),
    ipRange = Some("10.0.0.0/16")
  )

  val kubelbservice = LoadBalancerService(
    destinationPort = 6443,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 6443,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 6443,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val kubelbservicehttp = LoadBalancerService(
    destinationPort = 80,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 80,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 80,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

//firewall_attachment
  (1 to 10).map(x =>
    AttachServerToNetworkRequest(
      network = 123456,
      aliasIps = Some(Seq(s"10.0.1.${10 + x}")),
      ip = Some(s"10.0.1.${10 + x}"),
      ipRange = Some("10.0.1.0/24")
    )
  )

  val dedicatedServerTarget = LoadBalancerTarget(
    `type` = LoadBalancerTargetEnums.Type.ip,
    ip = Some(
      LoadBalancerTargetIp(ip = "10.0.20.55")
    ) // The IP of your bare-metal server
  )

// Adding it to a Load Balancer
  val addTargetRequest = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.ip,
    ip = Some(LoadBalancerTargetIp(ip = "10.0.20.55")),
    labelSelector = None,
    server = None,
    usePrivateIp = None
  )

  val mainLoadBalancer = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "main-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels = Some(
      Map("environment" -> "production", "role" -> "main-load-balancer")
    ),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 80,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 80,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 80,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    ),
    targets = Some(
      Seq(
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.`server`,
          ip = None,
          labelSelector = None,
          server = Some(
            ResourceId( /* ID of one of your servers */ 123456)
          ),
          usePrivateIp = Some(true)
        )
      )
    )
  )

  val mainNetwork = CreateNetworkRequest(
    name = "main-network",
    ipRange = "10.0.0.0/16",
    labels = Some(
      Map("environment" -> "production", "role" -> "main-network")
    )
  )

  val mainSubnet = Subnet(
    networkZone = "eu-central",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("10.0.1.0/24"),
    vswitchId = None
  )

  val attachMainLbToNetwork = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("10.0.1.5"),
    ipRange = Some("10.0.1.0/24")
  )

//firewall rule to allow requests only from cloudflare ips

  val cloudflareFirewall = CreateFirewallRequest(
    name = "cloudflare-only-firewall",
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("80"),
          sourceIps = Some(
            List(
              "103.21.244.0/22",
              "103.22.200.0/22",
              "103.31.4.0/22",
              "104.16.0.0/13",
              "104.24.0.0/14",
              "108.162.192.0/18",
              "131.0.72.0/22",
              "141.101.64.0/18",
              "162.158.0.0/15",
              "172.64.0.0/13",
              "173.245.48.0/20",
              "188.114.96.0/20",
              "190.93.240.0/20",
              "197.234.240.0/22",
              "198.41.128.0/17"
            )
          ),
          description = Some("Allow HTTP traffic from Cloudflare IPs only")
        )
      )
    )
  )

  val ingressDefaultServiceHttp = LoadBalancerService(
    destinationPort = 8080,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 8080,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 80,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = true,
    http = None
  )

  val ingressDefaultServiceHttps = LoadBalancerService(
    destinationPort = 8080,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 8080,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 443,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = true,
    http = None
  )

  val k3sapiLoadBalancer = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "k3s-api-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels = Some(
      Map("environment" -> "production", "role" -> "k3s-api-lb")
    ),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 6443,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 6443,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 6443,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    ),
    targets = Some(
      Seq(
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.`server`,
          ip = None,
          labelSelector = None,
          server = Some(
            ResourceId( /* ID of one of your Kubernetes servers */ 123456)
          ),
          usePrivateIp = Some(true)
        )
      )
    )
  )

  val attachK3sApiLbToNetwork = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("10.10.0.3"),
    ipRange = Some("10.10.0.0/24")
  )

  val k3sIngressDefaultLbToNetwork = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("10.10.0.4"),
    ipRange = Some("10.10.0.0/24")
  )

  val apiLoadBalancer = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "api-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels = Some(
      Map("environment" -> "production", "role" -> "api-lb")
    ),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 6443,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 6443,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 6443,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    ),
    targets = Some(
      Seq(
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.`server`,
          ip = None,
          labelSelector = None,
          server = Some(
            ResourceId( /* ID of one of your Kubernetes servers */ 123456)
          ),
          usePrivateIp = Some(true)
        )
      )
    )
  )

  val ingressLoadBalancer = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "ingress-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels = Some(
      Map("environment" -> "production", "role" -> "ingress-lb")
    ),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 443,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 443,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 443,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    ),
    targets = Some(
      Seq(
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.`server`,
          ip = None,
          labelSelector = None,
          server = Some(
            ResourceId( /* ID of one of your Kubernetes servers */ 123456)
          ),
          usePrivateIp = Some(true)
        )
      )
    )
  )

  val attachApiLbToNetwork = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("10.10.0.3"),
    ipRange = Some("10.10.0.0/24")
  )

  val apiLoadBalancerTarget = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.label_selector,
    ip = None,
    labelSelector = Some(LabelSelector("lb=api")),
    server = Some(
      ResourceId( /* ID of one of your Kubernetes servers */ 123456)
    ),
    usePrivateIp = Some(true)
  )

  val ingressLoadBalancerTarget = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.label_selector,
    ip = None,
    labelSelector = Some(LabelSelector("lb=ingress")),
    server = Some(
      ResourceId( /* ID of one of your Kubernetes servers */ 123456)
    ),
    usePrivateIp = Some(true)
  )

  val apiLoadBalancerService6443 = LoadBalancerService(
    destinationPort = 6443,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 6443,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 6443,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val apiLoadBalancerService80 = LoadBalancerService(
    destinationPort = 80,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 80,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 80,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val ingressLoadBalancerService443 = LoadBalancerService(
    destinationPort = 443,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 443,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 443,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val ingressLoadBalancerService22623 = LoadBalancerService(
    destinationPort = 22623,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 22623,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 22623,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )

  val kubeMasterServer1 = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "kube-master-01",
    serverType = "cx31",
    automount = Some(true),
    datacenter = None,
    firewalls = None,
    labels = Some(
      Map(
        "role" -> "kube-master",
        "environment" -> "production",
        "node" -> "master"
      )
    ),
    location = Some("nbg1"),
    networks = Some(List(123456)),
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = None,
    volumes = Some(List(123456))
  )

  val masterLoadBalancer = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "kube-master-lb",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`least_connections`)
    ),
    labels = Some(
      Map("environment" -> "production", "role" -> "kube-master-lb")
    ),
    location = Some("fsn1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(false),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 6443,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 6443,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
            retries = 3,
            timeout = 5,
            http = None
          ),
          listenPort = 6443,
          protocol = LoadBalancerServiceEnums.Protocol.tcp,
          proxyprotocol = false,
          http = None
        )
      )
    ),
    targets = Some(
      Seq(
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.`server`,
          ip = None,
          labelSelector = None,
          server = Some(
            ResourceId(
              /* ID of one of your Kubernetes master servers */ 123456
            )
          ),
          usePrivateIp = Some(true)
        )
      )
    )
  )

  val kubeNetworkRegions = List(
    Subnet(
      networkZone = "eu-central",
      `type` = SubnetEnums.Type.cloud,
      ipRange = Some("10.10.1.0/24")
    ),
    Subnet(
      networkZone = "us-west",
      `type` = SubnetEnums.Type.cloud,
      ipRange = Some("10.10.2.0/24")
    ),
    Subnet(
      networkZone = "asia-east",
      `type` = SubnetEnums.Type.cloud,
      ipRange = Some("10.10.3.0/24")
    )
  )

  val kubeNetwork = CreateNetworkRequest(
    name = "global-kube-network",
    ipRange = "10.10.0.0/16",
    subnets = Some(kubeNetworkRegions),
    labels = Some(Map("environment" -> "production", "role" -> "kube-network"))
  )

  val masterLoadBalancerTarget = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.label_selector,
    ip = None,
    labelSelector = Some(LabelSelector("role=kube-master")),
    server = Some(
      ResourceId( /* ID of one of your Kubernetes master servers */ 123456)
    ),
    usePrivateIp = Some(true)
  )

  val masterLoadBalancerService6443 = LoadBalancerService(
    destinationPort = 6443,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 6443,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.tcp,
      retries = 3,
      timeout = 5,
      http = None
    ),
    listenPort = 6443,
    protocol = LoadBalancerServiceEnums.Protocol.tcp,
    proxyprotocol = false,
    http = None
  )
  val attachMasterLbToNetwork = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("10.10.1.10"),
    ipRange = Some("10.10.1.0/24")
  )

  val masterFirewall = CreateFirewallRequest(
    name = "kube-master-firewall",
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("6443"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow Kubernetes API traffic from VPC")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("22"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow SSH within the VPC")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          port = Some("9345"),
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow UDP traffic within the VPC")
        )
      )
    )
  )

  val redisVolume = CreateVolumeRequest(
    name = "redis-data-volume",
    size = 100, // 100 GB for Redis data
    automount = Some(true),
    format = Some("xfs"),
    labels = Some(
      Map(
        "role" -> "redis",
        "environment" -> "production",
        "purpose" -> "data-storage/cache"
      )
    ),
    location = Some("nbg1"),
    server = Some( /* ID of your Redis server */ 123456)
  )

  val postgresVolume = CreateVolumeRequest(
    name = "postgres-data-volume",
    size = 200, // 200 GB for Postgres data
    automount = Some(true),
    format = Some("xfs"),
    labels = Some(
      Map(
        "role" -> "postgres",
        "environment" -> "production",
        "purpose" -> "data-storage/database"
      )
    ),
    location = Some("nbg1"),
    server = Some( /* ID of your Postgres server */ 123456)
  )
  val k8sSubnet = Subnet(
    networkZone = "eu-central",
    `type` = SubnetEnums.Type.cloud,
    ipRange = Some("10.0.1.0/24")
  )

  val databaseSubnet = Subnet(
    networkZone = "eu-central",
    `type` = SubnetEnums.Type.cloud,
    ipRange = Some("10.0.2.0/24")
  )

  val k8sFirewall = CreateFirewallRequest(
    name = "k8s-nodes-firewall",
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("22"),
          sourceIps = Some(
            List("management-ip-1/32", "management-ip-2/32")
          ),
          description = Some("Allow SSH from management IPs only")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = None,
          sourceIps = Some(List("10.0.0.0/16")),
          description = Some("Allow all internal TCP traffic within the VPC")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("6443"),
          sourceIps = Some(
            List("management-ip-1/32", "management-ip-2/32")
          ),
          description =
            Some("Allow Kubernetes API access from management IPs only")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = None,
          sourceIps = Some(List("0.0.0.0/0")),
          description = Some("Allow all outbound TCP traffic")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          port = None,
          sourceIps = Some(List("0.0.0.0/0")),
          description = Some("Allow all outbound UDP traffic")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.icmp,
          sourceIps = Some(List("0.0.0.0/0")),
          description = Some("Allow all outbound ICMP traffic")
        )
      )
    )
  )

  val clusterFirewall = CreateFirewallRequest(
    name = "k8s-cluster-firewall",
    rules = Some(
      List(
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.icmp,
          sourceIps = Some(List("0.0.0.0/0")),
          description = Some("Allow ICMP from anywhere")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = None,
          sourceIps = Some(List("192.168.0.0/16")),
          description = Some("Allow all TCP within the cluster network")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.udp,
          port = None,
          sourceIps = Some(List("192.168.0.0/16")),
          description = Some("Allow all UDP within the cluster network")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("22"),
          sourceIps = Some(List("management-ip-1/32", "management-ip-2/32")),
          description = Some("Allow SSH from management IPs only")
        ),
        Rule(
          direction = RuleEnums.Direction.in,
          protocol = RuleEnums.Protocol.tcp,
          port = Some("30000-32767"),
          sourceIps = Some(List("0.0.0.0/0")),
          description = Some("Allow NodePorts from any")
        )
      )
    )
  )

//prefix for cloud resources
  val clusterName = "my-k8s-cluster"

// subject alternative names for the API Server signing cert.
  val apiServerAlternativeNames = List(
    "k8s-api.my-domain.com",
    "internal-k8s-api.my-domain.com"
  )

// List of SSH keys that will have access to the server. If none are given, existing SSH keys in Hetzner will be used.
  val sshKeys = List("my-ssh-key")

// Selector to use when automatically pulling existing SSH keys.
  val sshKeySelector = "role=admin"

// SSH port to be used to provision instances
  val sshPort = 22

// E2E tests specific variable to disable usage of any loadbalancer in front of kubeapi-server
  val disableKubeapiLoadbalancer = false

// IP range to use for private network
  val ipRange = "192.16.0.0/16"

//IP range to use for the cloud instances
  val ipRangeCloud = "192.168.0.0/17"

// List of IPs that are allowed to connect to the control instances
  val controlPlaneSourceIps = List.empty[String]

// Network zone to use for private network
  val networkZone = "eu-central"

  clients.HetznerClient.servers.listServers(
    "token",
    None,
    None,
    List.empty,
    List.empty
  )

  val privateNetwork3 = CreateNetworkRequest(
    name = "kubernetes-cluster",
    ipRange = "10.0.0.0/16"
  )

  val privateSubnet3 = Subnet(
    networkZone = "eu-central",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("10.0.1.0/24")
  )

//Set the Cluster CIDR to 10.244.0.0/16

  val webLoadBalancer = CreateLoadBalancerRequest(
    loadBalancerType = "lb11",
    name = "web-load-balancer",
    algorithm = Some(
      LoadBalancerAlgorithm(LoadBalancerAlgorithmEnums.Type.`round_robin`)
    ),
    labels = Some(
      Map("type" -> "web")
    ),
    location = Some("nbg1"),
    network = Some( /* ID of the private network created earlier */ 123456),
    networkZone = None,
    publicInterface = Some(true),
    services = Some(
      Seq(
        LoadBalancerService(
          destinationPort = 80,
          healthCheck = LoadBalancerServiceHealthCheck(
            interval = 10,
            port = 80,
            protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
            retries = 3,
            timeout = 10,
            http = Some(
              LoadBalancerServiceHealthCheckHttp(
                domain = "example.com",
                path = "/",
                response = None,
                statusCodes = Some(Seq("2??", "3??")),
                tls = Some(false)
              )
            )
          ),
          listenPort = 80,
          protocol = LoadBalancerServiceEnums.Protocol.http,
          proxyprotocol = false,
          http = None
        )
      )
    ),
    targets = Some(
      Seq(
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.`server`,
          ip = None,
          labelSelector = None,
          server = Some(
            ResourceId( /* ID of one of your web servers */ 123456)
          ),
          usePrivateIp = Some(false)
        ),
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.`server`,
          ip = None,
          labelSelector = None,
          server = Some(
            ResourceId( /* ID of one of your web servers */ 123456)
          ),
          usePrivateIp = Some(false)
        ),
        LoadBalancerAddTarget(
          `type` = LoadBalancerAddTargetEnums.Type.`server`,
          ip = None,
          labelSelector = None,
          server = Some(
            ResourceId( /* ID of one of your web servers */ 123456)
          ),
          usePrivateIp = Some(false)
        )
      )
    )
  )

  val webNetwork = CreateNetworkRequest(
    name = "web-private-network",
    ipRange = "10.10.0.0/16"
  )
  val webSubnet = Subnet(
    networkZone = "eu-central",
    `type` = SubnetEnums.Type.`cloud`,
    ipRange = Some("10.10.0.0/24")
  )

  val attachWebLbToNetwork = AttachLoadBalancerToNetworkRequest(
    network = 123456,
    ip = Some("10.10.0.1"),
    ipRange = Some("10.10.0.0/24")
  )

  val webLoadBalancerTarget1 = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.`server`,
    ip = None,
    labelSelector = None,
    server = Some(
      ResourceId( /* ID of one of your web servers */ 123456)
    ),
    usePrivateIp = Some(false)
  )

  val webLoadBalancerTarget2 = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.`server`,
    ip = None,
    labelSelector = None,
    server = Some(
      ResourceId( /* ID of one of your web servers */ 123456)
    ),
    usePrivateIp = Some(false)
  )

  val webLoadBalancerTarget3 = LoadBalancerAddTarget(
    `type` = LoadBalancerAddTargetEnums.Type.`server`,
    ip = None,
    labelSelector = None,
    server = Some(
      ResourceId( /* ID of one of your web servers */ 123456)
    ),
    usePrivateIp = Some(false)
  )

  val webLoadBalancerService = LoadBalancerService(
    destinationPort = 80,
    healthCheck = LoadBalancerServiceHealthCheck(
      interval = 10,
      port = 80,
      protocol = LoadBalancerServiceHealthCheckEnums.Protocol.http,
      retries = 3,
      timeout = 10,
      http = Some(
        LoadBalancerServiceHealthCheckHttp(
          domain = "example.com",
          path = "/",
          response = None,
          statusCodes = Some(Seq("2??", "3??")),
          tls = Some(false)
        )
      )
    ),
    listenPort = 80,
    protocol = LoadBalancerServiceEnums.Protocol.http,
    proxyprotocol = false,
    http = Some(
      Http(
        certificates = None,
        cookieLifetime = None,
        cookieName = None,
        redirectHttp = Some(false),
        stickySessions = Some(false)
      )
    )
  )

  val webServer01 = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "web-server-01",
    serverType = "cx11",
    automount = Some(true),
    datacenter = None,
    firewalls = None,
    labels = Some(
      Map(
        "type" -> "web"
      )
    ),
    location = Some("nbg1"),
    networks = Some(List(123456)),
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = None,
    volumes = Some(List(123456))
  )

  val webServer02 = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "web-server-02",
    serverType = "cx11",
    automount = Some(true),
    datacenter = None,
    firewalls = None,
    labels = Some(
      Map(
        "type" -> "web"
      )
    ),
    location = Some("nbg1"),
    networks = Some(List(123456)),
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = None,
    volumes = Some(List(123456))
  )

  val webServer03 = CreateServerRequest(
    image = "ubuntu-24.04",
    name = "web-server-03",
    serverType = "cx11",
    automount = Some(true),
    datacenter = None,
    firewalls = None,
    labels = Some(
      Map(
        "type" -> "web"
      )
    ),
    location = Some("nbg1"),
    networks = Some(List(123456)),
    placementGroup = None,
    publicNet = Some(
      CreateServerRequestPublicNet(
        enableIpv4 = Some(true),
        enableIpv6 = Some(false),
        ipv4 = None,
        ipv6 = None
      )
    ),
    sshKeys = Some(List("my-ssh-key")),
    startAfterCreate = Some(true),
    userData = None,
    volumes = Some(List(123456))
  )

  val webServerVolume01 = CreateVolumeRequest(
    name = "web-server-volume-01",
    size = 20,
    automount = Some(true),
    format = Some("xfs"),
    labels = Some(
      Map(
        "type" -> "web"
      )
    ),
    location = Some("nbg1"),
    server = Some( /* ID of your web server 01 */ 123456)
  )

  val webServerVolume02 = CreateVolumeRequest(
    name = "web-server-volume-02",
    size = 20,
    automount = Some(true),
    format = Some("xfs"),
    labels = Some(
      Map(
        "type" -> "web"
      )
    ),
    location = Some("nbg1"),
    server = Some( /* ID of your web server 02 */ 123456)
  )

  val webServerVolume03 = CreateVolumeRequest(
    name = "web-server-volume-03",
    size = 20,
    automount = Some(true),
    format = Some("xfs"),
    labels = Some(
      Map(
        "type" -> "web"
      )
    ),
    location = Some("nbg1"),
    server = Some( /* ID of your web server 03 */ 123456)
  )

}
