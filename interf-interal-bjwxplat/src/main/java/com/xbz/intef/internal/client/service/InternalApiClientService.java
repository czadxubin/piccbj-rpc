package com.xbz.intef.internal.client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xbz.intef.internal.HandleStatus;
import com.xbz.intef.internal.model.InternalApiReq;
import com.xbz.intef.internal.model.InternalApiReqHead;
import com.xbz.intef.internal.model.InternalApiRes;
import com.xbz.intef.internal.model.InternalApiResHead;

@Service
public class InternalApiClientService {
	/**
	 * 	执行远程调用
	 * @param address
	 * @param reqHead
	 * @param body
	 * @return
	 */
	public String remoteInvoke(String address,InternalApiReqHead reqHead, String body) {
		return this.remoteInvoke(address, reqHead, body, null);
	}
	/**
	 * 	执行远程调用
	 * @param address
	 * @param reqHead
	 * @param body
	 * @param etxHeaders
	 * 			根据业务场景，可以在头部增加额外的头信息
	 * @return
	 */
	public String remoteInvoke(String address,InternalApiReqHead reqHead, String body,Map<String,String> etxHeaders) {
		String resStr = null;
		JSONObject resJson = new JSONObject();
		resJson.put("resCode", "0");
		resJson.put("resMsg", "内部服务调用错误");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(address);
		InternalApiReq req = new InternalApiReq();
		req.setReqHead(reqHead);
		req.setReqBody(body);
		if(etxHeaders!=null) {
			reqHead.setExternalHeaders(etxHeaders);
		}
		String reqStr = JSON.toJSONString(req);
		StringEntity entity = new StringEntity(reqStr,"utf-8");
		httpPost.setEntity(entity);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode()==200) {
				HttpEntity entity2 = response.getEntity();
				InputStream resInput = entity2.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(resInput, "UTF-8"));
				String oneLine = null;
				StringBuffer resSb = new StringBuffer();
				while((oneLine = br.readLine())!=null) {
					resSb.append(oneLine);
				}
				String responseMsg = resSb.toString();
				if(StringUtils.isNotBlank(responseMsg)) {
					InternalApiRes apiRes = JSON.parseObject(responseMsg,InternalApiRes.class);
					InternalApiResHead resHead = apiRes.getResHead();
					if(HandleStatus.成功.resCode.equals(resHead.getResCode())){
						resStr = apiRes.getResBody();
					}
				}
			}
		} catch (IOException e) {
			resJson.put("resCode", "0");
			resJson.put("resMsg", "内部服务调用错误");
		}finally {
			if(response!=null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(resStr==null) {
				resStr = resJson.toJSONString();
			}
		}
		return resStr;
	}

}
