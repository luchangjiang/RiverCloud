package com.cloud.river.common.security.feign;

import cn.hutool.core.collection.CollUtil;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.constant.SecurityConstants;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessTokenJackson1Deserializer;

import java.util.Collection;

/**
 * @program: RiverCloud
 * @description: 扩展OAuth2FeignRequestInterceptor
 * @author: River
 * @create: 2019-03-29 20:44
 **/
@Slf4j
public class RiverFeignClientInterceptor extends OAuth2FeignRequestInterceptor {
    private final OAuth2ClientContext oAuth2ClientContext;
    private final AccessTokenContextRelay accessTokenContextRelay;

    public RiverFeignClientInterceptor(OAuth2ClientContext oAuth2ClientContext,
                                       OAuth2ProtectedResourceDetails resource, AccessTokenContextRelay accessTokenContextRelay){
        super(oAuth2ClientContext, resource);
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.accessTokenContextRelay = accessTokenContextRelay;
    }

    @Override
    public void apply(RequestTemplate template){
        Collection<String> headers = template.headers().get(SecurityConstants.FROM);
        if(CollUtil.isNotEmpty(headers) && headers.contains(SecurityConstants.FROM_IN)){
            return;
        }
        accessTokenContextRelay.copyToken();
        if(oAuth2ClientContext!=null &&
                oAuth2ClientContext.getAccessToken()!=null){
            super.apply(template);
        }
    }
}
