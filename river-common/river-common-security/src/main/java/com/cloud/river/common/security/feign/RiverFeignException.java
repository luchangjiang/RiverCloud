package com.cloud.river.common.security.feign;

import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.R;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-29 15:25
 **/
@Data
public class RiverFeignException extends RuntimeException {
    @Getter
    private final R result;

    public RiverFeignException(R result){
        super(result.getMsg());
        this.result = result;
    }

    public RiverFeignException(String message){
        super(message);
        this.result = R.builder().code(CommonConstants.FAIL)
                .msg(message).build();

    }

    /**
     * 提高性能
     *
     * @return {Throwable}
     */
    @Override
    public Throwable fillInStackTrace(){ return this; }
}
