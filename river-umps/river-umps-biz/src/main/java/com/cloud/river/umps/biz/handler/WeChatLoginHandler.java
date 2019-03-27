package com.cloud.river.umps.biz.handler;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.core.constant.enums.LoginTypeEnum;
import com.cloud.river.umps.api.dto.UserInfo;
import com.cloud.river.umps.api.entity.SysSocialDetails;
import com.cloud.river.umps.api.entity.SysUser;
import com.cloud.river.umps.biz.service.SysSocialDetailsService;
import com.cloud.river.umps.biz.service.SysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 22:21
 **/
@Slf4j
@Component("WX")
@AllArgsConstructor
public class WeChatLoginHandler extends AbstractLoginHandler{
    private final RestTemplate restTemplate;
    private final SysUserService sysUserService;
    private final SysSocialDetailsService sysSocialDetailsService;

    /**
     * 微信登录传入code
     * <p>
     * 通过code 调用qq 获取唯一标识
     *
     * @param code
     * @return
     */
    @Override
    public String identify(String code){
        SysSocialDetails sysSocialDetails = sysSocialDetailsService.getOne(Wrappers.<SysSocialDetails>query().lambda()
                .eq(SysSocialDetails::getType, LoginTypeEnum.WECHAT.getType()));

        String url = String.format(SecurityConstants.WX_AUTHORIZATION_CODE_URL,
                sysSocialDetails.getAppId(), sysSocialDetails.getAppSecret(), code);
        String result = restTemplate.getForObject(url, String.class);
        log.debug("微信响应报文:{}", result);

        String openid = JSONUtil.parseObj(result).getStr("openid");
        return openid;
    }

    @Override
    public UserInfo info(String openId){
        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getWxOpenid, openId));
        if (sysUser == null) {
            log.info("微信未绑定:{}", openId);
            return null;
        }

        return sysUserService.findUserInfo(sysUser);
    }
}
