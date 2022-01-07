# Docker RocketMQ安装 & cloud集成RocketMQ

## Docker RocketMQ安装

### RocketMQ 安装

```shell
# 0. 安装
docker pull foxiswho/rocketmq:4.8.0

# 1. 新建挂载文件
mkdir -p /home/rocketmq/namesrv/logs
mkdir -p /home/rocketmq/broker/logs
mkdir -p /home/rocketmq/broker/store
mkdir -p /home/rocketmq/conf
chmod -R 777 /home/rocketmq

```

### RocketMQ 运行

```shell
# 0.运行nameserver

docker run -d \
-v /home/rocketmq/namesrv/logs:/home/rocketmq/logs \
--name rmqnamesrv \
--restart=always \
-e "JAVA_OPT_EXT=-Xms512M -Xmx512M -Xmn128m" \
-p 9876:9876 \
foxiswho/rocketmq:4.8.0 \
sh mqnamesrv

# 运行broker 

touch  /home/rocketmq/conf/broker.conf

# 0.本机新建broker.conf并配置
brokerClusterName = DefaultCluster
brokerName = broker-a
brokerId = 0
deleteWhen = 04
fileReservedTime = 48
brokerRole = ASYNC_MASTER
flushDiskType = ASYNC_FLUSH
brokerIP1 = 47.104.247.85
namesrvAddr = 47.104.247.85:9876
autoCreateTopicEnable=true
autoCreateSubscriptionGroup=true

# 运行broker
docker run -d  \
-v /home/rocketmq/broker/logs:/home/rocketmq/logs \
-v /home/rocketmq/broker/store:/home/rocketmq/store \
-v /home/rocketmq/conf/broker.conf:/home/rocketmq/conf/broker.conf \
--name rmqbroker \
-e "NAMESRV_ADDR=rmqnamesrv:9876" \
-e "JAVA_OPT_EXT=-Xms512M -Xmx512M -Xmn128m" \
-p 10911:10911 -p 10912:10912 -p 10909:10909 \
foxiswho/rocketmq:4.8.0 \
sh mqbroker -c /home/rocketmq/conf/broker.conf
```

### rocketmq控制台安装

```shell

# 控制台安装
docker pull styletang/rocketmq-console-ng:latest

# 控制台启动
docker run -d \
--name  rocketmq-console-ng \
-e "JAVA_OPTS=-Drocketmq.namesrv.addr=47.104.80.250:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" \
-p 8180:8080 -t styletang/rocketmq-console-ng:latest
```

## Cloud集成RocketMQ 4.8

### 依赖

```xml
 <dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>${rocketmq.version}</version>
</dependency>
```

### yml

```yml
rocketmq:
  name-server: 47.104.247.85:9876
  # 生产者配置
  producer:
    group: EDWARD_GROUP #自定义。确保producer分布式下消息的一致，
```