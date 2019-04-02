package com.cloud.river.auth;

import com.cloud.river.common.security.annotation.EnableRiverFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @program: RiverCloud
 * @description: 认证授权中心
 * @author: River
 * @create: 2019-04-01 10:57
 **/
@SpringCloudApplication
@EnableRiverFeignClients
public class RiverAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(RiverAuthApplication.class, args);
    }
}
