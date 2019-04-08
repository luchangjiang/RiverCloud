package com.cloud.river.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.core.constant.enums.LoginTypeEnum;
import com.cloud.river.common.core.exception.ValidateCodeException;
import com.cloud.river.common.core.util.WebUtils;
import com.cloud.river.gateway.config.FilterIgnorePropertiesConfig;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @program: RiverCloud
 * @description: 验证码过滤器
 * @author: River
 * @create: 2019-04-08 11:27
 **/
@Slf4j
@Component
@AllArgsConstructor
public class ValidateCodeGatewayFilter extends AbstractGatewayFilterFactory {
    private static final String SMS_CODE="code";
    private final RedisTemplate redisTemplate;
    private final FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

    @Override
    public GatewayFilter apply(Object config){
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 不是登录请求，直接向下执行
            if(!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(),
                    SecurityConstants.OAUTH_TOKEN_URL, SecurityConstants.MOBILE_TOKEN_URL, SecurityConstants.SOCIAL_TOKEN_URL)){
                return chain.filter(exchange);
            }

            // 刷新token，直接向下执行
            String grantType = request.getQueryParams().getFirst("grant_type").toString();
            if(StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)){
                return chain.filter(exchange);
            }

            // 终端设置不校验， 直接向下执行
            String[] clientIds = WebUtils.getClientId(request);
            if(filterIgnorePropertiesConfig.getClients().contains(clientIds[0])){
                return chain.filter(exchange);
            }
            // 如果是社交登录，判断是否包含SMS
            if(StrUtil.equals(request.getURI().getPath(), SecurityConstants.SOCIAL_TOKEN_URL)){
                String mobile = request.getQueryParams().getFirst("mobile");
                if(StrUtil.containsAny(mobile, LoginTypeEnum.SMS.toString())){
                    checkCode(request);

                }
            }
            else {
                return chain.filter(exchange);
            }

            return chain.filter(exchange);
        });
    }

    /**
     * 检查code
     *
     * @param request
     */
    @SneakyThrows
    public void checkCode(ServerHttpRequest request){
        String code = request.getQueryParams().getFirst(SMS_CODE);
        if(StrUtil.isBlank(code)){
            throw new ValidateCodeException("验证码不能为空");
        }

        String randomStr = request.getQueryParams().getFirst("randomStr");
        if(StrUtil.isBlank(randomStr)){
            randomStr = request.getQueryParams().getFirst("mobile");
        }

        String key = CommonConstants.DEFAULT_CODE_KEY + randomStr;
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        if(!redisTemplate.hasKey(key)){
            throw new ValidateCodeException("验证码不合法");
        }

        Object objCode = redisTemplate.opsForValue().get(key);
        if(objCode == null){
            throw new ValidateCodeException("验证码不合法");
        }

        String saveCode = objCode.toString();
        if(StrUtil.isBlank(saveCode)){
            redisTemplate.delete(key);
            throw new ValidateCodeException("验证码不合法");
        }

        if(!StrUtil.equals(saveCode, code)){
            redisTemplate.delete(key);
            throw new ValidateCodeException("验证码不合法");
        }

        redisTemplate.delete(key);
    }
}
