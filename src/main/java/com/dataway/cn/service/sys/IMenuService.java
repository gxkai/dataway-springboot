package com.dataway.cn.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dataway.cn.model.sys.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据角色查询菜单
     * @param roleCode 角色主键
     * @return List<Menu>
     */
    List<Menu> findMenuByRoleCode(String roleCode);

    /**
     * 自定义分页查询
     * @param menu:实体条件
     * @param page：current_page,size
     * @return Page<Role>
     */
    Page<Menu> getMenusByPage(Page<Menu> page,Menu menu);
}
