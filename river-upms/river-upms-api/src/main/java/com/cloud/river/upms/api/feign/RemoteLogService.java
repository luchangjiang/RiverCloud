package com.cloud.river.upms.api.feign;

import com.cloud.river.upms.api.entity.SysLog;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.core.constant.ServiceNameConstants;
import com.cloud.river.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-26 08:46
 **/
@FeignClient(contextId = "remoteLogService",value = ServiceNameConstants.RIVER_UPMS_BIZ)
public interface RemoteLogService {
    @PostMapping("/log/save")
    R<Boolean> saveLog(@RequestBody SysLog sysLog, @RequestHeader (SecurityConstants.FROM) String from);
}
