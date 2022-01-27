# Docker MySQL(8.0+) MySql-Cluster 集群

# mysql架构 HA进阶方式
- 单机
    - 没有容错性
- 一主多从
    - 读写可分离，但HA不行，master单节挂掉就完了.
- 主备切换
    - 将备库变为主库，主库变为备库.
    - 不是很好
- 多主多从
    - master多节点，HA已经非常好了。
    - 通过MMA、MHA等工具，维护master健康状态，实施自动故障转移。某个节点挂掉，自动升级副节点。故障监管VIP模式。
- MySql-Cluster集群 
    - 终极方案。费钱
    - 高可用、分布式、并行计算
    - 此方案比多主多从更为复杂，但更HA！
    
# MySql_cluster架构（3部分）
- 管理(MGM)节点：
    - 这类节点的作用是管理MySQL Cluster内的其他节点，如提供配置数据、启动并停止节点、运行备份...
    - 使用命令“ndb_mgmd”启动
- 数据节点：
    - 使用NDB Cluster（简称NDB）存储引擎存储数据
    - 使用命令“ndbd”启动
- SQL节点：
    - 用来访问 Cluster数据的节点
    - 使用命令“ndbd”启动

客户端访问 —> SQL节点 -> 数据节点


 
### 基础配置
```shell

# 创建网络
docker network create mysql-network-cluster --subnet=192.168.0.0/16

# 启动一个管理节点
docker run -d --net=mysql-network-cluster \
--name=mysql-cluster-management1 \
--ip=192.168.0.2 \
-v /home/mysql_cluster/mysql-cluster.cnf:/etc/mysql-cluster.cnf \
mysql/mysql-cluster:8.0.27 ndb_mgmd

# 启动两个数据节点
docker run -d --net=mysql-network-cluster --name=mysql-ndb1 --ip=192.168.0.3 mysql/mysql-cluster:8.0.27 ndbd
docker run -d --net=mysql-network-cluster --name=mysql-ndb2 --ip=192.168.0.4 mysql/mysql-cluster:8.0.27 ndbd

# 启动server节点
docker run -d --net=mysql-network-cluster \
--name=mysqld-1 \
--ip=192.168.0.10 \
-e MYSQL_ROOT_PASSWORD=E@w123456 \
mysql/mysql-cluster:8.0.27 mysqld

docker run -d --net=mysql-network-cluster \
--name=mysqld-2 \
--ip=192.168.0.11 \
-e MYSQL_ROOT_PASSWORD=E@w123456 \
mysql/mysql-cluster:8.0.27 mysqld

# 复制配置文件: 
docker cp mysqld-1:/etc/my.cnf /home/mysql_cluster
docker cp mysqld-1:/etc/mysql-cluster.cnf /home/mysql_cluster

```

```shell
# 全部启动
docker start mysql-cluster-management1 mysql-ndb1 mysql-ndb2 mysqld-1 mysqld-2
```


测试
```shell
# 在管理节点查看集群状态
docker run -it --net=mysql-network-cluster mysql/mysql-cluster:8.0.27 ndb_mgm
# 查看
show

```


## haproxy 负载均衡
```shell
 
mkdir -p /home/haproxy

vim /home/haproxy/haproxy.cfg

docker run -d \
--name haproxy \
-p 8189:8189 \
-p 3328:3328 \
-p 8999:8999 \
-v /home/haproxy:/usr/local/etc/haproxy \
haproxy:2.3

```

haproxy.cfg

```text
global 
        log         127.0.0.1 local2
        maxconn     4000
        daemon 

defaults 
        mode http          #默认的模式mode { tcp|http|health }，tcp是4层，http是7层，health只会返回OK 
        retries 3          #两次连接失败就认为是服务器不可用，也可以通过后面设置 
        option redispatch      #当serverId对应的服务器挂掉后，强制定向到其他健康的服务器 
        option abortonclose     #当服务器负载很高的时候，自动结束掉当前队列处理比较久的链接 
        maxconn 4096        #默认的最大连接数 
        timeout connect 5000ms   #连接超时 
        timeout client 30000ms   #客户端超时 
        timeout server 30000ms   #服务器超时 
        timeout check 2000     #=心跳检测超时 
        log global
listen mysql_cluster
    bind 172.29.33.25:3829
    mode tcp
    balance roundrobin
    server node1 192.168.0.10:3306
    server node2 192.168.0.11:3306
```