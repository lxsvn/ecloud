package com.ec.uid.service.impl;

import com.ec.apis.uid.IUidGeneratorService;
import com.github.wujun234.uid.UidGenerator;
import com.github.wujun234.uid.impl.CachedUidGenerator;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;


/**
 * UID 生成器
 * 使用百度 UID-Generator：
 * 比雪花ID更稳，支持时钟回拨等...
 */
@DubboService
public class UidGeneratorServiceImpl implements IUidGeneratorService {

    @Resource
    private CachedUidGenerator cachedUidGen;


    /**
     * UID 生成器
     * 使用百度 UID-Generator：
     * 比雪花ID更稳，支持时钟回拨等...
     */
    @Override
    public Long getCachedUid() {
        Long uid = cachedUidGen.getUID();
        return uid;
    }
}

