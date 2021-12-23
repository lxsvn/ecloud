package com.ec.commons.controller;


import com.ec.commons.util.StringUtilExtend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * base controller
 * */
public class BaseController {

    @Resource
    HttpServletRequest request;
    /**
     * 获取请求头中的nativeId
     */
    public String getNativeId() {

        String nativeId =
                StringUtilExtend.Trim(request.getHeader("nativeId"));

        return nativeId;

    }
}
