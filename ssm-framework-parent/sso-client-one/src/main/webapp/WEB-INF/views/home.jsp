<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>icode-myapp</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/statics/js/jquery/3.3.1/jquery-3.3.1.js"></script>
</head>
<body>
	<h1>MyApp</h1>
	<hr/>
	<form id="logoutFormId" action="${pageContext.request.contextPath }/logout" method="POST">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	欢迎${LOGIN_USER_INFO.user.nickName },来到MyApp		<a href="javascript:logout()">登出</a>
	</form>
	<hr/>
	<a href="http://localhost:8080/SSO-Server">认证中心</a>
	<br>
	<a href="javascript:getClientCredentialAccessToken()">获取客户端AccessToken</a>
	<div style="border: solid 1px red;" id="clienti_access_token_div_id">展示当前客户端AccessToken</div>
</body>
<script type="text/javascript">
function logout(){
	document.getElementById("logoutFormId").submit();
}
function getClientCredentialAccessToken(){
	$.ajax({
		url:"${pageContext.request.contextPath }/admin/getClientCredentialAccessToken"
		,type:"POST"
		,dataType:"json"
		,data:{}
	})
	.done(function(data){
		console.log(JSON.stringify(data));
		$("#clienti_access_token_div_id").html(JSON.stringify(data));
	})
	.error(function(){alert("服务端处理异常")})
}
</script>
</html>