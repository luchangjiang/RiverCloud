package com.cloud.river.gateway.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 15:57
 **/
@Slf4j
@Component
@AllArgsConstructor
public class SwaggerResourceHandler implements HandlerFunction<ServerResponse> {
    private SwaggerResourcesProvider swaggerProvider;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest){
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(swaggerProvider.get()));
    }
}
