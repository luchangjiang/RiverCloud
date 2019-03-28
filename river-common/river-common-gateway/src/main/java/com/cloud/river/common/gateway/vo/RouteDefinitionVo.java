package com.cloud.river.common.gateway.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 09:02
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class RouteDefinitionVo extends RouteDefinition {
    /**
     * 路由名称
     */
    private String routeName;
}
