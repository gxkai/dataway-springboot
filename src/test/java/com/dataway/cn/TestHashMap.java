package com.dataway.cn;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author phil
 * @date 2020/08/12 13:37
 */
public class TestHashMap {

    private static final Logger logger = LoggerFactory.getLogger(TestHashMap.class);
    @Test
    public void hashMap(){

        int defaultCap = 1 << 4;
        int defaultCap2 = 1 << 30;
        String bin = Integer.toBinaryString(1 << 30);

        System.out.println(bin);
        System.out.println(defaultCap2 / 1024 /1024);

        Map<String,String> map = new HashMap<>(16,0.75f);

        Object object = new HashMap<>();

        Map<?,?> map1 = (Map<?, ?>) object;
        map.put("asd","dasd");
        int hashCode = "asd".hashCode();

        System.out.println(hashCode);




        System.out.println(Objects.hashCode(111));

    }

}
