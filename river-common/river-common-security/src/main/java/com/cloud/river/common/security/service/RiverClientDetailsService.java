package com.cloud.river.common.security.service;

import com.cloud.river.common.core.constant.SecurityConstants;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * @program: RiverCloud
 * @description: JdbcClientDetailsService
 * @author: River
 * @create: 2019-04-01 17:31
 **/
public class RiverClientDetailsService extends JdbcClientDetailsService {
    public RiverClientDetailsService(DataSource dataSource){
        super(dataSource);
    }

    /**
     * 重写原生方法支持redis缓存
     *
     * @param clientId
     * @return
     * @throws InvalidClientException
     */
    @Override
    @SneakyThrows
    @Cacheable(value= SecurityConstants.CLIENT_DETAILS_KEY, key="#clientId", unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }
}
