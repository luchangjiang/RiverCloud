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
package com.cloud.river.umps.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.river.umps.api.entity.SysLog;
import com.cloud.river.umps.api.vo.LogVO;

import java.util.List;

/**
 * 系统日志
 *
 * @author River
 * @date 2019-03-25 19:53:37
 */
public interface SysLogService extends IService<SysLog> {

    Boolean saveBatchLogs(List<LogVO> logVos);
}
