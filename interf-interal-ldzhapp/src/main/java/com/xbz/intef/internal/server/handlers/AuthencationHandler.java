package com.xbz.intef.internal.server.handlers;

import com.xbz.intef.internal.model.InternalApiReqHead;
import com.xbz.intef.internal.server.exception.InternalApiException;

public class AuthencationHandler extends AbstractInHandler{
	
	
	@Override
	public boolean isHande(InternalApiReqHead reqHead) {
		return true;
	}

	@Override
	public void doHandle(InternalApiReqHead reqHead, String serviceData, InternalApiContext ctx) throws InternalApiException {
		
	}
}
