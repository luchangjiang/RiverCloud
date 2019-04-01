package com.cloud.river.umps.biz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.common.security.util.SecurityUtils;
import com.cloud.river.umps.api.dto.MenuTree;
import com.cloud.river.umps.api.entity.SysMenu;
import com.cloud.river.umps.api.util.TreeUtil;
import com.cloud.river.umps.api.vo.MenuVO;
import com.cloud.river.umps.biz.service.SysMenuService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: RiverCloud
 * @description: 菜单管理模块
 * @author: River
 * @create: 2019-03-30 14:59
 **/
@RestController
@Slf4j
@AllArgsConstructor
@Api(value="menu", description = "菜单管理模块")
public class MenuController {
    private final SysMenuService sysMenuService;

    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id){ return new R<>(sysMenuService.getById(id)); }

    @GetMapping("/page")
    public R getPage(Page page, SysMenu sysMenu){ return new R<>(sysMenuService.page(page, new QueryWrapper<>(sysMenu))); }

    @GetMapping("/list")
    public R getList(){ return new R<>(sysMenuService.list(Wrappers.emptyWrapper())); }

    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping("/tree")
    public R getTree(){
        return new R<>(TreeUtil.buildTree(sysMenuService.list(Wrappers.emptyWrapper()), -1));
    }

    /**
     * 返回当前用户树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping("/userTree")
    public R getUserMenu(@PathVariable String username){
        Set<MenuVO> menuVOS = new HashSet<>();

        List<Integer> roleIds = SecurityUtils.getRoles();
        roleIds.forEach(roleId -> {
            menuVOS.addAll(sysMenuService.listMenusByRoleId(roleId));
        });

        List<MenuTree> menuTrees = menuVOS.stream().filter(m -> CommonConstants.MENU.equals(m.getType()))
                .map(MenuTree::new)
                .sorted(Comparator.comparingInt(m -> m.getSort()))
                .collect(Collectors.toList());

        return new R<>(TreeUtil.build(menuTrees, -1));
    }

    @GetMapping("/tree/{roleId}")
    public R getListByRoleId(@PathVariable Integer roleId){
        return new R<>(sysMenuService.listMenusByRoleId(roleId).stream()
                .map(MenuVO::getMenuId)
                .collect(Collectors.toList()));
    }

    @Sys_Log("新增菜单")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_add')")
    public R save(@RequestBody SysMenu sysMenu){
        return new R<>(sysMenuService.save(sysMenu));
    }

    @Sys_Log("更新菜单")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_edit')")
    public R updateById(@RequestBody SysMenu sysMenu){
        return new R<>(sysMenuService.updateById(sysMenu));
    }

    @Sys_Log("删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_menu_del')")
    public R updateById(@PathVariable Integer id){
        return new R<>(sysMenuService.removeById(id));
    }
}
