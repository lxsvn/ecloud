#使用 percona-xtradb-cluster 搭建mysql集群
相对于多主多从，xtradb集群每个节点都可读写
1. 强一致性、无同步延迟
2. 写入效率取决于节点中最慢的一台

```shell
mkdir -p /home/percona-xtradb-cluster8/mysql/{cert,config,data}

chmod -R 777 /home/percona-xtradb-cluster8/mysql/data

```
1.创建ca证书
```shell

cd /home/percona-xtradb-cluster8/mysql/cert


# 使用openssl创建CA证书
openssl genrsa 2048 > ca-key.pem
openssl req -new -x509 -nodes -days 3600 \
        -key ca-key.pem -out ca.pem
#这里让填写一些基本资料
# Country Name (2 letter code) [AU]:CN
# State or Province Name (full name) [Some-State]:.
# Locality Name (eg, city) []:
# Organization Name (eg, company) [Internet Widgits Pty Ltd]:MySQL AB
# Organizational Unit Name (eg, section) []:
# Common Name (eg, YOUR name) []:MySQL admin
# Email Address []:

#创建服务器证书
# server-cert.pem = 公钥, server-key.pem = 私钥
openssl req -newkey rsa:2048 -days 3600 \
        -nodes -keyout server-key.pem -out server-req.pem
#这里让填写一些基本资料
# Country Name (2 letter code) [AU]:CN
# State or Province Name (full name) [Some-State]:.
# Locality Name (eg, city) []:
# Organization Name (eg, company) [Internet Widgits Pty Ltd]:MySQL AB
# Organizational Unit Name (eg, section) []:
# Common Name (eg, YOUR name) []:MySQL server
# Email Address []:
#
# Please enter the following 'extra' attributes
# to be sent with your certificate request
# A challenge password []:
# An optional company name []:

openssl rsa -in server-key.pem -out server-key.pem
openssl x509 -req -in server-req.pem -days 3600 \
        -CA ca.pem -CAkey ca-key.pem -set_serial 01 -out server-cert.pem

# 创建客户端证书
# client-cert.pem = 公钥, client-key.pem = 私钥
openssl req -newkey rsa:2048 -days 3600 \
        -nodes -keyout client-key.pem -out client-req.pem
#这里让填写一些基本资料
# Country Name (2 letter code) [AU]:CN
# State or Province Name (full name) [Some-State]:.
# Locality Name (eg, city) []:
# Organization Name (eg, company) [Internet Widgits Pty Ltd]:MySQL AB
# Organizational Unit Name (eg, section) []:
# Common Name (eg, YOUR name) []:MySQL server
# Email Address []:
#
# Please enter the following 'extra' attributes
# to be sent with your certificate request
# A challenge password []:
# An optional company name []:
openssl rsa -in client-key.pem -out client-key.pem
openssl x509 -req -in client-req.pem -days 3600 \
        -CA ca.pem -CAkey ca-key.pem -set_serial 01 -out client-cert.pem

```

2.创建完证书后可使用一下命令验证
```shell
openssl verify -CAfile ca.pem server-cert.pem client-cert.pem
#如果显示以下内容表示一切么问题
server-cert.pem: OK
client-cert.pem: OK
```

3.创建cert.cnf文件
```shell
ssl-ca = /cert/ca.pem
ssl-cert = /cert/server-cert.pem
ssl-key = /cert/server-key.pem

[client]
ssl-ca = /cert/ca.pem
ssl-cert = /cert/client-cert.pem
ssl-key = /cert/client-key.pem

[sst]
encrypt = 4
ssl-ca = /cert/ca.pem
ssl-cert = /cert/server-cert.pem
ssl-key = /cert/server-key.pem

```

4.创建网段
```shell
docker network create mysql-pxc-network-cluster --subnet=161.80.0.0/16
```

# docker-compose.yml 
```yaml
version: '3.6'
services:
 mysql-pxc1:
  restart: always
  image: percona/percona-xtradb-cluster:8.0
  container_name: mysql-pxc1
  networks:
    default:
      ipv4_address: 172.80.0.1
  environment: # 环境变量
      - TZ=Asia/Shanghai
      - MYSQL_ROOT_PASSWORD=123456
      - CLUSTER_NAME=pxc
  ports:
    - "3306:3306"
    - "4444:4444"
    - "4567:4567"
    - "4568:4568"
  volumes:
   - /home/percona-xtradb-cluster8/mysql/data:/var/lib/mysql/
   - /home/percona-xtradb-cluster8/mysql/cert:/cert/
   - /home/percona-xtradb-cluster8/mysql/config:/etc/percona-xtradb-cluster.conf.d
  tty: true
  privileged: true # 拥有容器内命令执行的权限
 
 mysql-pxc2:
  restart: always
  image: percona/percona-xtradb-cluster:8.0
  container_name: mysql-pxc2
  networks:
    default:
      ipv4_address: 172.80.0.2
  environment: # 环境变量
      - TZ=Asia/Shanghai
      - MYSQL_ROOT_PASSWORD=123456
      - CLUSTER_NAME=pxc
      - CLUSTER_JOIN=mysql-pxc1
  ports:
    - "3306:3306"
    - "4444:4444"
    - "4567:4567"
    - "4568:4568"
  volumes:
   - /home/percona-xtradb-cluster8/mysql/data:/var/lib/mysql/
   - /home/percona-xtradb-cluster8/mysql/cert:/cert/
   - /home/percona-xtradb-cluster8/mysql/config:/etc/percona-xtradb-cluster.conf.d
  tty: true
  privileged: true # 拥有容器内命令执行的权限

 
networks:
  default:
    external:
      name: mysql-pxc-network-cluster
```

 