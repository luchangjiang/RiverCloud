package com.cloud.river.common.data.tenant;

import cn.hutool.core.util.StrUtil;
import com.cloud.river.common.core.constant.CommonConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 11:19
 **/
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextHolderFilter extends GenericFilterBean {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain){
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String tenantId = request.getHeader(CommonConstants.TENANT_ID);
        if(StrUtil.isNotBlank(tenantId)){
            TenantContextHolder.setTenantId(Integer.parseInt(tenantId));
        }
        else{
            TenantContextHolder.setTenantId(1);
        }

        filterChain.doFilter(request, response);
        TenantContextHolder.clear();
    }
}
