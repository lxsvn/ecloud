package com.ec.gateway;

import com.edward.redis.annotation.EnableRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@RefreshScope
@EnableRedis
public class GateWayApiMainApp81 {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApiMainApp81.class, args);
    }
}
