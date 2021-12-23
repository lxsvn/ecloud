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

# docker 配置

删除全部容器：

```shell
docker rm -f $(docker ps -qa)
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