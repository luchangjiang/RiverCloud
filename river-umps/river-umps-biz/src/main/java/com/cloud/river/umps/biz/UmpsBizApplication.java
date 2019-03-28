package com.cloud.river.umps.biz;

import com.cloud.river.common.swagger.annotation.EnableRiverSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-26 10:16
 **/
@EnableRiverSwagger2
@SpringBootApplication
public class UmpsBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(UmpsBizApplication.class, args);
    }
}
