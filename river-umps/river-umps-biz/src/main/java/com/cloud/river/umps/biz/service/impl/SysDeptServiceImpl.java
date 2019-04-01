/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.cloud.river.umps.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.river.common.security.util.SecurityUtils;
import com.cloud.river.umps.api.entity.SysDeptRelation;
import com.cloud.river.umps.biz.mapper.SysDeptMapper;
import com.cloud.river.umps.biz.service.SysDeptRelationService;
import com.cloud.river.umps.biz.service.SysDeptService;
import com.cloud.river.umps.api.dto.DeptTree;
import com.cloud.river.umps.api.entity.SysDept;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理
 *
 * @author River
 * @date 2019-03-26 11:10:39
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    private final SysDeptRelationService sysDeptRelationService;

    public List<DeptTree> getDeptTree(List<SysDept> sysDepts){
        List<DeptTree> deptTrees = sysDepts.stream()
                .map(p->{
                    DeptTree deptTree = new DeptTree();
//                    BeanUtils.copyProperties(p, deptTree);
                    deptTree.setId(p.getDeptId());
                    deptTree.setParentId(p.getParentId());
                    deptTree.setName(p.getName());
                    return  deptTree;
                }).collect(Collectors.toList());
        return deptTrees;
    }
    /**
     * 查询部门树菜单
     *
     * @return 树
     */
    public List<DeptTree> selectTree(){
        return getDeptTree(list(Wrappers.emptyWrapper()));
    }

    /**
     * 查询用户部门树
     *
     * @return
     */
    public List<DeptTree> getUserTree(){
        int deptId = SecurityUtils.getUser().getDeptId();
        List<Integer> descendantIdList = sysDeptRelationService.list(Wrappers.<SysDeptRelation>query().lambda()
                .eq(SysDeptRelation::getAncestor, deptId))
                .stream()
                .map(SysDeptRelation::getDescendant)
                .collect(Collectors.toList());
        return getDeptTree(baseMapper.selectBatchIds(descendantIdList));
    }

    /**
     * 添加信息部门
     *
     * @param sysDept
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDept(SysDept sysDept){
        this.save(sysDept);
        sysDeptRelationService.insertDeptRelation(sysDept);
        return Boolean.TRUE;
    }

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeptById(Integer id){
        //找出所有下级部门
        List<Integer> ids = sysDeptRelationService.list(Wrappers.<SysDeptRelation>query().lambda()
                    .eq(SysDeptRelation::getAncestor, id))
                .stream()
                .map(SysDeptRelation::getDescendant)
                .collect(Collectors.toList());

        if(CollUtil.isNotEmpty(ids)){
            this.removeByIds(ids);
        }

        sysDeptRelationService.deleteAllDeptRealtion(id);
        return Boolean.TRUE;
    }

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDeptById(SysDept sysDept){
        this.updateById(sysDept);

        SysDeptRelation sysDeptRelation = new SysDeptRelation();
        sysDeptRelation.setAncestor(sysDept.getParentId());
        sysDeptRelation.setDescendant(sysDept.getDeptId());
        sysDeptRelationService.updateDeptRealtion(sysDeptRelation);

        return Boolean.TRUE;
    }
}
