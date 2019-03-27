package com.cloud.river.umps.biz.handler;

import com.cloud.river.umps.api.dto.UserInfo;

/**
 * @author River
 * @date 2019/3/27
 * <p>
 * 登录处理器
 */
public interface LoginHandler {
    /**
     * 通过用户传入获取唯一标识
     *
     * @param code
     * @return
     */
    String identify(String code);

    /**
     * 通过openId 获取用户信息
     *
     * @param identify
     * @return
     */
    UserInfo info(String identify);

    /**
     * 处理方法
     *
     * @param code 登录参数
     * @return
     */
    UserInfo handle(String code);
}
