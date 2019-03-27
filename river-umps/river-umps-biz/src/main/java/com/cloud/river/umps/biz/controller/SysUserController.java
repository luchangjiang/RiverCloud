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
import com.cloud.river.umps.api.entity.SysUser;
import com.cloud.river.umps.biz.service.SysUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 系统用户
 *
 * @author River
 * @date 2019-03-25 18:39:03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysuser")
public class SysUserController {

  private final SysUserService sysUserService;

  /**
   * 分页查询
   * @param page 分页对象
   * @param sysUser 系统用户
   * @return
   */
  @GetMapping("/page")
  public R getSysUserPage(Page page, SysUser sysUser) {
    return  new R<>(sysUserService.page(page,Wrappers.query(sysUser)));
  }


  /**
   * 通过id查询系统用户
   * @param userId id
   * @return R
   */
  @GetMapping("/{userId}")
  public R getById(@PathVariable("userId") Integer userId){
    return new R<>(sysUserService.getById(userId));
  }

  /**
   * 新增系统用户
   * @param sysUser 系统用户
   * @return R
   */
  @Sys_Log("新增系统用户")
  @PostMapping
  public R save(@RequestBody SysUser sysUser){
    return new R<>(sysUserService.save(sysUser));
  }

  /**
   * 修改系统用户
   * @param sysUser 系统用户
   * @return R
   */
  @Sys_Log("修改系统用户")
  @PutMapping
  public R updateById(@RequestBody SysUser sysUser){
    return new R<>(sysUserService.updateById(sysUser));
  }

  /**
   * 通过id删除系统用户
   * @param userId id
   * @return R
   */
  @Sys_Log("删除系统用户")
  @DeleteMapping("/{userId}")
  public R removeById(@PathVariable Integer userId){
    return new R<>(sysUserService.removeById(userId));
  }

}
