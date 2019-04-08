package com.cloud.river.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.gateway.config.SwaggerProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * @program: RiverCloud
 * @description: 全局过滤器，作用于所有的请求
 * @author: River
 * @create: 2019-04-08 13:10
 **/
@Component
public class RiverRequestGlobalFilter implements GlobalFilter, Ordered {
    private static final String HEADER_NAME = "X-Forwarded-Prefix";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> { httpHeaders.remove(SecurityConstants.FROM); })
                .build();

        // 2. 重写StripPrefix
        addOriginalRequestUrl(exchange, request.getURI());
        String rawPath = request.getURI().getRawPath();
        String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/"))
                .skip(1L).collect(Collectors.joining("/"));

        ServerHttpRequest newRequest = request.mutate().path(newPath).build();
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

        // 3. 支持swagger添加X-Forwarded-Prefix header
        String path = request.getURI().getPath();
        if(StrUtil.endWithIgnoreCase(path, SwaggerProvider.API_URI)){
            return chain.filter(exchange);
        }
        String basePath = path.substring(0, path.lastIndexOf(SwaggerProvider.API_URI));
        return chain.filter(exchange.mutate()
                .request(newRequest.mutate().header(HEADER_NAME, basePath).build())
                .build());
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
