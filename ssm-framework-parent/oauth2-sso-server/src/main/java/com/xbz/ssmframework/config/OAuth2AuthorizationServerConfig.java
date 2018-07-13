package com.xbz.ssmframework.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.xbz.ssmframework.security.oauth2.provider.approval.ICodeApprovalHandler;
import com.xbz.ssmframework.security.oauth2.provider.code.ICodeAuthorizationCodeServices;
import com.xbz.ssmframework.security.oauth2.provider.token.ICodeTokenEnhancer;

/**
 * Spring Security OAuth2.0
 * @author xbz
 * 
 *
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	/**
	 * 定义一个SSO单点登录资源
	 */
	public static final String SSO_RESOURCE_ID = "SSO_RESOURCE_ID";
	@Autowired
	private ClientDetailsService icodeClientDetialService;
	
	@Autowired
	private ICodeAuthorizationCodeServices icodeAuthorizationCodeServices;
	
	@Autowired
	private ICodeTokenEnhancer icodeTokenEnhancer;
	
	@Autowired
	private UserApprovalHandler icodeUserApprovalHandler;
	
	@Bean
	@Qualifier("oauth2RedisConnectionFactory")
	public LettuceConnectionFactory oauth2RedisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
		config.setPassword(RedisPassword.of("redispwd"));
		return new LettuceConnectionFactory(config);
	}
	
	@Bean
	@Qualifier("oauth2RedisTemplate")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RedisTemplate redisTemplate() {
		RedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(oauth2RedisConnectionFactory());
		RedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
		return redisTemplate;
	}
	
	@Bean
	public OAuth2RequestFactory oAuth2RequestFactory() {
		DefaultOAuth2RequestFactory oAuth2RequestFactory = new DefaultOAuth2RequestFactory(icodeClientDetialService);
		return oAuth2RequestFactory;
	}
		
	@Bean
	public TokenStore getRedisTokenStore() {
		RedisTokenStore tokenStore = new RedisTokenStore(oauth2RedisConnectionFactory());
		return tokenStore;
	}
	
	@Bean 
	public TokenEnhancer tokenEnhancer() {
		TokenEnhancerChain tokenEnhancer = new TokenEnhancerChain();
		List<TokenEnhancer> delegates = new ArrayList<TokenEnhancer>();
		delegates.add(icodeTokenEnhancer);
		tokenEnhancer.setTokenEnhancers(delegates);
		return tokenEnhancer;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(getRedisTokenStore())
			.authorizationCodeServices(icodeAuthorizationCodeServices)
			.tokenEnhancer(tokenEnhancer())
			.userApprovalHandler(icodeUserApprovalHandler)
			;
	}
	

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//security.realm("SecurityOAuth2/client");
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient("sso_client_app_001")			//定义一个能够访问单点资源的客户端			
			.resourceIds(SSO_RESOURCE_ID)				//该客户端可访问的资源
			.authorizedGrantTypes("authorization_code","client_credentials")//提供两种授权访问方案
			.authorities("ROLE_SSO_CLIENT")				//授权后角色
			.scopes("read")								//具备的权限
			.secret("sso_client_app_001_secret")		//client secret
			.redirectUris("http://localhost:8888/sso-client-one/login");		//合法的redirect uris
//			clients.withClientDetails(clientDetailsService);
	}
}
