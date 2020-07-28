package com.dataway.cn.constant;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.Set;

/**
 * 基础常量定义
 * @author phil
 * @date 2020/06/06 10:03
 */
public class BaseConstant {
    public static final String CUSTOM_EXCEPTION_SEPARATOR = " : -- : ";

    public static Set<String> METHOD_URL_SET = Sets.newConcurrentHashSet();

    /**
     * 令牌通
     * 做限流的时候使用
     * 使用url做为key,存放令牌桶 防止每次重新创建令牌桶
     */
    @SuppressWarnings("UnstableApiUsage")
    public static Map<String, RateLimiter> LIMIT_MAP = Maps.newConcurrentMap();

    /**
     * 菜单类型，1：菜单  2：按钮操作
     */
    public static final Integer TYPE_MENU = 1;

    /**
     * 菜单类型，1：菜单  2：按钮操作
     */
    public static final int TYPE_BUTTON = 2;

    /**
     * 根菜单节点
     */
    public static final String ROOT_MENU = "0";

    /**
     * 角色
     */
    public static class RoleType{
        //超级管理员
        public static final String SYS_ADMIN_ROLE= "sysadmin";
        //管理员
        public static final String ADMIN= "admin";
        //普通用户
        public static final String USER= "user";
    }

}
