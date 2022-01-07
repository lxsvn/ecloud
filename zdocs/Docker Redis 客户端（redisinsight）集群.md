# Docker 管理客户端 - redisinsight 集群


```shell
mkdir -p /home/redisinsight/data

chmod 777 /home/redisinsight/data

docker run -d \
--name redisinsight \
-v /home/redisinsight/data:/db \
-p 8001:8001 \
redislabs/redisinsight:latest

```
 