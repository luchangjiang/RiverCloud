package com.cloud.river.common.data.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 11:02
 **/
@UtilityClass
public class TenantContextHolder {
    private static final ThreadLocal<Integer> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

    /**
     * 设置租户ID
     *
     * @param tenantId
     */
    public void setTenantId(Integer tenantId){THREAD_LOCAL_TENANT.set(tenantId);}

    /**
     * 获取中的租户ID
     *
     * @return
     */
    public Integer getTenantId() {
        return THREAD_LOCAL_TENANT.get();
    }

    public void clear(){ THREAD_LOCAL_TENANT.remove(); }
}
