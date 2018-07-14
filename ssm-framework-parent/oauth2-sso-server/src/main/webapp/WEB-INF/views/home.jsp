<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>icode认证中心</title>
</head>
<body>
	<h1>认证中心</h1>
	<hr/>
	<form id="logoutFormId" action="${pageContext.request.contextPath }/logout" method="POST">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	欢迎${LOGIN_USER_INFO.user.nickName },来到来到认证中心	<a href="javascript:logout()"><p title="目前暂不支持登陆应用群同时退出">登出</p></a>
	</form>
	<hr/>
	<a href="http://localhost:8888/sso-client-one">MyApp</a>
</body>
<script type="text/javascript">
function logout(){
	document.getElementById("logoutFormId").submit();
}
</script>
</html>