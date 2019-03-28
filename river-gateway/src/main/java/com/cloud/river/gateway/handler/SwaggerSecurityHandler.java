package com.cloud.river.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.Optional;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 16:06
 **/
@Slf4j
@Component
public class SwaggerSecurityHandler implements HandlerFunction<ServerResponse> {
    @Autowired(required = false)
    private SecurityConfiguration securityConfiguration;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest){
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(
                        Optional.ofNullable(securityConfiguration)
                            .orElse(SecurityConfigurationBuilder.builder().build())));
    }
}
