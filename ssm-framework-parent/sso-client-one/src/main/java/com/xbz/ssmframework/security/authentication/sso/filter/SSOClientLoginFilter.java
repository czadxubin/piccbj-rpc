package com.xbz.ssmframework.security.authentication.sso.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.xbz.ssmframework.utils.HttpUtils;
/**
 * 单点登录
 * @author 许小宝
 *
 */
public class SSOClientLoginFilter extends AbstractAuthenticationProcessingFilter {
	@Autowired
	@Qualifier("ssoOAuth2RestTemplate")
	private OAuth2RestTemplate ssoOAuth2RestTemplate;
	public SSOClientLoginFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}
	public static final String SSO_AUTHORIZE_URL="http://localhost:8080/SSO-Server/oauth/authorize";
	public static final String SSO_ACCESS_TOKEN_URL="http://localhost:8080/SSO-Server/auth/token";
	public static final String SSO_CLIENT_ID="sso_client_app_001";
	public static final String SSO_CLIENT_SECRET="sso_client_app_001_secret";
	public static final String SSO_REDIECT_URI = "http://localhost:8888/sso-client-one/login";
	@Autowired
	private OAuth2ClientContext oauth2Context;
	/**
	 * 校验是否登陆
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
/*		//获取code参数
		String code = request.getParameter("code");
		if(StringUtils.isNotBlank(code)) {

			
		}else {
			throw new AuthenticationCredentialsNotFoundException("无登陆凭证，需跳转至SSO中心进行认证");
		}
*/		
		try {
			AccessTokenRequest accessTokenRequest = oauth2Context.getAccessTokenRequest();
			accessTokenRequest.setCurrentUri(SSO_REDIECT_URI);
			OAuth2AccessToken accessToken = ssoOAuth2RestTemplate.getAccessToken();
			System.out.println("accessToken:"+accessToken);
		}catch (UserRedirectRequiredException e) {
			e.printStackTrace();
			throw new AuthenticationCredentialsNotFoundException("无登陆凭证，需跳转至SSO中心进行认证");
		}
		
		return null;
	}
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		String ssoUrl = SSO_AUTHORIZE_URL+"?response_type=code&client_id="+SSO_CLIENT_ID+"&operate_type=authorized_login"+"&redirect_uri="+URLEncoder.encode(SSO_REDIECT_URI, "UTF-8");
		//redirect to sso center login 
		response.sendRedirect(ssoUrl);
	}
}
