package com.icode.ssmframework.security.oauth2.sso.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializerMap;
import com.icode.ssmframework.core.WebAppAttributes;
import com.icode.ssmframework.core.model.SysUser;
import com.icode.ssmframework.core.security.userdetials.SysUserDetails;
import com.icode.ssmframework.utils.HttpUtils;
/**
 * 单点登录
 * @author 许小宝
 *
 */
public class SSOClientLoginFilter extends AbstractAuthenticationProcessingFilter {
	@Autowired
	@Qualifier("ssoOAuth2RestTemplate")
	private OAuth2RestTemplate ssoOAuth2RestTemplate;
	@Autowired
	private OAuth2ClientContext oauth2Context;
	public SSOClientLoginFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}
	public static final String SSO_AUTHORIZE_URL="http://localhost:8080/SSO-Server/oauth/authorize";
	public static final String SSO_ACCESS_TOKEN_URL="http://localhost:8080/SSO-Server/auth/token";
	public static final String SSO_CLIENT_ID="sso_client_app_001";
	public static final String SSO_CLIENT_SECRET="sso_client_app_001_secret";
	public static final String SSO_REDIECT_URI = "http://localhost:8888/sso-client-one/login";
	
	
	
	/**
	 * 校验是否登陆
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			AccessTokenRequest accessTokenRequest = oauth2Context.getAccessTokenRequest();
			accessTokenRequest.setCurrentUri(SSO_REDIECT_URI);
			OAuth2AccessToken accessToken = ssoOAuth2RestTemplate.getAccessToken();
			System.out.println("accessToken:"+JSON.toJSONString(accessToken));
			Map<String, Object> additionalInformation = accessToken.getAdditionalInformation();
			String username = (String) additionalInformation.get("username");
			if(StringUtils.isNotBlank(username)) {
				//得到用户名登陆成功
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,"");
				return getAuthenticationManager().authenticate(authentication);
			}else {
				
			}
		}catch (UserRedirectRequiredException e) {
			e.printStackTrace();
			throw new AuthenticationCredentialsNotFoundException("无登陆凭证，需跳转至SSO中心进行认证");
		}
		
		return null;
	}
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		//登陆成功后，将一些重要用户信息放入session
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)authResult;
		SysUserDetails sysUserDetails = (SysUserDetails) auth.getPrincipal();
		HttpSession httpSession = request.getSession(false);
		httpSession.setAttribute(WebAppAttributes.LOGIN_SUCCESS_FLAG, "1");
		httpSession.setAttribute(WebAppAttributes.LOGIN_USER_INFO, sysUserDetails);
		
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		String ssoUrl = SSO_AUTHORIZE_URL+"?response_type=code&client_id="+SSO_CLIENT_ID+"&operate_type=authorized_login"+"&redirect_uri="+URLEncoder.encode(SSO_REDIECT_URI, "UTF-8");
		//redirect to sso center login 
		response.sendRedirect(ssoUrl);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		//填充默认authenticationManager
		if(getAuthenticationManager()==null) {
			List<AuthenticationProvider> providers = Collections.<AuthenticationProvider> emptyList();
			AuthenticationManager authenticationManager = new ProviderManager(providers);
			super.setAuthenticationManager(authenticationManager );
		}
	}
}
