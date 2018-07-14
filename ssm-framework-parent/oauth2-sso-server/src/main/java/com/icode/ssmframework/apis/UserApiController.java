package com.icode.ssmframework.apis;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.icode.ssmframework.security.userdetials.SysJdbcUserDetailService;

@RestController
@RequestMapping("/api")
public class UserApiController {
	private static Logger logger = LoggerFactory.getLogger(UserApiController.class);
	@Autowired
	private SysJdbcUserDetailService sysJdbcUserDetialService;
	/**
	 * 	得到details
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/user/{userId}",method=RequestMethod.GET,produces= {"application/json;charset=utf-8"})
	public ResponseEntity<UserDetails> getSysUserDetials(@PathVariable String userId,Principal principal){
		UserDetails userDetails = sysJdbcUserDetialService.loadUserByUsername(userId);
		logger.info("[获取用户信息]-[userId:{}]-[userDetials:{}]",userId,JSON.toJSONString(userDetails));
		if(userDetails!=null) {
			return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
		}else {
			return new ResponseEntity<UserDetails>(HttpStatus.UNAUTHORIZED);
		}
	}
	
}
