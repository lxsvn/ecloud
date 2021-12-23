package com.ec.commons.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class IdGenerator {

    public static Long getSnowflakeId() {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();
        return id;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            long id = IdGenerator.getSnowflakeId();
            System.out.println(id);
        }
    }
}
