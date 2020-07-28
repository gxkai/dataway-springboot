package com.dataway.cn.config.shiro;

import com.dataway.cn.component.SpringContextBeanComponent;
import com.dataway.cn.constant.BaseConstant;
import com.dataway.cn.exception.AuthException;
import com.dataway.cn.model.sys.Menu;
import com.dataway.cn.model.sys.User;
import com.dataway.cn.model.sys.UserToRole;
import com.dataway.cn.result.ResultStatusCode;
import com.dataway.cn.service.sys.IMenuService;
import com.dataway.cn.service.sys.IRoleService;
import com.dataway.cn.service.sys.IUserService;
import com.dataway.cn.service.sys.IUserToRoleService;
import com.dataway.cn.utils.BeanUtil;
import com.dataway.cn.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * MyRealm
 * @author phil
 * @date 2020/06/06 16:09
 */
public class MyRealm extends AuthorizingRealm {

    private IUserService userService;
    private IUserToRoleService userToRoleService;
    private IMenuService menuService;
    private IRoleService roleService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (userToRoleService == null) {
            this.userToRoleService = SpringContextBeanComponent.getBean(IUserToRoleService.class);
        }
        if (menuService == null) {
            this.menuService = SpringContextBeanComponent.getBean(IMenuService.class);
        }
        if (roleService == null) {
            this.roleService = SpringContextBeanComponent.getBean(IRoleService.class);
        }

        String userNo = JwtUtil.getUserNo(principalCollection.toString());
        User user = userService.getById(userNo);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if(null != user){
            UserToRole userToRole = userToRoleService.selectByUserNo(user.getUserNo());

            //控制菜单级别按钮  类中用@RequiresPermissions("user:list") 对应数据库中code字段来控制controller
            ArrayList<String> perList = new ArrayList<>();
            List<Menu> menuList = menuService.findMenuByRoleCode(userToRole.getRoleCode());
            for (Menu per : menuList) {
                if (!BeanUtil.isEmpty(per.getCode())) {
                    perList.add(String.valueOf(per.getCode()));
                }
            }
            Set<String> permission = new HashSet<>(perList);
            simpleAuthorizationInfo.addStringPermissions(permission);
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (userService == null) {
            this.userService = SpringContextBeanComponent.getBean(IUserService.class);
        }
        String token = (String) authenticationToken.getCredentials();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        //有 @Pass注解的方法可以不用登录直接访问，跳过验证
        if (BaseConstant.METHOD_URL_SET.contains(request.getRequestURI())){
            request.setAttribute("currentUser",new User());
            new SimpleAuthenticationInfo(token,token,getName());
        }

        // 解密获得username，用于和数据库进行对比
        String userNo = JwtUtil.getUserNo(token);
        if (userNo == null) {
            //token无效
            throw new AuthException(ResultStatusCode.AUTHORIZATION_INVALID_ERROR);
        }
        //查询当前用户
        User conditionUser = new User();
        conditionUser.setUserNo(userNo);
        conditionUser.setStatus(1);
        User userBean = userService.getUserInfoByUserNo(conditionUser);

        if (userBean == null) {
            //用户不存在
            throw new AuthException(ResultStatusCode.INVALID_USER);
        }
        if (! JwtUtil.verify(token, userNo, userBean.getPassword())) {
            //Authorization
            throw new AuthException(ResultStatusCode.AUTHORIZATION_EXPIRE_ERROR);
        }
        userBean.setToken(token);
        request.setAttribute("currentUser",userBean);
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}
