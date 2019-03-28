package com.cloud.river.common.gateway.support;

import org.springframework.context.ApplicationEvent;

/**
 * @program: RiverCloud
 * @description: 路由初始化事件
 * @author: River
 * @create: 2019-03-28 12:36
 **/
public class DynamicRouteInitEvent extends ApplicationEvent {
    public DynamicRouteInitEvent(Object source){
        super(source);
    }
}
