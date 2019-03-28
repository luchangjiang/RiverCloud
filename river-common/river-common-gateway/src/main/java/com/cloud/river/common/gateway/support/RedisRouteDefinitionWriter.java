package com.cloud.river.common.gateway.support;

import cn.hutool.core.bean.BeanUtil;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.gateway.vo.RouteDefinitionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 12:39
 **/
@Slf4j
@Component
@AllArgsConstructor
public class RedisRouteDefinitionWriter implements RouteDefinitionRepository {
    private final RedisTemplate redisTemplate;

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route){
        return route.flatMap(p->{
            RouteDefinitionVo vo=new RouteDefinitionVo();
            BeanUtils.copyProperties(p, vo);
            log.info("保存路由信息{}", vo);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.opsForHash().put(CommonConstants.ROUTE_KEY, vo.getId(), vo);

            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId){
        routeId.subscribe(id->{
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.opsForHash().delete(CommonConstants.ROUTE_KEY, id);
        });
        return Mono.empty();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions(){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionVo.class));
        List<RouteDefinitionVo> vos = redisTemplate.opsForHash().values(CommonConstants.ROUTE_KEY);
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        vos.stream().map(vo->{
            RouteDefinition routeDefinition=new RouteDefinition();
            BeanUtil.copyProperties(vo, routeDefinition);
            return routeDefinition;
        }).collect(Collectors.toList());

        return Flux.fromIterable(routeDefinitions);
    }
}
