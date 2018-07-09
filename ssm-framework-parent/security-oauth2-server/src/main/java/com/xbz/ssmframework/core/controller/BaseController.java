package com.xbz.ssmframework.core.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {
	@Value(value="${config.httpBasePath}")
	private String httpBasePath;
	
	/**
	 * 设置一些初始化参数，供所有请求使用
	 * @param model
	 */
	@ModelAttribute
	public void setGeneralModelAttribute(Model model) {
		model.addAttribute("$httpBasePath", httpBasePath);
	}
}
