<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Header -->
        <%@ include file="../public/header.jsp"%>
	</head>

	<body>
		<nav>
			<img id="homeIcon" src="${pageContext.request.contextPath}/images/logo.png" width="40" height="32"
            				alt="EBS" />
			<a class="logo" href="/">EBS</a>
			<ul>
				<li class="dd-list"><a href="/consumer/dashboard" class="active-home link-tag">Home</a></li>

				<li class="dd-list">
					<div class="dropdown">
						<label class="dropbtn" id="active-bill" onclick="dropdownClicked('bill')">Bill</label>
						<div id="bill" class="dropdown-content">
							 <a class="link-tag" href="/consumer/current-bill">View Bill</a>
						</div>
					</div>
				</li>

				<li class="dd-list">
					<div class="dropdown">
						<label class="dropbtn" id="active-profile" onclick="dropdownClicked('profile')">Profile</label>
						<div id="profile" class="dropdown-content" style="margin-left: 5px;">
							<a class="link-tag" href="/consumer/profile">View Profile</a>
							<a class="link-tag" href="/consumer/password/update">Change Password</a>
							<a class="link-tag" href="/logout">Logout</a>
						</div>
					</div>
				</li>

			</ul>
		</nav>
	</body>
</html>