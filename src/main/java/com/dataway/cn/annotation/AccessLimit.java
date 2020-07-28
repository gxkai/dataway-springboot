package com.dataway.cn.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 用于限流
 * 在需要限流的controller方法上加入该注解
 * 再在切面做相应的限流操作
 * @author phil
 * @date 2020/6/10 0010 12:32
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface AccessLimit {

    /**
     * 每秒向桶中放入令牌的数量   默认最大即不做限流
     */
    double perSecond() default Double.MAX_VALUE;

    /**
     * 获取令牌的等待时间  默认0
     */
    int timeOut() default 0;

    /**
     * 超时时间单位,默认是毫秒
     */
    TimeUnit timeOutUnit() default TimeUnit.MILLISECONDS;
}
