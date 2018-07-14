package com.icode.ssmframework.security.oauth2.sso.userdetials;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import com.icode.ssmframework.core.security.userdetials.SysUserDetails;

/**
 * 	通过OAuth2.0方式根据用户名得到单点系统的用户信息
 * @author 许宝众
 *
 */
@Service
@Qualifier("ssoUserDetialService")
public class SSOUserDetialService implements UserDetailsService{
	@Autowired
	@Qualifier("ssoOAuth2RestTemplate")
	private OAuth2RestTemplate ssoOAuth2RestTemplate;
	@Autowired
	private OAuth2ClientContext oauth2Context;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String,?> uriVariables = new HashMap<String,String>();
		String url = "http://localhost:8080/SSO-Server/api/user/"+username;
		ResponseEntity<SysUserDetails> responseEntity = ssoOAuth2RestTemplate.getForEntity(url , SysUserDetails.class, uriVariables);
		int statusCodeValue = responseEntity.getStatusCodeValue();
		if(statusCodeValue==200) {
			return responseEntity.getBody();
		}
		return null;
	}

}
