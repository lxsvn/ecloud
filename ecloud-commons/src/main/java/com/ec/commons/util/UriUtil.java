package com.ec.commons.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.impl.CollectionConverter;

import java.util.*;

public class UriUtil {


    /**
     * url参数转转换Map
     *
     * @param queryString :url参数
     * @return Map<?, ?>
     */
    public static Map<String, Object> queryStringToMap(String queryString) {

        queryString = queryString.toUpperCase();
        Map<String, Object> map = new HashMap<String, Object>();
        if (queryString == null || queryString.equals("")) {
            return map;
        }

        String[] text = queryString.split("&");
        for (String str : text) {
            String[] keyText = str.split("=");
            if (keyText.length < 1) {
                continue;
            }
            String key = keyText[0]; // key
            String value = keyText[1]; // value
            map.put(key, value);
        }
        return map;
    }

    /**
     * 获取url中的参数
     */
    public static Object getPara(String queryString, String key) {

        key = key.toUpperCase();
        Map<String, Object> map = queryStringToMap(queryString);

        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return "";
        }
    }

}
