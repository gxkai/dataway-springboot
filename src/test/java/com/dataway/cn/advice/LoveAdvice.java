package com.dataway.cn.advice;

import com.dataway.cn.service.Love;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

/**
 * @author phil
 * @date 2020/05/25 14:37
 */
public class LoveAdvice extends DelegatingIntroductionInterceptor implements Love {
    @Override
    public void display(String name) {
        System.out.println("You are my heart:"+name);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable{
        return super.invoke(invocation);
    }
}
