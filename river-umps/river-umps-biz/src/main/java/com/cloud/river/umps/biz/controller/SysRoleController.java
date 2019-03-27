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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.umps.api.entity.SysRole;
import com.cloud.river.umps.biz.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 角色管理
 *
 * @author River
 * @date 2019-03-26 17:27:28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysrole")
public class SysRoleController {

  private final SysRoleService sysRoleService;

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

}
