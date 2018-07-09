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
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.xbz.ssmframework.utils.HttpUtils;
/**
 * 单点登录
 * @author 许小宝
 *
 */
public class SSOClientLoginFilter extends AbstractAuthenticationProcessingFilter {
	public SSOClientLoginFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}
	public static final String SSO_AUTHORIZE_URL="http://localhost:8080/SSO-Server/login";
	public static final String SSO_ACCESS_TOKEN_URL="http://localhost:8080/SSO-Server/auth/token";
	public static final String SSO_CLIENT_ID="sso_client_app_001";
	public static final String SSO_CLIENT_SECRET="sso_client_app_001_secret";
	public static final String SSO_REDIECT_URI = "http://localhost:8080/sso-client-one/login";
	/**
	 * 校验是否登陆
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		//获取code参数
		String code = request.getParameter("code");
		if(StringUtils.isNotBlank(code)) {
			//根据code获取登陆访问凭证的access_token
			Map<String,String> params = new HashMap<>() ;
			params.put("grant_type", "authorization_code");
			params.put("code", code);
			params.put("redirect_uri", SSO_REDIECT_URI);
			params.put("client_id", SSO_CLIENT_ID);
			params.put("secret", SSO_CLIENT_SECRET);
			try {
				String resBody = HttpUtils.post(SSO_ACCESS_TOKEN_URL, params, "UFT-8");
				
			} catch (IOException e) {
				throw new InternalAuthenticationServiceException("认证中心响应异常");
			}
			
		}else {
			throw new AuthenticationCredentialsNotFoundException("无登陆凭证，需跳转至SSO中心进行认证");
		}
		return null;
	}
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		String ssoUrl = SSO_AUTHORIZE_URL+"?response_type=code&client_id="+SSO_CLIENT_ID+"&redirect_uri="+URLEncoder.encode(SSO_REDIECT_URI, "UTF-8");
		//redirect to sso center login 
		response.sendRedirect(ssoUrl);
	}
}
