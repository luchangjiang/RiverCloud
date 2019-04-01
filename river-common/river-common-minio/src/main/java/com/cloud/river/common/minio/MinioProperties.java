package com.cloud.river.common.minio;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: RiverCloud
 * @description: minio配置信息
 * @author: River
 * @create: 2019-04-01 10:21
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    /**
     * minio 服务地址 http://ip:port
     */
    private String url;

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;
}
