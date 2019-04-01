package com.cloud.river.auth.handler;

import com.cloud.river.common.security.handler.AbstractAuthenticationFailureEvenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-04-01 17:13
 **/
@Slf4j
@Component
public class RiverAuthenticationFailureEvenHandler extends AbstractAuthenticationFailureEvenHandler {
    /**
     * 处理登录失败方法
     * <p>
     *
     * @param authenticationException 登录的authentication 对象
     * @param authentication          登录的authenticationException 对象
     */
    @Override
    public void handle(AuthenticationException authenticationException, Authentication authentication) {
        log.info("用户：{} 登录失败，异常：{}", authentication.getPrincipal(), authenticationException.getLocalizedMessage());
    }
}
