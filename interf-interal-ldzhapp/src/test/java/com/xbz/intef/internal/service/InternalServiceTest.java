package com.xbz.intef.internal.service;

import java.util.Date;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xbz.intef.internal.model.HelloJavaBean;
import com.xbz.intef.internal.model.InternalApiReqHead;

public class InternalServiceTest {
	@Test
	public void test简单() {
		InternalApiReqHead reqHead = new InternalApiReqHead();
//		string	Y	请求编号
		String reqId = "1234567899";
//		string	Y	请求时间 日期格式： yyyy-MM-dd HH:mm:ss.SSS
		Date reqTime = new Date();
//		string	Y	调用端应用ID（需申请）
		String appId = "appid_1234";
//		string 	Y	分布式服务器IP地址
		String servIp = "servIP";
//		string 	Y	分布式服务器端口
		Integer servPort = 123;
//		string	Y	服务名称
		String serviceName = "helloService";
//		string	Y	服务方法
		String serviceMethod = "hello";
//		string	Y	服务版本号
		reqHead.setReqId(reqId);
		reqHead.setReqTime(reqTime);
		reqHead.setAppId(appId);
		reqHead.setServIp(servIp);
		reqHead.setServPort(servPort);
		reqHead.setServiceName(serviceName);
		reqHead.setServiceMethod(serviceMethod);
		JSONObject reqJson = new JSONObject();
		reqJson.put("req_head", reqHead);
		JSONObject reqBodyJson = new JSONObject();
		reqBodyJson.put("username", "许宝众");
		reqJson.put("req_body", reqBodyJson );
		System.out.println(reqJson.toJSONString());
	}
	
	@Test
	public void testJsonString直接转数值() {
		HelloJavaBean helloJavaBean = new HelloJavaBean();
		helloJavaBean.setId(111L);
		helloJavaBean.setUsername("许宝众");
		System.out.println(JSON.toJSONString(helloJavaBean));
		String jsonStr = "{\"id\":\"111\",\"username\":\"许宝众\"}";
		HelloJavaBean bean = JSON.parseObject(jsonStr,HelloJavaBean.class);
		System.out.println(bean.getId());
	} 
}
