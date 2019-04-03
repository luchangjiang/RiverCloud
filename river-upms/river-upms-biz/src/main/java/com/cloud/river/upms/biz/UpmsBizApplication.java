package com.cloud.river.upms.biz;

import com.cloud.river.common.security.annotation.EnableRiverFeignClients;
import com.cloud.river.common.security.annotation.EnableRiverResourceServer;
import com.cloud.river.common.swagger.annotation.EnableRiverSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-26 10:16
 **/
@EnableRiverSwagger2
@SpringCloudApplication
@EnableRiverFeignClients
@EnableRiverResourceServer(details = true)
public class UpmsBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpmsBizApplication.class, args);
    }
}
