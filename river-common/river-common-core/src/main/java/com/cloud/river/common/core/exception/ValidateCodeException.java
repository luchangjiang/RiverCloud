package com.cloud.river.common.core.exception;

/**
 * @program: RiverCloud
 * @description: 验证码失败
 * @author: River
 * @create: 2019-04-08 12:00
 **/
public class ValidateCodeException extends Exception {private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException() {
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
