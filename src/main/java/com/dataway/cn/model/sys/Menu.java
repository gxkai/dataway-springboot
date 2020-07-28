package com.dataway.cn.model.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单表
 * @author phil
 * @date 2020/06/06 16:39
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("tb_menu")
@ApiModel(value = "菜单实体")
public class Menu extends Model<Menu> {

    private static final long serialVersionUID = 1L;
    /**
     * 菜单代号,规范权限标识
     */
    @TableId("menu_code")
    @ApiModelProperty(value = "菜单代号,规范权限标识")
    private String menuCode;
    /**
     * 父菜单主键
     */
    @TableField("parent_id")
    @ApiModelProperty(value = "父菜单主键",required = true)
    private Integer parentId;

    /**
     * 菜单ID
     */
    @TableField("menu_id")
    @ApiModelProperty(value = "菜单ID")
    private Integer menuId;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称",required = true)
    private String name;
    /**
     * 菜单类型，0：菜单  1：业务按钮
     */
    @TableField("menu_type")
    @ApiModelProperty(value = "菜单类型0：菜单,1：业务按钮",required = true)
    private Integer menuType;
    /**
     * 菜单的序号
     */
    @ApiModelProperty(value = "菜单的序号")
    private Integer num;
    /**
     * 菜单地址
     */
    @ApiModelProperty(value = "菜单地址",required = true)
    private String url;

    /**
     * 菜单权限
     */
    @ApiModelProperty(value = "菜单权限",required = true)
    private String code;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @TableField(exist = false)
    private List<Menu> childMenu;

    @Override
    protected Serializable pkVal() {
        return this.menuId;
    }

}