<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page isELIgnored="false"%>
<%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sign in</title>
<link href="<c:url value="/resources/css/login&registration.css"/>"
	rel="stylesheet">
<script src="<c:url value="/resources/js/jquery-2.1.4.js" />"></script>
<script src="<c:url value="/resources/js/authentication.js" />"></script>


<%-- <link href="<c:url value="/resources/css/login&registration.css" />" rel="stylesheet"> --%>
<!-- <link rel="stylesheet" href="css/login&registration.css"> -->
<!-- <script src="resources/jquery-2.1.4.js"></script> -->
<!-- <script src="resources/js/authentication.js"></script> -->
<script>
	$(document).ready(function() {
		$("input[type=text]:first").focus();
	});
</script>
</head>
<body>

	<div id="loginContainer">
		<div id="confirm-result1">
			<div>User authentication:</div>
		</div>
		<form name='loginForm'
			action="<c:url value='/j_spring_security_check' />" method='POST'>
			<div id="loginUserInput">
				<div id="insideUserInput">

					<table class="userInput">
						<tbody>
							<tr>
								<td><input type='text' name='username'
									placeholder="Username" id="logUsername"></td>
							</tr>
							<tr>
								<td><input type='password' name='password'
									placeholder="Password" id="logPassword" /></td>
							</tr>
							<tr>
								<td>
									<button type="submit" id="loginBtn" name="log">
										<!-- height="101px"	width="282px" -->
										<img
											src="${pageContext.request.contextPath}/resources/img/login_button.png"
											height="40px" width="112px" />
									</button> <!-- 								<input name="submit" type="submit" value="submit" /> -->
								</td>
							</tr>

							<tr align="right" height="10px">
								<td><div id="registration-link">
										<a
											href="${pageContext.request.contextPath}/authentication/registration">Registration</a>
									</div></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>
		<div id="confirm-result2">
			<c:if test="${not empty error}">
				<div class="error">${error}</div>
			</c:if>
			<c:if test="${not empty msg}">
				<div class="msg">${msg}</div>
			</c:if>
			<%-- 			<h6>${result}</h6> --%>
		</div>
	</div>

	<div id="footer">
		<table class="footerNav">
			<tr>
				<td align="right">&#169;</td>
			</tr>
		</table>
	</div>

	<script type="text/javascript">
		var h = document.getElementById('loginContainer').offsetHeight;
		var q = document.getElementById('footer').offsetHeight;
		document.getElementById('loginContainer').style.height = h - q + 'px';
	</script>
</body>
</html>