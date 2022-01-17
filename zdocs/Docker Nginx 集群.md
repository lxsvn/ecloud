# Docker Nginx(6.2.6)集群

需要公网访问的端口，除了云后台安全组开启、nginx.conf配置监听端口外
还需
docker run 时 -p 映射公网访问端口！！！
因为nginx.conf配置的值nginx容器所代理的内部端口，而对外暴露需要一一指定

架构：
公网 -> nginx -> 应用容器
其中应用容器可隐藏公网IP or 关闭公网映射。请求全部走nginx

### 创建挂载目录

```shell
# 新建目录
mkdir -p /home/nginx/html /home/nginx/conf.d  /home/nginx/logs
touch /home/nginx/nginx.conf
chmod -R 777 /home/nginx
```

### nginx.conf 修改配置

```shell
user  nginx;
worker_processes  auto;
error_log  /var/log/nginx/error.log notice;
pid        /var/run/nginx.pid;
events {
    worker_connections  1024;
}
http {
	upstream testServe {
        server 172.29.33.25:8848;
    }
    
	default_type  application/octet-stream;
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';
    access_log  /var/log/nginx/access.log  main;
    sendfile     on;
    keepalive_timeout  65;
    include /etc/nginx/conf.d/*.conf;
}

```

启动空白未挂载容器，复制初始配置文件
```shell
docker run -d \
--name nginx \
-p 80:80 \
-p 443:443 \
nginx:1.21.5

docker cp nginx:/etc/nginx/nginx.conf /home/nginx
docker cp nginx:/etc/nginx/conf.d/default.conf /home/nginx/conf.d

docker rm -f nginx
```

启动
```shell
docker run -d \
--name nginx \
-p 80:80 \
-p 8828:8828 \
-p 3307:3307 \
-p 443:443 \
-v /home/nginx/html:/usr/share/nginx/html \
-v /home/nginx/nginx.conf:/etc/nginx/nginx.conf \
-v /home/nginx/conf.d:/etc/nginx/conf.d \
-v /home/nginx/logs:/var/log/nginx \
nginx:1.21.5
```

docker挂载文件夹：
宿主机本地先新建空白文件夹，里面不要有任何东西！
容器启动成功后，再新建文件!

## 在/home/nginx/conf.d 下新建各种自定义*.conf配置文件
如 test.conf
```shell
    server {
            listen 8828;
            localhost / {
                    proxy_pass http://testServe;
            }
    }
```