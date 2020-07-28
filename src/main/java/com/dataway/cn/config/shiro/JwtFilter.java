package com.dataway.cn.config.shiro;

import com.alibaba.fastjson.JSONObject;
import com.dataway.cn.constant.BaseConstant;
import com.dataway.cn.exception.AuthException;
import com.dataway.cn.model.sys.User;
import com.dataway.cn.result.ResultStatusCode;
import com.dataway.cn.result.WebResult;
import com.dataway.cn.utils.DateTimeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 *
 * 代码的执行流程preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 * @author phil
 * @date 2020/06/08 9:57
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);


    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String requestUri = ((HttpServletRequest) request).getRequestURI();

        //加了 @Pass 注解直接跳过验证,如 login 方法
        if(BaseConstant.METHOD_URL_SET.contains(requestUri)){
            return true;
        }

        //没有加 @Pass注解，判断是否带了 Authorization 字段
        //带了 Authorization 字段，认为是试图登录
        //请求的方法 没有加 @Pass 注解，请求也没有带 Authorization 字段，则不允许访问
        if (isLoginAttempt(request, response)) {
            //Header里面有 Authorization 字段
            long dateLong = System.currentTimeMillis();
            try {
                //执行登录时会提交给 Realm 进行验证处理，验证通过则登录成功
                // 验证不通过，则会抛出异常，在这里对异常进行处理
                executeLogin(request, response);
                logger.info("################################ 身份验证时间: " + DateTimeUtil.formatDateTimeToString(new Date(dateLong)) + "################################");
                logger.info("["+dateLong+"]ERROR: " + "身份验证成功！");
                logger.info("################################ 身份验证时间: " + DateTimeUtil.formatDateTimeToString(new Date(dateLong)) + "################################");

                return true;
            } catch (AuthException e) {

                logger.error("################################ 身份验证时间: " + DateTimeUtil.formatDateTimeToString(new Date(dateLong)) + "################################");
                logger.error("["+dateLong+"]ERROR: " + "身份验证失败！");
                logger.error("["+dateLong+"]ERROR: " + e.getMessage());
                logger.error("################################ 身份验证时间: " + DateTimeUtil.formatDateTimeToString(new Date(dateLong)) + "################################");

                //响应token相关的异常
                responseError(response,e.getResultStatusCode());
            }
        }
        // 在这里响应没有 Authorization 字段的提示
        responseError(response,ResultStatusCode.AUTHORIZATION_ERROR);
        // 没有Authorization字段，不允许进入系统
        return false;
    }

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }

    /**
     * 执行登录
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        JwtToken token = new JwtToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        setUserBean(request);
        return true;
    }

    private void setUserBean(ServletRequest request) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if(principal instanceof User){
            User userBean =(User)principal;
            request.setAttribute("currentUser", userBean);
        }
    }

    /**
     * 非法url返回身份错误信息
     */
    private void responseError(ServletResponse response, ResultStatusCode resultStatusCode) {
        Writer out= null;
        OutputStreamWriter outputStreamWriter =null;
        try {
            outputStreamWriter = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
            response.setContentType("application/json; charset=utf-8");
            out = new BufferedWriter(outputStreamWriter);
            out.write(JSONObject.toJSONString(new WebResult<>(false,resultStatusCode,"")));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null !=  outputStreamWriter){
                try {
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
