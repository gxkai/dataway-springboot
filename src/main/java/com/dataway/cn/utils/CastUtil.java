package com.dataway.cn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 类型转换Util
 * @author phil
 * @date 2020/06/19 14:02
 */
public class CastUtil {

    private static final Logger logger = LoggerFactory.getLogger(CastUtil.class);
    /**
     * Object强转为List
     * @param obj：被转对象
     * @param clazz：List接收对象
     * @param <T>：泛型
     * @return List<T>对象
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz){
        List<T> result = new ArrayList<>();
        if(obj instanceof List<?>){
            for (Object o : (List<?>) obj){
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

}
