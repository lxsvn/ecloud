package com.ec.account;

import com.edward.redis.annotation.EnableRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;



@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
@EnableRedis
public class IntAccountMainApp91 {

    public static void main(String[] args) {
        SpringApplication.run(IntAccountMainApp91.class, args);
    }
}
