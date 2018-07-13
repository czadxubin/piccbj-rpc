package com.xbz.ssmframework.security.oauth2.provider.approval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.stereotype.Component;
/**
 * 可以允许特殊请求不需要经过用户去页面进行授权，如登陆操作
 * @author 许小宝
 *
 */
@Component
public class ICodeApprovalHandler extends DefaultUserApprovalHandler{
	@Override
	public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest,
			Authentication userAuthentication) {
		Map<String, String> requestParameters = authorizationRequest.getRequestParameters();
		String operateType = requestParameters.get("operate_type");
		//登录授权操作，直接通过授权，不需要用户再去页面点击授权
		if("authorized_login".equals(operateType)) {
			authorizationRequest.setApproved(true);
			//设置默认权限
			List<String> scopes = new ArrayList<String>();
			scopes.add("read");
			authorizationRequest.setScope(scopes);
		}
		return super.checkForPreApproval(authorizationRequest, userAuthentication);
	}
}
