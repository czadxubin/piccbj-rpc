package com.xbz.ssmframework.security.authentication.dao;

import org.springframework.security.core.AuthenticationException;

public class UsernamePasswordMustBeNotNullException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public UsernamePasswordMustBeNotNullException(String msg) {
		super(msg);
	}

}
