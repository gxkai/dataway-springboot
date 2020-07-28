package com.dataway.cn.mapper.sys;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dataway.cn.model.sys.User;

/**
 * UserMapper
 * @author phil
 * @date 2020/05/28 11:06
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过用户编号查询可用（STATUS = 1）的用户
     * @param user：用户实体
     * @return User
     */
    User getUserInfoByUserNo(User user);
}
