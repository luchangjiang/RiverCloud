package com.cloud.river.common.log.event;

import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.umps.api.entity.SysLog;
import com.cloud.river.umps.api.feign.RemoteLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-25 23:31
 **/
@Slf4j
@AllArgsConstructor
public class SysLogListener {
    private final RemoteLogService remoteLogService;

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void SaveSysLog(SysLog sysLog){
        remoteLogService.saveLog(sysLog, SecurityConstants.FROM_IN);
    }
}
