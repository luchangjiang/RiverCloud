package com.cloud.river.common.minio;

import com.cloud.river.common.minio.service.MinioTemplate;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-04-01 10:23
 **/
@AllArgsConstructor
@EnableConfigurationProperties(MinioProperties.class)
public class MinioAutoConfiguration {
    private final MinioProperties minioProperties;

    @Bean
    @ConditionalOnMissingBean(MinioProperties.class)
    @ConditionalOnProperty(name = "minio.url")
    MinioTemplate template(){
        return new MinioTemplate(
                minioProperties.getUrl(),
                minioProperties.getAccessKey(),
                minioProperties.getSecretKey());
    }
}
