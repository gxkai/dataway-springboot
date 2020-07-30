package com.dataway.cn.exception;

import com.dataway.cn.constant.BaseConstant;

/**
 * 自定义异常
 * @author phil
 * @date 2020/06/06 9:50
 */
public class CustomException extends Exception {

    private static final long serialVersionUID = -5940450024522723131L;

    public CustomException(String message){
        super(message);
    }
    public CustomException(Throwable throwable) {
        super(throwable);
    }

    public CustomException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CustomException(String msg,String code){
        super(code+ BaseConstant.CUSTOM_EXCEPTION_SEPARATOR +msg);
    }

    public CustomException(String msg,Integer code){
        super(code+ BaseConstant.CUSTOM_EXCEPTION_SEPARATOR +msg);
    }
}
