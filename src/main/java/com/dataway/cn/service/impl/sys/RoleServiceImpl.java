package com.dataway.cn.service.impl.sys;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataway.cn.constant.BaseConstant;
import com.dataway.cn.exception.CustomException;
import com.dataway.cn.mapper.sys.RoleMapper;
import com.dataway.cn.model.sys.Role;
import com.dataway.cn.model.sys.RoleToMenu;
import com.dataway.cn.result.ResultStatusCode;
import com.dataway.cn.service.sys.IRoleService;
import com.dataway.cn.service.sys.IRoleToMenuService;
import com.dataway.cn.utils.BeanUtil;
import com.dataway.cn.vo.RoleModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMapper roleMapper;

    private final IRoleToMenuService roleToMenuService;

    public RoleServiceImpl(RoleMapper roleMapper, IRoleToMenuService roleToMenuService) {
        this.roleMapper = roleMapper;
        this.roleToMenuService = roleToMenuService;
    }

    /**
     * 分页获取角色列表
     * @param role：角色实体
     * @param page:分页参数 current_page,page_size
     * @return List<Role>
     */
    @Override
    public Page<Role> getRolesByPage(Page<Role> page,Role role) {
        return roleMapper.getRolesByPage(BeanUtil.isEmpty(page)?new Page<>():page, BeanUtil.isEmpty(role)?new Role():role);
    }

    /**
     * 新增角色
     * @exception CustomException:
     * @param roleModel:角色实体
     * @return Role
     */
    @Override
    public boolean addRoleAndPermission(RoleModel roleModel) throws CustomException {
        Role role = new Role();
        BeanUtils.copyProperties(roleModel,role);
        boolean result = this.save(role);
        if (! result) {
            throw  new CustomException(ResultStatusCode.UPDATE_ROLE_INFO_ERROR.getMsg(),ResultStatusCode.UPDATE_ROLE_INFO_ERROR.getCode());
        }
        result = roleToMenuService.saveAll(role.getRoleCode(), roleModel.getMenuCodes());
        return result;
    }


    @Override
    public boolean updateRoleInfo(RoleModel roleModel) throws Exception{
        if (roleModel.getRoleCode().equals(
                this.getOne(new QueryWrapper<Role>().eq("role_name", BaseConstant.RoleType.SYS_ADMIN_ROLE)).getRoleCode())){
            throw  new CustomException(ResultStatusCode.ADMIN_ERROR.getMsg(),ResultStatusCode.ADMIN_ERROR.getCode());
        }
        Role role = this.getById(roleModel.getRoleCode());
        if (BeanUtil.isEmpty(role)) {
            return false;
        }
        BeanUtils.copyProperties(roleModel,role);
        boolean result = this.updateById(role);
        if (! result) {
            throw  new CustomException(ResultStatusCode.UPDATE_ROLE_INFO_ERROR.getMsg(),ResultStatusCode.UPDATE_ROLE_INFO_ERROR.getCode());
        }
        result = roleToMenuService.remove(new QueryWrapper<RoleToMenu>().eq("role_code",roleModel.getRoleCode()));
        if (! result) {
            throw  new CustomException("删除权限信息失败");
        }
        result = roleToMenuService.saveAll(role.getRoleCode(), roleModel.getMenuCodes());
        if (! result) {
            throw  new CustomException("更新权限信息失败");
        }
        return true;

    }
}

