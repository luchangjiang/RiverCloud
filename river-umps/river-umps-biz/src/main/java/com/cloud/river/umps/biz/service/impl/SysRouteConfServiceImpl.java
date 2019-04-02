package com.cloud.river.umps.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.gateway.vo.RouteDefinitionVo;
import com.cloud.river.umps.api.entity.SysRouteConf;
import com.cloud.river.umps.biz.mapper.SysRouteConfMapper;
import com.cloud.river.umps.biz.service.SysRouteConfService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 23:59
 **/
@Slf4j
@Service
@AllArgsConstructor
public class SysRouteConfServiceImpl extends ServiceImpl<SysRouteConfMapper, SysRouteConf> implements SysRouteConfService {
    private RedisTemplate redisTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;
    /**
     * 获取全部路由
     * <p>
     * RedisRouteDefinitionWriter.java
     * PropertiesRouteDefinitionLocator.java
     *
     * @return
     */
    @Override
    public List<SysRouteConf> routes(){
        return baseMapper.selectList(Wrappers.emptyWrapper());
    }

    /**
     * 更新路由信息
     *
     * @param routes 路由信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> updateRoutes(JSONArray routes){
        // 清空Redis 缓存
        redisTemplate.delete(CommonConstants.ROUTE_KEY);

        List<RouteDefinitionVo> routeDefinitionVos =new ArrayList<>();

        routes.forEach(value -> {
            RouteDefinitionVo vo=new RouteDefinitionVo();
            Map<String, Object> map = (Map)value;
            Object id = map.get("routeId");
            if(id!=null){
                vo.setId(id.toString());
            }

            Object routeName = map.get("routeName");
            if(routeName != null){
                vo.setRouteName(routeName.toString());
            }

            Object predicates = map.get("predicates");
            if(predicates != null){
                List<PredicateDefinition> predicateDefinitions
                        = ((JSONArray)predicates).toList(PredicateDefinition.class);
                vo.setPredicates(predicateDefinitions);
            }

            Object filters = map.get("filters");
            if(filters != null){
                List<FilterDefinition> filterDefinitions =
                        ((JSONArray)filters).toList((FilterDefinition.class));
                vo.setFilters(filterDefinitions);
            }

            Object uri = map.get("uri");
            if(routeName != null){
                vo.setUri(URI.create(uri.toString()));
            }

            Object order = map.get("order");
            if(order != null){
                vo.setOrder(Integer.valueOf(order.toString()));
            }

            redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionVo.class));
            redisTemplate.opsForHash().put(CommonConstants.ROUTE_KEY, vo.getId(), vo);
            routeDefinitionVos.add(vo);
        });

        SysRouteConf condition = new SysRouteConf();
        condition.setDelFlag(CommonConstants.STATUS_NORMAL);
        this.remove(new UpdateWrapper<>(condition));

        List<SysRouteConf> sysRouteConfs
                = routeDefinitionVos.stream().map(route->{
                    SysRouteConf conf = new SysRouteConf();
                    BeanUtil.copyProperties(route, conf);
                    conf.setUpdateTime(LocalDateTime.now());
                    return conf;
                }).collect(Collectors.toList());

        this.saveBatch(sysRouteConfs);
        log.debug("更新网关路由结束 ");

        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        return Mono.empty();
    }
}
