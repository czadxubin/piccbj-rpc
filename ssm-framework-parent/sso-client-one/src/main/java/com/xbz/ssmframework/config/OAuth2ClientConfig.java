package com.xbz.ssmframework.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * 定义一个OAuth2.0的客户端
 * @author 许小宝
 *
 */
@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfig {
	private static final String SSO_SERVER_CLIENT_BEAN_ID = "ssoLoginAOuth2ResourceDetials";
	private static final String SSO_SERVER_ACCESS_TOKEN_URL = "http://localhost:8080/SSO-Server/oauth/token";
	private static final String SSO_SERVER_CLIENT_ID = "sso_client_app_001";
	private static final String SSO_SERVER_CLIENT_SECRET = "sso_client_app_001_secret";
	private static final String SSO_SERVER_AUTHORIZATION_REDIRECT_URI = "http://localhost:8080/SSO-Server/oauth/authorize";
	@Autowired
	private OAuth2ClientContext oauth2Context;

	@Bean
	@Qualifier("ssoOAuth2RestTemplate")
	public OAuth2RestTemplate sparklrRestTemplate() {
		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(ssoServer(), oauth2Context);
		AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider();
		accessTokenProvider.setStateMandatory(false);
		oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider );
		return oAuth2RestTemplate;
	}
	@Bean
	public OAuth2ProtectedResourceDetails ssoServer() {
		AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
		resourceDetails.setAccessTokenUri(SSO_SERVER_ACCESS_TOKEN_URL);
		resourceDetails.setClientAuthenticationScheme(AuthenticationScheme.header);
		resourceDetails.setClientId(SSO_SERVER_CLIENT_ID);
		resourceDetails.setClientSecret(SSO_SERVER_CLIENT_SECRET);
		resourceDetails.setUserAuthorizationUri(SSO_SERVER_AUTHORIZATION_REDIRECT_URI);
		resourceDetails.setId(SSO_SERVER_CLIENT_BEAN_ID);
		resourceDetails.setScope(Arrays.asList("authorization_code"));
		return resourceDetails;
	}
}
