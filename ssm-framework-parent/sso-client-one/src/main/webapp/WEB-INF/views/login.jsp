<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="org.springframework.security.web.WebAttributes,org.springframework.security.core.AuthenticationException"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆页</title>
</head>
<%
	request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
%>
<body>
	<form action="${pageContext.request.contextPath }/login" method="post">
	<center>
	<table>
		<caption>登陆</caption>
		<tr>
			<td>用户名：</td>
			<td><input name="username" type="text" value="${USERNAME }"></td>
		</tr>
		<tr>
			<td>密码：</td>
			<td><input name="password" type="password" value=""></td>
		</tr>
		<c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}">
			<tr align="center">
				<td>${login_error_msg }</td>
			</tr>
		</c:if>
		<tr>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<td><input type="reset" value="重置"></td>
			<td><input type="submit" value="登陆"></td>
		</tr>
	</table>
	</center>
	</form>
</body>
</html>