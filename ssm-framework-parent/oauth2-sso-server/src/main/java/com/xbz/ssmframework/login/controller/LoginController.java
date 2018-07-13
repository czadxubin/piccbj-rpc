package com.xbz.ssmframework.login.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController{
	@RequestMapping("/home")
	public String home(HttpServletRequest request,HttpSession session,Principal principal,RedirectAttributes attributes,@RequestParam Map<String,String> params) {
		return "home";
	}

	@RequestMapping("/403")
	public String _403() {
		return "403";
	}
	
}
