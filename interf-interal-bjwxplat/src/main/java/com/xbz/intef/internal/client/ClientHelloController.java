package com.xbz.intef.internal.client;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.xbz.intef.internal.client.service.InternalApiClientService;
import com.xbz.intef.internal.model.InternalApiReqHead;
import com.xbz.intef.internal.util.NetworkUtils;

@RestController
@RequestMapping("api")
public class ClientHelloController {
	private String serverIp;
	public ClientHelloController() {
		try {
			this.serverIp = NetworkUtils.getLocalHostLANAddress().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	@Autowired
	private InternalApiClientService apiClientService;
	/**
	 * 	统一异步请求入口地址
	 * @param serviceName
	 * 				服务名称，注意该名称不需要以Service结尾，接口调用时自动补充
	 * @param serviceMethod
	 * 				服务方法
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/{serviceName}/{serviceMethod}",method= {RequestMethod.POST},consumes="application/json;charset=utf-8",produces= {"application/json;charset=utf-8"})
	public String endpoint(HttpServletRequest request,@PathVariable String serviceName,@PathVariable String serviceMethod,Integer version,@RequestBody String body) {
		String resStr = null;
		JSONObject resJson = new JSONObject();
		resJson.put("resCode", "9999");
		resJson.put("resMsg", "未知错误");
		try {
			if(StringUtils.isNotBlank(serviceName)
					&&StringUtils.isNotBlank(serviceMethod)
						) {
				//当serviceName不以Service结尾时自动增加Service后缀
				if(!serviceName.endsWith("Service")) {
					serviceName += "Service";
				}
				String reqId = UUID.randomUUID().toString();//	string	Y	请求编号
				Date reqTime = new Date();//	string	Y	请求时间 日期格式： yyyy-MM-dd HH:mm:ss.SSS
				String appId = "123456";//	string	Y	调用端应用ID（需申请）
				Integer servPort = request.getLocalPort();//	string 	Y	分布式服务器端口
				Integer serviceVerison = version;//	int	Y	服务版本号
				InternalApiReqHead reqHead = new InternalApiReqHead();
				reqHead.setReqId(reqId);
				reqHead.setReqTime(reqTime);
				reqHead.setAppId(appId);
				reqHead.setServIp(serverIp);
				reqHead.setServPort(servPort);
				reqHead.setServiceName(serviceName);
				reqHead.setServiceMethod(serviceMethod);
				reqHead.setServiceVerison(serviceVerison);
				JSONObject reqJson = new JSONObject();
				reqJson.put("req_head", reqHead);
				if(StringUtils.isNotBlank(body)) {
					reqJson.put("req_body", body);
				}
				String address = "http://localhost:8080/internalApi/endpoint";
				Map<String, String> extHeaders = new HashMap<>();
				extHeaders.put("_access_ticket", "_access_ticket-value");
				extHeaders.put("_ticket_sign", "_ticket_sign-value");
				resStr = apiClientService.remoteInvoke(address,reqHead,body,extHeaders );
			}else {
				//参数错误
				resJson.put("resCode", "0");
				resJson.put("resMsg", "参数错误");
				resStr = resJson.toJSONString();
			}
		}catch (Exception e) {
			resJson.put("resCode", "10000");
			resJson.put("resMsg", "处理时发生错误");
		}
		return resStr;
	}
}
