package com.cloud.river.common.data.tenant;

import com.cloud.river.common.core.constant.CommonConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: RiverCloud
 * @description: 租户信息拦截
 * @author: River
 * @create: 2019-04-02 16:48
 **/
@Slf4j
public class RiverFeignTenantInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate){
        if(TenantContextHolder.getTenantId()==null){
            log.error("TTL 中的 租户ID为空，feign拦截器 >> 增强失败");
            return;
        }

        requestTemplate.header(CommonConstants.TENANT_ID, TenantContextHolder.getTenantId().toString());
    }
}
