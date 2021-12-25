package com.ec.initialize;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
                DataSourceAutoConfiguration.class
})
public class InitializeMainApp10 {
    public static void main(String[] args) {
        SpringApplication.run(InitializeMainApp10.class, args);
    }




}
