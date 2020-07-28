package com.dataway.cn.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Restful接口返回的数据模型
 * @author phil
 * @date 2020/05/22 14:33
 */
@ApiModel(value = "响应实体")
public class WebResult<T> implements Serializable {
    private static final long serialVersionUID = -1241360949457314497L;

    /**
     * 请求是否成功
     */
    @ApiModelProperty(value = "请求是否成功")
    private boolean success;
    /**
     * 返回的代码，200表示成功，其他表示失败
     */
    @ApiModelProperty(value = "返回的代码")
    private int code;
    /**
     * 成功或失败时返回的错误信息
     */
    @ApiModelProperty(value = "提示信息")
    private String msg;
    /**
     * 成功时返回的数据信息
     */
    @ApiModelProperty(value = "返回数据")
    private T result;


    /**
     * 推荐使用此种方法返回
     * @param resultStatusCode 枚举信息
     * @param data 返回数据
     */
    public WebResult(boolean success,ResultStatusCode resultStatusCode, T data){
        this(success,resultStatusCode.getCode(), resultStatusCode.getMsg(), data);
    }
    public WebResult() {
    }

    public WebResult(boolean success, int code, String msg, T result) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public static <T> WebResult<T> of(T obj) {
        WebResult<T> result = new WebResult<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMsg("OK");
        result.setResult(obj);
        return result;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "WebResult{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}
