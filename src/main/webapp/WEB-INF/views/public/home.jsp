<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>

<html lang="en">
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Home</title>
		<style type="text/css">
			a.active-home {
				background: var(--c-menu-link-selected);
				transition: .5s;
			}
		</style>
	</head>
	<body id="home-body">
		<!-- Navbar -->
		<%@ include file="normal-navbar.jsp"%>

		<main class="container">
			<section class="quick-bill-pay-container">
				<h1 id="quick-bill-header">Quick Bill Payment</h1>
				<form action="/quick-bill" method="post">
					<label id="consumerNo-label" for="consumerNo">CA Number</label>
					<div class="input-group">
						<input type="number" id="consumerNo" name="consumerNo" oninput="checkInputLength(this)"
							placeholder="Enter Consumer Number" required maxlength="8">
						<a id="sample-bill" onclick="showPopup()">Sample Bill</a>
					</div>

					<div class="captcha-group">
						<input type="checkbox" id="captcha" name="captcha" required>
						<label for="captcha">I'm not a robot</label>
					</div>
					<br>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

					<button id="submit-button" type="submit">Proceed</button>
					<div class="centered-text">
						<span>${message}</span>
					</div>
					<% session.removeAttribute("message"); %>
				</form>
			</section>

			<!-- Popup -->
			<aside class="popup-overlay" id="popup" onclick="hidePopup()">
				<div class="popup-content">
					<img src="/images/sample-bill.jpg" width="600" height="600" alt="Sample Bill">
				</div>
			</aside>
		</main>

	</body>
</html>
