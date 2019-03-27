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
package com.cloud.river.umps.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 角色管理
 *
 * @author River
 * @date 2019-03-26 17:27:28
 */
@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends Model<SysRole> {
private static final long serialVersionUID = 1L;

    /**
   * 
   */
    @TableId
    private Integer roleId;
    /**
   * 
   */
    private String roleName;
    /**
   * 
   */
    private String roleCode;
    /**
   * 
   */
    private String roleDesc;
    /**
   * 数据权限类型
   */
    private String dsType;
    /**
   * 数据权限范围
   */
    private String dsScope;
    /**
   * 
   */
    private LocalDateTime createTime;
    /**
   * 
   */
    private LocalDateTime updateTime;
    /**
   * 删除标识（0-正常,1-删除）
   */
    private String delFlag;
    /**
   * 
   */
    private Integer tenantId;
  
}
