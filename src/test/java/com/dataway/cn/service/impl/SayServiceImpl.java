package com.dataway.cn.service.impl;

import com.dataway.cn.service.SayService;

/**
 * @author phil
 * @date 2020/05/25 14:16
 */
public class SayServiceImpl implements SayService {
    @Override
    public void sayHello(String name) {
        System.out.println("hello " + name);
    }

    @Override
    public void saySth() {
        System.out.println("This is say Sth");
    }
}
