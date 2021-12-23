package com.ec.order.service;

import com.ec.commons.util.ret.R;
import com.ec.order.service.fallback.UidGeneratorServiceFallbak;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ui生成服务
 * */
@FeignClient(value = "ecloud-uid-generator-service", fallback = UidGeneratorServiceFallbak.class)
public interface UidGeneratorService {

    /**
     * ui生成接口
     * */
    @GetMapping("/uid/get")
    R getCachedUid();


}
