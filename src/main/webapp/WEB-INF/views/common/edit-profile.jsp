<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.ebs.entities.User"%>
<%@page import="com.ebs.entities.Admin"%>
	
<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Edit Profile</title>
		<style>
			#active-profile {
				background: var(--c-menu-link-selected);
				transition: .5s;
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
	        <div class="form-container">

				<h1>Edit Profile</h1>
				<form id="myForm"  action="edit" method="post">
					<div class="form-group">
	                    <label class="label">Full Name</label>
	                    <c:choose>
	                        <c:when test="${userType == 'consumer' }">
		                        <input type="text"	 name="fullName" value="${consumer.fullName}"
	                               required   maxlength="30">
	                        </c:when>
	                        <c:otherwise>
                                    <input type="text"	 name="fullName" value="${admin.firstName} ${admin.lastName}"
                                        required   maxlength="30">
	                        </c:otherwise>
	                    </c:choose>
	                </div>

	                <div class="form-group">
                        <label class="label">Email</label>
                        <c:choose>
                            <c:when test="${userType == 'consumer' }">
                                <input type="email" value="${consumer.emailId}"  name="email" required maxlength="50">
                            </c:when>
                            <c:otherwise>
                                 <input type="email" value="${admin.emailId}"  name="email" required maxlength="50">
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="form-group">
                        <label class="label">Mobile Number</label>
                        <c:choose>
                            <c:when test="${userType == 'consumer' }">
                                <input type="number" value="${consumer.mobNumber}" name="mobile"  oninput="checkInputLength(this)"
                                         required   maxlength="10">
                            </c:when>
                            <c:otherwise>
                                 <input type="number" value="${admin.mobNumber}" name="mobile"  oninput="checkInputLength(this)"
                                 		required   maxlength="10">
                            </c:otherwise>
                        </c:choose>
                    </div>

					<input type="hidden" name="userType" value="${userType}" >

					<div class="form-group">
						<button type="button" onclick="checkUpdate()">Update</button>
					</div>

                    <c:if test="${error}">
                        <div class="error">
                            <span>${message}</span>
                        </div>
                    </c:if>

                    <% session.removeAttribute("message"); %>

				</form>
			</div>
		</div>

		<script>
			let originalNameValue = document.getElementsByName("fullName")[0].value;
			let originalEmailValue = document.getElementsByName("email")[0].value;
			let originalMobileValue = document.getElementsByName("mobile")[0].value;

			function checkUpdate() {
			    let currentNameValue = document.getElementsByName("fullName")[0].value;
			    let currentEmailValue = document.getElementsByName("email")[0].value;
			    let currentMobileValue = document.getElementsByName("mobile")[0].value;

			    if (currentNameValue !== originalNameValue || currentEmailValue !== originalEmailValue || currentMobileValue !== originalMobileValue) {
					document.getElementById("myForm").submit();
			    } else {
					alert("No changes detected");
			    }
			}
		</script>

		<script>
            document.addEventListener("DOMContentLoaded", function() {
                let isError = <%= request.getAttribute("error") != null ? request.getAttribute("error").toString().equals("true") : false %>;
                console.log("isError: " + isError);
                let alertMessage = "<%= request.getAttribute("message") != null ? request.getAttribute("message").toString() : "" %>";
                console.log("AlertMessage: " + alertMessage);
                if (!isError && alertMessage && alertMessage.trim() !== "") {
                    setTimeout(function() {
                        alert(alertMessage);
                    }, 100);
                    //window.location.href = `profile`;
                }
            });
        </script>

	</body>
</html>