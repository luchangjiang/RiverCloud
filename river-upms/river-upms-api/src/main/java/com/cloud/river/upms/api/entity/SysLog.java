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
package com.cloud.river.upms.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统日志
 *
 * @author River
 * @date 2019-03-25 19:53:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLog extends Model<SysLog> {
    private static final long serialVersionUID = 1L;
    /**
   * 编号
   */
    @TableId
    private Long id;
    /**
   * 日志类型
   */
    private String type;
    /**
   * 日志标题
   */
    private String title;
    /**
   * 服务ID
   */
    private String serviceId;
    /**
   * 创建者
   */
    private String createBy;
    /**
   * 创建时间
   */
    private LocalDateTime createTime;
    /**
   * 更新时间
   */
    private LocalDateTime updateTime;
    /**
   * 操作IP地址
   */
    private String remoteAddr;
    /**
   * 用户代理
   */
    private String userAgent;
    /**
   * 请求URI
   */
    private String requestUri;
    /**
   * 操作方式
   */
    private String method;
    /**
   * 操作提交的数据
   */
    private String params;
    /**
   * 执行时间
   */
    private Long time;
    /**
   * 删除标记
   */
    private String delFlag;
    /**
   * 异常信息
   */
    private String exception;
    /**
   * 所属租户
   */
    private Integer tenantId;
  
}
