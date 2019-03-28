package com.cloud.river.gateway.handler;

import cn.hutool.core.io.FastByteArrayOutputStream;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.core.util.R;
import com.google.code.kaptcha.Producer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 10:42
 **/
@Slf4j
@Component
@AllArgsConstructor
public class ImageCodeHandler implements HandlerFunction<ServerResponse> {
    private final Producer producer;
    private final RedisTemplate redisTemplate;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest){
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);

        //缓存验证码的信息
        String randomStr = serverRequest.queryParam("randomStr").get();
        redisTemplate.opsForValue().set(CommonConstants.DEFAULT_CODE_KEY+randomStr, text,
                SecurityConstants.CODE_TIME, TimeUnit.SECONDS);

        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image,"jpeg", outputStream);
        } catch (IOException e) {
            log.error("ImageIO write error: {}", e);
            return Mono.error(e);
        }

        return ServerResponse
                .status(CommonConstants.SUCCESS)
                .contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromResource(new ByteArrayResource(outputStream.toByteArray())));
    }
}
