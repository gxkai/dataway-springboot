package com.dataway.cn.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author phil
 * @date 2020/06/15 17:27
 */
@ApiModel(value = "角色Model")
public class RoleModel {

    @ApiModelProperty(value = "角色名称",required = true)
    private String roleName;

    @ApiModelProperty(value = "角色编码",required = true)
    private String roleCode;

    @ApiModelProperty(value = "角色所对应的菜单编码List",required = true)
    private List<String> menuCodes;

    public RoleModel() {
    }

    public RoleModel(String roleName, String roleCode, List<String> menuCodes) {
        this.roleName = roleName;
        this.roleCode = roleCode;
        this.menuCodes = menuCodes;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<String> getMenuCodes() {
        return menuCodes;
    }

    public void setMenuCodes(List<String> menuCodes) {
        this.menuCodes = menuCodes;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Override
    public String toString() {
        return "RoleModel{" +
                ", roleName='" + roleName + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", menuCodes=" + menuCodes +
                '}';
    }

}
