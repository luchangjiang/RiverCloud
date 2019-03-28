package com.cloud.river.common.swagger.annotation;

import com.cloud.river.common.swagger.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SwaggerAutoConfiguration.class)
public @interface EnableRiverSwagger2 {
}
