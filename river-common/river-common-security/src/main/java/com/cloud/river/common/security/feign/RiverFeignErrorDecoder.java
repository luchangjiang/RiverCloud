package com.cloud.river.common.security.feign;

import cn.hutool.core.io.IoUtil;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.security.util.ConcurrentDateFormat;
import com.fasterxml.classmate.util.MethodKey;
import feign.Response;
import feign.RetryableException;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static feign.Util.checkNotNull;
import static java.util.Locale.US;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-29 19:12
 **/
public class RiverFeignErrorDecoder extends ErrorDecoder.Default {
    private final RetryAfterDecoder retryAfterDecoder = new RetryAfterDecoder();
    private static final String REGEX = "^[0-9]+$";

    @Override
    public Exception decode(String methodKey, Response response) {
        RiverFeignException exception = errorStatus(methodKey, response);
        Date retryAfter = retryAfterDecoder.apply(firstOrNull(response.headers()));
        return new RetryableException(exception.getMessage(), response.request().httpMethod(), exception, retryAfter);
    }

    private static RiverFeignException errorStatus(String methodKey, Response response){
        try {
            if(response.body() != null){
                Reader reader = response.body().asReader();
                return new RiverFeignException(R.builder()
                        .code(CommonConstants.FAIL)
                        .msg(IoUtil.read(reader))
                        .build());
            }
        } catch (IOException ignored) {
        }

        String message = String.format("status %s, reading %s", response.status(), methodKey);
        return new RiverFeignException(message);
    }

    @Nullable
    private <T> T firstOrNull(Map<String, Collection<T>> map){
        String key = Util.RETRY_AFTER;
        if(map.containsKey(key) && !map.get(key).isEmpty()){
            return map.get(key).iterator().next();
        }
        return null;
    }


    /**
     * Decodes a {@link feign.Util#RETRY_AFTER} header into an absolute date, if possible. <br> See <a
     * href="https://tools.ietf.org/html/rfc2616#section-14.37">Retry-After format</a>
     */
    static class RetryAfterDecoder {

        static final ConcurrentDateFormat RFC822_FORMAT = ConcurrentDateFormat.of
                ("EEE, dd MMM yyyy HH:mm:ss 'GMT'", US, TimeZone.getTimeZone(ZoneId.of("GMT")));
        private final ConcurrentDateFormat rfc822Format;

        RetryAfterDecoder() {
            this(RFC822_FORMAT);
        }

        RetryAfterDecoder(ConcurrentDateFormat rfc822Format) {
            this.rfc822Format = checkNotNull(rfc822Format, "rfc822Format");
        }

        private long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        /**
         * returns a date that corresponds to the first time a request can be retried.
         *
         * @param retryAfter String in <a href="https://tools.ietf.org/html/rfc2616#section-14.37">Retry-After format</a>
         */
        @Nullable
        Date apply(@Nullable String retryAfter) {
            if (retryAfter == null) {
                return null;
            }
            if (retryAfter.matches(REGEX)) {
                long deltaMillis = SECONDS.toMillis(Long.parseLong(retryAfter));
                return new Date(currentTimeMillis() + deltaMillis);
            }
            try {
                return rfc822Format.parse(retryAfter);
            } catch (ParseException ignored) {
                return null;
            }
        }
    }
}
