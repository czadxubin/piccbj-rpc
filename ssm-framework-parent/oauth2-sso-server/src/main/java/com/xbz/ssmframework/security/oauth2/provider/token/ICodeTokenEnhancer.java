package com.xbz.ssmframework.security.oauth2.provider.token;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
@Component
@Qualifier("icodeTokenEnhancer")
public class ICodeTokenEnhancer implements TokenEnhancer{
	private static Logger logger = LoggerFactory.getLogger(ICodeTokenEnhancer.class);
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		logger.debug("Token增强，处理是否为用户登录授权请求");
		Map<String, String> requestParameters = authentication.getOAuth2Request().getRequestParameters();
		String operateType = requestParameters.get("operate_type");
		switch (operateType) {
		case "authorized_login":
			enhanceAuhorizedLogin(accessToken,authentication);
			break;

		default:
			break;
		}
		return accessToken;
	}
	private void enhanceAuhorizedLogin(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Object credentials = authentication.getCredentials();
		System.out.println(credentials);
	}

}

