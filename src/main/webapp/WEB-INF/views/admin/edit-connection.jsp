<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Edit Connection</title>
		<style>
			#active-connection {
                background: var(--c-menu-link-selected);
                transition: .5s;
            }
		</style>
	</head>
	<body>

		<!-- navbar-->
		<%@ include file="admin-navbar.jsp"%>

		<div class="container">
	        <div class="add-form-container">
	            <h1>Edit Connection</h1>
	            <form action="/admin/connection/edit" method="post">
	                <div class="form-grid">

	                    <div class="form-group">
                            <label for="connId">Consumer Number</label>
                            <input type="text" id="connId" name="connId-disp" value="${connection.connId}" disabled required>
                            <input type="hidden" id="connId_hidden" name="connId" value="${connection.connId}">
                        </div>

	                    <div class="form-group">
	                        <label for="consumerNum">Consumer Number</label>
	                        <input type="text" id="consumerNum" name="consumerNum-disp" value="${connection.consumerNum}" disabled required >
	                        <input type="hidden" id="consumerNum_hidden" name="consumerNum" value="${connection.consumerNum}">
	                    </div>

	                    <div class="form-group">
	                        <label for="meterNum">Meter Number</label>
	                        <input type="text" id="meterNum" name="meterNum" value="${connection.meterNum}" required maxlength="8">
	                    </div>

	                    <div class="form-group">
	                        <label for="fullName">Full Name</label>
	                        <input type="text" id="fullName" name="fullName" value="${connection.fullName}" placeholder="Enter name" required maxlength="50">
	                    </div>

	                    <div class="form-group">
	                        <label for="mobNumber">Mobile Number</label>
	                        <input type="number" id="mobNumber" name="mobNumber" value="${connection.mobNumber}"  placeholder="Enter mobile number"  oninput="checkInputLength(this)"  required maxlength="10">
	                    </div>

	                    <div class="form-group">
	                        <label for="address">Address</label>
	                        <input type="text" id="address" name="address" value="${connection.address}" placeholder="Ex : 123, Main Apt, Andheri West, Mumbai, 400001" required maxlength="100">
	                    </div>

	                    <div class="form-group">
	                        <label for="startDate">Connection Start Date:</label>
	                        <input type="date" id="startDate" name="startDate" value="${connection.startDate}" placeholder="Connection Start Date" required disabled>
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

	                <button type="submit" class="add-button">Update</button>

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
