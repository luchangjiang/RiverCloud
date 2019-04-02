package com.cloud.river.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @program: RiverCloud
 * @description:  注册中心
 * @author: River
 * @create: 2019-04-02 09:15
 **/
@EnableEurekaServer
@SpringBootApplication
public class RiverEurekaApplication {
    public static void main( String[] args ) {
        SpringApplication.run(RiverEurekaApplication.class, args);
    }
}
