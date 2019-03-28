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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.common.security.util.SecurityUtils;
import com.cloud.river.umps.api.entity.SysUser;
import com.cloud.river.umps.biz.service.SysUserService;
import io.swagger.annotations.Api;
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
@RequestMapping("/user")
@Api(value="user", description = "用户管理模块")
public class SysUserController {
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
}
