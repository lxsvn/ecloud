# Canal

- 可用于解决实现Redis & MySql 数据一致性问题
- [文档](https://github.com/alibaba/canal)
- 阿里巴巴 MySQL binlog 增量订阅&消费组件

## 管理后台 - canal-admin 安装

https://github.com/alibaba/canal/wiki/Canal-Admin-Docker

```shell

# 1. 下载运行脚本
wget https://raw.githubusercontent.com/alibaba/canal/master/docker/run_admin.sh 

# 2. 新建admin的数据库，建库脚本：
# 建库是为了给canal-admin指定外部库，方便维护。否则就会自动生成在docker内部，一般需要指定外部！ 
https://github.com/alibaba/canal/blob/master/admin/admin-web/src/main/resources/canal_manager.sql

# 2. 以8089端口启动ecloud_canal_manager,且指定外部的mysql作为admin的库。自动下载镜像
# 访问 http://xx.xx.xxx.xxx:8089。默认账户密码 admin 123456。可登入后修改

sh  run_admin.sh \
         -e server.port=8089 \
         -e spring.datasource.address=172.29.33.25:3306 \
         -e spring.datasource.database=ecloud_canal_manager \
         -e spring.datasource.username=root  \
         -e spring.datasource.password=E@w123456

# 进入后台后，server列表会有一条数据。(没有就重启canal-server)
# 3. 配置server
# 配置此条数据中的rocketmq信息：
# canal.serverMode = rocketMQ
# 再配置其中RocketMQ节点的信息。此后instance用会用到此推送消息。

# 4.配置instance
# canal.instance.master.address = mysql地址（需要订阅的数据库）
# canal.instance.dbUsername= mysql账号（需要订阅的数据库）
# canal.instance.dbPassword= mysql密码（需要订阅的数据库）
# canal.instance.filter.regex=ecloud_accout.t_account （需要监听的表...多种配置自行百度）
# canal.mq.topic=canal_sync_t_account (MQ推送的topic)
# 参考：
https://blog.csdn.net/leilei1366615/article/details/108819651
```

## 服务端 - canal-server 安装

```shell

# 0. MySql配置

https://github.com/alibaba/canal/wiki/QuickStart

https://github.com/alibaba/canal/wiki/Docker-QuickStart



# 2. 下载运行脚本
wget https://raw.githubusercontent.com/alibaba/canal/master/docker/run.sh

# 修改run.sh：master.address、账号密码等

# 4ACFE3202A5FF5CF467898FC58AAB1D615029441 是myql通过MD5()函数得到的admin的密文
sh run.sh -e canal.admin.manager=172.29.33.25:8089 \
         -e canal.admin.port=11110 \
         -e canal.admin.user=admin \
         -e canal.admin.passwd=4ACFE3202A5FF5CF467898FC58AAB1D615029441
 
```







## 使用
canal一般结合mq使用，推荐使用rocketmq。这里使用rocketmq。参考项目：canal-rocketmq-consumer-service-4002。架构流程如下：
```text
1. canal 伪装成mysql的slave。
2. canal模拟MySQL slave的交互协议向MySQL Mater发送 dump协议。
3. MySQL mater收到canal发送过来的dump请求，开始推送binary log给canal。
4. 然后canal解析binary log，再发送到存储目的地，比如MQ，Kafka，Elastic Search等等
5. 假设使用MQ，目的是推送到redis，以此来保持缓存与mysql的数据一致性
6. 参考 https://blog.csdn.net/u012946310/article/details/109466833。才canal后台配置即可
7. 新建一个项目，消费MQ里面对应的topic
```