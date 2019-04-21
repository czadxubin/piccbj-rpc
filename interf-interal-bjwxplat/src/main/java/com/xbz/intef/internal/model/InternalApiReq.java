package com.xbz.intef.internal.model;

import com.alibaba.fastjson.annotation.JSONField;

public class InternalApiReq {
	@JSONField(name="req_head")
	private InternalApiReqHead reqHead;
	@JSONField(name="req_body")
	private String reqBody;
	public InternalApiReqHead getReqHead() {
		return reqHead;
	}
	public void setReqHead(InternalApiReqHead reqHead) {
		this.reqHead = reqHead;
	}
	public String getReqBody() {
		return reqBody;
	}
	public void setReqBody(String reqBody) {
		this.reqBody = reqBody;
	}
}
