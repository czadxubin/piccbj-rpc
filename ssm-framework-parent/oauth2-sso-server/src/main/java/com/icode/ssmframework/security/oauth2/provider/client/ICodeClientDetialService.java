package com.icode.ssmframework.security.oauth2.provider.client;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

//@Service
public class ICodeClientDetialService implements ClientDetailsService{

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		return null;
	}

}
