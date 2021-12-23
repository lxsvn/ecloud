package com.ec.account.dao;

import com.ec.commons.entities.bo.account.LoginBO;
import com.ec.commons.entities.bo.account.RegisterBO;
import com.ec.commons.entities.po.account.AccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface AccountDao {

    /**
     * 注册
     */
    void register(RegisterBO bo);

    /**
     * 扣减账户余额
     *
     * @param userId
     * @param money
     * @return
     */
    int decrease(@Param("userId") Long userId, @Param("money") BigDecimal money);

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     */
    AccountPO infoByUserId(@Param("userId") Long userId);

    /**
     * 获取用户信息
     *
     * @param mobile 用户手机号
     */
    AccountPO infoByMobile(@Param("mobile") String mobile);

    /**
     * 获取用户信息
     *
     * @param nativeId 用户唯一标识
     */
    AccountPO infoByNativeId(@Param("nativeId") String nativeId);

    /**
     * 登录
     */
    AccountPO login(LoginBO bo);
}
