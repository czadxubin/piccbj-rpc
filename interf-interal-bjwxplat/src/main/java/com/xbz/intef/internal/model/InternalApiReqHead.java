package com.xbz.intef.internal.model;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 内部接口请求头Vo
 * @author 许宝众
 *
 */
public class InternalApiReqHead {
	@JSONField(name="req_id")
	private String reqId;//	string	Y	请求编号
	@JSONField(name="req_time",format=" yyyy-MM-dd HH:mm:ss.SSS")
	private Date reqTime;//	string	Y	请求时间 日期格式： yyyy-MM-dd HH:mm:ss.SSS
	@JSONField(name="app_id")
	private String appId;//	string	Y	调用端应用ID（需申请）
	@JSONField(name="serv_ip")
	private String servIp;//	string 	Y	分布式服务器IP地址
	@JSONField(name="serv_port")
	private Integer servPort;//	string 	Y	分布式服务器端口
	@JSONField(name="service_name")
	private String serviceName;//	string	Y	服务名称
	@JSONField(name="service_method")
	private String serviceMethod;//	string	Y	服务方法
	@JSONField(name="service_verison")
	private Integer serviceVerison;//	int	Y	服务版本号
	/**额外的请求头信息，可以根据需求个性化定制**/
	@JSONField(name="external_headers")
	private Map<String,String> externalHeaders;
	
	public Map<String, String> getExternalHeaders() {
		return externalHeaders;
	}
	public void setExternalHeaders(Map<String, String> externalHeaders) {
		this.externalHeaders = externalHeaders;
	}
	public String getReqId() {
		return reqId;
	}
	public void setReqId(String reqId) {
		this.reqId = reqId;
	}
	public Date getReqTime() {
		return reqTime;
	}
	public void setReqTime(Date reqTime) {
		this.reqTime = reqTime;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getServIp() {
		return servIp;
	}
	public void setServIp(String servIp) {
		this.servIp = servIp;
	}
	public Integer getServPort() {
		return servPort;
	}
	public void setServPort(Integer servPort) {
		this.servPort = servPort;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceMethod() {
		return serviceMethod;
	}
	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
	}
	public Integer getServiceVerison() {
		return serviceVerison;
	}
	public void setServiceVerison(Integer serviceVerison) {
		this.serviceVerison = serviceVerison;
	}
}
