package com.cloud.river.gateway.handler;

import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;

/**
 * @program: RiverCloud
 * @description: Hystrix 降级处理
 * @author: River
 * @create: 2019-03-28 10:16
 **/
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest){
        Optional<Object> originalUris = serverRequest.attribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        originalUris.ifPresent(originalUri->log.error("网关请求失败{}, Hystrix 执行降级处理",originalUri));

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(R.builder()
                    .msg("服务异常")
                    .code(CommonConstants.FAIL)
                    .build()));
    }
}
