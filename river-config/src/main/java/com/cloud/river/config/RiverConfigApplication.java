package com.cloud.river.config;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @program: RiverCloud
 * @description: 配置中心
 * @author: River
 * @create: 2019-04-01 22:14
 **/
@EnableConfigServer
@SpringCloudApplication
public class RiverConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(RiverConfigApplication.class, args);
    }
}
