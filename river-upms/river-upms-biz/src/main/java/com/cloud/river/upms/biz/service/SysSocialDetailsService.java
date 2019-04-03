package com.cloud.river.upms.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.river.upms.api.dto.UserInfo;
import com.cloud.river.upms.api.entity.SysSocialDetails;

public interface SysSocialDetailsService extends IService<SysSocialDetails> {
    /**
     * 绑定社交账号
     *
     * @param state 类型
     * @param code  code
     * @return
     */
    Boolean bindSocial(String state, String code);

    /**
     * 根据入参查询用户信息
     *
     * @param inStr
     * @return
     */
    UserInfo getUserInfo(String inStr);
}
