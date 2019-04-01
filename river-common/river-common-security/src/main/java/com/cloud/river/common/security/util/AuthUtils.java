package com.cloud.river.common.security.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.google.common.net.HttpHeaders;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @program: RiverCloud
 * @description: 认证授权相关工具类
 * @author: River
 * @create: 2019-04-01 15:08
 **/
@Slf4j
@UtilityClass
public class AuthUtils {
    private final String BASIC_ = "Basic ";

    @SneakyThrows
    public String[] extractAndDecodeHeader(String header){
        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;

        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new RuntimeException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring((delim+1))};
    }

    @SneakyThrows
    public String[] extractAndDecodeHeader(HttpServletRequest request){
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StrUtil.isBlank(header) || !header.startsWith(BASIC_)){
            throw new UnapprovedClientAuthenticationException("请求头中client信息为空");
        }

        return extractAndDecodeHeader(header);
    }
}
