package com.dataway.cn.test;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author phil
 * @date 2020/05/23 10:43
 */
public class Test2 {
    @Test
    void test1(){
        Fun1 fun1=(a,b)->{
            System.out.println("=======================================");
            return a+b;
        };
        System.out.println(fun1);
        Integer res =fun1.add(3,4);
        System.out.println(res);

        Converter<String,Integer> convert = Test3::tx;
        Integer convert1 = convert.convert("123");
        Integer cn=Integer.valueOf("123");
        List<String > list=new LinkedList<>();
        list.add("xx");
        list.add("xsx");
        list.add("xxx");
        list.add("xax");

        list.forEach(System.out::println);
        for (String s : list) {
            System.out.println(s);
        }

    }
}
