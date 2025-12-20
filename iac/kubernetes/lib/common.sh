# Chapter 1: Install Kubernetes CLI tools

apt-get update
apt-get install -y apt-transport-https ca-certificates curl gpg
curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.34/deb/Release.key | gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg
echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.34/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list
apt-get update
apt-get install -y kubelet kubeadm kubectl
apt-mark hold kubelet kubeadm kubectl

# Chapter 2: Container Runtime

cat <<EOF | tee /etc/sysctl.d/k8s.conf
net.ipv4.ip_forward = 1
EOF

modprobe br_netfilter
echo 1 | sudo tee /proc/sys/net/bridge/bridge-nf-call-iptables

sysctl --system

wget https://github.com/containerd/containerd/releases/download/v2.2.0/containerd-2.2.0-linux-arm64.tar.gz
tar Cxzvf /usr/local containerd-2.2.0-linux-arm64.tar.gz

# Chapter 2.1: Configure containerd systemd service
wget https://raw.githubusercontent.com/containerd/containerd/main/containerd.service
mkdir -p /usr/local/lib/systemd/system
mv containerd.service /usr/local/lib/systemd/system/containerd.service

systemctl daemon-reload
systemctl enable --now containerd

# Chapter 2.2: Configure runc
wget https://github.com/opencontainers/runc/releases/download/v1.4.0/runc.arm64
install -m 755 runc.arm64 /usr/local/sbin/runc

# Chapter 2.3: Configure CNI plugins
wget https://github.com/containernetworking/plugins/releases/download/v1.9.0/cni-plugins-linux-arm64-v1.9.0.tgz
mkdir -p /opt/cni/bin
tar Cxzvf /opt/cni/bin cni-plugins-linux-arm64-v1.9.0.tgz

# Chapter 2.4: Configure systemd cgroup driver for containerd
mkdir -p /etc/containerd
containerd config default > /etc/containerd/config.toml

sed -i 's/SystemdCgroup = false/SystemdCgroup = true/' /etc/containerd/config.toml
sudo systemctl restart containerd