package com.dataway.cn.webservice;

import com.dataway.cn.webservice.pojo.User;

/**
 * WebService 接口
 * @author phil
 * @date 2020/7/14 0014 14:04
 */
public interface TestInterface {

    /**
     * 测试接口
     * @param color :颜色
     * @return String
     */
    String change(String color);

    String  mapTest(String  list);

    User testObject(User user);

    Object testUser(Object object);
}
