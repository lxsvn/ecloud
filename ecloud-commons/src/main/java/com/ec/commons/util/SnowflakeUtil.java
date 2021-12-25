package com.ec.commons.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 雪花ID 帮助类
 * */
public class SnowflakeUtil {
    private static final int workerId = 1;
    private static final int datacenterId = 1;

    /**
     * 生成一个雪花ID
     * */
    public static long getSnowflakeId() {
        Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        long id = snowflake.nextId();
        return id;
    }
}
