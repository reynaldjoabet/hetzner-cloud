import hcloud.models.*
import hcloud.JsonSupport.given
import com.github.plokhotnyuk.jsoniter_scala.core.*

val createServerRequestJson = """{

  "name": "my-server",

  "location": "nbg1",

  "datacenter": "nbg1-dc3",

  "server_type": "cpx22",

  "start_after_create": true,

  "image": "ubuntu-24.04",

  "placement_group": 1,

  "ssh_keys": [

    "my-ssh-key"

  ],

  "volumes": [

    123

  ],

  "networks": [

    456

  ],

  "firewalls": [

    {

      "firewall": 38

    }

  ],

  "user_data": "#cloud-config\nruncmd:\n- [touch, /root/cloud-init-worked]\n",

  "labels": {

    "environment": "prod",

    "example.com/my": "label",

    "just-a-key": ""

  },

  "automount": false,

  "public_net": {

    "enable_ipv4": false,

    "enable_ipv6": false,

    "ipv4": null,

    "ipv6": null

  }

}""".stripMargin

val createServerRequest =
  readFromString[CreateServerRequest](createServerRequestJson)

val createLoadBalancerRequestJson = """{
"name": "Web Frontend",

  "load_balancer_type": "lb11",

  "algorithm": {

    "type": "round_robin"

  },

  "services": [

    {

      "protocol": "https",

      "listen_port": 443,

      "destination_port": 80,

      "proxyprotocol": false,

      "health_check": {

        "protocol": "http",

        "port": 4711,

        "interval": 15,

        "timeout": 10,

        "retries": 3,

        "http": {

          "domain": "example.com",

          "path": "/",

          "response": "{\"status\": \"ok\"}",

          "status_codes": [

            "2??",

            "3??"

          ],

          "tls": false

        }

      },

      "http": {

        "cookie_name": "HCLBSTICKY",

        "cookie_lifetime": 300,

        "certificates": [

          897

        ],

        "redirect_http": true,

        "sticky_sessions": true

      }

    }

  ],

  "targets": [

    {

      "type": "server",

      "server": {

        "id": 80

      },

      "use_private_ip": true,

      "label_selector": {

        "selector": "env=prod"

      },

      "ip": {

        "ip": "203.0.113.1"

      }

    }

  ],

  "labels": {

    "environment": "prod",

    "example.com/my": "label",

    "just-a-key": ""

  },

  "public_interface": true,

  "network": 123,

  "network_zone": "eu-central",

  "location": "fsn1"

}""".stripMargin

val createLoadBalancerRequest =
  readFromString[CreateLoadBalancerRequest](createLoadBalancerRequestJson)

val CreateNetworkRequestJson = """{

  "name": "mynet",

  "ip_range": "10.0.0.0/16",

  "labels": {

    "environment": "prod",

    "example.com/my": "label",

    "just-a-key": ""

  },

  "subnets": [

    {

      "type": "cloud",

      "ip_range": "10.0.1.0/24",

      "network_zone": "eu-central",

      "vswitch_id": 1000

    }

  ],

  "routes": [

    {

      "destination": "10.100.1.0/24",

      "gateway": "10.0.1.1"

    }

  ],

  "expose_routes_to_vswitch": false

}""".stripMargin

val createNetworkRequest =
  readFromString[CreateNetworkRequest](CreateNetworkRequestJson)

val createVolumeRequestJson = """{

  "size": 42,

  "name": "test-database",

  "labels": {

    "key": "value"

  },

  "location": "nbg1",

  "automount": false,

  "format": "xfs"

}""".stripMargin

val createVolumeRequest =
  readFromString[CreateVolumeRequest](createVolumeRequestJson)

val createPrimaryZoneJson = """{
  "name": "example.com",

  "mode": "primary",

  "ttl": 10800,

  "labels": {

    "key": "value"

  },

  "rrsets": [

    {

      "name": "www",

      "type": "A",

      "records": [

        {

          "value": "198.51.100.1",

          "comment": "My web server at Hetzner Cloud."

        }

      ]

    }

  ]

}""".stripMargin

val createPrimaryZoneRequest =
  readFromString[CreateZoneRequest](createPrimaryZoneJson)

val createSecondaryZoneJson = """{

  "name": "example.com",

  "mode": "secondary",

  "ttl": 10800,

  "labels": {

    "key": "value"

  },

  "primary_nameservers": [

    {

      "address": "198.51.100.1",

      "port": 53

    },

    {

      "address": "203.0.113.1",

      "port": 53

    }

  ]

}""".stripMargin

val createSecondaryZoneRequest =
  readFromString[CreateZoneRequest](createSecondaryZoneJson)

val createFirewallRequestJon = """{

  "name": "Corporate Intranet Protection",

  "labels": {

    "key": "value"

  },

  "rules": [

    {

      "description": "Allow port 80",

      "direction": "in",

      "source_ips": [

        "192.0.2.2/32",

        "192.0.2.0/24",

        "2001:0db8:9a3b:ee58:5ca:990c:8bc9:c03b/128"

      ],

      "protocol": "tcp",

      "port": "80"

    }

  ],

  "apply_to": [

    {

      "type": "server",

      "server": {

        "id": 42

      }

    }

  ]

}""".stripMargin

val createFirewallRequest =
  readFromString[CreateFirewallRequest](createFirewallRequestJon)

val createCertificateRequestJson = """{

  "name": "my website cert",

  "type": "uploaded",

  "certificate": "-----BEGIN CERTIFICATE-----\n...",

  "private_key": "-----BEGIN PRIVATE KEY-----\n..."

}""".stripMargin

val createCertificateRequest =
  readFromString[CreateCertificateRequest](createCertificateRequestJson)

val createManagedCertificateRequestJson = """{

  "name": "my website cert",

  "type": "managed",

  "domain_names": [

    "example.com",

    "webmail.example.com",

    "www.example.com"

  ]

}
""".stripMargin

val createManagedCertificateRequest =
  readFromString[CreateCertificateRequest](createManagedCertificateRequestJson)

val createSSHKeyRequestJson = """{

  "name": "My ssh key",

  "public_key": "ssh-rsa AAAjjk76kgf...Xt",

  "labels": {

    "environment": "prod",

    "example.com/my": "label",

    "just-a-key": ""

  }

}""".stripMargin

val createSSHKeyRequest =
  readFromString[CreateSshKeyRequest](createSSHKeyRequestJson)

val updateSSHKeyRequestJson = """{

  "name": "My ssh key",

  "labels": {

    "environment": "prod",

    "example.com/my": "label",

    "just-a-key": ""

  }

}
""".stripMargin

val updateSSHKeyRequest =
  readFromString[ReplaceSshKeyRequest](updateSSHKeyRequestJson)
