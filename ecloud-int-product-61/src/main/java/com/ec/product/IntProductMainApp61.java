package com.ec.product;

import com.ec.commons.filter.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
// 1. import方式，可手动扫描公共类中的配置
//      项目启动时，默认只会扫描当前项目的的配置

// 2. 也可以在公共类中，新增spring.factories文件，
//      加入需要自动注入的类。
//      这样只要pom中引用了公共类，就会自动配置
//@Import(GlobalExceptionHandler.class)
public class IntProductMainApp61 {

    public static void main(String[] args) {
        SpringApplication.run(IntProductMainApp61.class, args);
    }
}
