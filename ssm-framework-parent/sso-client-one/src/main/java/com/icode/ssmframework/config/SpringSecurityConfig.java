package com.icode.ssmframework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.icode.ssmframework.security.oauth2.sso.filter.SSOClientLoginFilter;
import com.icode.ssmframework.security.oauth2.sso.provider.UserInfoAuthenticationProvider;

/**
 * Oauth资源服务器启动的SpringSecurity顺序是3默认EnableWebSecurity启动顺序是100（深坑）
 * 所以这里要声明一个Order为2的，保证应用本身SpringSecurity在资源服务器前面
 * @author 许小宝
 *
 */
@Configuration
@EnableWebSecurity
@Order(2)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(SpringSecurityConfig.class); 
	@Bean
	public PasswordEncoder passwordEnCoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean 
	public AuthenticationProvider daoAuthenticationProvider() {
		AuthenticationProvider authenticationProvider = new UserInfoAuthenticationProvider();
		return authenticationProvider;
	}
	@Bean
	public SSOClientLoginFilter usernamePasswordAuthenticationFilter() throws Exception {
		SSOClientLoginFilter filter = new SSOClientLoginFilter("/login");
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAllowSessionCreation(true);
		return filter;
	}
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		AuthenticationManager authenticationManagerBean = super.authenticationManagerBean();
		return authenticationManagerBean;
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("SpringSecurity安全配置....");
		http
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		.and()
		.addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers("/login").permitAll()
		.antMatchers("/**").hasAnyRole("USER","ADMIN")
		.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
		.and()
			.logout()
				.invalidateHttpSession(true)
				.logoutSuccessUrl("/")
				.logoutUrl("/logout")
				.deleteCookies("JSESSIONID")
			;
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/statics/**","/api/**","/oauth/**");
	}
	
}
