package com.cloud.river.gateway.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

/**
 * @program: RiverCloud
 * @description: 演示环境过滤器
 * @author: River
 * @create: 2019-04-08 11:18
 **/
@Slf4j
@Component
public class PreviewGatewayFilter extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config){
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if(StrUtil.equalsIgnoreCase(request.getMethodValue(), HttpMethod.GET.toString())){
                return chain.filter(exchange);
            }

            log.warn("演示环境不能进行操作 -> {}{}", request.getMethodValue(), request.getURI().getPath());
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.LOCKED);
            return response.setComplete();
        });
    }
}
