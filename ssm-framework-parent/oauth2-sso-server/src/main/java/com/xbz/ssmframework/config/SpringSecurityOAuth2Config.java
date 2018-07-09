package com.xbz.ssmframework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.xbz.ssmframework.security.authentication.filter.SysUsernamePasswordAuthenticationFilter;

/**
 * Spring Security OAuth2.0
 * @author xbz
 * 
 *
 */
@Configuration
public class SpringSecurityOAuth2Config extends AuthorizationServerConfigurerAdapter {
	/**
	 * 定义一个SSO单点登录资源
	 */
	public static final String SSO_RESOURCE_ID = "SSO_RESOURCE_ID";
	/**
	 *资源服务器配置 
	 *
	 */
	@Configuration
	@EnableResourceServer
	public static class ResourceServerConfigurer extends ResourceServerConfigurerAdapter{
		private Logger logger = LoggerFactory.getLogger(ResourceServerConfigurer.class); 
		/**
		 * 此方法父类实现说明了：保护了除/oauth/**外其他所有资源且没有做精确的访问控制，在这里与我们应用本身的SpringSecurity是冲突的
		 * 所以我们这里应该重写，并且只拦截我们所需要保护的一些资源
		 */
		@Override
		public void configure(HttpSecurity http) throws Exception {
			logger.debug("配置资源服务器安全配置....");
			http
			.anonymous().disable()
			.csrf().disable()
			.cors().disable()
			.requestMatchers().antMatchers("/oauth/**","/api/**")	//该安全配置所希望拦截的路径
			.and()
			.authorizeRequests()
//				.antMatchers("/me").access("#oauth2.hasScope('read')")					
//				.antMatchers("/photos").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")                                        
//				.antMatchers("/photos/trusted/**").access("#oauth2.hasScope('trust')")
//				.antMatchers("/photos/user/**").access("#oauth2.hasScope('trust')")					
//				.antMatchers("/photos/**").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
//				.regexMatchers(HttpMethod.DELETE, "/oauth/users/([^/].*?)/tokens/.*")
//					.access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('write')")
//				.regexMatchers(HttpMethod.GET, "/oauth/clients/([^/].*?)/users/.*")
//					.access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('read')")
//				.regexMatchers(HttpMethod.GET, "/oauth/clients/.*")
//					.access("#oauth2.clientHasRole('ROLE_CLIENT') and #oauth2.isClient() and #oauth2.hasScope('read')");
			.antMatchers("/api/**").access("#oauth2.isOAuth()")
			;// add by xbz 
		}
		
		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.resourceId(SSO_RESOURCE_ID).stateless(true);
		}
	}
	/**
	 *授权服务器配置 
	 *
	 */
	@Configuration
	@EnableAuthorizationServer
	public static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter{
		@Autowired
		private TokenStore tokenStore;
		@Bean
		public TokenStore getTokenStore() {
			return new InMemoryTokenStore();
		}
		
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.tokenStore(tokenStore);
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
				.redirectUris("http://localhost:8080/sso-client-one/login");		//合法的redirect uris
		}
	}

}
