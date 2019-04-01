package com.cloud.river.common.security.mobile;

import lombok.SneakyThrows;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @program: RiverCloud
 * @description: 手机号登录令牌
 * @author: River
 * @create: 2019-04-01 16:15
 **/
public class MobileAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;

    public MobileAuthenticationToken(String mobile){
        super(null);
        this.principal = mobile;
        setAuthenticated(false);
    }

    public MobileAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal(){ return this.principal; }

    @Override
    public Object getCredentials() { return null; }

    @Override
    @SneakyThrows
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }
}