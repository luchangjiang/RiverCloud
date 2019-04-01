package com.cloud.river.common.security.annotation;

import com.cloud.river.common.security.component.RiverResourceServerAutoConfiguration;
import com.cloud.river.common.security.component.RiverSecurityBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({RiverResourceServerAutoConfiguration.class, RiverSecurityBeanDefinitionRegistrar.class})
public @interface EnableRiverResourceServer {
    /**
     * false：上下文获取用户名
     * true： 上下文获取全部用户信息
     *
     * @return
     */
    boolean details() default false;
}
