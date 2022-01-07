package com.ec.canal;

import com.edward.redis.annotation.EnableRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;


@SpringBootApplication
@EnableRedis
@RefreshScope
public class CanalConsumerMainApp1001 {
    public static void main(String[] args) {
        SpringApplication.run(CanalConsumerMainApp1001.class, args);
    }
}
