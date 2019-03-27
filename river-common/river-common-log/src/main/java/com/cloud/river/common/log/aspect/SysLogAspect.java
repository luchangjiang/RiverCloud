package com.cloud.river.common.log.aspect;

import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.common.log.event.SysLogEvent;
import com.cloud.river.common.log.util.SysLogUtils;
import com.cloud.river.umps.api.entity.SysLog;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @program: RiverCloud
 * @description:
 * @author: luchangjiang
 * @create: 2019-03-25 20:01
 **/
@Slf4j
@Aspect
@AllArgsConstructor
public class SysLogAspect {
    private final ApplicationEventPublisher publisher;

    @SneakyThrows
    @Around("@annotation(sys_Log)")
    public Object around(ProceedingJoinPoint point, Sys_Log sys_Log) {
        String className = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", className, methodName);

        SysLog sysLog = SysLogUtils.getSysLog();
        sysLog.setTitle(sys_Log.value());
        long  startTime = System.currentTimeMillis();
        Object obj= point.proceed();
        long endTime = System.currentTimeMillis();
        sysLog.setTime(endTime - startTime);

        publisher.publishEvent(new SysLogEvent(sysLog));
        return obj;
    }
}
