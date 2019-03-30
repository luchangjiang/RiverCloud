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
package com.cloud.river.umps.biz.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.common.security.annotation.Inner;
import com.cloud.river.common.security.util.SecurityUtils;
import com.cloud.river.umps.api.dto.UserDTO;
import com.cloud.river.umps.api.entity.SysUser;
import com.cloud.river.umps.biz.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 系统用户
 *
 * @author River
 * @date 2019-03-25 18:39:03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Api(value="user", description = "用户管理模块")
public class UserController {
    private final SysUserService sysUserService;

    /**
    * 获取当前用户全部信息
    *
    * @return 用户信息
    */
    @GetMapping(value = {"/info"})
    public R info() {
        String username = SecurityUtils.getUser().getUsername();
        if(StrUtil.isBlank(username)){
            return new R<>(Boolean.FALSE, "用户未登入");
        }

        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getUsername, username));
        if(sysUser == null){
            return new R<>(Boolean.FALSE, "未找到用户信息");
        }
        return new R<>(sysUserService.findUserInfo(sysUser));
    }

    /**
     * 获取指定用户全部信息
     *
     * @return 用户信息
     */
    @Inner
    @GetMapping("/info/{username}")
    public R info(String username){
        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getUsername, username));
        if(sysUser == null){
            return new R<>(Boolean.FALSE, "未找到用户信息");
        }
        return new R<>(sysUserService.findUserInfo(sysUser));
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public R userVO(@PathVariable Integer id){ return new R<>(sysUserService.selectUserVoById(id)); }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return
     */
    @GetMapping("/details/{username}")
    public R user(@PathVariable String username){
        /*SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getUsername, username));*/

        SysUser condition = new SysUser();
        condition.setUsername(username);

        return new R<>(sysUserService.getOne(new QueryWrapper(condition)));
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return R
     */
    @Sys_Log("删除用户信息")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_user_del')")
    @ApiOperation(value="删除用户", notes = "根据ID删除用户")
    @ApiImplicitParam(name="id", value="用户ID", required = true, dataType = "int", paramType = "path")
    public R del(@PathVariable Integer id){
        SysUser sysUser = sysUserService.getById(id);
        return new R<>(sysUserService.deleteUserById(sysUser));
    }

    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param userDTO 查询参数列表
     * @return 用户集合
     */
    @GetMapping("/page")
    public R getUserPage(Page page, UserDTO userDTO){
        return new R<>(sysUserService.getUsersWithRolePage(page, userDTO));
    }

    /**
     * 添加用户
     *
     * @param userDTO 用户信息
     * @return success/false
     */
    @Sys_Log("添加用户")
    @PostMapping("/save")
    @PreAuthorize("@pms.hasPermission('sys_user_add')")
    public R save(UserDTO userDTO) {
        return new R<>(sysUserService.saveUser(userDTO));
    }

    @Sys_Log("修改用户")
    @PutMapping()
    @PreAuthorize("@pms.hasPermission('sys_user_edit')")
    public R updateUser(UserDTO userDTO){
        return sysUserService.updateUserInfo(userDTO);
    }

    @GetMapping("/accessor/{username}")
    public R listAncestorUsers(@PathVariable String username){
        return new R<>(sysUserService.listAncestorUsers(username));
    }
}
