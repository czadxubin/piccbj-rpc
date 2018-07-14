package com.icode.ssmframework.myapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icode.ssmframework.core.controller.BaseController;

@RestController
@RequestMapping("/admin")
public class AdminController  extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(AdminController.class);
	@Autowired
	@Qualifier("clientOAuth2RestTemplate")
	private OAuth2RestTemplate clientOAuth2RestTemplate;
	/**
	 * 提供一个前端触发获取AccessToken方式
	 * @return
	 */
	@RequestMapping(value="/getClientCredentialAccessToken",produces= {"application/json;charset=utf-8"})
	public ResponseEntity<OAuth2AccessToken> getClientAccessToken(){
		OAuth2AccessToken accessToken = null ;
		try{
			accessToken = clientOAuth2RestTemplate.getAccessToken();
		}catch (Exception e) {
			logger.error("[获取客户端access_token]-[异常]",e);
		}
		if(accessToken!=null) {
			return new ResponseEntity<OAuth2AccessToken>(accessToken,HttpStatus.OK);
		}else {
			return new ResponseEntity<OAuth2AccessToken>(HttpStatus.UNAUTHORIZED);
		}
	}
}
