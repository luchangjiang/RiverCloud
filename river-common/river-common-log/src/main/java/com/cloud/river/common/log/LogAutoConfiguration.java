package com.cloud.river.common.log;

import com.cloud.river.common.log.aspect.SysLogAspect;
import com.cloud.river.common.log.event.SysLogListener;
import com.cloud.river.upms.api.feign.RemoteLogService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @program: RiverCloud
 * @description: auto configuration for log
 * @author: River
 * @create: 2019-03-26 09:32
 **/
@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogAutoConfiguration {
    private final RemoteLogService remoteLogService;

    @Bean
    public SysLogListener saveSysLog(){return new SysLogListener(remoteLogService);}

    @Bean
    public SysLogAspect sysLogAspect(ApplicationEventPublisher publisher){ return  new SysLogAspect(publisher); }
}
