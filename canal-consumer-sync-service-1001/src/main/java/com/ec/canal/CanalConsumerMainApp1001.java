package com.ec.canal;

import com.ec.commons.middleware.redis.RedisConfig;
import com.ec.commons.middleware.redis.RedisUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import({RedisConfig.class, RedisUtil.class})
public class CanalConsumerMainApp1001 {
    public static void main(String[] args) {
        SpringApplication.run(CanalConsumerMainApp1001.class, args);
    }
}
