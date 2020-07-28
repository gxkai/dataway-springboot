package com.dataway.cn.exception;

import com.dataway.cn.result.ResultStatusCode;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Token相关的异常
 * @author phil
 * @date 2020/06/09 16:26
 */
public class AuthException extends AuthenticationException {

    private static final long serialVersionUID = 5700724682020651960L;
    private final String message;

    private final ResultStatusCode resultStatusCode;

    @Override
    public String getMessage() {
        return message;
    }

    public ResultStatusCode getResultStatusCode() {
        return resultStatusCode;
    }

    public AuthException(ResultStatusCode resultStatusCode) {
        super(resultStatusCode.getMsg());
        this.message = resultStatusCode.getMsg();
        this.resultStatusCode = resultStatusCode;
    }
}
