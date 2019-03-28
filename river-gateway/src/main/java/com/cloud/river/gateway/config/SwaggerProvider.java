package com.cloud.river.gateway.config;

import com.cloud.river.common.core.constant.CommonConstants;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 11:10
 **/
@Component
@Primary
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {
    private static final String API_URI="/v2/api-docs";
    private final RouteDefinitionRepository routeDefinitionRepository;
    private final FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

    @Override
    public List<SwaggerResource> get(){
        List<SwaggerResource> resources = new ArrayList<>();
        List<RouteDefinition> routes = new ArrayList<>();
        routeDefinitionRepository.getRouteDefinitions().subscribe(route->routes.add(route));

        routes.forEach(routeDefinition-> routeDefinition.getPredicates().stream()
            .filter(predicateDefinition -> "Path".equals(predicateDefinition.getName()))
            .filter(predicateDefinition -> filterIgnorePropertiesConfig.swaggerProviders.contains(routeDefinition.getId()))
            .forEach(predicateDefinition -> resources.add(swaggerResource(routeDefinition.getId(),
                predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX+"0")
                    .replace("**", API_URI)))));

        return resources.stream().sorted(Comparator.comparing(SwaggerResource::getName))
                .collect(Collectors.toList());
    }

    private SwaggerResource swaggerResource(String name, String location){
        SwaggerResource sr=new SwaggerResource();
        sr.setName(name);
        sr.setLocation(location);
        sr.setSwaggerVersion("2.0");
        return sr;
    }
}
