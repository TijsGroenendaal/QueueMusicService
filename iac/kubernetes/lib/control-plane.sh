#!/bin/sh

/bin/sh /tmp/scripts/lib/common.sh

kubeadm init --pod-network-cidr=10.244.0.0/16

export KUBECONFIG=/etc/kubernetes/admin.conf

kubectl apply -f https://github.com/flannel-io/flannel/releases/latest/download/kube-flannel.yml

kubectl apply --server-side -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.4.1/standard-install.yaml

wget https://github.com/derailed/k9s/releases/download/v0.50.18/k9s_Linux_arm64.tar.gz
tar Cxzvf /usr/local/bin k9s_Linux_arm64.tar.gz