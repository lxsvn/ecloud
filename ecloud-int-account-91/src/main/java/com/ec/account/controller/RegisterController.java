package com.ec.account.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.ec.account.service.AccountService;
import com.ec.commons.constant.RedisKeyConstant;
import com.ec.commons.entities.bo.account.AccountBO;
import com.ec.commons.entities.dto.account.LoginDTO;
import com.ec.commons.entities.dto.account.RegisterDTO;
import com.ec.commons.util.JwtUtil;
import com.ec.commons.util.ret.R;
import com.edward.redis.template.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@AllArgsConstructor
public class RegisterController {

    private final AccountService accountService;
    private final RedisUtil redisTemplate;

    @PostMapping("/register")
    public R register(@RequestBody RegisterDTO dto) {
        accountService.register(dto);
        return R.success("注册成功！");
    }

    @PostMapping("/login")
    public R login(@RequestBody LoginDTO dto) {
        AccountBO info = accountService.login(dto);
        if (info == null)
            return R.fail("登录失败！用户名或密码错误！");


        //生成token
        String token = JwtUtil.sign(
                info.getNativeId(),
                info.getPwd());

        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        redisTemplate.setEx(
                RedisKeyConstant.AUTH_TOKEN + token,
                JSONUtil.toJsonStr(info), 10, TimeUnit.DAYS);
        return R.success("登录成功！", res);
    }
}