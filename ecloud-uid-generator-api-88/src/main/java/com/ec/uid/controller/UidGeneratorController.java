package com.ec.uid.controller;

import com.ec.apis.uid.IUidGeneratorService;
import com.ec.commons.util.ret.R;
import com.github.wujun234.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * UID 生成器
 * 使用百度 UID-Generator：
 * 比雪花ID更稳，支持时钟回拨等...
 * */
@RestController
@RequestMapping("/uid")
@Slf4j
public class UidGeneratorController {

    @Resource
    private IUidGeneratorService uidGeneratorService;

    @GetMapping(value = "/get")
    public R getCachedUid() {
        Long uid = uidGeneratorService.getCachedUid();

        //System.out.println(cachedUidGenerator.parseUID(uid));
        return R.success("获取成功", uid);
    }

}