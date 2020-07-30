package com.dataway.cn.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author phil
 * @date 2020/06/08 10:34
 */
public class StringUtil {
    /**
     * 获取方法中指定注解的value值返回
     * @param method 方法名
     * @param validationParamValue 注解的类名
     * @return String
     */
    public static String getMethodAnnotationOne(Method method, String validationParamValue) {
        String retParam =null;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                String str = annotation.toString();
                if (str.indexOf(validationParamValue) > 0) {
                    retParam = str.substring(str.indexOf("=") + 1, str.indexOf(")"));
                }
            }
        }
        return retParam;
    }
    public static String format(String target, Object... params) {
        return String.format(target, params);
    }
}
