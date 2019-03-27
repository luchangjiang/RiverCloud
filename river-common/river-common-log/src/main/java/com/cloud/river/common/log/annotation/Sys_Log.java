package com.cloud.river.common.log.annotation;

import java.lang.annotation.*;

/**
 * @author luchangjiang
 * @date 2019/3/28
 * 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sys_Log {
    /**
     * 描述
     *
     * @return {String}
     */
    String value();
}
