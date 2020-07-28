package com.dataway.cn.annotation;

import java.lang.annotation.*;

/**
 * 在Controller方法上加入改注解会自动记录日志
 * @author phil
 * @date 2020/6/4 0004 19:37
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface SysLog {

    /**
     * 模块名称
     */
    String modelName() default "";

    /**
     * 操作
     */
    String action()default "";

    /**
     * 描述.
     */
    String description() default "";
}
