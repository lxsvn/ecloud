# Docker MySQL(8.0+)集群

### 0. 安装mysql

```shell
docker pull mysql:8.0.27
```

### 1. 创建网络（失败报错执行：docker swarm init）

```shell
docker network create --driver overlay mysql-network --attachable
```

### 创建挂载目录

```shell
# 新建目录
mkdir -p /home/mysql/conf
mkdir -p /home/mysql/data
mkdir -p /home/mysql/log
chmod -R 777 /home/mysql

# 新建配置文件：/home/mysql/conf/my.cnf
[mysqld]
user=mysql
character-set-server=utf8
default_authentication_plugin=mysql_native_password
secure_file_priv=/var/lib/mysql
expire_logs_days=7
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
max_connections=1000
default_time_zone = "+8:00"

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

```

### 2. 创建 master & slave

```shell
# 2.1 创建主 master
docker run -d \
--name mysql-master \
--network mysql-network \
--restart=always \
-e MYSQL_ROOT_PASSWORD=E@w123456 \
-e TZ=Asia/Shanghai \
-v /home/mysql/conf/my.cnf:/etc/mysql/my.cnf  \
-v /home/mysql/data:/var/lib/mysql \
-v /home/mysql/log:/logs \
-p 3306:3306 \
-d mysql:8.0.27

# 2.2 创建从slave (无需挂载。因为master挂了)
docker run -d \
--name mysql-slave \
--network mysql-network \
-e MYSQL_ROOT_PASSWORD=E@w123456 \
-e TZ=Asia/Shanghai \
-p 3307:3306 \
-d mysql:8.0.27
```

### 3. 配置 master & slave

```shell
# 3.1 配置主 master
docker run -it --rm --network mysql-network mysql:8.0.27 mysql -hmysql-master -uroot -pE@w123456 \
-e "SET PERSIST server_id=1;" \
-e "SET PERSIST_ONLY gtid_mode=ON;" \
-e "SET PERSIST_ONLY enforce_gtid_consistency=true; " \
-e "CREATE USER 'repl'@'%' IDENTIFIED BY 'password' REQUIRE SSL; " \
-e "GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';"


# 3.2 配置从slave
docker run -it --rm --network mysql-network mysql:8.0.27 mysql -hmysql-slave -uroot -pE@w123456 \
-e "SET PERSIST server_id=2;" \
-e "SET PERSIST_ONLY gtid_mode=ON;" \
-e "SET PERSIST_ONLY enforce_gtid_consistency=true; "

```

### 4. 重启 master & slave

```shell
docker restart mysql-master mysql-slave
```

### 5. 链接 master & slave

```shell
docker run -it --rm --network mysql-network mysql:8.0.27 mysql -hmysql-slave -uroot -pE@w123456 \
-e "CHANGE MASTER TO MASTER_HOST='mysql-master', MASTER_PORT=3306, MASTER_USER='repl', MASTER_PASSWORD='password', MASTER_AUTO_POSITION=1, MASTER_SSL=1;" \
-e "START SLAVE;" 
```

### 其他

```shell
# 验证slave状态
docker run -it --rm --network mysql-network mysql:8.0.27 mysql -hmysql-slave -uroot -pE@w123456 -e "show slave status\G"

# 查看slave数据库同步状态
docker run -it --rm --network mysql-network mysql:8.0.27 mysql -hmysql-slave -uroot -pE@w123456 \
 -e "show databases;"

```