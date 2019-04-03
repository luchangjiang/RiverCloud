package com.cloud.river.common.log.event;

import com.cloud.river.upms.api.entity.SysLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-25 23:27
 **/
@Getter
@AllArgsConstructor
public class SysLogEvent {
    private final SysLog sysLog;
}
