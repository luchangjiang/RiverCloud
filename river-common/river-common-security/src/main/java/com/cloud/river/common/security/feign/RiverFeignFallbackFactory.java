package com.cloud.river.common.security.feign;

import feign.hystrix.FallbackFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;

import java.util.ConcurrentModificationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @program: RiverCloud
 * @description: 默认 Fallback
 * @author: River
 * @create: 2019-03-29 15:52
 **/
@Slf4j
@NoArgsConstructor
public final class RiverFeignFallbackFactory<T> implements FallbackFactory<T> {
    public static final RiverFeignFallbackFactory INSTANCE = new RiverFeignFallbackFactory();
    private static final ConcurrentMap<Class<?>, Object> FALLBACK_MAP = new ConcurrentHashMap<>();

    public T create(final Class<?> type, final Throwable cause){
        return (T) FALLBACK_MAP.computeIfAbsent(type, key->{
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(key);
            enhancer.setCallback(new RiverFeignFallbackMethod(type, cause));
            return enhancer.create();
        });
    }

    @Override
    public T create(Throwable cause){ return null; }
}
