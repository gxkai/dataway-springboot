package com.dataway.cn.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dataway.cn.model.sys.User;

import java.util.Map;

/**
 * @author phil
 * @date 2020/05/28 11:11
 */
public interface IUserService extends IService<User> {

    /**
     * 验证手机号和密码
     * @param identity：登录账户
     * @param password: 登录密码
     * @return Map
     * @throws Exception：捕捉异常
     */
    Map<String,Object> checkMobileAndPassword(String identity,String password)throws Exception;

    /**
     * 获取登录用户的菜单信息
     * @param user：登录用户
     * @return Map
     */
    Map<String, Object> getLoginUserAndMenuInfo(User user);

    /**
     * 通过用户编号查询可用（STATUS = 1）的用户
     * @param user：用户实体
     * @return User
     */
    User getUserInfoByUserNo(User user);
}
