package com.ec.account.service;

import com.ec.commons.entities.bo.account.AccountBO;
import com.ec.commons.entities.dto.account.LoginDTO;
import com.ec.commons.entities.dto.account.RegisterDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface AccountService {
    /**
     * 注册
     */
    void register(RegisterDTO dto);

    /**
     * 减库存
     *
     * @param userId 用户id
     * @param money  金额
     * @return
     */
    void decrease(Long userId, BigDecimal money);

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     */
    AccountBO infoByUserId(Long userId);

    /**
     * 获取用户信息
     *
     * @param mobile 用户mobile
     */
    AccountBO infoByMobile(String mobile);

    /**
     * 获取用户信息
     *
     * @param nativeId 用户唯一标识
     */
    AccountBO infoByNativeId(String nativeId);

    /**
     *
     * 登录
     * */
    AccountBO login(LoginDTO dto);
}