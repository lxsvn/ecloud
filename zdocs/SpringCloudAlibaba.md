# Spring Cloud Alibaba : nacos + sentinel + seata

## nacos配置

```html
服务注册与发现
```

启动nacos:

- 现在本地新建application.properties,再进行-v挂载
- 挂在文件夹不存在的，需先新建

```shell
docker run \
-e JVM_XMS=256m -e JVM_XMX=256m \
--env MODE=standalone  \
-v /home/nacos/logs:/home/nacos/logs \
-v /home/nacos/conf/application.properties:/home/nacos/conf/application.properties \
--name nacos -d \
-p 8848:8848 \
nacos/nacos-server:2.0.3

```

- application.properties文件示例

mysql 8.0以上，url参数需加上：serverTimezone=GMT%2B8

```yaml
# spring
server.servlet.contextPath=${SERVER_SERVLET_CONTEXTPATH:/nacos}
server.contextPath=/nacos
server.port=8848
spring.datasource.platform=mysql
nacos.cmdb.dumpTaskInterval=3600
nacos.cmdb.eventTaskInterval=10
nacos.cmdb.labelTaskInterval=300
nacos.cmdb.loadDataAtStart=false
db.num=1
db.url.0=jdbc:mysql://12.3.4.5:3306/nacos_config?serverTimezone=GMT%2B8&characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useSSL=false
db.user=root
db.password=root
### The auth system to use, currently only 'nacos' is supported:
nacos.core.auth.system.type=${NACOS_AUTH_SYSTEM_TYPE:nacos}


### The token expiration in seconds:
nacos.core.auth.default.token.expire.seconds=${NACOS_AUTH_TOKEN_EXPIRE_SECONDS:18000}

### The default token:
nacos.core.auth.default.token.secret.key=${NACOS_AUTH_TOKEN:SecretKey012345678901234567890123456789012345678901234567890123456789}

### Turn on/off caching of auth information. By turning on this switch, the update of auth information would have a 15 seconds delay.
nacos.core.auth.caching.enabled=${NACOS_AUTH_CACHE_ENABLE:false}
nacos.core.auth.enable.userAgentAuthWhite=${NACOS_AUTH_USER_AGENT_AUTH_WHITE_ENABLE:false}
nacos.core.auth.server.identity.key=${NACOS_AUTH_IDENTITY_KEY:serverIdentity}
nacos.core.auth.server.identity.value=${NACOS_AUTH_IDENTITY_VALUE:security}
server.tomcat.accesslog.enabled=${TOMCAT_ACCESSLOG_ENABLED:false}
server.tomcat.accesslog.pattern=%h %l %u %t "%r" %s %b %D
# default current work dir
server.tomcat.basedir=
## spring security config
### turn off security
nacos.security.ignore.urls=${NACOS_SECURITY_IGNORE_URLS:/,/error,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-fe/public/**,/v1/auth/**,/v1/console/health/**,/actuator/**,/v1/console/server/**}
# metrics for elastic search
management.metrics.export.elastic.enabled=false
management.metrics.export.influx.enabled=false

nacos.naming.distro.taskDispatchThreadCount=10
nacos.naming.distro.taskDispatchPeriod=200
nacos.naming.distro.batchSyncKeyCount=1000
nacos.naming.distro.initDataRatio=0.9
nacos.naming.distro.syncRetryDelay=5000
nacos.naming.data.warmup=true

```

## sentinel

- 保证服务的稳定性
- 以流量为切入点：限流、熔断降级、系统自适应保护

```shell
# 1. 安装sentinel
docker pull bladex/sentinel-dashboard:1.7.2

# 2. 运行sentinel
docker run --name sentinel -d -p 8858:8858 bladex/sentinel-dashboard:1.7.2
```

## seata 分布式微服务一致性解决方案

### mysql 建库脚本 [地址](https://github.com/seata/seata/blob/e2e5fa6c9edc0766e53fa7cc08a927391b898f67/script/server/db/mysql.sql)

```shell

# 1. 安装镜像
docker pull seataio/seata-server:l.4.2

# 新建seata配置数据库https://github.com/seata/seata/blob/develop/script/server/db/mysql.sql
# 根据使用不同的seata模式，在每个数据库中新增对应的表，如AT(默认)，新建undo_log:https://github.com/seata/seata/blob/develop/script/client/at/db/mysql.sql

# 2. 启动，并挂载

## 2.1 本机创建目录挂载
mkdir /home/seata-server

## 2.2 启动并挂载
docker run -d \
--name seata-server \
-p 8091:8091  \
-e SEATA_IP=47.104.247.85 \
-e SEATA_PORT=8091   \
-e SEATA_CONFIG_NAME=file:/root/seata-config/registry \
-v /home/seata-server/:/root/seata-config \
seataio/seata-server:1.4.2


# 3 配置并初始化seata

## 3.1 创建配置文件
https://github.com/seata/seata/tree/develop/script/config-center/nacos
在/home/seata-server下，分别创建config.txt、file.conf、registry.conf、nacos-config.sh

##运行nacos-config.sh，使得配置到nacos
sh nacos-config.sh -h 47.104.247.85 -g SEATA_GROUP

```