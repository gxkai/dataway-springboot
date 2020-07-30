package com.dataway.cn.result;

import java.io.Serializable;

/**
 * 响应的工具类
 * @author phil
 * @date 2020/06/08 15:27
 */
public class ResponseHelper implements Serializable {
    private static final long serialVersionUID = 7719484759098717132L;

    public static <T> WebResult<T> succeed(T data, String msg) {
        return succeed(true,data,ResultStatusCode.SUCCESS.getCode(), msg);
    }

    public static <T> WebResult<T> succeed(T data) {
        return succeed(true,data,ResultStatusCode.SUCCESS.getCode(), ResultStatusCode.SUCCESS.getMsg());
    }

    public static <T> WebResult<T> succeed(T data, Integer code, String msg) {
        return succeed(true,data, code, msg);
    }

    public static <T> WebResult<T> failed(String msg) {
        return failedWith(false,null, ResultStatusCode.ERROR.getCode(), msg);
    }

    public static <T> WebResult<T> failed(T data, String msg) {
        return failedWith(false,data, ResultStatusCode.ERROR.getCode(), msg);
    }
    public static <T> WebResult<T> failed(T data) {
        return failedWith(false,data, ResultStatusCode.ERROR.getCode(), ResultStatusCode.ERROR.getMsg());
    }
    public static <T> WebResult<T> failed(ResultStatusCode resultStatusCode,T data) {
        return failedWith(false,data, resultStatusCode.getCode(), resultStatusCode.getMsg());
    }
    public static <T> WebResult<T> succeed(boolean success,T data, int code, String msg) {
        return new WebResult<>(success, code, msg, data);
    }
    public static <T> WebResult<T> failedWith(boolean success,T data, Integer code, String msg) {
        return new WebResult<>(success,code, msg, data);
    }
}
