package com.dataway.cn.test;

import com.dataway.cn.service.SayService;
import com.dataway.cn.advice.SayBeforeAndAfterAdvice2;
import com.dataway.cn.service.impl.SayServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author phil
 * @date 2020/05/25 14:24
 */
public class AopTest {

    @Test
    public void test(){

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(new SayServiceImpl());
        proxyFactory.addAdvice(new SayBeforeAndAfterAdvice2());

        SayService sayService = (SayService) proxyFactory.getProxy();

        sayService.sayHello("曹操");
        sayService.saySth();
    }
}
