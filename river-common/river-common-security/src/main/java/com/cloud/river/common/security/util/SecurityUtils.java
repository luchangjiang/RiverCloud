package com.cloud.river.common.security.util;

import cn.hutool.core.util.StrUtil;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.security.service.RiverUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-26 13:20
 **/
@UtilityClass
public class SecurityUtils {
    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     *
     * @param authentication
     * @return RiverUser
     * <p>
     * 获取当前用户的全部信息 EnablePigxResourceServer true
     * 获取当前用户的用户名 EnablePigxResourceServer false
     */
    public RiverUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if(principal instanceof RiverUser){
            return (RiverUser)principal;
        }
        return null;
    }
    /**
     * 获取用户
     */
    public RiverUser getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
    }

    /**
     * 获取当前用名
     *
     * @return String
     */
    public String getUsername() {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof String) {
            return principal.toString();
        }
        return null;
    }

    private String getClientId(){
        Authentication authentication = getAuthentication();
        if(authentication instanceof OAuth2Authentication){
            return ((OAuth2Authentication)authentication).getOAuth2Request().getClientId();
        }
        return null;
    }

    /**
     * 获取用户角色信息
     *
     * @return 角色集合
     */
    public List<Integer> getRoles() {
        Authentication authentication = getAuthentication();

        List<Integer> roleIds=new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.stream()
                .filter(p-> StrUtil.startWith(((GrantedAuthority) p).getAuthority(), SecurityConstants.ROLE))
                .forEach(p->{
                    String id = StrUtil.removePrefix(((GrantedAuthority) p).getAuthority(), SecurityConstants.ROLE);
                    roleIds.add(Integer.parseInt(id));
                });

        return roleIds;
    }
}
