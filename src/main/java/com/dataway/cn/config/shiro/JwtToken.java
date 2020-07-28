package com.dataway.cn.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Phil
 * @since 2020-06-09
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 4514378897716743038L;
    /**
     * 密钥
     */
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
