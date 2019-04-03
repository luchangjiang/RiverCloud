package com.cloud.river.upms.biz.controller;

import com.cloud.river.common.core.util.R;
import com.cloud.river.upms.biz.service.MobileService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: RiverCloud
 * @description: 手机验证码
 * @author: River
 * @create: 2019-04-01 21:08
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/mobile")
@Api(value = "mobile", description = "手机管理模块")
public class MobileController {
    private final MobileService mobileService;

    @GetMapping("/{mobile}")
    public R sendSmsCode(@PathVariable String mobile) {
        return mobileService.sendSmsCode(mobile);
    }
}
