package com.ec.commons.filter.exception;

import com.ec.commons.util.ret.R;
import com.ec.commons.util.ret.RetCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局捕获异常，并将异常按自定义格式返回给前端
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常.
     *
     * @param e
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public R<Object> exception(Exception e) {
        log.error(e.getMessage());
        return R.fail(RetCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }


    /**
     * 获取@NotBlank，@NotNull等注解的message信息
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public R<Object> bindExceptionHandler(BindException e) {
        //获取校验错误信息
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.fail(RetCode.PARAM_VALID_ERROR, message);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上@validate失败后
     * 抛出的异常是javax.validation.ConstraintViolationException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<Object> constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return R.fail(RetCode.PARAM_VALID_ERROR, message);
    }

    /**
     * 处理请求参数格式错误 @RequestBody上@validate失败后
     * 抛出的异常是MethodArgumentNotValidException异常。
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return R.fail(RetCode.PARAM_VALID_ERROR, message);
    }

//    @ExceptionHandler(AuthorizationException.class)
//    public R<Object> handleException(AuthorizationException e) {
////        e.printStackTrace();
//        //获取错误中中括号的内容
//        String message = e.getMessage();
//        String msg=message.substring(message.indexOf("[")+1,message.indexOf("]"));
//        String retMsg = "权限错误";
//        //判断是角色错误还是权限错误
//        if (message.contains("role")) {
//            retMsg = "对不起，您没有" + msg + "角色";
//        } else if (message.contains("permission")) {
//            retMsg = "对不起，您没有" + msg + "权限";
//        }
//        return R.fail(RetCode.UN_AUTHORIZED, retMsg);
//    }


}
