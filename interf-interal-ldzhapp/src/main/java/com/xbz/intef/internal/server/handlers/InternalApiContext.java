package com.xbz.intef.internal.server.handlers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xbz.intef.internal.model.InternalApiReq;
import com.xbz.intef.internal.model.InternalApiReqHead;

/**
 * 	一次请求处理执行链的上下文用于保存一些上下文中传输的数据
 * @author 许宝众
 *
 */
public class InternalApiContext {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private InternalApiReq req;
	private Map<String,Object> dataMap;
	private Map<String,String> extHeaders;
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	public InternalApiReq getReq() {
		return req;
	}
	public void setReq(InternalApiReq req) {
		this.req = req;
		if(req!=null) {
			InternalApiReqHead reqHead = req.getReqHead();
			if(reqHead!=null) {
				this.extHeaders = reqHead.getExternalHeaders();
			}
		}
	}
	/**
	 * 	增加自定义上下文数据
	 * @param key
	 * @param data
	 */
	public void putData(String key,Object data) {
		if(dataMap!=null) {
			dataMap.put(key, data);
		}else {
			dataMap = new HashMap<>();
			dataMap.put(key, data);
		}
	}
	/**
	 * 获取自定义上下文数据
	 * @param key
	 * @return
	 */
	public Object getData(String key) {
		if(dataMap!=null) {
			return dataMap.get(key);
		}
		return null;
	}
	/**
	 * 	获取本次请求额外的请求头Map
	 * @return
	 */
	public Map<String,String> getExternalHeaders(){
		return this.extHeaders;
	}
	
	/**
	 * 	获取本次请求额外的请求头Map中指定key的value
	 * @return
	 */
	public String getExternalHeaderValue(String key){
		if(this.extHeaders!=null) {
			return this.extHeaders.get(key);
		}
		return null;
	}
}
