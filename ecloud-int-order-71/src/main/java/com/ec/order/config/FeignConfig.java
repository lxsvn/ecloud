package com.ec.order.config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign 配置文件
 * 将请求头中的参数，全部作为 feign 请求头参数传递
 * @author: edward
 **/
@Configuration
public class FeignConfig implements RequestInterceptor {
    @Resource
    HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                requestTemplate.header(name, values);
            }
        }
    }
}