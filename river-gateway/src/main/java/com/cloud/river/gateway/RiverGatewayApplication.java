package com.cloud.river.gateway;

import com.cloud.river.common.gateway.annotation.EnableRiverDynamicRoute;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 12:24
 **/
@EnableRiverDynamicRoute
@SpringBootApplication
public class RiverGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(RiverGatewayApplication.class);
    }
}
