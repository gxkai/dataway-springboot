package com.dataway.cn.service.impl.sys;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataway.cn.mapper.sys.MenuMapper;
import com.dataway.cn.model.sys.Menu;
import com.dataway.cn.service.sys.IMenuService;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final MenuMapper menuMapper;

    /**
     * 根据角色查询菜单
     * @param roleCode 角色主键
     * @return List<Menu>
     */
    @Override
    public List<Menu> findMenuByRoleCode(String roleCode) {
        return menuMapper.findMenuByRoleCode(roleCode);
    }

    /**
     * 自定义分页查询
     * @param menu:实体条件
     * @param page：current_page,size
     * @return Page<Role>
     */
    @Override
    public Page<Menu> getMenusByPage(Page<Menu> page, Menu menu) {
        return menuMapper.getMenusByPage(BeanUtil.isEmpty(page)?new Page<>():page, BeanUtil.isEmpty(menu)?new Menu():menu);
    }


}
