package com.cloud.river.gateway.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

/**
 * @program: RiverCloud
 * @description: 基本的请求过滤器
 * @author: River
 * @create: 2019-04-08 10:06
 **/
@Slf4j
@Component
public class HttpBasicGatewayFilter extends AbstractGatewayFilterFactory {
    @Override
    public  GatewayFilter apply(Object config){
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if(!StrUtil.isBlank(auth)){
                return chain.filter(exchange);
            }
            else {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add(HttpHeaders.WWW_AUTHENTICATE, "Basic Realm=\"river\"");
                return response.setComplete();
            }
        };
    }
}
