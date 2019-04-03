package com.cloud.river.upms.biz.handler;

import com.cloud.river.upms.api.dto.UserInfo;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 22:18
 **/
public abstract class AbstractLoginHandler implements LoginHandler {
    @Override
    public UserInfo handle(String code){
        String openid = identify(code);
        return info(openid);
    }
}
