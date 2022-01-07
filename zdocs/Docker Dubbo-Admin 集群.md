# Docker Dubbo-Admin (dubbo 管理工具)

### 启动

```shell

docker run -d \
--name dubbo-admin \
-p 3301:8080 \
-e admin.root.user.name=root \
-e admin.root.user.password=Edward \
-e admin.registry.address='zookeeper://47.104.80.250:2181' \
-e admin.config-center='zookeeper://47.104.80.250:2181' \
-e admin.metadata-report.address='zookeeper://47.104.80.250:2181' \
apache/dubbo-admin:0.4.0

```

 
 