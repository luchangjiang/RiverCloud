package com.cloud.river.umps.biz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.umps.api.entity.SysDict;
import com.cloud.river.umps.biz.service.SysDictService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @program: RiverCloud
 * @description: 字典管理模块
 * @author: River
 * @create: 2019-03-30 14:38
 **/
@RestController
@Slf4j
@AllArgsConstructor
@Api(value="dict", description = "字典管理模块")
public class DictController {
    private final SysDictService sysDictService;

    @GetMapping("/id")
    public R getById(@PathVariable Integer id){ return new R<>(sysDictService.getById(id)); }

    @GetMapping("/page")
    public R<IPage> getPage(Page page, SysDict sysDict){
        return new R<>(sysDictService.page(page, new QueryWrapper<>(sysDict)));
    }

    @GetMapping("/type/{type}")
    @Cacheable(value="dict_details", key="#type")
    public R getDictByType(@PathVariable String type){
        return new R<>(sysDictService.list(Wrappers.<SysDict>query().lambda()
                .eq(SysDict::getType, type)));
    }

    @Sys_Log("添加字典")
    @PostMapping
    @CacheEvict(value="dict_details", key = "#sysDict.type")
    @PreAuthorize("@pms.hasPermission('sys_dict_add')")
    public R save(@RequestBody SysDict sysDict){
        return new R<>(sysDictService.save(sysDict));
    }

    @Sys_Log("更新字典")
    @PutMapping
    @CacheEvict(value = "dict_details", key="#sysDict.type")
    @PreAuthorize("@pms.hasPermission('sys_dict_edit')")
    public R updateById(@RequestBody SysDict sysDict){
        return new R<>(sysDictService.updateById(sysDict));
    }

    @Sys_Log("删除字典")
    @DeleteMapping
    @CacheEvict(value="dict_details", key = "#sysDict.type")
    @PreAuthorize("@pms.hasPermission('sys_dict_del')")
    public R removeById(@RequestBody SysDict sysDict){
        return new R<>(sysDictService.removeById(sysDict));
    }
}
