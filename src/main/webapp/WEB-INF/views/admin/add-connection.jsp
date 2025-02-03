<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Add Connection</title>
		<style>
			#active-connection {
                background:var(--c-menu-link-selected);
                transition: .5s;
            }
		</style>
	</head>
	<body>

		<!-- navbar-->
		<%@ include file="admin-navbar.jsp"%>

		<div class="container">
	        <div class="add-form-container">
	            <h1>Add New Connection</h1>
	            <form id="addConnectionForm" action="add-connection" method="post">
	                <div class="form-grid">
	                    <div class="form-group">
	                        <label for="consumerNum">Consumer Number</label>
	                        <input type="text" id="consumerNum" name="consumerNum" placeholder="Enter consumer number"
	                               required onblur="validateConsumerNo()" maxlength="8">
	                    </div>

	                    <div class="form-group">
	                        <label for="meterNum">Meter Number</label>
	                        <input type="text" id="meterNum" name="meterNum" placeholder="Enter meter number" required maxlength="8">
	                    </div>

	                    <div class="form-group">
	                        <label for="fullName">Full Name</label>
	                        <input type="text" id="fullName" name="fullName" placeholder="Enter name" required maxlength="50">
	                    </div>

	                    <div class="form-group">
	                        <label for="mobNumber">Mobile Number</label>
	                        <input type="number" id="mobNumber" name="mobNumber" placeholder="Enter mobile number"  oninput="checkInputLength(this)"  required maxlength="10">
	                    </div>

	                    <div class="form-group">
                            <label for="emailId">Email ID</label>
                            <input type="email" id="emailId" name="emailId" placeholder="Enter Email id" required maxlength="50">
                        </div>

	                    <div class="form-group">
	                        <label for="address">Address</label>
	                        <input type="text" id="address" name="address" placeholder="Ex : 123, Main Apt, Andheri West, Mumbai, 400001" required maxlength="100">
	                    </div>

	                    <div class="form-group">
	                        <label for="startDate">Connection Start Date:</label>
	                        <input type="date" id="startDate" name="startDate" placeholder="Connection Start Date" required disabled>
	                    </div>

	                    <div class="form-group">
	                        <label for="type" class="con-type-label">Connection Type:</label>
	                        <select id="type" name="type" class="con-type-select">
	                            <option value="Residential">Residential</option>
	                            <option value="Commercial">Commercial</option>
	                        </select>
	                    </div>
	                </div>

	                <input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>

	                <button type="button" class="add-button" onClick="validateMeterNo()">Add Connection</button>

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
