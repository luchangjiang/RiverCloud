package com.cloud.river.common.security.feign;

import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

/**
 * @author lengleng
 * @date 2019-01-22
 */
@Slf4j
@AllArgsConstructor
public class RiverFeignFallbackMethod implements MethodInterceptor {
	private Class<?> type;
	private Throwable cause;

	@Nullable
	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
		log.error("Fallback class:[{}] method:[{}] message:[{}]",
				type.getName(), method.getName(), cause.getMessage());

		if (R.class == method.getReturnType()) {
			final R result = cause instanceof RiverFeignException ?
					((RiverFeignException) cause).getResult() : R.builder()
					.code(CommonConstants.FAIL)
					.msg(cause.getMessage()).build();
			return result;
		}
		return null;
	}
}
