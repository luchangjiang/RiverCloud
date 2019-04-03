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
package com.cloud.river.upms.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.data.datascope.DataScope;
import com.cloud.river.upms.api.dto.UserDTO;
import com.cloud.river.upms.api.dto.UserInfo;
import com.cloud.river.upms.api.entity.SysDept;
import com.cloud.river.upms.api.entity.SysRole;
import com.cloud.river.upms.api.entity.SysUser;
import com.cloud.river.upms.api.entity.SysUserRole;
import com.cloud.river.upms.api.vo.MenuVO;
import com.cloud.river.upms.api.vo.UserVO;
import com.cloud.river.upms.biz.mapper.SysUserMapper;
import com.cloud.river.upms.biz.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @author River
 * @date 2019-03-25 18:39:03
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private final SysRoleService sysRoleService;
    private final SysUserRoleService sysUserRoleService;
    private final SysMenuService sysMenuService;
    private final SysDeptService sysDeptService;

    /**
     * 查询用户信息
     *
     * @param sysUser 用户
     * @return userInfo
     */
    @Override
    public UserInfo findUserInfo(SysUser sysUser){
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        List<Integer> roleIds = sysRoleService.listRolesByUserId(sysUser.getUserId())
                .stream()
                .map(SysRole::getRoleId)
                .collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Integer.class));

        Set<String> permissionSet = new HashSet<>();
        roleIds.forEach(p->{
            permissionSet.addAll(sysMenuService.listMenusByRoleId(p)
                    .stream()
                    .filter(menuVo-> StringUtils.isNotEmpty(menuVo.getPermission()))
                    .map(MenuVO::getPermission)
                    .collect(Collectors.toList()));
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissionSet, String.class));
        return userInfo;
    }

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    @Override
    public IPage getUsersWithRolePage(Page page, UserDTO userDTO){
        return baseMapper.getUserVOsPage(page, userDTO, new DataScope());
    }

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user_details", allEntries = true)
    public Boolean deleteUserById(SysUser sysUser){
        sysUserRoleService.deleteByUserId(sysUser.getUserId());
        this.removeById(sysUser);
        return Boolean.TRUE;
    }

    /**
     * 更新当前用户基本信息
     *
     * @param userDto 用户信息
     * @return Boolean
     */
    @Override
    @CacheEvict(value = "user_details", key = "#userDto.username")
    public R<Boolean> updateUserInfo(UserDTO userDto){
        SysUser sysUser=new SysUser();
//        BeanUtil.copyProperties(userDto, sysUser);
        UserVO userVO = baseMapper.getUserVOByUsername(userDto.getUsername());
        sysUser.setUserId(userVO.getUserId());
        if(StrUtil.isNotBlank(userDto.getPassword())
                && StrUtil.isNotBlank(userVO.getPassword())){
            if(ENCODER.matches(userDto.getPassword(), userVO.getPassword())){
                sysUser.setPassword(ENCODER.encode(userDto.getNewPassword()));
            }
            else {
                log.warn("原密码错误，修改失败 {}", userDto.getUsername());
                return new R<>(Boolean.FALSE, "原密码错误，修改失败");
            }
        }
        sysUser.setPhone(userDto.getPhone());
        sysUser.setAvatar(userDto.getAvatar());

        return new R<>(this.updateById(sysUser));
    }

    /**
     * 更新指定用户信息
     *
     * @param userDto 用户信息
     * @return
     */
    @Override
    @CacheEvict(value = "user_details", key = "#userDto.username")
    public Boolean updateUser(UserDTO userDto){
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(userDto, sysUser);
        if(StrUtil.isNotBlank(userDto.getNewPassword())){
            sysUser.setPassword(ENCODER.encode(userDto.getNewPassword()));
        }
        sysUser.setUpdateTime(LocalDateTime.now());
        this.updateById(sysUser);

        sysUserRoleService.remove(Wrappers.<SysUserRole>query().lambda()
                .eq(SysUserRole::getUserId, userDto.getUserId()));

        userDto.getRoleIds().forEach(p->{
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(p);
            sysUserRole.setUserId(userDto.getUserId());
            sysUserRole.insert();
        });
        return Boolean.TRUE;
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserVO selectUserVoById(Integer id){
        return baseMapper.getUserVOById(id);
    }

    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return R
     */
    @Override
    public List<SysUser> listAncestorUsers(String username){
        Integer deptId = this.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getUsername, username)).getDeptId();
        SysDept sysDept = sysDeptService.getOne(Wrappers.<SysDept>query().lambda()
                .eq(SysDept::getDeptId, deptId));
        if(sysDept == null){
            return null;
        }

        return this.list(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getDeptId, sysDept.getParentId()));
    }

    /**
     * 保存用户信息
     *
     * @param userDto DTO 对象
     * @return success/fail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user_details", allEntries = true)
    public Boolean saveUser(UserDTO userDto){
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(userDto, sysUser);
        if(StringUtils.isNotEmpty(userDto.getNewPassword())){
            sysUser.setPassword(ENCODER.encode(userDto.getNewPassword()));
        }
        sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        sysUser.setUpdateTime(LocalDateTime.now());
        baseMapper.insert(sysUser);

        List<SysUserRole> sysUserRoles = userDto.getRoleIds()
                .stream()
                .map(p->{
                    SysUserRole userRole = new SysUserRole();
                    userRole.setRoleId(p);
                    userRole.setUserId(sysUser.getUserId());
                    return userRole;
                }).collect(Collectors.toList());
        sysUserRoleService.saveBatch(sysUserRoles);
        return Boolean.TRUE;
    }
}
