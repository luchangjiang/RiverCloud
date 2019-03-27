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
import com.cloud.river.umps.api.entity.SysDept;
import com.cloud.river.umps.biz.mapper.SysDeptRelationMapper;
import com.cloud.river.umps.biz.service.SysDeptRelationService;
import com.cloud.river.umps.api.entity.SysDeptRelation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门关系
 *
 * @author River
 * @date 2019-03-26 11:13:01
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation> implements SysDeptRelationService {
    private final SysDeptRelationMapper sysDeptRelationMapper;
    /**
     * 维护部门关系
     *
     * @param sysDept 部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertDeptRelation(SysDept sysDept) {
        //找出所有先祖，与当前deptId结成对，存入部门上下级关系表；
        List<SysDeptRelation> sysDeptRelations = sysDeptRelationMapper.selectList(Wrappers.<SysDeptRelation>query().lambda()
                    .eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
                .stream()
                .map(p-> {
                    p.setDescendant(sysDept.getParentId());
                    return p;
                }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(sysDeptRelations)){
            this.saveBatch(sysDeptRelations);
        }

        //自己也要维护到关系表中
        SysDeptRelation sysDeptRelation=new SysDeptRelation();
        sysDeptRelation.setAncestor(sysDept.getDeptId());
        sysDeptRelation.setDescendant(sysDept.getDeptId());
        this.save(sysDeptRelation);
    }

    /**
     * 通过ID删除部门关系
     *
     * @param id
     */
    public void deleteAllDeptRealtion(Integer id){
        sysDeptRelationMapper.deleteDeptRelationsByAncestor(id);
    }

    /**
     * 更新部门关系
     *
     * @param relation
     */
    public void updateDeptRealtion(SysDeptRelation relation){
        sysDeptRelationMapper.updateDeptRelations(relation);
    }
}
