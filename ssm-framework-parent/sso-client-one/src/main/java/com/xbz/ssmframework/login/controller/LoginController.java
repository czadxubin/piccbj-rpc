package com.xbz.ssmframework.login.controller;

import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@Autowired
	@Qualifier("messageSource")
	public MessageSource messageSource;
	@RequestMapping("/login")
	public String login(Model model,HttpServletRequest request,HttpSession httpSession) {
		System.out.println("login request");
		AuthenticationException authenticationException = (AuthenticationException) httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (authenticationException!=null) {
			String errMsg = authenticationException.getMessage();//i18n 登陆错误信息
			model.addAttribute("login_error_msg", errMsg);
		}
		System.out.println("SysDaoAuthenticationProvider.usernameAndPasswordMustBeNotNull:"+messageSource.getMessage("SysDaoAuthenticationProvider.usernameAndPasswordMustBeNotNull", null, "123",Locale.SIMPLIFIED_CHINESE));
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		return "login";
	}
	
	@RequestMapping("/home")
	public String home(HttpServletRequest request) {
		Principal userPrincipal = request.getUserPrincipal();
		System.out.println("userPrincipal:"+userPrincipal);
		return "home";
	}
	
	@RequestMapping("/403")
	public String _403() {
		return "403";
	}
}
