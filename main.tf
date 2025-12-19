
terraform {
  required_providers {
    hcloud = {
      source  = "hetznercloud/hcloud"
      version = "~> 1.31"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "2.14.0"
    }
  }
}


locals {
  servers = ["server0", "server1", "server2"]
}

resource "hcloud_network" "vpc" {
  name     = "K8s Network"
  ip_range = "10.10.0.0/16"
}

resource "hcloud_network_subnet" "subnet" {
  network_id   = hcloud_network.vpc.id
  type         = "cloud"
  network_zone = "eu-central"
  ip_range     = "10.10.0.0/24"
}

resource "hcloud_server" "server" {
  for_each    = toset(local.servers)
  name        = each.key
  # Image for Ubuntu 22.04
  image       = "67794396"
  # Falkenberg data center
  location    = "fsn1"
  # 4 vCPU, 8GB Ram, 16 euro monthly (2023-11)
  server_type = "cx31"
  network {
    network_id = hcloud_network.vpc.id
    ip         = "10.10.0.${index(local.servers, each.value) + 5}"
  }
  labels = {
    instance = each.key
  }
  # Use cloud-config to set up a user with our SSH key.
  # We'll use said SSH key to log in to the server via SSH and provision k3s.
  user_data = <<EOT
#cloud-config
package_upgrade: true
users:
  - default
  - name: ubuntu
    sudo: ALL=(ALL) NOPASSWD:ALL
    ssh_authorized_keys:
      - ${file("./ssh_key.pub")}
EOT

  # The image forces changes, so ignore it
  # Also ignore any changes to user_data as we want to manually tear the cluster down.
  lifecycle {
    ignore_changes = [
      user_data, image
    ]
  }
}

output "server_ips" {
  value = [
    for server in hcloud_server.server : server.ipv4_address
  ]
}


