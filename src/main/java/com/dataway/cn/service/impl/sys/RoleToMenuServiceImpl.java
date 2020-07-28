package com.dataway.cn.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataway.cn.mapper.sys.RoleToMenuMapper;
import com.dataway.cn.model.sys.RoleToMenu;
import com.dataway.cn.service.sys.IRoleToMenuService;
import com.dataway.cn.utils.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class RoleToMenuServiceImpl extends ServiceImpl<RoleToMenuMapper, RoleToMenu> implements IRoleToMenuService {

    @Override
    public List<RoleToMenu> selectByRoleCode(String roleCode) {
        QueryWrapper<RoleToMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code",roleCode);
        return this.list(queryWrapper);
    }

    @Override
    public boolean saveAll(String roleCode, List<String> menuCodes) {
        boolean result = true;
        if (!BeanUtil.isEmpty(menuCodes)) {
            List<RoleToMenu> modelList = new ArrayList<>();
            for (String menuCode : menuCodes) {
                modelList.add(RoleToMenu.builder().roleCode(roleCode).menuCode(menuCode).build());
            }
            result = this.saveBatch(modelList);
        }
        return result;
    }

    @Override
    public boolean deleteAllByRoleCode(String roleCode) {
        return removeById(roleCode);
    }
}
