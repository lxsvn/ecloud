# docker 安装(centos7)

```shell

# 1. 需要的安装包
yum install -y yum-utils

# 2. 设置镜像仓库
yum-config-manager \
    --add-repo \
    http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 3. 更新yum软件包索引
yum makecache fast

# 4. 安装docker
yum install docker-ce docker-ce-cli containerd.io

# 5. 启动docker
systemctl start docker
```

# docker-compose

```shell

# 安装
curl -L https://get.daocloud.io/docker/compose/releases/download/v2.2.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

# 授权
chmod +x /usr/local/bin/docker-compose

# 查看版本
docker-compose version

```

# docker 配置

删除全部容器：

```shell
docker rm -f $(docker ps -qa)

``` 

模糊删除容器

```shell
#删除容器名包含xxxx的全部容器
docker rm -f $(docker ps -a |grep xxxx)

#暂停容器名包含xxxx的全部容器
docker stop $(docker ps -a |grep xxxx)
```

启动已有容器：

```shell
docker start 容器id

```

生成镜像：

```shell
# 在Dockerfile文件同目录下，执行。（最后有个. !!!!!表示上下文）：
docker build -t seata-account-service-2003:1.0 .
```
docker build -t ecloud/uid-generator:1.0 .
重启容器

```shell
docker restart 容器id
```

查看容器使用率（ctrl+c 退出）：

```shell
dokcer stats
```

进入一个正在运行的容器：

```shell
docker exec -it 容器id /bin/bash

```

特殊
```shell
docker挂载文件夹！
宿主机本地先新建空白文件夹，里面不要有任何东西！
容器启动成功后，在新建文件!
```

```shell
#查看端口占用
netstat -ntulp |grep 6666
```