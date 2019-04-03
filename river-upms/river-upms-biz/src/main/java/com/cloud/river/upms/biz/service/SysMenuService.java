package com.cloud.river.upms.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.river.common.core.util.R;
import com.cloud.river.upms.api.entity.SysMenu;
import com.cloud.river.upms.api.vo.MenuVO;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<MenuVO> listMenusByRoleId(Integer roleId);

    List<String> listPermissionsByRoleIds(String roleIds);

    /**
     * 级联删除菜单
     *
     * @param id 菜单ID
     * @return 成功、失败
     */
    R removeMenuById(Integer id);

    /**
     * 更新菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 成功、失败
     */
    Boolean updateMenuById(SysMenu sysMenu);
}
