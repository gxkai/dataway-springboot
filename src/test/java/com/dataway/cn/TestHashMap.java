package com.dataway.cn;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author phil
 * @date 2020/08/12 13:37
 */
public class TestHashMap {

    private static final Logger logger = LoggerFactory.getLogger(TestHashMap.class);
    @Test
    public void hashMap(){
        Map<Integer,Object> map = new HashMap<>();
        map.put(1,1);
        map.put(5,1);
        map.put(3,1);
        map.put(4,1);
        map.put(2,1);

        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);
        System.out.println(map);

    }
}
