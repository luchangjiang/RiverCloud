package com.cloud.river.common.security.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @program: RiverCloud
 * @description: 扩展用户信息
 * @author: River
 * @create: 2019-03-26 13:31
 **/
public class RiverUser extends User {
    @Getter
    private Integer id;
    @Getter
    private Integer deptId;
    @Getter
    private Integer tenantId;

    public RiverUser(Integer id, Integer detpId, Integer tenantId, String username, String password,
                     boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                     Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.deptId = detpId;
        this.tenantId = tenantId;
    }
}
