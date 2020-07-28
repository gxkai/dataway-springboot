package com.dataway.cn.annotation;

import java.lang.annotation.*;

/**
 * 在Controller方法上加入该注解不会验证身份
 * @author phil
 * @date 2020/6/8 0008 9:48
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Pass {

}