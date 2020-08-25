package com.dataway.cn.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author phil
 * @date 2020/08/24 10:37
 */
public class MyTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("temp/tx.xml");
        StudentA studentA = (StudentA) applicationContext.getBean("studentA");
        System.out.println(studentA.getName());
    }
}
