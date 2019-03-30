package com.cloud.river.common.security.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.UserInfo;
import com.cloud.river.common.security.service.RiverUser;
import com.cloud.river.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

/**
 * @program: RiverCloud
 * @description: 接口权限判断工具
 * @author: River
 * @create: 2019-03-29 11:25
 **/
@Slf4j
@Component("pms")
@AllArgsConstructor
public class PermissionService {
    /**
     * 判断接口是否有xxx:xxx权限
     *
     * @param permission 权限
     * @return {boolean}
     */
    public Boolean hasPermission(String permission){
        if(StrUtil.isBlank(permission)){
            log.warn("权限名称不能为空");
            return Boolean.FALSE;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText)
                .anyMatch(x-> PatternMatchUtils.simpleMatch(permission, x));
    }
}
