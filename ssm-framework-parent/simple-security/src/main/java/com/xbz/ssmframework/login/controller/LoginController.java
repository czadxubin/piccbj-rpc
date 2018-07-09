package com.xbz.ssmframework.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	@RequestMapping("/login")
	public String login(Model model,HttpServletRequest request,HttpSession httpSession) {
		System.out.println("login request");
		AuthenticationException authenticationException = (AuthenticationException) httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (authenticationException!=null) {
			String errMsg = authenticationException.getMessage();//i18n 登陆错误信息
			model.addAttribute("login_error_msg", errMsg);
		}
		return "login";
	}
	
	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/403")
	public String _403() {
		return "403";
	}
}
