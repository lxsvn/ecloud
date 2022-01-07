package com.edward.redis.annotation;

import com.edward.redis.RedisAutoConfigure;
import com.edward.redis.template.RedisUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用redis
 *
 * @author edward
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({RedisAutoConfigure.class, RedisUtil.class})
public @interface EnableRedis {


}
