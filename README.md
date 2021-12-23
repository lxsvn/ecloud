# ecloud
基于spring cloud alibaba的分布式微服务解决方案


# [Docker 基础](zdocs/Docker%20基础.md)

# [Docker + MySQL + 集群](zdocs/Docker%20MySQL%20集群.md)

# [SpringCloudAlibaba: nacos + sentinel + seata](zdocs/zdocs/SpringCloudAlibaba.md)

# [Docker + RocketMQ | cloud + RocketMQ | 集群](zdocs/Docker%20RocketMQ%20集成.md)

# [Docker + Redis | cloud | 集群](zdocs/Docker%20Redis%20集群.md)

# [Docker + Canal | cloud | 集群](zdocs/Docker%20Canal集成.md)

# [Docker + Kafka | cloud | 集群](zdocs/Docker%20Kafka集成.md)

# [Docker + ELK | cloud | 集群](zdocs/Docker%20ELK集成.md)


# 架构基础信息

## Spring boot 版本

```xml
<dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>2.2.2.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
```

## Spring Cloud 版本

```xml
<!--spring cloud Hoxton.SR1-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.SR1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
```

## Spring Cloud Alibaba

```xml
<!--spring cloud alibaba 2.1.0.RELEASE-->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>2.1.0.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
```

## 后台地址

```shell
#nacos
http://47.104.247.85:8848/nacos
nacos
nacos

#sentinel
http://47.104.247.85:8858/
sentinel
sentinel
```