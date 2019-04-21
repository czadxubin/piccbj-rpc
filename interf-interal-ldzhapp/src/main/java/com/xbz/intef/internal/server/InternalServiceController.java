package com.xbz.intef.internal.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xbz.intef.internal.server.handlers.HandlerChain;

@RestController
@RequestMapping("/internalApi")
public class InternalServiceController {
	@Autowired
	private HandlerChain handlerChain;
	private static Logger logger = LoggerFactory.getLogger(InternalServiceController.class);
	@RequestMapping(value="{path}")
	public String internalApi(@PathVariable String path,HttpServletRequest request,HttpServletResponse response) {
		if("endpoint".equals(path)) {
			return this.handlerChain.invokeService(request,response);
		}else {
			return "welcome to internal api service";
		}
	}
}
