package com.ec.commons.filter.exception;

import com.ec.commons.util.ret.RetCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 * 继承RuntimeException主要因为在springboot中事务回滚只能用这个异常才能回滚，
 * 如果不想事务回滚可以继承Exception
 */
public class BaseException extends RuntimeException {

    @Getter
    @Setter
    private int errorCode = RetCode.FAILURE.getCode();

    public BaseException() {
        super();
    }

    public BaseException(Exception e) {
        super(e);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(int errorCode, String message, Throwable t) {
        super(message, t);
        this.errorCode = errorCode;
    }

    public BaseException(String message, Throwable t) {
        super(message, t);
    }

}

