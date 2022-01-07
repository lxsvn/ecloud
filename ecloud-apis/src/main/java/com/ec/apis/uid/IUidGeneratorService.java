package com.ec.apis.uid;

/**
 * UID 生成器
 * 使用百度 UID-Generator：
 * 比雪花ID更稳，支持时钟回拨等...
 */
public interface IUidGeneratorService {

    /**
     * UID 生成器
     * 使用百度 UID-Generator：
     * 比雪花ID更稳，支持时钟回拨等...
     */
    Long getCachedUid();
}
