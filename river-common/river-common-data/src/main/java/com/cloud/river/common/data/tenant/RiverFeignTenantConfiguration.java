package com.cloud.river.common.data.tenant;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-04-02 16:52
 **/
public class RiverFeignTenantConfiguration {

    @Bean
    public RequestInterceptor riverFeignTenantInterceptor() {return new RiverFeignTenantInterceptor(); }
}
