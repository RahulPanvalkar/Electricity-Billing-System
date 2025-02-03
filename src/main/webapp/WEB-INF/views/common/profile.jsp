<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.ebs.entities.Admin"%>
<%@page import="com.ebs.entities.User"%>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Profile</title>
		<style>
			#active-profile {
			    background: var(--c-menu-link-selected);
			    transition: .5s;
			}

			.profile-container {
				width: 400px;
			    align-self: start;
			    margin: 50px;
			    padding: 20px;
			    background-color: #fff;
			    border-radius: 10px;
			    text-align: center;
			}

			.profile-container h1 {
				color: #337ab7;
				text-decoration: underline;
			}

			.profile-info {
				margin-top: 30px;
			}

			.profile-info h2 {
				color: #333;
			}

			.profile-info div {
				margin-top: 10px;
				color: #777;
			}

			.profile-info .label {
				font-weight: bold;
				font-size: 20px;
			}

			.profile-info .value {
				margin-left: 10px;
				font-size: 20px;
			}

			.profile-info .button {
				display: inline-block;
                padding: 8px 15px;
                background-color: #000;
                color: #fff;
                text-decoration: none;
                border-radius: 20px;
                margin-top: 20px;
                font-weight: 700;
			}

			.button:hover{
				color: #fff;
                background-color: #00BCD4;
			}
		</style>
	</head>
	<body>

		<!-- navbar-->
		<c:choose>
			<c:when test="${ consumer != null }">
				<%@ include file="../consumer/consumer-navbar.jsp"%>
				<c:set var="userType" value="consumer" />
			</c:when>
			<c:when test="${admin != null }">
				<%@ include file="../admin/admin-navbar.jsp"%>
				<c:set var="userType" value="admin" />
			</c:when>
			<c:otherwise>
				<%@ include file="../public/default-navbar.jsp"%>
			</c:otherwise>
		</c:choose>

		<div class="container">
			<div class="profile-container">
				<h1>Profile</h1>
				<div class="profile-info">
					<h2>Personal Information</h2>

					<div>
	                    <label class="label">Name:</label>
	                    <c:choose>
	                        <c:when test="${userType == 'consumer' }">
	                            <span class="value"><c:out value="${consumer.fullName}" /></span>
	                        </c:when>
	                        <c:otherwise>
	                             <span class="value"><c:out value="${admin.firstName} ${admin.lastName}" /></span>
	                        </c:otherwise>
	                    </c:choose>
	                </div>

					<c:choose>
						<c:when test="${userType == 'consumer' }">
							<div>
								<label class="label">Consumer Number:</label>
								<span class="value"><c:out value="${consumer.consumerNum}" /></span>
							</div>
						</c:when>
						<c:otherwise>
							<div>
								<label class="label">Admin Id:</label>
								<span class="value"><c:out value="${admin.id}" /></span>
							</div>
						</c:otherwise>
					</c:choose>

					<div>
	                    <label class="label">Phone:</label>
	                    <c:choose>
	                        <c:when test="${userType == 'consumer' }">
	                            <span class="value"><c:out value="${consumer.mobNumber}" /></span>
	                        </c:when>
	                        <c:otherwise>
	                             <span class="value"><c:out value="${admin.mobNumber}" /></span>
	                        </c:otherwise>
	                    </c:choose>
	                </div>

	                <div>
		                <label class="label">Email:</label>
						<c:choose>
		                    <c:when test="${userType == 'consumer' }">
		                        <span class="value"><c:out value="${consumer.emailId}" /></span>
		                    </c:when>
		                    <c:otherwise>
		                         <span class="value"><c:out value="${admin.emailId}" /></span>
		                    </c:otherwise>
		                </c:choose>
	                </div>

	                <div>
	                    <label class="label">Location:</label>
						<c:choose>
		                    <c:when test="${userType == 'consumer' }">
		                        <span class="value"><c:out value="${consumer.address}" /></span>
		                    </c:when>
		                    <c:otherwise>
		                        <span class="value"><c:out value="${admin.address}" /></span>
		                    </c:otherwise>
		                </c:choose>
	                </div>

					<div>
                        <a href="profile/edit" class="button">Edit Profile</a>
	                </div>
				</div>
			</div>
		</div>
	</body>
</html>
