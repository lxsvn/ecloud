# HAProxy 负载均衡架构


```shell
docker run -d \
--name haproxy \
-p 8189:8189 \
-p 3333:3333 \
-p 3334:3334 \
-v /home/haproxy/haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg \
haproxy:2.2.20



```

haproxy.cfg 参考
```shell
global
    log 127.0.0.1 local0 debug
    maxconn 4096
    daemon
defaults
    mode http
    retries 3 # 连接后端服务器失败的次数如果超过这里设置的值，haproxy会将对应的后端服务器标记为不可用
    timeout connect 10s
    timeout client 20s
    timeout server 30s
    timeout check 5s
listen  mysql_master_prx # mysql双主集群
    bind 0.0.0.0:3333
    mode tcp
    balance roundrobin
    server mysqlm1 172.29.33.25:3310
    server mysqlm2 172.29.33.25:3311
listen  mysql_slave_prx # mysql从库集群
    bind 0.0.0.0:3334
    mode tcp
    balance roundrobin
    server mysqls1 172.29.33.25:3320
    server mysqls2 172.29.33.25:3321
listen admin_stats # 管理后台
    bind *:8189
    mode http
    stats refresh 30s
    stats uri /admin
    stats realm welcome login\ Haproxy
    stats auth admin:admin123
    stats admin if TRUE

```