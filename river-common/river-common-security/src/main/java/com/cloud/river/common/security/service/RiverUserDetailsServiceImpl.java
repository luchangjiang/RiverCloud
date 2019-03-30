package com.cloud.river.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.core.util.R;
import com.cloud.river.umps.api.dto.UserInfo;
import com.cloud.river.umps.api.entity.SysUser;
import com.cloud.river.umps.api.feign.RemoteUserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-30 10:50
 **/
@Slf4j
@Service
@AllArgsConstructor
public class RiverUserDetailsServiceImpl implements RiverUserDetailsService {
    private final RemoteUserService remoteUserService;
    private final CacheManager cacheManager;

    /**
     * 用户密码登录
     *
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username){
        Cache cache = cacheManager.getCache("user_details");
        if(cache != null && cache.get(username) !=null){
            return (RiverUser)cache.get(username);
        }

        R<UserInfo> result = remoteUserService.info(username, SecurityConstants.FROM_IN);
        UserDetails userDetails = getUserDetails((UserInfo)result.getData());
        cache.put(username, userDetails);
        return userDetails;
    }

    @Override
    @SneakyThrows
    public UserDetails loadUserBySocial(String loginStr){
        return getUserDetails(remoteUserService.social(loginStr, SecurityConstants.FROM_IN).getData());
    }

    private UserDetails getUserDetails(UserInfo userInfo) {
        if(userInfo == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        Set<String> dbAuthsSet = new HashSet<String>();
        Arrays.stream(userInfo.getRoles()).forEach(roleId->{
            //获取角色
            dbAuthsSet.add(SecurityConstants.ROLE + roleId);
        });
        //获取资源
        dbAuthsSet.addAll(Arrays.asList(userInfo.getPermissions()));

        Collection<? extends GrantedAuthority> authorities =
                AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));

        SysUser sysUser = userInfo.getSysUser();
        Boolean enabled = StrUtil.equals(sysUser.getLockFlag(), CommonConstants.STATUS_NORMAL);
        Boolean locked = StrUtil.equals(sysUser.getLockFlag(), CommonConstants.STATUS_LOCK);
        return new RiverUser(sysUser.getUserId(), sysUser.getDeptId(), sysUser.getTenantId(), sysUser.getUsername(), sysUser.getPassword(),
                enabled, true, true,!locked, authorities);
    }
}
