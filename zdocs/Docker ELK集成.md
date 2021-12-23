# ELK集成

## 安装 elasticsearch 服务端
```shell
# 1. 下载镜像
docker pull elasticsearch:7.16.1

# 准备挂载配置文件。elasticsearch.yml 文件编码utf-8.
# 编码可用Xshell，打开yml设置
mkdir -p /home/elk/elasticsearch
touch /home/elk/elasticsearch/elasticsearch.yml

# yaml中放入下面配置
cluster.name: "docker-cluster"
network.host: 0.0.0.0
# 下面的配置是关闭跨域验证（可以不开启）需关闭，否则head会因为跨域连不上
http.cors.enabled: true
http.cors.allow-origin: "*"

# 访问ID限定，0.0.0.0为不限制，生产环境请设置为固定IP
transport.host: 0.0.0.0
# elasticsearch节点名称
node.name: node-1
# elasticsearch节点信息
cluster.initial_master_nodes: ["node-1"]


# 2. 单机启动es
# es默认占用内存4G，这里设置成512m

docker run -d -it \
--name elasticsearch \
-v /home/elk/elasticsearch/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms256m -Xmx256m" \
elasticsearch:7.16.1

# 安装完成测试
curl http://localhost:9200

```

## 安装 kibana 服务端
```shell
# 1. 下载镜像。选择的版本必须和elasticsearch的版本相同或者低，建议和elasticsearch的版本相同。否则会无法将无法使用kibana
docker pull kibana:7.16.1

# 准备挂载配置文件。kibana.yml 文件编码utf-8.
# 编码可用Xshell，打开yml设置
mkdir -p /home/elk/kibana
touch /home/elk/kibana/kibana.yml

# yaml中放入下面配置
server.port: 5601
server.host: "0.0.0.0"
# elasticsearch的ip。不能用localhost，用宿主机内网IP。因为是不同容器
elasticsearch.hosts: ["http://172.29.33.23:9200"]
# 操作界面语言设置为中文
i18n.locale: "zh-CN"

# 启动
docker run -d -it \
--name kibana \
-p 5601:5601 \
-v /home/elk/kibana/kibana.yml:/usr/share/kibana/config/kibana.yml \
kibana:7.16.1

# 安装完成测试
curl http://localhost:5601
```

## 安装 elasticsearch 客户端
```shell
# 1. 拉取镜像
docker pull coorpacademy/elasticsearch-head:latest

# 2. 启动
docker run -d -it \
--restart=always \
--name elasticsearch-head \
-p 9100:9100 \
docker.io/mobz/elasticsearch-head:5

```

## 安装 logstash 服务端 核心！！复杂！！

### 文件配置 & 挂载

#### 说明
```shell
# logstash配置有点多，可以先启动一个无挂载的容器。
# 再将自带的配置，复制一份到宿主机。查看全部默认配置。
docker cp logstash:/usr/share/logstash/ /home/test 
```

#### 下载镜像 & 初始化配置文件
```shell
# 下载镜像。选择的版本必须和elasticsearch的版本相同或者低，建议和elasticsearch的版本相同。
docker pull logstash:7.16.1

# 准备挂载配置文件。需要修改：logstash.conf、logstash.yml、jvm.options
# 编码可用Xshell，打开yml设置
mkdir -p  /home/elk/logstash/config
mkdir -p  /home/elk/logstash/pipeline
touch  /home/elk/logstash/config/jvm.options
touch /home/elk/logstash/config/logstash.yml
touch /home/elk/logstash/config/pipelines.yml
touch /home/elk/logstash/pipeline/logstash.conf
chmod 777 /home/elk/logstash
```

#### jvm.options文件参考配置

```text
-Xmx512m
-Xms512m
```

#### logstash.conf文件参考配置

```text
# Sample Logstash configuration for creating a simple
# Beats -> Logstash -> Elasticsearch pipeline.

input {
  beats {
    port => 5044
  }
  kafka{
    bootstrap_servers =>["172.29.33.23:9092"]
    group_id => "app_consumer_grup_1"
    auto_offset_reset => "latest"
    consumer_threads => 5
    decorate_events => true
    topics => ["api_log","err_log"]
    type => "edwardx"
  }
}

output {
  elasticsearch {
    hosts => ["http://172.29.33.23:9200"]
    index => "%{[@metadata][beat]}-%{[@metadata][version]}-%{+YYYY.MM.dd}"
    #user => "elastic"
    #password => "changeme"
  }
}

```

#### logstash.yml文件参考配置
```yaml
http.host: "0.0.0.0"
xpack.monitoring.elasticsearch.hosts: [ "http://172.29.33.23:9200" ]
```

#### pipelines.yml文件参考配置。 指向logstash.conf

```yaml
- pipeline.id: main
  path.config: "/usr/share/logstash/pipelines/logstash.conf"
```

### 启动 & 挂载
```shell

# 启动
docker run -d -it \
--name logstash \
-p 5044:5044 \
-v /home/elk/logstash/config/logstash.yml/:/usr/share/logstash/config/logstash.yml \
-v /home/elk/logstash/config/jvm.options/:/usr/share/logstash/config/jvm.options \
-v /home/elk/logstash/config/pipelines.yml/:/usr/share/logstash/config/pipelines.yml \
-v /home/elk/logstash/pipeline/logstash.conf/:/usr/share/logstash/pipeline/logstash.conf \
logstash:7.16.1

# 安装完成测试
curl http://localhost:5044
```

## 密码设置

### elasticsearch 配置
```shell
# 0. elasticsearch.yml修改，加入：
xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true

# 0. 重启elasticsearch容器  
docker restart elasticsearch

# 1. 进入elasticsearch容器
docker exec -it elasticsearch /bin/bash

# 2.执行密码初始化命令
bin/elasticsearch-setup-passwords interactive

# 3. 密码最好设置为一样。一个个输入。
# 账号 elastic。设置完退出容器，重启elasticsearch。
```

### kibana 配置
```shell
# 0. kibana.yml修改，加入：
elasticsearch.username: "elastic"
elasticsearch.password: "上面配置的密码"
```

### logstash 配置
```shell
# 0. logstash.conf修改，加入：
output{
    elasticsearch {
    user => "elastic"
    password => "之前配置的密码"
    }
}

# 1. logstash.yml，加入：
xpack.monitoring.elasticsearch.username: "elastic"
xpack.monitoring.elasticsearch.password: "之前配置密码"

```
### 最后全部重启ELK

```shell
docker restart elasticsearch kibana logstash
```