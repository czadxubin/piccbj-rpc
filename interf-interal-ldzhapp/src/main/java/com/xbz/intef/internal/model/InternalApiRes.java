package com.xbz.intef.internal.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class InternalApiRes {
	@JSONField(name="res_head")
	private InternalApiResHead resHead;
	@JSONField(name="res_body")
	private String resBody;
	public InternalApiResHead getResHead() {
		return resHead;
	}
	public void setResHead(InternalApiResHead resHead) {
		this.resHead = resHead;
	}
	public String getResBody() {
		return resBody;
	}
	public void setResBody(String resBody) {
		this.resBody = resBody;
	}
}
