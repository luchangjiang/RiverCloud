package com.cloud.river.umps.biz.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.river.umps.api.entity.SysRouteConf;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 23:57
 **/
public interface SysRouteConfService extends IService<SysRouteConf> {
    /**
     * 获取全部路由
     * <p>
     * RedisRouteDefinitionWriter.java
     * PropertiesRouteDefinitionLocator.java
     *
     * @return
     */
    List<SysRouteConf> routes();

    /**
     * 更新路由信息
     *
     * @param routes 路由信息
     * @return
     */
    Mono<Void> updateRoutes(JSONArray routes);
}
