package com.cloud.river.common.log.util;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.WebUtils;
import com.cloud.river.upms.api.entity.SysLog;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: RiverCloud
 * @description:
 * @author: luchangjiang
 * @create: 2019-03-25 20:08
 **/
@UtilityClass
public class SysLogUtils {
    public SysLog getSysLog() {
        /*HttpServletRequest request =((ServletRequestAttributes)Objects.
                requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();*/
        HttpServletRequest request = WebUtils.getRequest();

        SysLog sysLog = new SysLog();
        sysLog.setType(CommonConstants.STATUS_NORMAL);
        sysLog.setCreateBy(getUsername());
        sysLog.setServiceId(getClientId());
        sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        sysLog.setRemoteAddr(HttpUtil.getClientIP(request));
        sysLog.setMethod(request.getMethod());
        sysLog.setUserAgent(request.getHeader("user-agent"));
        sysLog.setParams(HttpUtil.toParams(request.getParameterMap()));
        return sysLog;
    }

    private String getClientId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof OAuth2Authentication){
            OAuth2Authentication oAuth2Authentication =(OAuth2Authentication)authentication;
            return oAuth2Authentication.getOAuth2Request().getClientId();
        }
        return null;
    }

    private String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return null;
        }
        return authentication.getName();
    }
}
