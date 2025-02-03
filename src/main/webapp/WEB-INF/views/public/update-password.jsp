<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>	
	
<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Update Password</title>
		<style>
			#active-profile {
				background: var(--c-menu-link-selected);
				transition: .5s;
			}

			.check-box{
			    margin-bottom: 20px;
                display: flex;
                flex-direction: row;
                column-gap: 5px;
                align-items: center;
			}
		</style>
		<!-- for password hashing -->
		<script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.1.1/crypto-js.min.js"></script>
	</head>
	<body>

		<!-- navbar /// Add other navbar options-->

		        <%@ include file="normal-navbar.jsp"%>

		<div class="container">
			<div class="form-container">
				<form action="${reqType}" method="post">

					<c:choose>
	                    <c:when test="${reqType == 'reset'}">
	                        <h1>Reset Password</h1>
	                    </c:when>
	                    <c:otherwise>
	                        <h1>Change Password</h1>
	                        <div class="form-group">
	                            <input type="password" id="oldPassword" name="oldPassword"
	                                   placeholder="Enter Old Password" required maxlength="20">
	                        </div>
	                    </c:otherwise>
	                </c:choose>

					<div class="form-group">
						<input type="password" id="password1" name="password1"
							placeholder="Enter New Password" required maxlength="20">
					</div>

					<div class="form-group">
						<input type="password" id="password2" name="password2" onBlur="validatePassword(this)"
						placeholder="Re-Enter Password" required maxlength="20">
					</div>

					<div class="check-box">
						<input type="checkbox" id="showPassword" onClick="showEnteredPasswords()" >
						<label for="showPassword">Show Passwords</label>
					</div>

					<input type="hidden" name="userId" id="userId" value="${user.userId}">
					<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <c:choose>
                        <c:when test="${reqType == 'reset'}">
                            <div class="form-group">
                                <button type="submit" onClick="return validatePasswords()">Reset</button>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="form-group">
                                <button type="submit" onClick="return validatePasswords()">Change</button>
                            </div>
                        </c:otherwise>
                    </c:choose>

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
			//function to validate passwords
			function validatePasswords() {
			    console.log("Inside validatePasswords..");

			    let pass1 = document.getElementById("password1").value;
			    let pass2 = document.getElementById("password2").value;

			    console.log("pass1: " + pass1 + " && pass2: " + pass2);

			    let reqType = "${reqType}";
			    console.log("reqType: " + reqType);

			    let returnValue = false;

                var regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,}$/;
			    if (reqType === "change") {
			        let oldPass = document.getElementById("oldPassword").value;
			        console.log("OldPass: ", oldPass);
			        if (!oldPass || !oldPass.trim() || oldPass.length < 8) {
				        alert("Invalid old password");
				        return false;
			        } else if (pass1 !== pass2) {
			            alert("New passwords does not match!");
			            return false;
			        } else if (oldPass === pass1) {
			            alert("Old password and new password cannot be the same!");
			            return false;
			        } else if( !pass1 || pass1.length < 8 ){
                        alert("Password must be at least 8 characters long");
                        return false;
                    } else if (!regex.test(password)) {
                        alert("Password must contain at least one capital letter, one small letter, one number, and one special character (!@#$%^&*()_+).");
                        return false;
                    }
			        else {
			            returnValue = true; // Validation passed
			        }
			    }
			    else if (reqType === "reset") {
			        if (!pass1 || !pass2 || pass1.length < 8 || pass2.length < 8) {
		                alert("Password must be at least 8 characters long");
		                return false;
			        } else if (pass1 !== pass2) {
			            alert("New passwords does not match!");
			            return false;
			        } else if( !pass1 || pass1.length < 8 ){
                        alert("Password must be at least 8 characters long");
                        return false;
                    } else if (!regex.test(password)) {
                        alert("Password must contain at least one capital letter, one small letter, one number, and one special character (!@#$%^&*()_+).");
                        return false;
                    }
			        else {
			            returnValue = true; // Validation passed
			        }
			    }
			    console.log("returnValue: ", returnValue);
			    if(returnValue){
                    // Hash the new password using SHA-256
                    var hashedPassword1 = CryptoJS.SHA256(pass1).toString();

                    // Set the hashed passwords back to the form inputs
                    pass1 = hashedPassword1;
                    pass2 = hashedPassword1; // Ensure both hashed passwords match
					console.log("pass1: " + pass1 + " && pass2: " + pass2);
                    if (reqType === "Change") {
                        let oldPass = document.getElementById("oldPassword").value;
						// Optional: Hash the old password if it exists in the form
						oldPass = oldPass ? CryptoJS.SHA256(oldPass).toString() : null;
						console.log("pass1.value: " + pass1.value);
                    }
                }
			    return returnValue;
			}

			function hashPassword() {
                var password = document.getElementById("password").value;
                var hashedPassword = CryptoJS.SHA256(password).toString(); // Using SHA-256 hash algorithm
                console.log("Hashed Password: ", hashedPassword);

                // Send the hashed password to the server
                var payload = {
                    password: hashedPassword
                };

                // Example of sending it using fetch
                fetch('/password-reset', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                }).then(response => response.json())
                  .then(data => console.log(data));
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
	                    window.location.href = "/sign-in";
                    }, 100);
			    }
			});
		</script>

	</body>
</html>



