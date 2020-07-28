package com.dataway.cn.utils;

import com.dataway.cn.constant.BaseConstant;
import com.dataway.cn.model.sys.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 树操作Util
 * @author phil
 * @date 2020/06/09 10:27
 */
public class TreeUtil {

    /**
     * 获取菜单树形结构
     * @param pId:z菜单父ID
     * @param list:菜单List
     * @return List<Menu>
     */
    public static List<Menu> treeMenuList(String pId, List<Menu> list) {
        List<Menu> iteratorMenuList = new ArrayList<>();
        for (Menu m : list) {
            if (String.valueOf(m.getParentId()).equals(pId)) {
                List<Menu> childMenuList = treeMenuList(String.valueOf(m.getMenuId()), list);
                m.setChildMenu(childMenuList);
                if(m.getMenuType().equals(BaseConstant.TYPE_MENU)){
                    iteratorMenuList.add(m);
                }
            }
        }
        return iteratorMenuList;
    }
}
