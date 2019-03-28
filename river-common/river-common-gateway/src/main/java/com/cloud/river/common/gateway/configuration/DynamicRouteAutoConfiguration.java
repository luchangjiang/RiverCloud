package com.cloud.river.common.gateway.configuration;

import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: RiverCloud
 * @description: 动态路由配置类
 * @author: River
 * @create: 2019-03-28 12:29
 **/
@Configuration
@ComponentScan("com.cloud.river.common.gateway")
public class DynamicRouteAutoConfiguration {
    /**
     * 配置文件设置为空
     * redis 加载为准
     *
     * @return
     */
    @Bean
    public PropertiesRouteDefinitionLocator propertiesRouteDefinitionLocator(){
        return new PropertiesRouteDefinitionLocator(new GatewayProperties());
    }
}
