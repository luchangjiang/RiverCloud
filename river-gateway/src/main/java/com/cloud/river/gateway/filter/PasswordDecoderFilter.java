package com.cloud.river.gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.cloud.river.common.core.constant.SecurityConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @program: RiverCloud
 * @description: 登入过滤器
 * @author: River
 * @create: 2019-04-08 10:29
 **/
@Slf4j
@Component
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory {
    private static final String PASSWORD = "password";
    private static final String KEY_ALGORITHM= "AES";

    @Value("${security.encode.key:1234567812345678}")
    private String encodeKey;

    @SneakyThrows
    private String descryptAES(String password, String encodeKey){
        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(encodeKey.getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(encodeKey.getBytes()));
        byte[] result = aes.decrypt(Base64.decode(password.getBytes(StandardCharsets.UTF_8)));
        return new String(result, StandardCharsets.UTF_8);
    }

    @Override
    public GatewayFilter apply(Object config){
        return (exchange, chain)->{
            ServerHttpRequest request = exchange.getRequest();

            URI uri = request.getURI();
            // 不是登录请求，直接向下执行
            if(StrUtil.containsAnyIgnoreCase(uri.getPath(), SecurityConstants.OAUTH_TOKEN_URL)){
                return chain.filter(exchange);
            }

            String queryParam = uri.getRawQuery();
            Map<String, String> paramMap = HttpUtil.decodeParamMap(queryParam, CharsetUtil.UTF_8);
            String password = paramMap.get(PASSWORD);

            try {
                password = descryptAES(password, encodeKey);
            } catch (Exception e) {
                log.error("密码解密失败 {}", password);
                return Mono.error(e);
            }

            paramMap.put(PASSWORD, password.trim());

            URI newUri = UriComponentsBuilder.fromUri(uri)
                    .replaceQuery(HttpUtil.toParams(paramMap))
                    .build(true)
                    .toUri();

            ServerHttpRequest newRequest = exchange.getRequest().mutate().uri(newUri).build();
            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }
}
