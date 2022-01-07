
package com.ec.account.service.impl;

import com.ec.account.dao.AccountDao;
import com.ec.account.service.AccountService;
import com.ec.commons.entities.bo.account.AccountBO;
import com.ec.commons.entities.bo.account.LoginBO;
import com.ec.commons.entities.bo.account.RegisterBO;
import com.ec.commons.entities.dto.account.LoginDTO;
import com.ec.commons.entities.dto.account.RegisterDTO;
import com.ec.commons.entities.po.account.AccountPO;
import com.ec.commons.util.BeanUtils;
import com.ec.commons.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Resource
    private AccountDao accountDao;

    @Override
    public void register(RegisterDTO dto) {
        RegisterBO bo = BeanUtils.toBean(dto, RegisterBO.class);

        String nativeId = UUIDUtil.generateNativeId(16);
        bo.setNativeId(nativeId);
        bo.setTotal(1200);

        accountDao.register(bo);
    }

    //@GlobalTransactional
    @Override
    public void decrease(Long userId, BigDecimal money) {
        log.info("*******->account-service中扣减账户余额开始");

        accountDao.decrease(userId, money);

//        int x = 0;
//        int w = 1 / x;

        log.info("*******->account-service中扣减账户余额结束");
    }

    @Override
    public AccountBO infoByUserId(Long userId) {
        AccountPO info = accountDao.infoByUserId(userId);
        AccountBO bo = BeanUtils.toBean(info, AccountBO.class);
        return bo;
    }

    @Override
    public AccountBO infoByMobile(String mobile) {
        AccountPO info = accountDao.infoByMobile(mobile);
        AccountBO bo = BeanUtils.toBean(info, AccountBO.class);
        return bo;
    }

    @Override
    public AccountBO infoByNativeId(String nativeId) {
        AccountPO info = accountDao.infoByNativeId(nativeId);
        AccountBO bo = BeanUtils.toBean(info, AccountBO.class);
        return bo;
    }

    @Override
    public AccountBO login(LoginDTO dto) {
        AccountPO info = accountDao.login(BeanUtils.toBean(dto, LoginBO.class));

        AccountBO bo = BeanUtils.toBean(info, AccountBO.class);
        return bo;
    }
}
