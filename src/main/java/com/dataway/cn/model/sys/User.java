package com.dataway.cn.model.sys;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 用户表
 * @author phil
 * @date 2020/05/28 11:07
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tb_user")
@ApiModel(value = "用户实体")
public class User extends Model<User> implements Serializable {
    private static final long serialVersionUID = -7155619049650360470L;
    /**
     * 用户主键
     */
    @TableId("user_no")
    @ApiModelProperty(value = "用户主键")
    private String userNo;
    /**
     * 是电话号码，也是账号（登录用）
     */
    @ApiModelProperty(value = "是电话号码，也是账号（登录用）")
    private String mobile;
    /**
     * 姓名
     */
    @TableField("user_name")
    @ApiModelProperty(value = "姓名")
    private String username;
    /**
     * 密码
     */
    @TableField("pass_word")
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Long createTime;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 状态值（1：启用，2：禁用，3：删除）
     */
    @ApiModelProperty(value = "状态值（1：启用，2：禁用，3：删除）")
    private Integer status;
    /**
     * 职位
     */
    @ApiModelProperty(value = "职位")
    private String job;


    @TableField(exist = false)
    @ApiModelProperty(value = "Token",hidden = true)
    private String token;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色名称",hidden = true)
    private String roleName;


    @Override
    protected Serializable pkVal() {
        return this.userNo;
    }


}
