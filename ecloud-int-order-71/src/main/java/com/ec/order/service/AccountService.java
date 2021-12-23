package com.ec.order.service;

import com.ec.commons.util.ret.R;
import com.ec.order.service.fallback.AccountServiceFallbak;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ecloud-int-account", fallback = AccountServiceFallbak.class)
public interface AccountService {

    @GetMapping("/account/info/userid/{userId}")
    R infoByUserId(@PathVariable("userId") Long userId);

}
