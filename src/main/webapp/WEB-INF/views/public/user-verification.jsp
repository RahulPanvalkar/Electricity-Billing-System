<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>User Verification</title>
		<style>
			.send-otp {
				display: flex;
                flex-direction: row;
                align-items: center;
			}

			#send-otp-button {
			    width: 35%;
			}

			.check-box{
                margin-bottom: 20px;
                 display: flex;
                 flex-direction: row;
                 column-gap: 5px;
                 align-items: center;
            }
		</style>
	</head>
	<body>

		<!-- navbar-->
		<%@ include file="normal-navbar.jsp"%>

		<div class="container">
			<div class="form-container">
				<h1>User Verification</h1>
				<form action="/register" method="post">
					<div class="form-group">
                        <label for="emailId">Email Id</label>
                        <input type="email" id="emailId" name="emailId" value="${userReg.emailId}" readonly>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" name="password" id="password1" onBlur="validatePassword(this)" placeholder="Enter Password"
                          maxlength="20" required>
                    </div>

                    <div class="form-group">
                        <label for="password2">ReEnter</label>
                        <input type="password" name="password2" id="password2"  placeholder="ReEnter Password"
                          maxlength="20" required>
                    </div>

					<div class="form-group">
						<label for="otpCode">OTP</label>
						<input type="number" name="otpCode" id="otpCode" oninput="checkInputLength(this)" placeholder="Enter OTP"
						  maxlength="6" required>
					</div>

					<div class="check-box">
                        <input type="checkbox" id="showPassword" onClick="showEnteredPasswords()" >
                        <label for="showPassword">Show Passwords</label>
                    </div>

                    <input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>

					<div class="form-group">
						<button type="submit" onClick="return validateInput()">Submit</button>
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

			//function to validate inputs
            function validateInput() {
                let email = document.getElementById("emailId").value;
                console.log("validateInput >> emailId : ", email);
                let otp = document.getElementById("otpCode").value;
                console.log("validateInput >> otp : ", otp);

                if(!email || !otp || !email.trim() || !otp.trim()){
                    return false;
                }
                else if(otp.trim().length < 6 || otp.trim().length > 6){
                    alert("Invalid OTP! OTP should be exactly 6 digits.");
                    return false;
                }

                let pass1 = document.getElementById("password1").value;
                let pass2 = document.getElementById("password2").value;
                console.log("pass1: " + pass1 + ", pass2: " + pass2);

                var regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,}$/;
                if (pass1 !== pass2) {
                    alert("passwords do not match!");
                    return false;
                } else if( !pass1 || pass1.length < 8 ){
                    alert("Password must be at least 8 characters long");
                    return false;
                } else if (!regex.test(password)) {
                    alert("Password must contain at least one capital letter, one small letter, one number, and one special character (!@#$%^&*()_+).");
                    return false;
                }
                return true;
            }

		</script>

		<script>
            document.addEventListener("DOMContentLoaded", function() {
                let isError = <%= request.getAttribute("error") != null ? request.getAttribute("error").toString().equals("true") : false %>;
                console.log("isError: " + isError);
                let alertMessage = "<%= request.getAttribute("MESSAGE") != null ? request.getAttribute("MESSAGE").toString() : "" %>";
                console.log("AlertMessage: " + alertMessage);
                if (!isError && alertMessage && alertMessage.trim() !== "") {
                    setTimeout(function() {
                        alert(alertMessage);
                    }, 100);
                }
            });
        </script>
	</body>
</html>