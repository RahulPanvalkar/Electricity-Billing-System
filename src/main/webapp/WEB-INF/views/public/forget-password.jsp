<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Forget Password</title>
		<style>
			#active-login {
				background: var(--c-menu-link-selected);
				transition: .5s;
			}
			.send-otp {
				display: flex;
                flex-direction: row;
                align-items: center;
			}

			#send-otp-button {
			    width: 35%;
			}

		</style>
	</head>
	<body>

		<!-- navbar-->
		<%@ include file="normal-navbar.jsp"%>

		<div class="container">
			<div class="form-container">
				<h1>User Verification</h1>
				<form action="/forget-password/verification" method="post">
					<div class="form-group">
                        <label for="emailId">Email Id</label>
                        <div class="send-otp">
	                        <input type="email" name="emailId" id="emailId"
	                            placeholder="Enter your email id" required maxlength="50">
	                        <button id="send-otp-button" type="button" onClick="sendOtpRequest()" >Send OTP</button>
                        </div>
                    </div>

					<div class="form-group">
						<label for="otpCode">OTP</label>
						<input type="number" name="otpCode" id="otpCode" oninput="checkInputLength(this)"  maxlength="6" required>
					</div>

					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>

					<div class="form-group" >
						<button id="submit-btn" type="submit" style="cursor: default;" onClick="return validateEmailAndOTP()">Submit</button>
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

		<script>
			function sendOtpRequest() {
                let email = document.getElementById("emailId").value;
                console.log("sendOtpRequest >> emailId : ", email);

		        if (!validateEmail(email)) {
		            return;
		        }

				let csrf = document.getElementById("csrf").value;
                console.log("sendOtpRequest >> csrf : ", csrf);

                fetch("/forget-password", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': csrf
                    },
                    body: JSON.stringify({ emailId: email.trim() })
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("Response data:", data);
                    alert(data.MSG);
                    if (data.enableOTP) {
                        console.log("enabling field....");
                        let otpCode = document.getElementById("otpCode");
                        otpCode.disabled = false;
                        otpCode.placeholder="Enter OTP";

                        let submitBtn = document.getElementById("submit-btn");
                        submitBtn.style.cursor="pointer";
                    }

                })
                .catch(error => {
                    console.error("Error:", error);
                });
            }
		</script>

		<script>

			//function to validate inputs
            function validateEmailAndOTP() {
                let email = document.getElementById("emailId").value;
                console.log("validateEmailAndOTP >> emailId : ", email);
                let otp = document.getElementById("otpCode").value;
                console.log("validateEmailAndOTP >> otp : ", otp);

                if(!email || !otp || !email.trim() || !otp.trim()){
                    return false;
                }
                else if(otp.trim().length < 6 || otp.trim().length > 6){
                    alert("Invalid OTP! OTP should be exactly 6 digits.");
                    return false;
                }
                return true;
            }

		</script>
	</body>
</html>