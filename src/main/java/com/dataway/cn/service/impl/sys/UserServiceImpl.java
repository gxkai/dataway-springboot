package com.dataway.cn.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataway.cn.constant.BaseConstant;
import com.dataway.cn.exception.CustomException;
import com.dataway.cn.mapper.sys.InfoToUserMapper;
import com.dataway.cn.mapper.sys.MenuMapper;
import com.dataway.cn.mapper.sys.UserMapper;
import com.dataway.cn.mapper.sys.UserToRoleMapper;
import com.dataway.cn.model.sys.InfoToUser;
import com.dataway.cn.model.sys.Menu;
import com.dataway.cn.model.sys.User;
import com.dataway.cn.model.sys.UserToRole;
import com.dataway.cn.result.ResultStatusCode;
import com.dataway.cn.service.sys.IUserService;
import com.dataway.cn.utils.BeanUtil;
import com.dataway.cn.utils.JwtUtil;
import com.dataway.cn.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author phil
 * @date 2020/05/28 11:11
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    private final InfoToUserMapper infoToUserMapper;

    private final UserToRoleMapper userToRoleMapper;

    private final MenuMapper menuMapper;

    private final UserMapper userMapper;
    @Override
    public Map<String, Object> checkMobileAndPassword(String identity,String password) throws Exception{
        //由于 @ValidationParam注解已经验证过mobile和passWord参数，所以可以直接get使用没毛病。
        InfoToUser infoToUser = infoToUserMapper.selectOne(new QueryWrapper<InfoToUser>().eq("identity_info ", identity));
        if(BeanUtil.isEmpty(infoToUser)){
            throw new CustomException(ResultStatusCode.INVALID_USER.getMsg(),ResultStatusCode.INVALID_USER.getCode());
        }
        //获取登录用户
        User conditionUser = new User();
        conditionUser.setUserNo(infoToUser.getUserNo());
        conditionUser.setStatus(1);
        User user = userMapper.getUserInfoByUserNo(conditionUser);


        if (BeanUtil.isEmpty(user) || !BCrypt.checkpw(password, user.getPassword())) {
            throw new CustomException(ResultStatusCode.INVALID_USERNAME_PASSWORD.getMsg(),ResultStatusCode.INVALID_USERNAME_PASSWORD.getCode());
        }

        return getLoginUserAndMenuInfo(user);
    }

    /**
     * 获取登录用户的菜单信息
     * @param user：登录用户
     * @return Map
     */
    @Override
    public Map<String, Object> getLoginUserAndMenuInfo(User user) {
        Map<String, Object> result = new HashMap<>(16);
        UserToRole userToRole = userToRoleMapper.selectOne(new QueryWrapper<UserToRole>().eq("user_no",user.getUserNo()));
        user.setToken(JwtUtil.sign(user.getUserNo(), user.getPassword()));

        //密码不要反回到前台,但一定要在生成token之后放，否则会导致验证失败
        user.setPassword("");

        result.put("user",user);
        List<Menu> buttonList = new ArrayList<>();
        //根据角色主键查询启用的菜单权限
        List<Menu> menuList = menuMapper.findMenuByRoleCode(userToRole.getRoleCode());
        List<Menu> retMenuList = TreeUtil.treeMenuList(BaseConstant.ROOT_MENU, menuList);
        for (Menu buttonMenu : menuList) {
            if(buttonMenu.getMenuType() == BaseConstant.TYPE_BUTTON){
                buttonList.add(buttonMenu);
            }
        }
        result.put("menuList",retMenuList);
        result.put("buttonList",buttonList);
        return result;
    }

    /**
     * 通过用户编号查询可用（STATUS = 1）的用户
     * @param user：用户实体
     * @return User
     */
    @Override
    public User getUserInfoByUserNo(User user) {
        return userMapper.getUserInfoByUserNo(user);
    }
}
