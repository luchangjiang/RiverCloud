package com.cloud.river.umps.biz.service;

import com.cloud.river.common.core.util.R;

public interface MobileService {
    /**
     * 发送手机验证码
     *
     * @param mobile mobile
     * @return code
     */
    R<Boolean> sendSmsCode(String mobile);
}
