package com.ec.order.service.fallback;

import com.ec.commons.util.ret.R;
import com.ec.order.service.AccountService;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceFallbak implements AccountService {

    @Override
    public R infoByUserId(Long userId) {
        return null;
    }


}