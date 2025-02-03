<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.ebs.entities.Bill" %>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Payment Page</title>
		<style>
			a.active-home {
				background: var(--c-menu-link-selected);
				transition: .5s;
			}

			.container h1 {
				font-size: 27px;
				margin-bottom: 30px;
				border-bottom: 2px solid #ccc;
			}

			.form-group input[type="date"] {
				width: 100%;
				padding: 3.5px;
				border-radius: 4px;
				border: 1px solid #ccc;
			}

			.row {
				display: flex;
				justify-content: space-between;
				align-items: center;
			}

			.col {
				flex: 1;
				margin-right: 20px;
			}

			.col:last-child {
				margin-right: 0;
			}

			.col label {
				display: block;
				margin-bottom: 5px;
				font-weight: bold;
			}
		</style>
	</head>

	<body>

		<c:choose>
		    <c:when test="${consumer != null}">
		        <%@ include file="../consumer-navbar.jsp" %>
		    </c:when>
		    <c:otherwise>
		        <%@ include file="../../public/default-navbar.jsp"%>
		    </c:otherwise>
		</c:choose>

		<c:if test="${paid}">
		    <h4 style="color:white; margin: 10px;">
                You will be redirected to home page in <span id="seconds"></span> seconds..
            </h4>
		</c:if>

		<div class="container">
			<div class="form-container">
				<h1>
					Payment Details <img alt="card" src="${pageContext.request.contextPath}/images/card.png" width="40" height="40">
				</h1>

				<form action="payment/process" method="post" id="myForm">
					<input type="hidden" name="billNo" value="${billNo}">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

					<div class="form-group">
						<label class="required">Card Number</label>
						<input type="number" class="input-field" placeholder="Enter your card number" oninput="checkInputLength(this)" required maxlength="16">
					</div>

					<div class="form-group">
						<div class="row">
							<div class="col">
								<label class="required">Expiration Date</label>
								<input type="date" class="input-field" id="expiryDateField" min="getMinDate()" name="expiryDate" placeholder="Expiration Date" required>
							</div>
							<div class="col">
								<label class="required">CVC Code</label>
								<input type="number"  class="input-field" placeholder="Enter cvc" required oninput="checkInputLength(this)" maxlength="3">
							</div>
						</div>
					</div>

					<div class="form-group">
		                <label style="font-size: 20px">Amount to Pay: ${totalAmount} &#8377;</label>
					</div>

					<div class="form-group">
						<button type="submit" id="submitButton" disabled>Pay</button>
					</div>

					<c:choose>
						<c:when test="${paid}">
							<div class="message">
								<span>${message}</span>
							</div>
						</c:when>
						<c:otherwise>
							<div class="error">
								<span>${message}</span>
							</div>
						</c:otherwise>
					</c:choose>

				</form>
			</div>
		</div>

		<script>
	        document.addEventListener("DOMContentLoaded", function() {
	            var expiryDateField = document.getElementById('expiryDateField');
	            expiryDateField.min = getMinDate();
	        });

	        // Attach event listeners to all form fields
            document.querySelectorAll('.input-field').forEach(input => {
                input.addEventListener('input', checkFormFields);
            });

            // Initial check in case the form is pre-filled
            checkFormFields();
	    </script>

	    <script>
            document.addEventListener("DOMContentLoaded", function() {
                var seconds = 10;
                document.getElementById('seconds').innerText = seconds;

                var countdown = setInterval(function() {
                    seconds--;
                    document.getElementById('seconds').innerText = seconds;

                    // Redirect when the countdown reaches 0
                    if (seconds <= 0) {
                        clearInterval(countdown);
                        window.location.href = '/';
                    }
                }, 1000);
            });
        </script>
	</body>
</html>