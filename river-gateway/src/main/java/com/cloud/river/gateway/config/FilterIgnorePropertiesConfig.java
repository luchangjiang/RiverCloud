package com.cloud.river.gateway.config;

import ch.qos.logback.core.net.server.Client;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: RiverCloud
 * @description: 网关不校验终端配置
 * @author: River
 * @create: 2019-03-28 15:22
 **/
@Data
@Configuration
@RefreshScope
@ConditionalOnExpression("!'${ignore}'.isEmpty()")
@ConditionalOnProperty(prefix = "ignore")
public class FilterIgnorePropertiesConfig {
    List<String> clients =new ArrayList<>();
    List<String> swaggerProviders=new ArrayList<>();
}
