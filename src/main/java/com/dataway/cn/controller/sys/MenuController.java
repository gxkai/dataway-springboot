package com.dataway.cn.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataway.cn.annotation.AccessLimit;
import com.dataway.cn.annotation.SysLog;
import com.dataway.cn.annotation.ValidationParam;
import com.dataway.cn.model.sys.Menu;
import com.dataway.cn.result.ResponseHelper;
import com.dataway.cn.result.WebResult;
import com.dataway.cn.service.sys.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单前端控制器
 * @author phil
 * @date 2020/06/18 15:42
 */
@Api(value = "菜单Controller",tags = {"菜单接口"})
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuController {

    private final IMenuService iMenuService;

    /**
     *  分页菜单列表
     *  拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @GetMapping("/pageList")
    @SysLog(action="分页获取菜单列表",modelName= "Menu",description="前台带分页条件调用菜单列表接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "菜单分页列表接口",
            tags = {"菜单接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "params",
                    paramType="query",
                    value = "查询条件,请以JSON字符串的形式传递",
                    defaultValue = "{\"page\":{\"current\":1,\"size\":10},\"menu\":{\"menuId\":\"\",\"name\":\"\"}}",
                    required = true,
                    dataType = "string")
    })
    public WebResult<Page<Menu>> getPageList(@ValidationParam("page,menu") @RequestParam(name = "params") String params){

        JSONObject paramsJson = JSONObject.parseObject(params);
        Menu menu = JSONObject.parseObject(paramsJson.getString("menu"),Menu.class);
        Page<Menu> page = JSONObject.parseObject(paramsJson.getString("page"),new TypeReference<Page<Menu>>(){});

        //根据菜单名称查分页
        return ResponseHelper.succeed(iMenuService.getMenusByPage(page,menu));
    }

    /**
     *  获取所有菜单
     *  拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @GetMapping("/all")
    @SysLog(action="获取所有菜单",modelName= "Menu",description="前台调用菜单列表接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "所有菜单接口",
            tags = {"菜单接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "GET")
    public  WebResult<List<Menu>> getAllMenu(){
        return ResponseHelper.succeed(iMenuService.list(new QueryWrapper<>()));
    }

    /**
     *  获取菜单详细信息
     *  拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @GetMapping(value = "/{menuCode}")
    @SysLog(action="获取菜单详细信息",modelName= "Menu",description="前台带菜单编码调用菜单详细信息接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "菜单详细信息接口",
            tags = {"菜单接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "menuCode",
                    paramType="path",
                    value = "菜单主键",
                    required = true,
                    dataType = "string")
    })
    public WebResult<Menu> getById(@PathVariable("menuCode") String menuCode){
        return ResponseHelper.succeed(iMenuService.getById(menuCode));
    }

    /**
     *  删除菜单
     *  拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @SysLog(action="删除菜单",modelName= "Menu",description="前台带菜单编码调用删除菜单接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "菜单删除接口",
            tags = {"菜单接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "menuCode",
                    paramType="path",
                    value = "菜单主键",
                    required = true,
                    dataType = "string")
    })
    @DeleteMapping(value = "/{menuCode}")
    public WebResult<JSONObject> deleteMenu(@PathVariable("menuCode") String menuCode){
        boolean result = iMenuService.removeById(menuCode);
        if (result){
            return ResponseHelper.succeed(null);
        }else {
            return ResponseHelper.failed(null);
        }
    }
}
