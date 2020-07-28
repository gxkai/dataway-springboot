package com.dataway.cn.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataway.cn.annotation.AccessLimit;
import com.dataway.cn.annotation.SysLog;
import com.dataway.cn.annotation.ValidationParam;
import com.dataway.cn.model.sys.Role;
import com.dataway.cn.result.ResponseHelper;
import com.dataway.cn.result.WebResult;
import com.dataway.cn.service.sys.IRoleService;
import com.dataway.cn.vo.RoleModel;
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
 * 角色前端控制器
 * @author phil
 * @date 2020/06/11 15:32
 */
@Api(value = "角色Controller",tags = {"角色接口"})
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    private final IRoleService roleService;

    /**
     *  分页角色列表
     *  拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @GetMapping("/pageList")
    @SysLog(action="分页获取角色列表",modelName= "Role",description="前台带分页条件调用角色列表接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "角色分页列表接口",
            tags = {"角色接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "params",
                    paramType="query",
                    value = "查询条件,请以JSON字符串的形式传递",
                    defaultValue = "{\"page\":{\"current\":1,\"size\":10},\"role\":{\"roleCode\":\"\",\"roleName\":\"\"}}",
                    required = true,
                    dataType = "string")
    })
    public WebResult<Page<Role>> getPageList(@ValidationParam("page,role") @RequestParam(name = "params") String params){

        JSONObject paramsJson = JSONObject.parseObject(params);
        Role role = JSONObject.parseObject(paramsJson.getString("role"),Role.class);
        Page<Role> page = JSONObject.parseObject(paramsJson.getString("page"),new TypeReference<Page<Role>>(){});

        //根据姓名查分页
        return ResponseHelper.succeed(roleService.getRolesByPage(page,role));
    }


    /**
     *  获取所有角色
     *  拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @GetMapping("/all")
    @SysLog(action="获取所有角色",modelName= "Role",description="前台调用角色列表接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "所有角色接口",
            tags = {"角色接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "GET")
    public  WebResult<List<Role>> getAllRole(){
        return ResponseHelper.succeed(roleService.list(new QueryWrapper<>()));
    }


    /**
     *  获取角色详细信息
     *  拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @GetMapping(value = "/{roleCode}")
    @SysLog(action="获取角色详细信息",modelName= "Role",description="前台带角色编码调用角色详细信息接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "角色详细信息接口",
            tags = {"角色接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "roleCode",
                    paramType="path",
                    value = "角色主键",
                    required = true,
                    dataType = "string")
    })
    public WebResult<Role> getById(@PathVariable("roleCode") String roleCode){
        return ResponseHelper.succeed(roleService.getById(roleCode));
    }

    /**
     *  删除角色
     *  拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @SysLog(action="删除角色",modelName= "Role",description="前台带角色编码调用删除角色接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "角色删除接口",
            tags = {"角色接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "roleCode",
                    paramType="path",
                    value = "角色主键",
                    required = true,
                    dataType = "string")
    })
    @DeleteMapping(value = "/{roleCode}")
    public WebResult<JSONObject> deleteRole(@PathVariable("roleCode") String roleCode){
        boolean result = roleService.removeById(roleCode);
        if (result){
            return ResponseHelper.succeed(null);
        }else {
            return ResponseHelper.failed(null);
        }
    }

    /**
     *  添加角色
     *  拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @PostMapping
    @SysLog(action="新增角色",modelName= "Role",description="前台带角色信息调用新增角色接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "角色新增接口",
            tags = {"角色接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "POST")
    public WebResult<Boolean> addRole(@RequestBody RoleModel roleModel) throws Exception{
        return ResponseHelper.succeed(roleService.addRoleAndPermission(roleModel));
    }

    /**
     * 修改角色信息
     * 拥有超级管理员或管理员角色的用户可以访问这个接口,换成角色控制权限,改变请看MyRealm.class
     */
    @PutMapping
    @SysLog(action="修改角色",modelName= "Role",description="前台带角色信息调用修改角色接口")
    @AccessLimit(perSecond = 10,timeOut = 500)
    @RequiresPermissions(value = {"admin"},logical = Logical.OR)
    @ApiOperation(value = "角色修改接口",
            tags = {"角色接口"},
            produces = "application/json",
            notes = "注：需要Authorization",
            httpMethod = "PUT")
    public WebResult<Boolean> updateRole(@RequestBody RoleModel roleModel) throws Exception{
        return ResponseHelper.succeed(roleService.addRoleAndPermission(roleModel));
    }
}
