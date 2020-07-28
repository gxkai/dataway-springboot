package com.dataway.cn.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * 切面装饰器的实现，做一些公共处理
 * @author phil
 * @date 2020/06/04 19:53
 */
public class AspectApiImpl implements AspectApi{
    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable {
        System.out.println("This is Decorator");
        return null;
    }
}
