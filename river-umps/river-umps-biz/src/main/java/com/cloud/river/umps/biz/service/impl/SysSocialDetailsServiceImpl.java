package com.cloud.river.umps.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.river.common.core.constant.enums.LoginTypeEnum;
import com.cloud.river.common.security.util.SecurityUtils;
import com.cloud.river.umps.api.dto.UserInfo;
import com.cloud.river.umps.api.entity.SysSocialDetails;
import com.cloud.river.umps.api.entity.SysUser;
import com.cloud.river.umps.biz.handler.LoginHandler;
import com.cloud.river.umps.biz.mapper.SysSocialDetailsMapper;
import com.cloud.river.umps.biz.service.SysSocialDetailsService;
import com.cloud.river.umps.biz.service.SysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 21:52
 **/
@Slf4j
@Service
@AllArgsConstructor
public class SysSocialDetailsServiceImpl extends ServiceImpl<SysSocialDetailsMapper, SysSocialDetails> implements SysSocialDetailsService {
    private final SysUserService sysUserService;
    private final Map<String, LoginHandler> loginHandlerMap;
    private final CacheManager cacheManager;
    /**
     * 绑定社交账号
     *
     * @param type 类型
     * @param code  code
     * @return
     */
    public Boolean bindSocial(String type, String code){
        SysUser sysUser=new SysUser();
        sysUser.setUserId(SecurityUtils.getUser().getId());
        String identify = loginHandlerMap.get(type).identify(code);

        if(LoginTypeEnum.WECHAT.getType().equals(type)){
            sysUser.setWxOpenid(identify);
            sysUser.setUpdateTime(LocalDateTime.now());
            sysUserService.updateById(sysUser);
            cacheManager.getCache("user_details").evict(sysUser.getUsername());
        }
        else if(LoginTypeEnum.SMS.getType().equals(type)){
            sysUser.setPhone(identify);
            sysUser.setUpdateTime(LocalDateTime.now());
            sysUserService.updateById(sysUser);
            cacheManager.getCache("user_details").evict(sysUser.getUsername());
        }
        else {
            log.warn("未找到要绑定的类别：{}", type);
        }
        return Boolean.TRUE;
    }

    /**
     * 根据入参查询用户信息
     *
     * @param loginStr
     * @return
     */
    public UserInfo getUserInfo(String loginStr){
        String[] ids = loginStr.split("@");
        return loginHandlerMap.get(ids[0]).handle(ids[1]);
    }
}
