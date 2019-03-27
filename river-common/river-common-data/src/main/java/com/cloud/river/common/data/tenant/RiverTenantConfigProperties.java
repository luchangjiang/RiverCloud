package com.cloud.river.common.data.tenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: RiverCloud
 * @description: 多租户配置
 * @author: River
 * @create: 2019-03-27 11:34
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "river.tenant")
public class RiverTenantConfigProperties {
    /**
     * 维护租户列名称
     */
    private String column="tenant_id";
    /**
     * 多租户的数据表集合
     */
    private List<String> tables=new ArrayList<>();
}
