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
package com.cloud.river.upms.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.river.upms.api.entity.SysUserRole;
import com.cloud.river.upms.biz.mapper.SysUserRoleMapper;
import com.cloud.river.upms.biz.service.SysUserRoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户角色
 *
 * @author River
 * @date 2019-03-25 18:38:44
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public Boolean deleteByUserId(Integer userId){
        return baseMapper.deleteByUserId(userId);
    }
}