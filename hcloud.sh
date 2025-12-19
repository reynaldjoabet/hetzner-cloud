set -o errexit
set -o nounset
set -o pipefail



hcloud_start() {
    echo "Staring Hetzner cloud-config: "
    # Ensure that we clear SSHD host keys
    rm -f /etc/ssh/ssh_host_*_key /etc/ssh/ssh_host_*_key.pub
    # Ensure /var/hcloud directory is clear
    rm -rf /var/hcloud
    mkdir -m 700 /var/hcloud
    # Change directory to /var/hcloud
    cd /var/hcloud
    # Run cloud-config
    /usr/local/bin/hcloud 2>&1 | tee hcloud.$(date +%Y%m%d-%H%M%S).log
    echo "cloud-config done"
}


install_hcloud() {
    if [[ "${OSTYPE}" == "linux"* ]]; then
      curl -fsSL https://github.com/hetznercloud/cli/releases/download/v${MINIMUM_HCLOUD_VERSION}/hcloud-linux-amd64.tar.gz | tar -xzv hcloud
      copy_binary
    elif [[ "$OSTYPE" == "darwin"* ]]; then
      curl -fsSL https://github.com/hetznercloud/cli/releases/download/v${MINIMUM_HCLOUD_VERSION}/hcloud-macos-amd64.tar.gz | tar -xzv hcloud
      copy_binary
    else
      set +x
      echo "The installer does not work for your platform: $OSTYPE"
      exit 1
    fi
}


function copy_binary() {
  if [[ ":$PATH:" == *":$HOME/.local/bin:"* ]]; then
      if [ ! -d "$HOME/.local/bin" ]; then
        mkdir -p "$HOME/.local/bin"
      fi
      mv hcloud "$HOME/.local/bin/hcloud"
      chmod +x "$HOME/.local/bin/hcloud"
  else
      echo "Installing HCLOUD to /usr/local/bin which is write protected"
      echo "If you'd prefer to install HCLOUD without sudo permissions, add \$HOME/.local/bin to your \$PATH and rerun the installer"
      sudo mv hcloud /usr/local/bin/hcloud
      chmod +x "/usr/local/bin/hcloud"
  fi
  echo "Installation Finished"
}



install_helm() {
    if ! [ -d "${PATH_BIN}" ]; then
        mkdir -p "${PATH_BIN}"
    fi
    curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash
    echo "Done"
}

install_kubectl() {
    if [[ "${OSTYPE}" == "linux"* ]]; then
      curl -sLo "kubectl" https://storage.googleapis.com/kubernetes-release/release/${MINIMUM_KUBECTL_VERSION}/bin/linux/amd64/kubectl
      copy_binary
    elif [[ "$OSTYPE" == "darwin"* ]]; then
      curl -sLo "kubectl" https://storage.googleapis.com/kubernetes-release/release/${MINIMUM_KUBECTL_VERSION}/bin/darwin/amd64/kubectl
      copy_binary
    else
      set +x
      echo "The installer does not work for your platform: $OSTYPE"
      exit 1
    fi

}

function copy_binary3() {
  if [[ ":$PATH:" == *":$HOME/.local/bin:"* ]]; then
      if [ ! -d "$HOME/.local/bin" ]; then
        mkdir -p "$HOME/.local/bin"
      fi
      mv kubectl "$HOME/.local/bin/kubectl"
      chmod +x "$HOME/.local/bin/kubectl"
  else
      echo "Installing Kubectl to /usr/local/bin which is write protected"
      echo "If you'd prefer to install Kubectl without sudo permissions, add \$HOME/.local/bin to your \$PATH and rerun the installer"
      sudo mv kubectl /usr/local/bin/kubectl
      chmod +x "/usr/local/bin/kubectl"
  fi
  echo "Installation Finished"
}

hcloud2ssh() {
    rm -f ~/.ssh/config.d/hcloud_*
    hcloud server list -o columns=name,ipv4 | tail -n +2 | while read LINE
    do
        SERVER_NAME=$(echo $LINE | awk '{print $1}')
        SERVER_IP=$(echo $LINE | awk '{print $2}')

        echo "Adding SSH configuration for <${SERVER_NAME}> at <${SERVER_IP}>"

        cat > ~/.ssh/config.d/hcloud_${SERVER_NAME} <<EOF
Host ${SERVER_NAME} ${SERVER_IP}
    HostName ${SERVER_IP}
    User root
    StrictHostKeyChecking no
    UserKnownHostsFile /dev/null
EOF
        chmod 0640 ~/.ssh/config.d/hcloud_${SERVER_NAME}
    done
}


#!/bin/bash

# bash init
set -e

# default values
HETZNER_INSTANCE_TYPE=cx21
HETZNER_WORKER_COUNT=0

# option parsing
usage() {
	cat <<-"EOF"
	usage: ./hcloud_kube [<options>]

		-t	type of hetzner node
		-n	number of worker nodes
	EOF
}
while getopts "t:n:h" option; do
	case ${option} in
	t)
		HETZNER_INSTANCE_TYPE=${OPTARG}
		;;
	n)
		HETZNER_WORKER_COUNT=${OPTARG}
		;;
	h)
		usage
		exit 1
		;;
	\?)
		exit 1
		;;
	esac
done
shift $((OPTIND-1))

# require an HCLOUD_TOKEN
if [ -z "$HCLOUD_TOKEN" ]; then
	echo '$HCLOUD_TOKEN is not set'
	exit 1
fi

# save source directory
BASE=$(dirname $(realpath $0))

# create working directory
rm -rf /tmp/hcloud_kube
mkdir -p /tmp/hcloud_kube
cd /tmp/hcloud_kube

# create ssh key
ssh-keygen -t ed25519 -C hcloud-provision-key -f provision_key -N "" -q
hcloud ssh-key create --name hcloud-provision-key --public-key-from-file provision_key.pub

hcloud_ssh() {
	IP=$(hcloud server ip "$1")
	ssh -o StrictHostKeyChecking=no -o ConnectionAttempts=60 -i provision_key root@${IP} "bash -s"
}

# setup network
hcloud network create --name kubernetes --ip-range 10.98.0.0/16
hcloud network add-subnet kubernetes --network-zone eu-central --type server --ip-range 10.98.0.0/16

# create master node
hcloud server create \
	--type $HETZNER_INSTANCE_TYPE \
	--name master-1 \
	--image ubuntu-20.04 \
	--ssh-key hcloud-provision-key \
	--network kubernetes \
	--user-data-from-file $BASE/cloud-init.yml

# start kubernetes
hcloud_ssh master-1 <<"EOF"
cloud-init status -w

IP=$(ip addr | grep -P 'inet.*(ens\\d+|enp\\d+s0)' | head -n 1 | awk '{ print $4 }')
kubeadm init \
	--pod-network-cidr=10.244.0.0/16 \
	--ignore-preflight-errors=NumCPU \
	--apiserver-cert-extra-sans=$IP
EOF

# copy kubernetes config to local
IP=$(hcloud server ip "master-1")
scp -o StrictHostKeyChecking=no -i provision_key root@${IP}:/etc/kubernetes/admin.conf ./admin.conf
export KUBECONFIG=./admin.conf

# secrets for kube
kubectl -n kube-system create secret generic hcloud \
	--from-literal="token=$HCLOUD_TOKEN" \
	--from-literal=network=kubernetes
kubectl -n kube-system create secret generic hcloud-csi \
	--from-literal="token=$HCLOUD_TOKEN"

# cloud controller manager
kubectl apply -f https://raw.githubusercontent.com/hetznercloud/hcloud-cloud-controller-manager/master/deploy/v1.6.1-networks.yaml

# cluster networking
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml

# tolerate taints
kubectl -n kube-system patch daemonset kube-flannel-ds-amd64 --type json -p '[{"op":"add","path":"/spec/template/spec/tolerations/-","value":{"key":"node.cloudprovider.kubernetes.io/uninitialized","value":"true","effect":"NoSchedule"}}]'
kubectl -n kube-system patch deployment coredns --type json -p '[{"op":"add","path":"/spec/template/spec/tolerations/-","value":{"key":"node.cloudprovider.kubernetes.io/uninitialized","value":"true","effect":"NoSchedule"}}]'
if [[ "$HETZNER_WORKER_COUNT" -eq 0 ]]; then
	kubectl taint nodes --all node-role.kubernetes.io/master-
fi

# cloud container storage
kubectl apply -f https://raw.githubusercontent.com/kubernetes/csi-api/release-1.14/pkg/crd/manifests/csidriver.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/csi-api/release-1.14/pkg/crd/manifests/csinodeinfo.yaml
kubectl apply -f https://raw.githubusercontent.com/hetznercloud/csi-driver/master/deploy/kubernetes/hcloud-csi.yml

# kured
kubectl apply -f https://github.com/weaveworks/kured/releases/download/1.3.0/kured-1.3.0-dockerhub.yaml

# get worker join script
hcloud_ssh master-1 <<"EOF" > ./join_command.sh
kubeadm token create --print-join-command
EOF

for (( i = 1; i <= $HETZNER_WORKER_COUNT; i++ )); do
	# create worker
	hcloud server create \
		--type $HETZNER_INSTANCE_TYPE \
		--name worker-${i} \
		--image ubuntu-20.04 \
		--ssh-key hcloud-provision-key \
		--network kubernetes \
		--user-data-from-file $BASE/cloud-init.yml

	# join worker
	(echo "cloud-init status -w"; cat ./join_command.sh) | hcloud_ssh worker-${i} &
done
wait