package com.dataway.cn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Object 转 Map
     * @param obj:被转对象
     * @return Map<?,?>
     */
    public static <String, V> Map<String,V> castMapObject(Object obj, Class<Map<String,V>> clazz){

        if(BeanUtil.isEmpty(obj)){
            return new HashMap<>(16);
        }
        Map<String,V> result = new HashMap<>(16);
        if (obj instanceof Map<?,?>){
            result.putAll(clazz.cast(obj));
            return result;
        }
        return new HashMap<>(16);
    }

}
