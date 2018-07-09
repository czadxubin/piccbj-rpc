package com.xbz.ssmframework.user.controller;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xbz.ssmframework.core.controller.BaseController;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
	@RequestMapping("list")
	public String list(HttpServletRequest request) {
		System.out.println("userList is invoked!!!");
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
		Map<String, UserController> beans = wac.getBeansOfType(UserController.class);
		System.out.println(beans);
		return "user/list";
	}
}
