package com.xbz.intef.internal.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class InternalApiResHead {
	@JSONField(name="res_id")
	private String resId;//	string	Y	响应编号
	@JSONField(name="res_time")
	private Date resTime;//	string	Y	响应时间 日期格式：yyyy-MM-dd HH:mm:ss.SSS
	@JSONField(name="serv_ip")
	private String servIp;//	string 	Y	分布式服务器IP地址
	@JSONField(name="serv_port")
	private String servPort;//	string 	Y	分布式服务器端口
	@JSONField(name="res_code")
	private String resCode;//	string	Y	响应代码，1：成功，否则失败，详细参见响应代码说明
	@JSONField(name="res_msg")
	private String resMsg;//	string	Y	非成功响应时，可获取详细错误信息
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public Date getResTime() {
		return resTime;
	}
	public void setResTime(Date resTime) {
		this.resTime = resTime;
	}
	public String getServIp() {
		return servIp;
	}
	public void setServIp(String servIp) {
		this.servIp = servIp;
	}
	public String getServPort() {
		return servPort;
	}
	public void setServPort(String servPort) {
		this.servPort = servPort;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
}
