<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isErrorPage="true" %>

<html>
	<head>
	    <meta charset="UTF-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	    <title>Error</title>
	</head>

	<body>
	    <%@ include file="default-navbar.jsp" %>

		<div class="container">
		    <div class="error-container">
		        <h1>Error</h1>

				 <c:if test="${not empty exception}">
				    <div class="error-div">
				        <p class="exception-para"><strong>Exception:</strong> ${exceptionName}</p>
				        <p class="exception-para"><strong>Message:</strong> ${exception.message}</p>
				    </div>
				</c:if>

				<c:if test="${empty exception}">
				    <div class="error-div">
				        <p class="empty-exc-para">Something went wrong, please try again later.</p>
				    </div>
				</c:if>

		        <p>
		            <a href="javascript:history.back()" class="back-btn">Go Back</a>
		        </p>
		    </div>
	    </div>
	</body>
</html>
