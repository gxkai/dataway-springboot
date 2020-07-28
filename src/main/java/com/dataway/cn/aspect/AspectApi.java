package com.dataway.cn.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * 装饰器模式,将切面的事抽象为装饰器
 * @author phil
 * @date 2020/6/4 0004 19:49
 */
public interface AspectApi {

    /**
     * 处理一些公共的事
     * @param pjp：切入点
     * @param method：用于反射
     * @return Object
     * @throws Throwable：抛出异常
     */
    Object doHandlerAspect(ProceedingJoinPoint pjp, Method method)throws Throwable;

}
