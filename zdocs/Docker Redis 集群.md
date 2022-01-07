# Docker Redis(6.2)集群

### 0. 安装 Redis

```shell
docker pull redis:6.2

```

### 创建挂载目录

```shell
# 新建目录
mkdir -p /home/redis/data /home/redis/conf
chmod -R 777 /home/redis

# 新建配置文件：/home/redis/conf/redis.conf
# 配置文件参考： https://github.com/redis/redis/blob/unstable/redis.conf
# 配置文件最好从上面直接下载再修改，不然各种奇怪问题。修改时，注意空格！
touch /home/redis/conf/redis.conf

```

### redis.conf 修改配置

```text

# 1：开启外网访问：注释bind 127.0.0.1，
# bind 127.0.0.1

# 2：使用密码访问
protected-mode yes  
requirepass ED888888

```

### 2. 启动redis并挂载

```shell
docker run \
-p 6379:6379 \
--name redis  \
--restart=always \
-v /home/redis/conf/redis.conf:/usr/local/etc/redis/redis.conf \
-v /home/redis/data:/data \
-d redis:6.2 redis-server /usr/local/etc/redis/redis.conf --appendonly yes 


# redis-server：指定以配置文件启动
# --appendonly yes 持久化

```





## docker-compose.yaml
```yaml
version: '3'

services:
  redis:
    image: redis:6.2
    container_name: redis
    restart: always
    ports:
      - 6379:6379
    volumes:
      - /home/redis/conf/redis.conf:/etc/redis/redis.conf:rw
      - /home/redis/data:/data:rw
```