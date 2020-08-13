package com.dataway.cn.controller;

import com.alibaba.fastjson.JSONObject;
import com.dataway.cn.annotation.AccessLimit;
import com.dataway.cn.annotation.CurrentUser;
import com.dataway.cn.annotation.ValidationParam;
import com.dataway.cn.model.sys.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author phil
 * @date 2020/05/23 9:35
 */
@Api(value = "用户Controller",tags = {"测试接口"})
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "test1/{ userId }",method = RequestMethod.POST)
    @RequiresPermissions(value = {"test:get","admin"},logical = Logical.OR)
    @AccessLimit(perSecond = 10,timeOut = 500)
    @ApiOperation(
            value = "测试接口",
            tags = {"测试接口"},
            produces = "application/json",
            notes = "注：需要header里加入Authorization",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "params",
                    paramType="body",
                    value = "参数以JSON的格式放在这里",
                    defaultValue = "{\"test2\":\"123\",\"test1\":\"345\"}",
                    dataType = "string")
    })
    public JSONObject test1(@PathVariable("userId") Integer userId,
                            //验证params中是否包含了 identity,password 参数
                            @ValidationParam("identity,password")@RequestBody JSONObject params,
                            @ApiIgnore @CurrentUser User user){

        JSONObject object = new JSONObject();
        object.put("userId",userId);

        System.out.println("params:"+params);
        System.out.println("user:"+user);
        return object;
    }
}
