package com.dataway.cn.service.impl.sys;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataway.cn.mapper.sys.UserToRoleMapper;
import com.dataway.cn.model.sys.UserToRole;
import com.dataway.cn.service.sys.IUserToRoleService;
import com.dataway.cn.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserToRoleServiceImpl extends ServiceImpl<UserToRoleMapper, UserToRole> implements IUserToRoleService {
    @Override
    public UserToRole selectByUserNo(String userNo) {
        QueryWrapper<UserToRole> queryWrapper = new QueryWrapper<>();

        UserToRole userToRole = new UserToRole();
        userToRole.setUserNo(userNo);
        queryWrapper.setEntity(userToRole);

        List<UserToRole> userToRoleList = this.list(queryWrapper);
        return BeanUtil.isEmpty(userToRoleList)? null: userToRoleList.get(0);
    }
}
