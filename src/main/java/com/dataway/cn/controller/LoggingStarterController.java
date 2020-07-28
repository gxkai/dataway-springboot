package com.dataway.cn.controller;

import com.alibaba.fastjson.JSONObject;
import com.starter.service.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author phil
 * @date 2020/05/27 11:13
 */
@RestController
@RequestMapping("logging")
@ApiIgnore
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoggingStarterController {

    private final LoggingService loggingService;
    @RequestMapping(value = "test1/{ a }",method = RequestMethod.GET)
    public JSONObject test1(@PathVariable(" a ") Integer a){
        JSONObject object = new JSONObject();
        object.put("param",loggingService.say());
        return object;
    }
}
