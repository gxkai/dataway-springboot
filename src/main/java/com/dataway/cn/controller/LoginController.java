package com.dataway.cn.controller;

import com.alibaba.fastjson.JSONObject;
import com.dataway.cn.annotation.AccessLimit;
import com.dataway.cn.annotation.Pass;
import com.dataway.cn.annotation.SysLog;
import com.dataway.cn.annotation.ValidationParam;
import com.dataway.cn.result.ResponseHelper;
import com.dataway.cn.result.WebResult;
import com.dataway.cn.service.sys.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 登录接口
 * @author phil
 * @date 2020/06/08 10:12
 */
@Api(value = "登录Controller",tags = {"登录接口"})
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {

    private final IUserService userService;

    /**
     * 登录接口
     * @param requestParam：登录参数，包含 identity,password
     * @return WebResult<Map<String, Object>>
     * @throws Exception：抛出可能的异常，在异常处理器统一处理
     */
    @PostMapping("/login")
    @Pass
    @SysLog(action="SignIn",modelName= "Login",description="前台密码登录接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @ApiOperation(value = "登录接口",
            tags = {"登录接口"},
            produces = "application/json",
            notes = "注：不需要Authorization",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "requestParam",
                    paramType="body",
                    value = "参数以JSON的格式放在这里",
                    defaultValue = "{\"identity\":\"admin\",\"password\":\"123456\"}",
                    dataType = "string")
    })
    public WebResult<Map<String, Object>> login(@ValidationParam("identity,password")@RequestBody JSONObject requestParam) throws Exception{

        return ResponseHelper.succeed(userService.checkMobileAndPassword(requestParam.getString("identity"),requestParam.getString("password")));
    }
}
