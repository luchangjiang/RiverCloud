package com.cloud.river.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @program: RiverCloud
 * @description: 网关限流配置
 * @author: River
 * @create: 2019-04-08 15:27
 **/
@Configuration
public class RateLimiterConfiguration {
    @Bean(value ="remoteAddrKeyResolver")
    public KeyResolver remoteAddrKeyResolver(){
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostName());
    }
}
