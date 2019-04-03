package com.cloud.river.common.security.exception;

import com.cloud.river.common.security.component.RiverAuth2ExceptionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author lengleng
 * @date 2018/7/8
 * 自定义OAuth2Exception
 */
@JsonSerialize(using = RiverAuth2ExceptionSerializer.class)
public class RiverAuth2Exception extends OAuth2Exception {
	@Getter
	private String errorCode;

	public RiverAuth2Exception(String msg) {
		super(msg);
	}

	public RiverAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}
}
