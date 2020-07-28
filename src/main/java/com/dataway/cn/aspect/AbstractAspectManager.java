package com.dataway.cn.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author phil
 * @date 2020/06/04 19:57
 */
public abstract class AbstractAspectManager implements AspectApi{

    private final AspectApi aspectApi;

    /**
     * 在构造器中初始化 aspectApi
     */
    public AbstractAspectManager(AspectApi aspectApi){
        this.aspectApi = aspectApi;
    }

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable {
        return this.aspectApi.doHandlerAspect(pjp,method);
    }

    /**
     * 抽象方法，用于具体做切面事务
     * @param pjp ：切入点
     * @param method ：用于反射
     * @return Object
     * @throws Throwable：抛出可能的异常
     */
    protected abstract Object executing(ProceedingJoinPoint pjp, Method method) throws Throwable;

}
