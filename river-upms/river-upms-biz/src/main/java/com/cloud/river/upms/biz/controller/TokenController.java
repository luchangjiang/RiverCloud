package com.cloud.river.upms.biz.controller;

import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.upms.api.feign.RemoteTokenService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: RiverCloud
 * @description: 令牌管理模块
 * @author: River
 * @create: 2019-03-30 14:04
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/token")
@Api(value="token", description="令牌管理模块")
public class TokenController {
    private final RemoteTokenService remoteTokenService;

    /**
     * 分页token 信息
     *
     * @param params 参数集
     * @return token集合
     */
    @GetMapping("/page")
    public R getPage(@RequestParam Map<String, Object> params){
        return remoteTokenService.getTokenPage(params, SecurityConstants.FROM_IN);
    }

    /**
     * 删除
     *
     * @param token getTokenPage
     * @return success/false
     */
    @Sys_Log("删除用户token")
    @DeleteMapping("/{token}")
    @PreAuthorize("@pms.hasPermission('sys_token_del')")
    public R removeToken(@PathVariable String token){
        return remoteTokenService.removeTokenById(token, SecurityConstants.FROM_IN);
    }
}
