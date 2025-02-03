<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Admin Dashboard</title>
		<!-- Header -->
        <%@ include file="../public/header.jsp"%>
        <script src="${pageContext.request.contextPath}/script/add-entity.js"></script>
	</head>

	<body>
		<nav>
			<div class="home-logo">
                <img id="homeIcon" src="${pageContext.request.contextPath}/images/logo.png" width="40" height="32" alt="EBS" />
                <a class="logo" href="/">EBS</a>
            </div>

			<ul>
				<li class="dd-list"><a href="/admin/dashboard" class="active-home link-tag">Home</a></li>

				<li class="dd-list">
					<div class="dropdown">
						<label class="dropbtn" id="active-customer" onclick="dropdownClicked('Customer')">Customer</label>
						<div id="Customer" class="dropdown-content"  style="margin-left:10px;">
							<a class="link-tag" href="/admin/add-consumer">Add Consumer</a>
							 <a class="link-tag" href="/admin/consumers">View Consumers</a>
						</div>
					</div>
				</li>

				<li class="dd-list">
					<div class="dropdown" >
						<label class="dropbtn" id="active-connection" onclick="dropdownClicked('Connection')">Connection</label>
						<div id="Connection" class="dropdown-content" style="margin-left:10px;">
							<a class="link-tag" href="/admin/add-connection">Add Connection</a>
							<a class="link-tag" href="/admin/connections">View Connections</a>
						</div>
					</div>
				</li>

				<li class="dd-list">
					<div class="dropdown" >
						<label class="dropbtn" id="active-bill" onclick="dropdownClicked('Bill')">Bill</label>
						<div  id="Bill" class="dropdown-content" >
							<a class="link-tag" href="/admin/add-bill">Add Bill</a>
							<a class="link-tag" href="/admin/bills">View Bills</a>
						</div>
					</div>
				</li>

				<li class="dd-list">
                    <div class="dropdown" >
                        <label class="dropbtn" id="active-users" onclick="dropdownClicked('User')">Users</label>
                        <div  id="User" class="dropdown-content" >
                            <a class="link-tag" href="/admin/register">Register</a>
                            <a class="link-tag" href="/admin/users">View Users</a>
                        </div>
                    </div>
                </li>

				<li class="dd-list"><a class="active-update-cost link-tag" href="/admin/update-cost" >Update Cost</a></li>

				<li class="dd-list">
					<div class="dropdown">
						<label class="dropbtn" id="active-profile" onclick="dropdownClicked('Profile')">Profile</label>
						<div  id="Profile" class="dropdown-content">
							<a class="link-tag"  href="/admin/profile">View Profile</a>
							<a class="link-tag"  href="/admin/password/update">Change Password</a>
							<a class="link-tag"  href="/logout">LogOut</a>
						</div>
					</div>
				</li>
			</ul>
		</nav>
	</body>
</html>