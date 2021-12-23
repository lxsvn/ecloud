package com.ec.account.controller;


import com.ec.account.service.AccountService;
import com.ec.commons.controller.BaseController;
import com.ec.commons.entities.bo.account.AccountBO;
import com.ec.commons.entities.dto.account.AccountInfoDTO;
import com.ec.commons.entities.vo.account.AccountVO;
import com.ec.commons.util.BeanUtils;
import com.ec.commons.util.ret.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController extends BaseController {

    @Resource
    private AccountService accountService;

    @GetMapping(value = "/info")
    public R info() {
        AccountBO info = accountService.infoByNativeId(getNativeId());

        AccountVO vo = BeanUtils.toBean(info, AccountVO.class);

        return R.success(vo);
    }

    @GetMapping("/info/userid/{userId}")
    public R infoByUserId(AccountInfoDTO dto) {
        AccountBO info = accountService.infoByUserId(dto.getUserId());

        AccountVO vo = BeanUtils.toBean(info, AccountVO.class);
        return R.success(vo);
    }

    @GetMapping("/info/mobile/{mobile}")
    public R infoByMobile(AccountInfoDTO dto) {
        AccountBO info = accountService.infoByMobile(dto.getMobile());

        AccountVO vo = BeanUtils.toBean(info, AccountVO.class);
        return R.success(vo);
    }

}