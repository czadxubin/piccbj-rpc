/*package com.xbz.ssmframework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

*//**
 * Spring Security OAuth2.0
 * @author xbz
 * 
 *
 *//*
@Configuration
@EnableResourceServer
public class SpringSecurityOAuth2Config extends ResourceServerConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(SpringSecurityOAuth2Config.class); 
	*//**
	 * 此方法父类实现说明了：保护了除/oauth/**外其他所有资源且没有做精确的访问控制，在这里与我们应用本身的SpringSecurity是冲突的
	 * 所以我们这里应该重写，并且只拦截我们所需要保护的一些资源
	 *//*
	@Override
	public void configure(HttpSecurity http) throws Exception {
		logger.debug("配置资源服务器安全配置....");
		http
		.anonymous().disable()
		.csrf().disable()
		.cors().disable()
		.requestMatchers().antMatchers("/api/**")	//该安全配置所希望拦截的路径
		.and()
		.authorizeRequests()
		//.antMatchers("/oauth/authorize").permitAll()
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
		resources.resourceId(OAuth2AuthorizationServerConfig.SSO_RESOURCE_ID).stateless(true);
	}
}
*/