# Docker zookeeper集群

### 创建挂载目录

```shell
# 新建目录
mkdir -p /home/zookeeper
chmod -R 777 /home/zookeeper
touch /home/zookeeper/zoo.cfg

```

### 启动并挂载

```shell
docker run -d \
-p 2181:2181 \
--name zookeeper \
zookeeper:3.7


```

### 启动并挂载 后台zkui 
账号密码：admin/manager

```shell
#
docker run -d \
--name zkui \
-p 9090:9090 \
-e ZK_SERVER=172.29.33.25:2181 \
juris/zkui

```
 