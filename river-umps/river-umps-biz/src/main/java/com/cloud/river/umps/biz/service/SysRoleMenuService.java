package com.cloud.river.umps.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.river.umps.api.entity.SysRoleMenu;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 19:06
 **/
public interface SysRoleMenuService extends IService<SysRoleMenu> {
    /**
     * 更新角色菜单
     *
     * @param roleId  角色
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return
     */
    Boolean saveRoleMenus(Integer roleId, String menuIds);
}
