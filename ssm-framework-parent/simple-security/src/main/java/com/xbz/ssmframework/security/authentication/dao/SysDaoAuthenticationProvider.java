package com.xbz.ssmframework.security.authentication.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class SysDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{
	/**
	 * 获取数据库用户信息Service
	 * SysAbstractUserDetailsAuthenticationProvider
	 */
	private UserDetailsService userDetailsService;
	/**
	 * 登陆密码编码器
	 * BCryptPasswordEncoder
	 */
	private PasswordEncoder passwordEncoder;
	
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

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

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails loadedUser;
		try {
			loadedUser = this.userDetailsService.loadUserByUsername(username);
			if(loadedUser==null) {
				throw new UsernameNotFoundException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.usernameNotFound","用户名不存在"));
			}
		}
		catch (UsernameNotFoundException notFound) {
			throw notFound;
		}
		catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(
					repositoryProblem.getMessage(), repositoryProblem);
		}
		return loadedUser;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
				messages.getMessage(
						"AbstractUserDetailsAuthenticationProvider.onlySupports",
						"Only UsernamePasswordAuthenticationToken is supported"));
		// Determine username
		String username = (authentication.getPrincipal() == null) ? null: authentication.getName();

		//测试i18n 错误信息提示  2018-06-23 BEGIN
		String password = (authentication.getPrincipal() == null)? null: authentication.getCredentials().toString();
		if(StringUtils.isBlank(username)||StringUtils.isBlank(password)) {
			throw new UsernamePasswordMustBeNotNullException(messages.getMessage(
					"SysDaoAuthenticationProvider.usernameAndPasswordMustBeNotNull",
					"username and password must be not null"));
		}
		
		//测试i18n 错误信息提示  2018-06-23 END
		
		boolean cacheWasUsed = true;
		UserDetails user = this.getUserCache().getUserFromCache(username);

		if (user == null) {
			cacheWasUsed = false;

			try {
				user = retrieveUser(username,
						(UsernamePasswordAuthenticationToken) authentication);
			}
			catch (UsernameNotFoundException notFound) {
				logger.debug("用户 '" + username + "' 未找到！");
				if (hideUserNotFoundExceptions) {
					throw new BadCredentialsException(messages.getMessage(
							"AbstractUserDetailsAuthenticationProvider.badCredentials",
							"Bad credentials"));
				}
				else {
					throw notFound;
				}
			}
			//因为上面如果UserDetial查询为null则应抛出UsernameNotFoundException，所以到这里应该是UserDetial不可能为空
			Assert.notNull(user,"retrieveUser returned null - a violation of the interface contract");
		}

		try {
			getPreAuthenticationChecks().check(user);
			additionalAuthenticationChecks(user,
					(UsernamePasswordAuthenticationToken) authentication);
		}
		catch (AuthenticationException exception) {
			if (cacheWasUsed) {
				// There was a problem, so try again after checking
				// we're using latest data (i.e. not from the cache)
				cacheWasUsed = false;
				user = retrieveUser(username,
						(UsernamePasswordAuthenticationToken) authentication);
				getPreAuthenticationChecks().check(user);
				additionalAuthenticationChecks(user,
						(UsernamePasswordAuthenticationToken) authentication);
			}
			else {
				throw exception;
			}
		}

		getPostAuthenticationChecks().check(user);

		if (!cacheWasUsed) {
			this.getUserCache().putUserInCache(user);
		}

		Object principalToReturn = user;

		if (isForcePrincipalAsString()) {
			principalToReturn = user.getUsername();
		}
		return createSuccessAuthentication(principalToReturn, authentication, user);
	}
}
