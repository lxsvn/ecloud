# Docker MySQL(8.0+)多主
HA: 2台master，互为主从，挂了一台另一台做主机。保证主机master高可用
Shading: 2台slave，读写分离。


### 配置文件创建
```shell

# 创建文件夹

mkdir -p /home/mysql_master_slave
chmod -R 777 /home/mysql_master_slave 

# 配置master文件模板
cd /home/mysql_master_slave
vi mysql-master.tmpl

```

mysql-master.tmpl
```shell
[mysqld]
server-id=${PORT}
user=mysql
character-set-server=utf8
default_authentication_plugin=mysql_native_password
secure_file_priv=/var/lib/mysql
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
max_connections=1000
default_time_zone = "+8:00"
log-bin=mysql-bin
expire_logs_days=7
binlog_format=ROW

binlog-ignore-db=mysql
binlog-ignore-db=information_schema
binlog-ignore-db=performance_schema
binlog-ignore-db=sys

log-slave-updates=1
auto-increment-increment=2
auto-increment-offset=1

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8
```

```shell
# 配置slave文件模板
cd /home/mysql_master_slave
vi mysql-slave.tmpl
```

mysql-slave.tmpl
```shell
[mysqld]
log-bin = mysql-bin
secure_file_priv=/var/lib/mysql
server-id =${PORT}

 
sql_mode=ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
 
relay-log=mysql-relay
 
[client]
default-character-set=utf8
 
[mysql]
default-character-set=utf8
```

### 生成配置文件
```shell
cd /home/mysql_master_slave

# 生成master配置文件。端口号看需求。
for port in `seq 3310 3311`; do \
  mkdir -p master/${port}/conf \
  && PORT=${port} envsubst < mysql-master.tmpl > master/${port}/conf/my.cnf \
  && mkdir -p master/${port}/data \
  && mkdir -p master/${port}/log; \
done

# 生成slave配置文件。端口号看需求。
for port in `seq 3320 3321`; do \
  mkdir -p slave/${port}/conf \
  && PORT=${port} envsubst < mysql-slave.tmpl > slave/${port}/conf/my.cnf \
  && mkdir -p slave/${port}/data \
  && mkdir -p slave/${port}/log; \
done
```

### 0. 安装mysql

```shell
docker pull mysql:8.0.27
```

### 1. 创建网络（失败报错执行：docker swarm init）

```shell
docker network create --driver overlay mysql-network-ms --attachable
```

### 2. 创建 master & slave

```shell
# 2.1 
# 创建 主 master-1
docker run -d \
--name mysql-master-1 \
--restart=always \
--network mysql-network-ms \
-e MYSQL_ROOT_PASSWORD=E@w123456 \
-e TZ=Asia/Shanghai \
-v /home/mysql_master_slave/master/3310/conf/my.cnf:/etc/mysql/my.cnf  \
-v /home/mysql_master_slave/master/3310/data:/var/lib/mysql \
-v /home/mysql_master_slave/master/3310/log:/logs \
-p 3310:3306 \
-d mysql:8.0.27

# 创建 主 master-2
docker run -d \
--name mysql-master-2 \
--restart=always \
--network mysql-network-ms \
-e MYSQL_ROOT_PASSWORD=E@w123456 \
-e TZ=Asia/Shanghai \
-v /home/mysql_master_slave/master/3311/conf/my.cnf:/etc/mysql/my.cnf  \
-v /home/mysql_master_slave/master/3311/data:/var/lib/mysql \
-v /home/mysql_master_slave/master/3311/log:/logs \
-p 3311:3306 \
-d mysql:8.0.27

# 2.2 

# 创建 从 slave-1
docker run -d \
--name mysql-slave-1 \
--restart=always \
--network mysql-network-ms \
-e MYSQL_ROOT_PASSWORD=E@w123456 \
-e TZ=Asia/Shanghai \
-v /home/mysql_master_slave/slave/3320/conf/my.cnf:/etc/mysql/my.cnf  \
-v /home/mysql_master_slave/slave/3320/data:/var/lib/mysql \
-v /home/mysql_master_slave/slave/3320/log:/logs \
-p 3320:3306 \
-d mysql:8.0.27

# 创建 从 slave-2
docker run -d \
--name mysql-slave-2 \
--restart=always \
--network mysql-network-ms \
-e MYSQL_ROOT_PASSWORD=E@w123456 \
-e TZ=Asia/Shanghai \
-v /home/mysql_master_slave/slave/3321/conf/my.cnf:/etc/mysql/my.cnf  \
-v /home/mysql_master_slave/slave/3321/data:/var/lib/mysql \
-v /home/mysql_master_slave/slave/3321/log:/logs \
-p 3321:3306 \
-d mysql:8.0.27
```

### 3. 配置 master & slave

```shell
#  配置主 master-1: 创建一个用户来做同步的用户,并授权,所有集群内的服务器都需要做
docker run -it --rm --network mysql-network-ms mysql:8.0.27 mysql -hmysql-master-1 -uroot -pE@w123456 \
-e "CREATE USER 'master1'@'%' IDENTIFIED with mysql_native_password by 'wE@w123456'; " \
-e "GRANT REPLICATION SLAVE,REPLICATION CLIENT ON *.* TO 'master1'@'%';" \
-e "flush privileges;"

#  配置主 master-2: 创建一个用户来做同步的用户,并授权,所有集群内的服务器都需要做
docker run -it --rm --network mysql-network-ms mysql:8.0.27 mysql -hmysql-master-2 -uroot -pE@w123456 \
-e "CREATE USER 'master2'@'%' IDENTIFIED with mysql_native_password by 'wE@w123456'; " \
-e "GRANT REPLICATION SLAVE,REPLICATION CLIENT ON *.* TO 'master2'@'%';" \
-e "flush privileges;"


```

### 从机复制主机
```shell

#  slave1复制master1
docker run -it --rm --network mysql-network-ms mysql:8.0.27 mysql -hmysql-slave-1 -uroot -pE@w123456 \
-e "CHANGE MASTER TO MASTER_HOST='mysql-master-1', MASTER_PORT=3306, MASTER_USER='master1', MASTER_PASSWORD='wE@w123456';" \
-e "START SLAVE;" 

#  slave2复制master2
docker run -it --rm --network mysql-network-ms mysql:8.0.27 mysql -hmysql-slave-2 -uroot -pE@w123456 \
-e "CHANGE MASTER TO MASTER_HOST='mysql-master-2', MASTER_PORT=3306, MASTER_USER='master2', MASTER_PASSWORD='wE@w123456';" \
-e "START SLAVE;" 

```

### 主机相互复制
```shell

#  master1复制master2
docker run -it --rm --network mysql-network-ms mysql:8.0.27 mysql -hmysql-master-1 -uroot -pE@w123456 \
-e "CHANGE MASTER TO MASTER_HOST='mysql-master-2', MASTER_PORT=3306, MASTER_USER='master2', MASTER_PASSWORD='wE@w123456';" \
-e "START SLAVE;" 

#  master2复制master1
docker run -it --rm --network mysql-network-ms mysql:8.0.27 mysql -hmysql-master-2 -uroot -pE@w123456 \
-e "CHANGE MASTER TO MASTER_HOST='mysql-master-1', MASTER_PORT=3306, MASTER_USER='master1', MASTER_PASSWORD='wE@w123456';" \
-e "START SLAVE;" 

```

### 其他

```shell
# 验证slave状态
docker run -it --rm --network mysql-network-ms mysql:8.0.27 mysql -hmysql-slave-1 -uroot -pE@w123456 -e "show slave status\G"

# 查看slave数据库同步状态
docker run -it --rm --network mysql-network-ms mysql:8.0.27 mysql -hmysql-slave-2 -uroot -pE@w123456 \
 -e "show databases;"

```