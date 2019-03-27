package com.cloud.river.umps.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.R;
import com.cloud.river.umps.api.entity.SysMenu;
import com.cloud.river.umps.api.entity.SysRoleMenu;
import com.cloud.river.umps.api.vo.MenuVO;
import com.cloud.river.umps.biz.mapper.SysMenuMapper;
import com.cloud.river.umps.biz.mapper.SysRoleMapper;
import com.cloud.river.umps.biz.mapper.SysRoleMenuMapper;
import com.cloud.river.umps.biz.service.SysMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 15:48
 **/
@Slf4j
@Service
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    @Cacheable(value="menu_details", key = "#roleId+'_menu'")
    public List<MenuVO> listMenusByRoleId(Integer roleId){
        return baseMapper.listMenusByRoleId(roleId);
    }

    @Override
    public List<String> listPermissionsByRoleIds(String roleIds){
        return baseMapper.listPermissionsByRoleIds(roleIds);
    }

    /**
     * 级联删除菜单
     *
     * @param id 菜单ID
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value="menu_details", allEntries = true)
    public R removeMenuById(Integer id){
        List<SysMenu> list = this.list(Wrappers.<SysMenu>query().lambda()
                .eq(SysMenu::getMenuId, id));
        if(CollUtil.isNotEmpty(list)){
            return R.builder()
                    .code(CommonConstants.FAIL)
                    .msg("该菜单含有下级菜单不删除").build();
        }

        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda()
                .eq(SysRoleMenu::getMenuId, id));
        return new R(this.removeById(id));
    }

    /**
     * 更新菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 成功、失败
     */
    @Override
    @CacheEvict(value = "menu_details", allEntries = true)
    public Boolean updateMenuById(SysMenu sysMenu){
        return this.updateById(sysMenu);
    }
}
