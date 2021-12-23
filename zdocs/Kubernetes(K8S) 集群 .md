# Kubernetes架构

- Pod：K8s最小部署单元，一组容器的集合
- Deployment：最常见的控制器，用于更高级别部署和管理Pod。上线部署、升级/回滚
- Service：为一组Pod提供负载均衡，对外提供统一访问入口。共三中类型：
    - 类型一：ClusterIP：只能在集群内部节点之间相互访问。外围不能访问。
    - 类型二：NodePort： 对外暴露端口30000-32767，需要每个节点都暴露端口，且每个节点外外网都可访问，不安全。
    - 类型三：LoadBalancer：
        - 与NodePort类似，但补足NodePort类型的不足
        - 适配云服务器！云服务器推荐采用类型！
        - 会自动请求云平台底层的负载均衡器，将[NodeIP]:[NodePort]作为后端加入进去，自动集群。


- Label ：标签，附加到某个资源上，用于关联对象、查询和筛选

# Kubernetes部署环境准备

## 说明！！以下全部操作未特别说明的情况下 ，需要在全部节点上执行对应命令！

```shell
 # 0. 关闭防火墙。(阿里云默认关闭,可先查看：firewall-cmd --state)
systemctl stop firewalld
systemctl disable firewalld

# 0. 关闭selinux。(阿里云默认关闭,可先查看：getenforce)
sed -i 's/enforcing/disabled/' /etc/selinux/config  #永久

# 0. 关闭swap。(阿里云默认关闭,可先查看：free)
sed -ri 's/.*swap.*/#&/' /etc/fstab #永久

# 1. 在master添加hosts。(内网ip 别名；机器之间可访问。)
# 可以通过命令永久修改机器名：hostnamectl set-hostname k8s-master。执行完再输入bash

cat >> /etc/hosts << EOF
172.29.33.23 k8s-master
172.29.33.24 k8s-node1
EOF
# 设置网桥参数。将IPv4流量传递到iptables的链，防止丢包（master执行）。
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sysctl --system  #生效
# 时间同步
yum install ntpdate -y
ntpdate time.windows.com
``` 

# Kubernetes安装具体步骤

## 安装docker

### 0. —> [参考](Docker%20基础.md)

## 搭建：kubeadm、kubelet、kubectl

### 0. 添加k8s的阿里云YUM源

```shell
cat > /etc/yum.repos.d/kubernetes.repo << EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
```

### 1. 安装 kubeadm，kubelet 和 kubectl

```shell
# 安装
yum install -y kubelet-1.20.0 kubeadm-1.20.0 kubectl-1.20.0
systemctl enable kubelet

# 开机启动
systemctl enable kubelet.service
```

```shell
# 查看有没有安装：
yum list installed | grep kubelet
yum list installed | grep kubeadm
yum list installed | grep kubectl

# 查看安装的版本： kubelet --version
# Kubelet：运行在cluster所有节点上，负责启动POD和容器；
# Kubeadm：用于初始化cluster的一个工具；
# Kubectl：kubectl是kubenetes命令行工具，通过kubectl可以部署和管理应用，查看各种资源，创建，删除和更新组件；

```

### 2. 部署Kubernetes Master主节点。（此命令在master执行！！！）

```shell  
# 部署master
# apiserver-advertise-address: master 的 ip
# service-cidr、pod-network-cidr 集群内部通讯地址。可以不用修改

kubeadm init \
  --apiserver-advertise-address=172.29.33.23 \
  --image-repository registry.aliyuncs.com/google_containers \
  --kubernetes-version v1.20.0 \
  --service-cidr=10.96.0.0/12 \
  --pod-network-cidr=10.244.0.0/16 \
  --ignore-preflight-errors=all

# 初始化完成后，保存一些输出内容,后续会用到：
kubeadm join 172.29.33.23:6443 --token hs5hw0.suwi21u78nd34fdm \
    --discovery-token-ca-cert-hash sha256:dc28f258da203aa1a722113c3f923c54be498dcea8629b8bdaecf5e287e77662 
#token有效期24小时，过期使用如下（master）：
kubeadm token create --print-join-command

# 在master上执行：
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

# 查看节点
kubectl get nodes
```

### 3. 将node节点加入master中，在node机器上执行:

```shell
# 向集群添加新节点，执行的命令就是kubeadm init最后输出的kubeadm join命令：
kubeadm join 172.29.33.23:6443 --token 1gux0r.128l5lljftq3mjf1 \
    --discovery-token-ca-cert-hash sha256:b917ae3bce7fbabd86525dbe059d5a5eabfb5df94f9bc8b421a0e79fb659138f
```

### 4. 链接master 和 node;配置网络插件，有3种+。这里使用Calico插件

```shell
# 在master上下载部署文件：wget https://docs.projectcalico.org/manifests/calico.yaml
# 修改calico.yaml文件中的内容，calue为kubeadm init命令初始化的值：pod-network-cidr
    - name: CALICO_IPV4POOL_CIDR
    value: "10.244.0.0/16" 
    
# 执行配置  
kubectl apply -f kube-flannel.yml # 在master机器，当前目录下执行

# 等待pods全部running,可执行查看：
kubectl get pods -n kube-system

# 再master上查看全部节点是否ready：
kubectl get nodes

```

## 部署完成！！！！

```shell
# 测试kubernetes集群
#在Kubernetes集群中创建一个pod，验证是否正常运行：

kubectl create deployment nginx --image=nginx
kubectl expose deployment nginx --port=80 --type=NodePort
kubectl get pod,svc

#访问地址：http://NodeIP:新生成的端口号 
```

# kubernetes重装

```shell
#重置服务
kubeadm reset

#删除残留文件
rm -rf /etc/kubernetes
rm -rf /var/lib/etcd/
rm -rf $HOME/.kube

#重启系统
reboot
```

# kubernetes重装部署 Dashboard / UI / 管理工具 / 管理后台

```shell
# 1. Dashboard是官方提供的一个UI，可用于基本管理K8s资源(master执行)。
wget http://raw.githubusercontent.com/kubernetes/dashboard/v2.0.3/aio/deploy/recommended.yaml

# 2. 默认Dashboard只能集群内部访问，修改recommended.yaml的Service为NodePort类型，暴露到外部：
vi recommended.yaml
...
kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
spec:
  ports:
    - port: 443
      targetPort: 8443
      nodePort: 30001
  selector:
    k8s-app: kubernetes-dashboard
  type: NodePort
... 

# 3. 启动Dashboard
kubectl apply -f recommended.yaml

# 4. 查看进度等待安装完成
kubectl get pods -n kubernetes-dashboard
#访问地址：https://NodeIP:30001。 是https!!!

# 5. dashboard

# 创建service account并绑定默认cluster-admin管理员集群角色：
# 创建用户
kubectl create serviceaccount dashboard-admin -n kube-system
# 用户授权
kubectl create clusterrolebinding dashboard-admin --clusterrole=cluster-admin --serviceaccount=kube-system:dashboard-admin
# 获取用户Token
kubectl describe secrets -n kube-system $(kubectl -n kube-system get secret | awk '/dashboard-admin/{print $1}')
# 使用输出的token登录Dashboard。

```

# #kubernetes常用命令

```shell

# 查看全部pod。默认default命名空间下
kubectl get pods

# 查看全部deploy。默认default命名空间下
kubectl get deploy

# 查看全部service。。默认default命名空间下。有点类似：docker ps 
kubectl get service

# 以上三个查看可以写成一条。默认default命名空间下
kubectl get pods,deploy,svc

# 删除指定deploy,会自动删除下面的全部pods
kubectl delete deploy <deploy-name>

# 1. 查看pod日志
kubectl logs <pod-name>



# 3. 查看service关联的pod。（可以看出，请求是通过api-service负载均衡到下面的全部pod的）
kubectl get endpoints
```

# 应用下线 OR 删除

```shell
# 第一种：应用下线 OR 删除
kubectl delete deploy <deploy-name>
kubectl delete svc <service-name>

# 第二种：如果是通过yaml文件部署的，直接删除yaml可以全部删除：
kubectl delete -f xxx.yaml
```

# 网站部署

[部署yaml文件参考](app-deploy.yaml)

```shell
#-------
# 前提说明
# k8s部署配置文件中：imagePullPolicy: Never
# 表示镜像来源，IfNotPresent本地没有就从hub仓库拉取，Never表示只从本地
# 本地的意思：全部节点上都要提前部署好对应镜像
# 否则报错类似：ErrImageNeverPull
# 远程下载：一般搭建私仓。pom里添加插件dockerfile-maven-plugin。idea即可自动推送到私仓。部署时，节点自动拉取构建镜像


```

## 1. 测试部署一个nginx

```shell
# 使用Deployment控制器部署镜像：
# replicas：自动生成的pod的数量，完全一样，自带负载均衡
kubectl create deployment web --image=nginx --replicas=3
kubectl get deploy,pods
#使用Service将Pod暴露出去：
kubectl expose deployment web --port=80 --target-port=80 --type=NodePort
kubectl get service
#浏览器访问应用：
http://NodeIP:Port # 端口随机生成，通过get svc获取

# 查看全部pod
kubectl get pods

# 查看pod日志
kubectl logs <pod-name>
```

# 动态扩容

```shell
# 第一种方式，零时扩容：
kubectl scale deploy <deploy-name> --replicas=4
# 查看创建状态
kubectl get pods 
# 动态查看扩容挂载
# -w:监听，可实时看到新扩容的实例加入进来。service和pod的挂载关系
kubectl get ep -w
# 第二种方式，永久扩容：
# 修改yaml里replicas，然后apply -f
```