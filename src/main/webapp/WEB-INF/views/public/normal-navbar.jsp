<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<!-- Header -->
        <%@ include file="header.jsp"%>
	</head>

	<body>
		<nav>
			<div class="home-logo">
				<img id="homeIcon" src="/images/logo.png" width="40" height="32" alt="EBS" />
				<a class="logo" href="/">EBS</a>
			</div>

			<ul>
				<li class="dd-list"><a href="/" class="active-home link-tag">Home</a></li>

				<li class="dd-list"><a href="/sign-in" class="active-login link-tag">Login</a>

				<li class="dd-list"><a href="/register" class="active-register link-tag">Register</a>

				<li class="dd-list"><a href="/about" class="active-about link-tag">About</a></li>
			</ul>
		</nav>
	</body>
</html>