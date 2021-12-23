package com.ec.commons.util;

import java.util.Random;

public class SaltUtil {
    /**
     * 生成salt的静态方法
     */
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~!@#$%^&*()_+".toCharArray();
        int length = chars.length;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++){
            char item = chars[new Random().nextInt(length)];
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 获取随机字符串
     * @param n 随机字符串长度
     * @return
     */
    public static String getRandom(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        int length = chars.length;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++){
            char item = chars[new Random().nextInt(length)];
            sb.append(item);
        }
        return sb.toString();
    }
}
