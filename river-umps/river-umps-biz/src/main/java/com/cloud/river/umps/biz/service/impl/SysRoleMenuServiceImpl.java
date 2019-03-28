package com.cloud.river.umps.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.river.umps.api.entity.SysRoleMenu;
import com.cloud.river.umps.biz.mapper.SysRoleMenuMapper;
import com.cloud.river.umps.biz.service.SysRoleMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 19:09
 **/
@Slf4j
@Service
@AllArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    private CacheManager cacheManager;
    /**
     * 更新角色菜单
     *
     * @param role
     * @param roleId  角色
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value="menu_details", key = "#roleId + '_menu'" )
    public Boolean saveRoleMenus(String role, Integer roleId, String menuIds){
        this.remove(Wrappers.<SysRoleMenu>query().lambda()
                .eq(SysRoleMenu::getRoleId, roleId));

        if(StrUtil.isBlank(menuIds)){
            return Boolean.TRUE;
        }

        String[] menus = menuIds.split(",");
        List<SysRoleMenu> sysRoleMenus = Arrays.stream(menus).map(p->{
                    SysRoleMenu sysRoleMenu=new SysRoleMenu();
                    sysRoleMenu.setMenuId(Integer.valueOf(p));
                    sysRoleMenu.setRoleId(roleId);
                    return sysRoleMenu;
                }).collect(Collectors.toList());
        this.saveBatch(sysRoleMenus);

        cacheManager.getCache("user_details").clear();
        return Boolean.TRUE;
    }
}
