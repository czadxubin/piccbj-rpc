package com.icode.ssmframework.sso.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.icode.ssmframework.core.WebAppAttributes;
import com.icode.ssmframework.core.model.SysUser;
import com.icode.ssmframework.core.security.userdetials.SysUserDetails;
import com.icode.ssmframework.sso.mapper.SysUserMapper;

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
		//登陆成功后，将一些重要用户信息放入session
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)authResult;
		SysUserDetails sysUserDetails = (SysUserDetails) auth.getPrincipal();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute(WebAppAttributes.LOGIN_SUCCESS_FLAG, "1");
		httpSession.setAttribute(WebAppAttributes.LOGIN_USER_INFO, sysUserDetails);
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
