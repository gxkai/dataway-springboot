package com.dataway.cn.component;

import com.dataway.cn.webservice.impl.TestInterfaceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;

/**
 * webservice 发布
 * @author phil
 * @date 2020/07/14 14:08
 */
@Component
public class WebServer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    @Value("${test-webservice-path}")
    private String testWebServicePath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("开始发布WebService！");
        logger.info("发布地址为："+testWebServicePath);

        Endpoint.publish(testWebServicePath,new TestInterfaceImpl());

        logger.info("发布成功！");
    }
}
