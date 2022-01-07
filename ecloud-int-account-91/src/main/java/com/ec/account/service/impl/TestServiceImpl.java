package com.ec.account.service.impl;

import com.ec.apis.product.ITestService;
import org.apache.dubbo.config.annotation.Service;

@Service
public class TestServiceImpl implements ITestService {
    @Override
    public String hello2(String name) {
        return "Hello2:"+name;
    }
}
