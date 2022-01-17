package com.ec.initialize;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {
                DataSourceAutoConfiguration.class
})
public class InitializeMainApp10 {
    public static void main(String[] args) {
        List<String> xs=new ArrayList<>();
        SpringApplication.run(InitializeMainApp10.class, args);
    }




}
