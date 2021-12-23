package com.ec.commons.util;

import cn.hutool.core.bean.BeanUtil;

/**
 * 对象转换
 * */
public class BeanUtils {
    /**
     * 对象转换
     * */
    public static <T, U> U toBean(T t,
                                     Class<U> uClass) {
            return BeanUtil.toBean(t, uClass);
    }
}
