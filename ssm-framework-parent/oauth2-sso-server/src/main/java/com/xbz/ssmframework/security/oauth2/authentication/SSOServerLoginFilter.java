package com.xbz.ssmframework.security.oauth2.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.xbz.ssmframework.core.WebAppAttributes;
import com.xbz.ssmframework.model.SysUser;
import com.xbz.ssmframework.user.controller.SysUserMapper;

public class SSOServerLoginFilter extends UsernamePasswordAuthenticationFilter{
	@Autowired
	private SysUserMapper sysUserMapper;
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		return super.attemptAuthentication(request, response);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		if(session!=null) {
			session.setAttribute(WebAppAttributes.LOGIN_SUCCESS_FLAG, "1");
			SysUser querySysUser = new SysUser();
			querySysUser.setUsername(obtainUsername(request));
			SysUser sysUser = sysUserMapper.selectOne(querySysUser);
			session.setAttribute(WebAppAttributes.LOGIN_USER_INFO, sysUser);
			
		}
		//登陆成功，转发到到home页面
		super.successfulAuthentication(request, response, chain, authResult);
	}
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		if(session!=null) {
			String username = obtainUsername(request);
			session.setAttribute("USERNAME", username);
		}
		//重定向到上次登陆页面
		String redirectUrl = request.getHeader("Referer");
		if(redirectUrl!=null) {
			response.sendRedirect(redirectUrl);
		}else {
			super.unsuccessfulAuthentication(request, response, failed);
		}
		
	}
	
	
}
