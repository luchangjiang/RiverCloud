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
import com.cloud.river.umps.api.entity.SysDeptRelation;
import com.cloud.river.umps.biz.service.SysDeptRelationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 部门关系
 *
 * @author River
 * @date 2019-03-26 11:13:01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysdeptrelation")
public class SysDeptRelationController {

  private final SysDeptRelationService sysDeptRelationService;

  /**
   * 分页查询
   * @param page 分页对象
   * @param sysDeptRelation 部门关系
   * @return
   */
  @GetMapping("/page")
  public R getSysDeptRelationPage(Page page, SysDeptRelation sysDeptRelation) {
    return  new R<>(sysDeptRelationService.page(page,Wrappers.query(sysDeptRelation)));
  }


  /**
   * 通过id查询部门关系
   * @param ancestor id
   * @return R
   */
  @GetMapping("/{ancestor}")
  public R getById(@PathVariable("ancestor") Integer ancestor){
    return new R<>(sysDeptRelationService.getById(ancestor));
  }

  /**
   * 新增部门关系
   * @param sysDeptRelation 部门关系
   * @return R
   */
  @Sys_Log("新增部门关系")
  @PostMapping
  public R save(@RequestBody SysDeptRelation sysDeptRelation){
    return new R<>(sysDeptRelationService.save(sysDeptRelation));
  }

  /**
   * 修改部门关系
   * @param sysDeptRelation 部门关系
   * @return R
   */
  @Sys_Log("修改部门关系")
  @PutMapping
  public R updateById(@RequestBody SysDeptRelation sysDeptRelation){
    return new R<>(sysDeptRelationService.updateById(sysDeptRelation));
  }

  /**
   * 通过id删除部门关系
   * @param ancestor id
   * @return R
   */
  @Sys_Log("删除部门关系")
  @DeleteMapping("/{ancestor}")
  public R removeById(@PathVariable Integer ancestor){
    return new R<>(sysDeptRelationService.removeById(ancestor));
  }

}
