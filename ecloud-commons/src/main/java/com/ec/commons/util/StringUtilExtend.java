package com.ec.commons.util;

/**
 * 字符串扩展帮助类
 */
public class StringUtilExtend {
    /**
     * 去空格：去前后空格
     */
    public static String Trim(Object obj) {
        if (obj == null)
            return "";
        return obj.toString().trim();
    }

    /**
     * 是否为空：去前后空格判断
     * */
    public static boolean IsEmpty(Object obj) {
        return Trim(obj) == "";
    }
}
