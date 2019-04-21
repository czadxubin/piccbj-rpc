package com.xbz.intef.internal;

public enum HandleStatus {
	成功("SUCCESS","成功"),
	未知错误("UNKNOWN_ERROR","未知错误"),
	内部调用错误("INTERNAL_ERROR","内部服务调用错误"),
	请求报文为空("0001"," 请求报文为空 "),
	reqHead为空("0002"," reqHead为空 "),
	请求头参数错误("0003"," 请求头参数错误"),
	目标服务不存在("0004"," 目标服务不存在"),
	目标服务参数个数不支持("0005"," 目标服务参数个数不支持"); 

	public final String resCode;
	public final String resMsg;
	private HandleStatus(String resCode,String resMsg) {
		this.resCode = resCode;
		this.resMsg = resMsg;
	}
}
