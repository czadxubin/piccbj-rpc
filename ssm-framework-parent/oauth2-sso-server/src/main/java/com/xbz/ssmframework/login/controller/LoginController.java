package com.xbz.ssmframework.login.controller;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.io.AbstractEndPoint;
import org.eclipse.jetty.util.thread.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AbstractEndpoint;
import org.springframework.security.oauth2.provider.endpoint.DefaultRedirectResolver;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

//@Controller
public class LoginController extends AbstractEndpoint{
	@Autowired
	private ClientDetailsService clientDetailsService;
	private AuthorizationCodeServices authorizationCodeServices = new InMemoryAuthorizationCodeServices();

	private RedirectResolver redirectResolver = new DefaultRedirectResolver();

	private UserApprovalHandler userApprovalHandler = new DefaultUserApprovalHandler();

	private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();

	private OAuth2RequestValidator oauth2RequestValidator = new DefaultOAuth2RequestValidator();

	private String userApprovalPage = "forward:/oauth/confirm_access";

	private String errorPage = "forward:/oauth/error";

	
	@RequestMapping("/login")
	public String login(Model model,HttpServletRequest request,HttpSession httpSession,Principal principal) {
		if(principal!=null) {
			return "forward:/home";
		}
		System.out.println("login request");
		AuthenticationException authenticationException = (AuthenticationException) httpSession.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (authenticationException==null) {
			String clientId = request.getParameter("client_id");
			String redirectUri = request.getParameter("redirect_uri");
			String responseType = request.getParameter("response_type");
			if(StringUtils.isNoneBlank(clientId)
					&&"code".equals(responseType)
					&&StringUtils.isNoneBlank(redirectUri)
					&&StringUtils.isNoneBlank(responseType)) {
				model.addAttribute("client_id", clientId);
				model.addAttribute("redirect_uri", redirectUri);
				model.addAttribute("response_type", responseType);
			}
		}else {
			String errMsg = authenticationException.getMessage();//i18n 登陆错误信息
			model.addAttribute("login_error_msg", errMsg);
		}
		return "login";
	}
	
	@RequestMapping("/home")
	public String home(HttpServletRequest request,HttpSession session,Principal principal,RedirectAttributes attributes,@RequestParam Map<String,String> params) {
		//尝试取 单点跳转登陆请求参数
		String clientId = (String) params.get("client_id");
		String redirectUri = (String) params.get("redirect_uri");
		String responseType = (String) params.get("response_type");
		//检验参数是否是跳转登陆
		if(StringUtils.isNoneBlank(clientId)
				&&"code".equals(responseType)
				&&StringUtils.isNoneBlank(redirectUri)
				&&StringUtils.isNoneBlank(responseType)) {
			ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
			if(client!=null) {
				Map<String, String> authorizationParameters = new HashMap<>();
				authorizationParameters.put(OAuth2Utils.CLIENT_ID, clientId);
				authorizationParameters.put(OAuth2Utils.REDIRECT_URI, redirectUri);
				authorizationParameters.put(OAuth2Utils.RESPONSE_TYPE, responseType);
				AuthorizationRequest authorizationRequest = getOAuth2RequestFactory().createAuthorizationRequest(authorizationParameters);
				
				//验证redirect_uri是否合法
				String resolvedRedirect = redirectResolver.resolveRedirect(redirectUri, client);
				if (StringUtils.isBlank(resolvedRedirect)) {
					throw new RedirectMismatchException(
							"A redirectUri must be either supplied or preconfigured in the ClientDetails");
				}
				authorizationRequest.setRedirectUri(resolvedRedirect);
				//不需要用户授权通过
				authorizationRequest.setApproved(true);
				
				//授权请求放入Session
				session.setAttribute("authorizationRequest", authorizationRequest);
				
				// Validation is all done, so we can check for auto approval...
				if(authorizationRequest.isApproved()) {
					return getAuthorizationCodeResponse(authorizationRequest,(Authentication)principal);
				}
			}
		}
		return "home";
	}
	/**
	 * 生成AuthorizationCode响应
	 * @param authorizationRequest
	 * @param principal
	 * @return
	 */
	private String getAuthorizationCodeResponse(AuthorizationRequest authorizationRequest, Authentication authUser) {
		String authorizationCode = generateCode(authorizationRequest,authUser);
		return getSuccessfulRedirect(authorizationRequest,authorizationCode);
	}
	
	/**
	 * 授权成功返回redirect_uri 
	 * @param authorizationRequest
	 * @param code
	 * @return
	 */
	private String getSuccessfulRedirect(AuthorizationRequest authorizationRequest, String authorizationCode) {
		if (authorizationCode == null) {
			throw new IllegalStateException("No authorization code found in the current request scope.");
		}

		Map<String, String> query = new LinkedHashMap<String, String>();
		query.put("code", authorizationCode);

		String state = authorizationRequest.getState();
		if (state != null) {
			query.put("state", state);
		}

		return "redirect:"+append(authorizationRequest.getRedirectUri(), query, false);
	}
	private String getUnsuccessfulRedirect(AuthorizationRequest authorizationRequest, OAuth2Exception failure,
			boolean fragment) {

		if (authorizationRequest == null || authorizationRequest.getRedirectUri() == null) {
			// we have no redirect for the user. very sad.
			throw new UnapprovedClientAuthenticationException("Authorization failure, and no redirect URI.", failure);
		}

		Map<String, String> query = new LinkedHashMap<String, String>();

		query.put("error", failure.getOAuth2ErrorCode());
		query.put("error_description", failure.getMessage());

		if (authorizationRequest.getState() != null) {
			query.put("state", authorizationRequest.getState());
		}

		if (failure.getAdditionalInformation() != null) {
			for (Map.Entry<String, String> additionalInfo : failure.getAdditionalInformation().entrySet()) {
				query.put(additionalInfo.getKey(), additionalInfo.getValue());
			}
		}

		return "redirect:"+append(authorizationRequest.getRedirectUri(), query, fragment);

	}
	/**
	 * 生成授权码
	 * @param authorizationRequest
	 * @param authUser
	 * @return
	 */
	private String generateCode(AuthorizationRequest authorizationRequest, Authentication authUser) {

		try {

			OAuth2Request storedOAuth2Request = getOAuth2RequestFactory().createOAuth2Request(authorizationRequest);

			OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authUser);
			String code = authorizationCodeServices.createAuthorizationCode(combinedAuth);

			return code;

		}
		catch (OAuth2Exception e) {

			if (authorizationRequest.getState() != null) {
				e.addAdditionalInformation("state", authorizationRequest.getState());
			}

			throw e;

		}
	}

	@RequestMapping("/403")
	public String _403() {
		return "403";
	}
	
	private String append(String base, Map<String, ?> query, boolean fragment) {
		return append(base, query, null, fragment);
	}

	private String append(String base, Map<String, ?> query, Map<String, String> keys, boolean fragment) {

		UriComponentsBuilder template = UriComponentsBuilder.newInstance();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base);
		URI redirectUri;
		try {
			// assume it's encoded to start with (if it came in over the wire)
			redirectUri = builder.build(true).toUri();
		}
		catch (Exception e) {
			// ... but allow client registrations to contain hard-coded non-encoded values
			redirectUri = builder.build().toUri();
			builder = UriComponentsBuilder.fromUri(redirectUri);
		}
		template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost())
				.userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());

		if (fragment) {
			StringBuilder values = new StringBuilder();
			if (redirectUri.getFragment() != null) {
				String append = redirectUri.getFragment();
				values.append(append);
			}
			for (String key : query.keySet()) {
				if (values.length() > 0) {
					values.append("&");
				}
				String name = key;
				if (keys != null && keys.containsKey(key)) {
					name = keys.get(key);
				}
				values.append(name + "={" + key + "}");
			}
			if (values.length() > 0) {
				template.fragment(values.toString());
			}
			UriComponents encoded = template.build().expand(query).encode();
			builder.fragment(encoded.getFragment());
		}
		else {
			for (String key : query.keySet()) {
				String name = key;
				if (keys != null && keys.containsKey(key)) {
					name = keys.get(key);
				}
				template.queryParam(name, "{" + key + "}");
			}
			template.fragment(redirectUri.getFragment());
			UriComponents encoded = template.build().expand(query).encode();
			builder.query(encoded.getQuery());
		}
		return builder.build().toUriString();
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(new InMemoryTokenStore());
		TokenGranter tokenGranter = new AuthorizationCodeTokenGranter(tokenServices ,authorizationCodeServices,clientDetailsService,getOAuth2RequestFactory());
		super.setTokenGranter(tokenGranter);
		super.setClientDetailsService(clientDetailsService);
		super.afterPropertiesSet();
	}
}
