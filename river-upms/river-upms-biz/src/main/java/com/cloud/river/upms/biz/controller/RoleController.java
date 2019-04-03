/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.cloud.river.upms.biz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.upms.api.entity.SysRole;
import com.cloud.river.upms.biz.service.SysRoleMenuService;
import com.cloud.river.upms.biz.service.SysRoleService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 角色管理
 *
 * @author River
 * @date 2019-03-26 17:27:28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Api(value = "role", description = "角色管理模块")
public class RoleController {
    private final SysRoleService sysRoleService;
    private final SysRoleMenuService sysRoleMenuService;
    /**
    * 分页查询
    * @param page 分页对象
    * @param sysRole 角色管理
    * @return
    */
    @GetMapping("/page")
    public R getSysRolePage(Page page, SysRole sysRole) {
        return  new R<>(sysRoleService.page(page,Wrappers.query(sysRole)));
    }


    /**
    * 通过id查询角色管理
    * @param roleId id
    * @return R
    */
    @GetMapping("/{roleId}")
    public R getById(@PathVariable("roleId") Integer roleId){
    return new R<>(sysRoleService.getById(roleId));
    }

    @GetMapping("/list")
    public R listRoles(){ return new R<>(sysRoleService.list(Wrappers.emptyWrapper())); }

    /**
    * 新增角色管理
    * @param sysRole 角色管理
    * @return R
    */
    @Sys_Log("新增角色管理")
    @PostMapping
    public R save(@RequestBody SysRole sysRole){
    return new R<>(sysRoleService.save(sysRole));
    }

    /**
    * 修改角色管理
    * @param sysRole 角色管理
    * @return R
    */
    @Sys_Log("修改角色管理")
    @PutMapping
    public R updateById(@RequestBody SysRole sysRole){
    return new R<>(sysRoleService.updateById(sysRole));
    }

    /**
    * 通过id删除角色管理
    * @param roleId id
    * @return R
    */
    @Sys_Log("删除角色管理")
    @DeleteMapping("/{roleId}")
    public R removeById(@PathVariable Integer roleId){
    return new R<>(sysRoleService.removeById(roleId));
    }

    @Sys_Log(("更新角色菜单"))
    @PostMapping("/menu")
    @PreAuthorize("@pms.hasPermission('sys_role_perm')")
    public R saveRoleMenu(Integer roleId, @RequestParam(value = "menuIds", required = false) String menuIds){
        return new R<>(sysRoleMenuService.saveRoleMenus(roleId, menuIds));
    }
}
