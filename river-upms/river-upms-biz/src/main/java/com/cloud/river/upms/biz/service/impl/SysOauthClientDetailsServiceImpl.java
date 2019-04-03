package com.cloud.river.upms.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.upms.api.entity.SysOauthClientDetails;
import com.cloud.river.upms.biz.mapper.SysOauthClientDetailsMapper;
import com.cloud.river.upms.biz.service.SysOauthClientDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 19:52
 **/
@Slf4j
@Service
@AllArgsConstructor
public class SysOauthClientDetailsServiceImpl extends ServiceImpl<SysOauthClientDetailsMapper, SysOauthClientDetails> implements SysOauthClientDetailsService {
    /**
     * 通过ID删除客户端
     *
     * @param id
     * @return
     */
    @Override
    @CacheEvict(value = SecurityConstants.CLIENT_DETAILS_KEY, key = "#id")
    public Boolean removeClientDetailsById(String id){
        return this.removeById(id);
    }

    /**
     * 根据客户端信息
     *
     * @param sysOauthClientDetails
     * @return
     */
    @Override
    @CacheEvict(value = SecurityConstants.CLIENT_DETAILS_KEY, key = "#sysOauthClientDetails.clientId")
    public Boolean updateClientDetailsById(SysOauthClientDetails sysOauthClientDetails){
        return this.updateById(sysOauthClientDetails);
    }
}
