package com.dataway.cn.mapper.sys;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataway.cn.model.sys.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色查询菜单
     * @param roleCode 角色主键
     * @return List<Menu>
     */
    List<Menu> findMenuByRoleCode(@Param("roleCode") String roleCode);


    /**
     * 自定义分页查询
     * @param page：current_page,size
     * @param menu:Role实体条件
     * @return Page<Role>
     */
    Page<Menu> getMenusByPage(@Param("page") Page<Menu> page, @Param("menu") Menu menu);
}
