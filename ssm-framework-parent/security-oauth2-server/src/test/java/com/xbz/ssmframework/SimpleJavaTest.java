package com.xbz.ssmframework;

import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class SimpleJavaTest {
	@Test
	public void testBcrypt() {
		String password = "123456!";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("raw pwd:"+password);
		System.out.println("des pwd:"+encoder.encode(password));
		String encodeStr = "$2a$12$bUVEn19OxuQ2utE734u6D.Jtm8wk.IwFLrHlvGvoMgePMgC3MV2vy";
		System.out.println(encoder.matches(password, encodeStr));
	}
}
