package com.dataway.cn.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dataway.cn.model.sys.UserToRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
public interface IUserToRoleService extends IService<UserToRole> {

    /**
     * 根据用户ID查询人员角色
     * @param userNo 用户编号
     * @return  结果
     */
    UserToRole selectByUserNo(String  userNo);
}
