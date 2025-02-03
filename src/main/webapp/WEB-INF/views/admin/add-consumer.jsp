<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Add Consumer</title>
		<style>
			#active-customer {
                background: var(--c-menu-link-selected);
                transition: .5s;
            }
		</style>
	</head>

	<body>
		<!-- navbar-->
		<%@ include file="admin-navbar.jsp"%>

		<div class="container">
	        <div class="form-container">
				<h1>Add New Consumer</h1>
				<form action="add-consumer" method="post">
					<div class="form-group">
						<label for="fullName">Full Name</label>
						<input type="text" id="fullName" name="fullName" placeholder="Enter name here" required
								  maxlength="50">
					</div>

					<div class="form-group">
						<label for="emailId">Email Id</label>
						<input type="email" id="emailId" name="emailId" placeholder="Enter email here" required
								  maxlength="50">
					</div>

					<div class="form-group">
						<label for="mobNumber">Mobile Number</label>
						<input type="number" id="mobNumber" name="mobNumber" placeholder="Enter mobile number here"	required
								  oninput="checkInputLength(this)"  maxlength="10">
					</div>

					<div class="form-group">
						<label for="address">Address</label>
						<input type="text"	id="address" name="address" placeholder="Ex : 123, Main Apt, Andheri West, Mumbai, 400001"
						required maxlength="100">
					</div>

					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>

					<div class="form-group">
                        <button type="submit">Add Consumer</button>
                    </div>

					<c:choose>
                        <c:when test="${error}">
                            <div class="error">
                                <span>${message}</span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="message">
                                <span>${message}</span>
                            </div>
                        </c:otherwise>
                    </c:choose>

					<% session.removeAttribute("message"); %>

				</form>
			</div>
		</div>

	</body>
</html>
