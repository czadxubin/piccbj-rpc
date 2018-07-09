package com.xbz.ssmframework.interf;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
	@RequestMapping(value="/**",produces= {"application/json; charset=UTF-8"})
	public String hello() {
		return "请检查接口地址是否正确";
	}
	
}
