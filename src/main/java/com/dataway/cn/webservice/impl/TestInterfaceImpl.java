package com.dataway.cn.webservice.impl;

import com.dataway.cn.webservice.TestInterface;
import com.dataway.cn.webservice.pojo.User;

import javax.jws.WebService;

/**
 * WebService实现
 * @author phil
 * @date 2020/07/14 14:07
 */
@WebService
public class TestInterfaceImpl implements TestInterface {
    @Override
    public String change(String color) {
        return color;
    }

    @Override
    public String  mapTest(String list) {
        return list;
    }

    @Override
    public User testObject(User user) {
        return user;
    }

    @Override
    public Object testUser(Object object) {
        return object;
    }
}
