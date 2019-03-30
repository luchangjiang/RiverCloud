package com.cloud.river.common.security.feign;

import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.R;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-29 15:14
 **/
@Slf4j
@AllArgsConstructor
public class RiverFeignFallbackMethod implements MethodInterceptor {
    private Class<?> type;
    private Throwable cause;

    @Nullable
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy proxy){
        log.error("Fallback class: [{}] method[{}] message",
                type.getName(), method, cause.getMessage());

        if(R.class == method.getReturnType()){
            final R result = cause instanceof RiverFeignException?
                    ((RiverFeignException)cause).getResult():
                    R.builder().code(CommonConstants.FAIL)
                    .msg(cause.getMessage()).build();
            return result;
        }
        return null;
    }
}
