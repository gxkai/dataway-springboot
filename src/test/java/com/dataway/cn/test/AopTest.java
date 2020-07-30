package com.dataway.cn.test;

import com.alibaba.fastjson.JSONObject;
import com.dataway.cn.model.sys.User;
import com.dataway.cn.utils.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author phil
 * @date 2020/05/25 14:24
 */
public class AopTest {

    @Test
    public void test(){

        Map<String,Object> map = new HashMap<>();
        System.out.println(BeanUtil.isEmpty(map));
    }
    @Test
    public void te() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = new User();
        BeanUtils.setProperty(user,"userNo","sdadsd");
        Map<String,String> map = BeanUtils.describe(user);
        System.out.println(map);

        User user1 = new User();
        BeanUtils.copyProperties(user1,user);
        System.out.println(BeanUtils.describe(user1));

        Map<String,Object> map1 = new HashMap<>();
        map1.put("userNo",123);
        map1.put("mobile",123);
        map1.put("password","123");
        map1.put("email",123);
        map1.put("status",1);

        JSONObject jsonObject = new JSONObject();
        BeanUtils.populate(jsonObject,map1);
        System.out.println(jsonObject+"================");

        String[] getArrayProperty =BeanUtils.getArrayProperty(user,"password");
        System.out.println(Arrays.toString(getArrayProperty));
    }


}
