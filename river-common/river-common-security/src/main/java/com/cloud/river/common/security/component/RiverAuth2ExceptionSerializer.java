package com.cloud.river.common.security.component;

import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.security.exception.RiverAuth2Exception;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-04-01 16:56
 **/
public class RiverAuth2ExceptionSerializer extends StdSerializer<RiverAuth2Exception> {
    public RiverAuth2ExceptionSerializer() {
        super(RiverAuth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(RiverAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
        gen.writeStartObject();
        gen.writeObjectField("code", CommonConstants.FAIL);
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", value.getErrorCode());
        gen.writeEndObject();
    }
}
