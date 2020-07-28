package com.dataway.cn.annotation;

import java.lang.annotation.*;

/**
 * @author phil
 * @date 2020/6/8 0008 10:23
 */
@Target(ElementType.PARAMETER)          // 可用在方法的参数上
@Retention(RetentionPolicy.RUNTIME)     // 运行时有效
@Documented
public @interface ValidationParam {

    /**
     * 必填参数
     */
    String value() default "";
}
