package com.ec.commons.util.ret;

import com.ec.commons.util.ret.impl.RCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {
    //成功
    private static final RCode DEFAULT_SUCCESS_CODE = RetCode.SUCCESS;
    //失败
    private static final RCode DEFAULT_FAIL_CODE = RetCode.FAILURE;
    /**
     * 200 - 成功
     * 其他 - 失败
     */
    private int code;

    private String message;

    private T data;

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public R(RCode rCode, String message, T data) {
        this(rCode.getCode(), message, data);
    }

    public R(RCode rCode) {
        this(rCode, rCode.getMessage(), null);
    }

    public R(RCode rCode, String message) {
        this(rCode, message, null);
    }

    public R(RCode rCode, T data) {
        this(rCode, rCode.getMessage(), data);
    }

    public R() {
    }


    public static <T> R<T> success() {
        return new R<T>(DEFAULT_SUCCESS_CODE);
    }

    public static <T> R<T> success(T data) {
        return new R<T>(DEFAULT_SUCCESS_CODE, data);
    }

    public static <T> R<T> success(String message) {
        return new R<T>(DEFAULT_SUCCESS_CODE, message);
    }

    public static <T> R<T> success(String message, T data) {
        return new R<T>(DEFAULT_SUCCESS_CODE, message, data);
    }


    public static <T> R<T> fail(String message) {
        return new R<T>(DEFAULT_FAIL_CODE, message);
    }

    public static <T> R<T> fail(int code, String message) {
        return new R<T>(code, message, null);
    }

    public static <T> R<T> fail(T data) {
        return new R<T>(DEFAULT_FAIL_CODE, null, data);
    }

    public static <T> R<T> fail(T data, String message) {
        return new R<T>(DEFAULT_FAIL_CODE, message, data);
    }

    public static <T> R<T> fail(RCode rCode) {
        return new R<T>(rCode);
    }

    public static <T> R<T> fail(RCode rCode, String message) {
        return new R<T>(rCode, message);
    }


}
