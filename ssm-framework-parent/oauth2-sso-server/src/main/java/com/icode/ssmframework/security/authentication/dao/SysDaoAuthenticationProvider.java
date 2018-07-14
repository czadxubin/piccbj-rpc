package com.icode.ssmframework.security.authentication.dao;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SysDaoAuthenticationProvider extends DaoAuthenticationProvider{
	/**
	 * 登陆密码编码器
	 * BCryptPasswordEncoder
	 */
	private PasswordEncoder passwordEncoder;
	/**
	 * 校验登陆凭证（密码）
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}
		//前端输入登录凭证（密码）
		String presentedPassword = authentication.getCredentials().toString();
		//数据库存储密码
		String password = userDetails.getPassword();
		if (!passwordEncoder.matches(presentedPassword, password)) {
			logger.debug("Authentication failed: password does not match stored value");

			throw new BadCredentialsException(messages.getMessage(
					"SysAbstractUserDetailsAuthenticationProvider.badCredentials",
					"登陆密码错误"));
		}
	}

	/**
	 * 校验用户输入用户名、密码是否正确（真正的认证过程，包含了retrieveUser+additionalAuthenticationChecks）
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = (authentication.getPrincipal() == null) ? null: authentication.getName();

		//测试i18n 错误信息提示  2018-06-23 BEGIN
		String password = (authentication.getPrincipal() == null)? null: authentication.getCredentials().toString();
		if(StringUtils.isBlank(username)||StringUtils.isBlank(password)) {
			throw new UsernamePasswordMustBeNotNullException(messages.getMessage(
					"SysDaoAuthenticationProvider.usernameAndPasswordMustBeNotNull",
					"username and password must be not null"));
		}
		
		//测试i18n 错误信息提示  2018-06-23 END
		return super.authenticate(authentication);
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	public static void main(String[] args) {
		System.out.println(Locale.getDefault());
	}
}
