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
import com.cloud.river.upms.api.entity.SysLog;
import com.cloud.river.upms.api.vo.LogVO;
import com.cloud.river.upms.biz.service.SysLogService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 系统日志
 *
 * @author River
 * @date 2019-03-25 19:53:37
 */
@RestController
@AllArgsConstructor
@RequestMapping("/log")
@Api(value="log", description = "日志管理模块")
public class LogController {
    private final SysLogService sysLogService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param sysLog 系统日志
    * @return
    */

    @GetMapping("/page")
    public R getSysLogPage(Page page, SysLog sysLog) {
    return  new R<>(sysLogService.page(page, Wrappers.query(sysLog)));
    }


    /**
    * 通过id查询系统日志
    * @param id id
    * @return R
    */
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Long id){
    return new R<>(sysLogService.getById(id));
    }

    /**
    * 新增系统日志
    * @param sysLog 系统日志
    * @return R
    */
    @Sys_Log("新增系统日志")
    @PostMapping("/save")
    public R save(@RequestBody SysLog sysLog){
    return new R<>(sysLogService.save(sysLog));
    }

     /**
     * 批量插入前端异常日志
     *
     * @param logVos 日志实体
     * @return success/false
     */
      @Sys_Log("批量新增系统日志")
      @PostMapping("/logs")
      public R saveBatchLogs(@RequestBody List<LogVO> logVos){
          return new R<>(sysLogService.saveBatchLogs(logVos));
      }
}
