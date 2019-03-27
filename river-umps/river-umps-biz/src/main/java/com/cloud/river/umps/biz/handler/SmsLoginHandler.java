package com.cloud.river.umps.biz.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cloud.river.umps.api.dto.UserInfo;
import com.cloud.river.umps.api.entity.SysUser;
import com.cloud.river.umps.biz.service.SysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 23:04
 **/
@Slf4j
@Component("SMS")
@AllArgsConstructor
public class SmsLoginHandler extends AbstractLoginHandler {
    private final SysUserService sysUserService;

    /**
     * 验证码登录传入为手机号
     * 不用不处理
     *
     * @param mobile
     * @return
     */
    @Override
    public String identify(String mobile){
        return mobile;
    }

    /**
     * 通过mobile 获取用户信息
     *
     * @param identify
     * @return
     */
    @Override
    public UserInfo info(String mobile){
        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getPhone, mobile));
        if(sysUser==null){
            log.warn("手机号未注册 {}", mobile);
            return null;
        }

        return sysUserService.findUserInfo(sysUser);
    }
}
