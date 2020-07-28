package com.dataway.cn.test;

/**
 * @author phil
 * @date 2020/5/23 0023 11:14
 */
@FunctionalInterface
public interface Fun1 {
    Integer add(int a,int b);
    default void div(){

    }
}
