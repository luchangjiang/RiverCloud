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
import com.cloud.river.umps.api.entity.SysDept;
import com.cloud.river.umps.biz.service.SysDeptService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 部门管理
 *
 * @author River
 * @date 2019-03-26 11:10:39
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysdept")
public class SysDeptController {

  private final SysDeptService sysDeptService;

  /**
   * 分页查询
   * @param page 分页对象
   * @param sysDept 部门管理
   * @return
   */
  @GetMapping("/page")
  public R getSysDeptPage(Page page, SysDept sysDept) {
    return  new R<>(sysDeptService.page(page,Wrappers.query(sysDept)));
  }


  /**
   * 通过id查询部门管理
   * @param deptId id
   * @return R
   */
  @GetMapping("/{deptId}")
  public R getById(@PathVariable("deptId") Integer deptId){
    return new R<>(sysDeptService.getById(deptId));
  }

  /**
   * 新增部门管理
   * @param sysDept 部门管理
   * @return R
   */
  @Sys_Log("新增部门管理")
  @PostMapping
  public R save(@RequestBody SysDept sysDept){
    return new R<>(sysDeptService.save(sysDept));
  }

  /**
   * 修改部门管理
   * @param sysDept 部门管理
   * @return R
   */
  @Sys_Log("修改部门管理")
  @PutMapping
  public R updateById(@RequestBody SysDept sysDept){
    return new R<>(sysDeptService.updateById(sysDept));
  }

  /**
   * 通过id删除部门管理
   * @param deptId id
   * @return R
   */
  @Sys_Log("删除部门管理")
  @DeleteMapping("/{deptId}")
  public R removeById(@PathVariable Integer deptId){
    return new R<>(sysDeptService.removeById(deptId));
  }

}
