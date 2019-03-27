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
import com.cloud.river.umps.api.entity.SysUserRole;
import com.cloud.river.umps.biz.service.SysUserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 用户角色
 *
 * @author River
 * @date 2019-03-25 18:38:44
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysuserrole")
public class SysUserRoleController {

  private final SysUserRoleService sysUserRoleService;

  /**
   * 分页查询
   * @param page 分页对象
   * @param sysUserRole 用户角色
   * @return
   */
  @GetMapping("/page")
  public R getSysUserRolePage(Page page, SysUserRole sysUserRole) {
    return  new R<>(sysUserRoleService.page(page,Wrappers.query(sysUserRole)));
  }


  /**
   * 通过id查询用户角色
   * @param userId id
   * @return R
   */
  @GetMapping("/{userId}")
  public R getById(@PathVariable("userId") Integer userId){
    return new R<>(sysUserRoleService.getById(userId));
  }

  /**
   * 新增用户角色
   * @param sysUserRole 用户角色
   * @return R
   */
  @Sys_Log("新增用户角色")
  @PostMapping
  public R save(@RequestBody SysUserRole sysUserRole){
    return new R<>(sysUserRoleService.save(sysUserRole));
  }

  /**
   * 修改用户角色
   * @param sysUserRole 用户角色
   * @return R
   */
  @Sys_Log("修改用户角色")
  @PutMapping
  public R updateById(@RequestBody SysUserRole sysUserRole){
    return new R<>(sysUserRoleService.updateById(sysUserRole));
  }

  /**
   * 通过id删除用户角色
   * @param userId id
   * @return R
   */
  @Sys_Log("删除用户角色")
  @DeleteMapping("/{userId}")
  public R removeById(@PathVariable Integer userId){
    return new R<>(sysUserRoleService.removeById(userId));
  }

}
