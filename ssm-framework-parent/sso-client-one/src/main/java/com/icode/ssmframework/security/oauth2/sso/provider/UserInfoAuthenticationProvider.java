package com.icode.ssmframework.security.oauth2.sso.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.icode.ssmframework.security.oauth2.sso.userdetials.SSOUserDetialService;
/**
 * 	通过用户标识得到用户信息（包括权限）
 * @author 许小宝
 *
 */
public class UserInfoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{
	@Autowired
	@Qualifier("ssoUserDetialService")
	private SSOUserDetialService ssoUserDetialService;
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails userDetails = ssoUserDetialService.loadUserByUsername(username);
		return userDetails;
	}


}
