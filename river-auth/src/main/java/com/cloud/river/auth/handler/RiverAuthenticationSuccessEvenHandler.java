package com.cloud.river.auth.handler;

import com.cloud.river.common.security.handler.AbstractAuthenticationSuccessEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-04-01 17:17
 **/
@Slf4j
@Component
public class RiverAuthenticationSuccessEvenHandler extends AbstractAuthenticationSuccessEventHandler {

    /**
     * 处理登录成功方法
     * <p>
     * 获取到登录的authentication 对象
     *
     * @param authentication 登录对象
     */
    @Override
    public void handle(Authentication authentication) {
        log.info("用户：{} 登录成功", authentication.getPrincipal());
    }
}
