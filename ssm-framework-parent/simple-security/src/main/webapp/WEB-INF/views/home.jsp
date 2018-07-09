<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人主页</title>
</head>
<body>
	<h1>个人主页</h1>
	<hr/>
	<form id="logoutFormId" action="${pageContext.request.contextPath }/logout" method="POST">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	欢迎,${LOGIN_USER_INFO.nickName }<a href="javascript:logout()">登出</a>
	</form>
</body>
<script type="text/javascript">
function logout(){
	document.getElementById("logoutFormId").submit();
}
</script>
</html>