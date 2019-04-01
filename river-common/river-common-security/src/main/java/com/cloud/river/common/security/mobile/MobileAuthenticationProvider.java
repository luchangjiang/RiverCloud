package com.cloud.river.common.security.mobile;

import com.cloud.river.common.security.component.RiverPreAuthenticationChecks;
import com.cloud.river.common.security.service.RiverUserDetailsService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * @program: RiverCloud
 * @description: 手机登录校验逻辑
 * 验证码登录、社交登录
 * @author: River
 * @create: 2019-04-01 16:40
 **/
@Slf4j
public class MobileAuthenticationProvider implements AuthenticationProvider {
    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserDetailsChecker detailsChecker = new RiverPreAuthenticationChecks();

    @Getter
    @Setter
    private RiverUserDetailsService userDetailsService;

    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;

        String principal = mobileAuthenticationToken.getPrincipal().toString();
        UserDetails userDetails = userDetailsService.loadUserBySocial(principal);
        if (userDetails == null) {
            log.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.noopBindAccount",
                    "Noop Bind Account"));
        }

        // 检查账号状态
        detailsChecker.check(userDetails);

        MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(mobileAuthenticationToken.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
