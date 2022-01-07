package com.ec.product;

import com.edward.redis.annotation.EnableRedis;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRedis
@EnableDubbo
public class IntProductMainApp61 {

    public static void main(String[] args) {
        SpringApplication.run(IntProductMainApp61.class, args);
    }
}
