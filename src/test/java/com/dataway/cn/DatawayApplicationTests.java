package com.dataway.cn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class DatawayApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(UUID.randomUUID().toString());
    }

    @Test
    void test1(){
        List<String> languages = Arrays.asList("java","scala","python");
        languages.forEach(System.out::println);
    }

}
