<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration</title>
<link href="<c:url value="/resources/css/login&registration.css"/>"
	rel="stylesheet">
<script src="<c:url value="/resources/js/jquery-2.1.4.js" />"></script>
<script src="<c:url value="/resources/js/authentication.js" />"></script>

<!-- <link rel="stylesheet" href="css/login&registration.css"> -->
<!-- <script src="jQuery/jquery-2.1.4.js"></script> -->
<!-- <script src="jQuery/authentication.js"></script> -->
</head>
<body>
	<div id="registrationContainer">
		<div id="containerTopNav">
			<table class="containerTopNav">
				<tr>
					<td align="right"><a
						href="${pageContext.request.contextPath}/">back to sign in</a></td>
				</tr>
			</table>
		</div>
		<div id="confirm-result1">
			<div>Registration:</div>
		</div>

		<form method=POST
			action="${pageContext.request.contextPath}/authentication/check_user">
			<div id="registrationUserInput">
				<div id="insideUserInput">
					<!-- border="1" -->
					<table class="userInput">
						<tr>
							<td align="right" style="width: 39%">Enter login here:</td>
							<td><input type="text" placeholder="Username"
								id="regUsername" name="login" required></td>
						</tr>
						<tr>
							<td align="right">Enter password here:</td>
							<td><input type="password" placeholder="Password"
								id="regPassword" name="password" pattern=".{5,30}"
								title="5 to 30 characters required for password" required></td>
						</tr>
						<tr>
							<td colspan="2" height="40px"><div id="submitRegInputBtn">
									<button type="submit" id="submitRegInputBtn" name="reg">
										<img src="resources/img/okBtn.png" height="100%" width="100%" />
									</button>
								</div></td>
						</tr>
					</table>
				</div>
			</div>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>

		<div id="confirm-result2">
			<h6>${result}</h6>
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
		$(document).ready(
				function() {
					var registrationContainerHeight = $(
							"#registrationContainer").height();
					var footerHeight = $("#footer").height();
					console.log(registrationContainerHeight + " "
							+ footerHeight);
					$("#registrationContainer").css({
						"height" : registrationContainerHeight - footerHeight
					});
				});
	</script>
</body>
</html>