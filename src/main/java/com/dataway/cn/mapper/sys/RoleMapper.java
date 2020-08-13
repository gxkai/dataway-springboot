package com.dataway.cn.mapper.sys;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataway.cn.model.sys.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 自定义分页查询
     * @param page：current_page,size
     * @param role:Role实体条件
     * @return Page<Role>
     */
    Page<Role> getRolesByPage(@Param("page") Page<Role> page, @Param("role") Role role);
}
