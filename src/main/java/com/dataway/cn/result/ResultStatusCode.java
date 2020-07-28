package com.dataway.cn.result;

/**
 * @author phil
 * @date 2020/5/22 0022 14:39
 */
public enum ResultStatusCode {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 操作失败
     */
    ERROR(400,"操作失败"),
    /**
     * 签名错误
     */
    SIGN_ERROR(120, "签名错误"),
    /**
     * 访问超时
     */
    TIME_OUT(130, "访问超时"),
    /**
     * 参数错误
     */
    PARAM_ERROR(140,"参数错误"),
    /**
     * 身份异常
     */
    IDENTIFICATION_ERROR(150,"身份异常"),
    /**
     * 请求头Authorization字段不能为空
     */
    AUTHORIZATION_ERROR(160,"请求头Authorization字段不能为空"),
    /**
     * 权限不足
     */
    PERMISSION_ERROR(170,"权限不足"),
    /**
     * Authorization无效
     */
    AUTHORIZATION_INVALID_ERROR(180,"Authorization无效"),
    /**
     * Authorization已过期,请重新登录
     */
    AUTHORIZATION_EXPIRE_ERROR(190,"Authorization已过期,请重新登录"),
    /**
     * JSON参数异常
     */
    JSON_STRING_ERROR(210,"JSON参数异常"),
    /**
     * 更新角色信息失败
     */
    UPDATE_ROLE_INFO_ERROR(220,"更新角色信息失败"),
    /**
     * 不能修改管理员信息
     */
    ADMIN_ERROR(230,"不能修改管理员信息!"),
    /**
     * 您已经在其他地方登录，请重新登录！
     */
    KICK_OUT(300, "您已经在其他地方登录，请重新登录！"),
    /**
     * 参数解析失败
     */
    BAD_REQUEST(407, "参数解析失败"),
    /**
     * 系统自定义异常
     */
    CUSTOM_EXCEPTION(408, "系统自定义异常"),
    /**
     * 参数异常
     */
    PARAM_JSON_EXCEPTION(409, "参数异常"),
    /**
     * 无效的授权码
     */
    INVALID_TOKEN(401, "无效的授权码"),
    /**
     * 访问地址不存在
     */
    REQUEST_NOT_FOUND(404, "访问地址不存在！"),
    /**
     * 不支持当前请求方法
     */
    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    /**
     * 请求重复提交
     */
    REPEAT_REQUEST_NOT_ALLOWED(406, "请求重复提交"),
    /**
     * 服务器运行异常
     */
    SYSTEM_ERR(500, "服务器运行异常"),
    /**
     * 用户不存在
     */
    INVALID_USER(10000,"用户不存在"),
    /**
     * 用户名或密码错误
     */
    INVALID_USERNAME_PASSWORD(10001,"用户名或密码错误");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResultStatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
