# Kafka集成

## 安装 kafka & zookeeper服务端
```shell
# 1. 下载镜像
docker pull wurstmeister/zookeeper
docker pull wurstmeister/kafka

# 2. 单机启动zookeeper
docker run -d -t \
--name zookeeper \
-p 2181:2181  \
wurstmeister/zookeeper

# 3. 单机启动kafka
# KAFKA_ADVERTISED_LISTENERS 中IP为外网IP，外网才可访问
# --link 容器互联

docker run  -d \
--name kafka  \
-p 9092:9092  \
--link zookeeper  \
-e KAFKA_HEAP_OPTS="-Xmx512M -Xms16M" \
-e KAFKA_BROKER_ID=0 \
-e KAFKA_ZOOKEEPER_CONNECT=172.29.33.23:2181  \
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://47.104.247.85:9092  \
-e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 \
-t wurstmeister/kafka
```


## kafka 后台
```shell
# 拉取
docker pull obsidiandynamics/kafdrop:latest

# 运行
docker run -d --rm -p 9000:9000 \
--name kafdrop \
-e KAFKA_BROKERCONNECT=172.29.33.23:9092 \
-e JVM_OPTS="-Xms32M -Xmx64M" \
-e SERVER_SERVLET_CONTEXTPATH="/" \
obsidiandynamics/kafdrop:latest
```

## zookeeper 后台
```shell

# 拉取
docker pull qnib/zkui

# 运行
docker run -d -it \
--name zkui \
-p 9090:9090 \
-e ZKUI_ZK_SERVER=172.29.33.23:2181 \
qnib/zkui


```