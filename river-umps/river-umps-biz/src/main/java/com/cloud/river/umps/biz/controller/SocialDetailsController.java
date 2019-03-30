package com.cloud.river.umps.biz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.umps.api.dto.UserInfo;
import com.cloud.river.umps.api.entity.SysSocialDetails;
import com.cloud.river.umps.biz.service.SysSocialDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-30 11:04
 **/
@Slf4j
@Controller("/social")
@AllArgsConstructor
public class SocialDetailsController {
    private final SysSocialDetailsService sysSocialDetailsService;

    /**
     * 社交登录账户简单分页查询
     *
     * @param page             分页对象
     * @param sysSocialDetails 社交登录
     * @return
     */
    @GetMapping("/page")
    public R getPage(Page page, SysSocialDetails sysSocialDetails){
        return new R<>(sysSocialDetailsService.page(page, new QueryWrapper<>(sysSocialDetails)));
    }

    /**
     * 信息
     *
     * @param id
     * @return R
     */
    @GetMapping("/{id")
    public R getById(@PathVariable int id){
        return new R<>(sysSocialDetailsService.getById(id));
    }

    /**
     * 保存
     *
     * @param sysSocialDetails
     * @return R
     */
    @Sys_Log(("保存第三方信息"))
    @PostMapping
    public R save(@Valid @RequestBody SysSocialDetails sysSocialDetails){
        return new R<>(sysSocialDetailsService.save(sysSocialDetails));
    }

    @Sys_Log("修改第三方信息")
    @PutMapping
    public R update(@Valid @RequestBody SysSocialDetails sysSocialDetails){
        return new R<>(sysSocialDetailsService.updateById(sysSocialDetails));
    }

    /**
     * 绑定社交账号
     *
     * @param state 类型
     * @param code  code
     * @return
     */
    @PostMapping("/bind")
    public R<Boolean> bindSocial(String state, String code){
        return new R<>(sysSocialDetailsService.bindSocial(state,code));
    }

    /**
     * 根据入参查询用户信息
     *
     * @param loginStr (LoginTypeEnum@code)
     * @return
     */
    @GetMapping("/info/{loginStr}")
    public R<UserInfo> getUserInfo(@RequestBody String loginStr){
        return new R<>(sysSocialDetailsService.getUserInfo(loginStr));
    }
}
