package com.ec.order.service.fallback;

import com.ec.commons.util.ret.R;
import com.ec.order.service.UidGeneratorService;
import org.springframework.stereotype.Component;

@Component
public class UidGeneratorServiceFallbak implements UidGeneratorService {
    @Override
    public R getCachedUid() {
        return R.fail("UID生成失败，服务降级。请稍候再试！");
    }
}
