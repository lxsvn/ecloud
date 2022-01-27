# Docker Redis-Cluster(6.2.6)集群
插槽
3主 3从 根据Raft算法自动选举


配置文件创建
```shell

# 创建文件夹
mkdir -p /home/redis_cluster/redis-config
chmod -R 777 /home/redis_cluster

# 配置文件模板
cd /home/redis_cluster
vi redis-cluster.tmpl

```

redis-cluster.tmpl
```shell

port ${PORT}
appendonly yes
requirepass ED888888
masterauth ED888888
# 关闭保护，外网可直接访问
protected-mode no
daemonize no
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 15000
# 因为需要外网访问，所以设置外网ip
cluster-announce-ip 47.104.80.250
cluster-announce-port ${PORT}
cluster-announce-bus-port 1${PORT}

```

```shell

cd /home/redis_cluster

# 生成配置文件。端口号看需求。
for port in `seq 6379 6384`; do \
  mkdir -p redis-config/${port}/conf \
  && PORT=${port} envsubst < redis-cluster.tmpl > redis-config/${port}/conf/redis.conf \
  && mkdir -p redis-config/${port}/data; \
done
```


## docker-compose.yml
```yml
version: '3'
services:
  # redis1配置
  redis1:
    image: redis:6.2.6
    container_name: redis-1
    environment: # 环境变量
      - PORT=6379 # 会使用config/nodes-${PORT}.conf这个配置文件
      - TZ=Asia/Shanghai
    ports:
      - 6379:6379
      - 16379:16379
    # 标准输入打开
    stdin_open: true
    # 后台运行不退出
    tty: true
    # 拥有容器内命令执行的权限
    privileged: true
    volumes:
      - /home/redis_cluster/redis-config/6379/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - /home/redis_cluster/redis-config/6379/data:/data
    command: sh -c "redis-server /usr/local/etc/redis/redis.conf"
  # redis2配置
  redis2:
    image: redis:6.2.6
    container_name: redis-2
    environment:
      - PORT=6380
      - TZ=Asia/Shanghai
    ports:
      - 6380:6380
      - 16380:16380
    stdin_open: true
    tty: true
    privileged: true
    volumes:
      - /home/redis_cluster/redis-config/6380/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - /home/redis_cluster/redis-config/6380/data:/data
    command: sh -c "redis-server /usr/local/etc/redis/redis.conf"
  # redis3配置
  redis3:
    image: redis:6.2.6
    container_name: redis-3
    environment:
      - PORT=6381
      - TZ=Asia/Shanghai
    ports:
      - 6381:6381
      - 16381:16381
    stdin_open: true
    tty: true
    volumes:
      - /home/redis_cluster/redis-config/6381/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - /home/redis_cluster/redis-config/6381/data:/data
    command: sh -c "redis-server /usr/local/etc/redis/redis.conf"
  # redis4配置
  redis4:
    image: redis:6.2.6
    container_name: redis-4
    environment:
      - PORT=6382
      - TZ=Asia/Shanghai
    ports:
      - 6382:6382
      - 16382:16382
    stdin_open: true
    tty: true
    volumes:
      - /home/redis_cluster/redis-config/6382/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - /home/redis_cluster/redis-config/6382/data:/data
    command: sh -c "redis-server /usr/local/etc/redis/redis.conf"
  # redis5配置
  redis5:
    image: redis:6.2.6
    container_name: redis-5
    environment:
      - PORT=6383
      - TZ=Asia/Shanghai
    ports:
      - 6383:6383
      - 16383:16383
    stdin_open: true
    tty: true
    volumes:
      - /home/redis_cluster/redis-config/6383/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - /home/redis_cluster/redis-config/6383/data:/data
    command: sh -c "redis-server /usr/local/etc/redis/redis.conf"
  # redis6配置
  redis6:
    image: redis:6.2.6
    container_name: redis-6
    environment:
      - PORT=6384
      - TZ=Asia/Shanghai
    ports:
      - 6384:6384
      - 16384:16384
    stdin_open: true
    tty: true
    volumes:
      - /home/redis_cluster/redis-config/6384/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - /home/redis_cluster/redis-config/6384/data:/data
    command: sh -c "redis-server /usr/local/etc/redis/redis.conf"
```

```shell
#生成启动Redis集群容器
docker-compose up -d
```

## 创建redis-cluster集群
```shell
# 随便进入容器，可进入容器1
docker exec -it redis-1 bash
# 切换至指定目录
cd /usr/local/bin/

# 集群： -a 为上面配置的密码。 这里需求是外网可访问，所以用外网ip，对应上面redis.conf配置
redis-cli -a ED888888 --cluster create 47.104.80.250:6379 47.104.80.250:6380 47.104.80.250:6381 47.104.80.250:6382 47.104.80.250:6383 47.104.80.250:6384 --cluster-replicas 1
```

## 集群状态查看
```shell
# 随便进入容器，可进入容器1
docker exec -it redis-1 bash
# 切换至指定目录
cd /usr/local/bin/

#查看集群状态
redis-cli -a ED888888 --cluster check 47.104.80.250:6380
```

## 查看集群信息
```shell
# 随便进入容器，可进入容器1
docker exec -it redis-1 bash
# 切换至指定目录
cd /usr/local/bin/

# 连接至集群某个节点
redis-cli -c -a ED888888 -h 47.104.80.250 -p 6379

# 查看集群信息
cluster info
# 查看集群结点信息
cluster nodes


# 客户端工具链接时，勾选上cluster选项！
```



## 报错
```shell
# 出现节点不为空，启动失败的报错:
# 删除宿主机redis-config下对应节点的data dump.rdb 文件
```