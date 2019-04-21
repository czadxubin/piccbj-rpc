package com.xbz.intef.internal.server;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xbz.intef.internal.model.HelloJavaBean;
import com.xbz.intef.internal.server.handlers.InternalApiContext;
import com.xbz.intef.internal.util.InternalApiUtils;

@Service
public class HelloService {
	/**
	 * 	该方法演示入参为string类型，此时不会进行参数的简单校验，会将原始的res_body内容传输进来，由业务方法自行处理全部业务信息
	 * @param reqBody
	 * @return
	 */
	public String helloReqBody(String reqBody) {
		System.out.println("reqBody --> "+reqBody);
		return "reqBody " + reqBody;
	}
	public String helloParamJavaBean(HelloJavaBean params) {
		String username = params.getUsername();
		System.out.println("params.usrname --> "+username);
		return "params.usrname-->" + username;
	}
	
	public HelloJavaBean helloResultJavaBean(HelloJavaBean params) {
		String username = params.getUsername();
		InternalApiContext currentContext = InternalApiUtils.getCurrentContext();
		System.out.println("context:"+currentContext.getReq());
		System.out.println("params.usrname --> "+username);
		return params;
	}
}
