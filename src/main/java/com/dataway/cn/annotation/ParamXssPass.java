package com.dataway.cn.annotation;

import java.lang.annotation.*;

/**
 *  在Controller方法上加入该注解不会转义参数，
 *  如果不加该注解则会：<script>alert(1)</script> --> &lt;script&gt;alert(1)&lt;script&gt;
 * @author phil
 * @date 2020/6/9 0009 18:07
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface ParamXssPass {
}
