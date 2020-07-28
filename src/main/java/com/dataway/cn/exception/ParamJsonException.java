package com.dataway.cn.exception;

/**
 * 参数异常
 * @author phil
 * @date 2020/06/08 10:37
 */
public class ParamJsonException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    public ParamJsonException(String message) {
        super(message);
        this.message = message;
    }


}