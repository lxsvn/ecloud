package com.ec.commons.util;


import cn.hutool.core.util.IdUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * uuid帮助类
 */
public class UUIDUtil {

    /**
     * 生成用户唯一标识nativeid。
     * 推荐16位，减少存储空间
     */
    public static String generateNativeId(int len) {
        //生成随机uuid
        String uuid = IdUtil.simpleUUID();
        List<Character> charList = uuid.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        // 打乱uuid
        Collections.shuffle(charList);
        // 截取指定长度

        List<Character> newCharList = charList.stream().limit(len).collect(Collectors.toList());
        //char构建字符串
        String x = newCharList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        return x;
    }
}
