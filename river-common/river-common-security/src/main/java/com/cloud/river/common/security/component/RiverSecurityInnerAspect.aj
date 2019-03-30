package com.cloud.river.common.security.component;

import cn.hutool.core.util.StrUtil;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.security.annotation.Inner;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class RiverSecurityInnerAspect {
    private final HttpServletRequest request;

    @SneakyThrows
    @Around("@annotation(inner)")
    public Object around(ProceedingJoinPoint point, Inner inner) {
        String header = request.getHeader(SecurityConstants.FROM);
        if(inner.value() && StrUtil.equals(SecurityConstants.FROM_IN, header)){
            log.warn("没有权限访问内部接口{}", point.getSignature().getName());
            throw new AccessDeniedException("Access is denied");
        }
        return point.proceed();
    }
}
