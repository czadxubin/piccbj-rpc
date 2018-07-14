//package com.icode.ssmframework.core.security.filter;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.icode.ssmframework.core.WebAppAttributes;
//import com.icode.ssmframework.core.mapper.SysUserMapper;
//import com.icode.ssmframework.core.model.SysUser;
//
//public class SysUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
//	@Autowired
//	private SysUserMapper sysUserMapper;
//	@Override
//	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
//			Authentication authResult) throws IOException, ServletException {
//		HttpSession session = request.getSession(false);
//		if(session!=null) {
//			session.setAttribute(WebAppAttributes.LOGIN_SUCCESS_FLAG, "1");
//			SysUser querySysUser = new SysUser();
//			querySysUser.setUsername(obtainUsername(request));
//			SysUser sysUser = sysUserMapper.selectOne(querySysUser);
//			session.setAttribute(WebAppAttributes.LOGIN_USER_INFO, sysUser);
//		}
//		super.successfulAuthentication(request, response, chain, authResult);
//	}
//	@Override
//	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//			AuthenticationException failed) throws IOException, ServletException {
//		HttpSession session = request.getSession(false);
//		if(session!=null) {
//			String username = obtainUsername(request);
//			session.setAttribute("USERNAME", username);
//		}
//		super.unsuccessfulAuthentication(request, response, failed);
//	}
//}
