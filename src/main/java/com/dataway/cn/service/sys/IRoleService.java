package com.dataway.cn.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dataway.cn.exception.CustomException;
import com.dataway.cn.model.sys.Role;
import com.dataway.cn.vo.RoleModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
public interface IRoleService extends IService<Role> {

    /**
     * 自定义分页查询
     * @param role:实体条件
     * @param page：current_page,size
     * @return Page<Role>
     */
    Page<Role> getRolesByPage(Page<Role> page,Role role);

    /**
     * 新增角色
     * @param roleModel：角色实体
     * @return boolean
     * @throws CustomException：捕捉可能的异常
     */
    boolean addRoleAndPermission(RoleModel roleModel) throws CustomException;

    /**
     * 更新色以及角色权限信息
     * @param roleModel：角色实体
     * @return boolean
     * @throws Exception CustomException:捕捉可能的异常
     */
    boolean updateRoleInfo(RoleModel roleModel)throws Exception;
}
