package com.dataway.cn;

import net.hasor.spring.boot.EnableHasor;
import net.hasor.spring.boot.EnableHasorWeb;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Repository;

/**
 *@author phil
 */
@EnableJms
@EnableHasor()
@EnableHasorWeb()
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "com.dataway.cn.mapper",annotationClass = Repository.class)
public class DatawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatawayApplication.class, args);
    }

}
