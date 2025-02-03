<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>


<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>User Registration</title>
        <style>
            a.active-register {
                background: var(--c-menu-link-selected);
                transition: .5s;
            }
        </style>
    </head>

    <body>

        <!-- navbar-->
        <%@ include file="normal-navbar.jsp"%>

        <c:if test="${Register}">
            <h4 style="color:white; margin: 10px;">
                You will be redirected to login page in <span id="seconds"></span> seconds..
            </h4>
        </c:if>

        <div class="container">

            <div class="form-container">
                <h1>User Registration</h1>
                <form id= "myForm" action="register/user-verification" method="post">

                    <div class="form-group">
                        <label for="firstName">First Name</label>
                        <input type="text" id="firstName" name="firstName" placeholder="Enter First Name" required  maxlength="25">
                    </div>

                    <div class="form-group">
                        <label for="lastName">First Name</label>
                        <input type="text" id="lastName" name="lastName" placeholder="Enter Last Name" required  maxlength="25">
                    </div>

                    <div class="form-group">
                        <label for="emailId">Email Id</label>
                        <input type="email" id="emailId" name="emailId" placeholder="Email Address" required  maxlength="60">
                    </div>

                    <div class="form-group">
                        <label for="mobNumber">Mobile No</label>
                        <input type="number"  id="mobNumber" name="mobNumber" placeholder="Mobile Number" required  oninput="checkInputLength(this)"  maxlength="10">
                    </div>

                    <div class="form-group">
                        <label for="address">Address</label>
                        <input type="text"  placeholder="Address" id="address" name="address" required  maxlength="200">
                    </div>

                    <input type="hidden" name="userType" value="C">
                    <input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="form-group">
                        <button type="submit" onClick="return valRegisterFormData()">Register</button>
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
                    <% session.removeAttribute("error"); %>

                    <div class="text-center">
                        <p>
                            Already have an account? <a href="/sign-in">Login here</a>
                        </p>
                    </div>
                </form>
            </div>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function() {
                var seconds = 10;
                let secondsElement = document.getElementById('seconds');

                if(secondsElement){
                    secondsElement.innerText = seconds;

                    var countdown = setInterval(function() {
                        seconds--;
                        secondsElement.innerText = seconds;

                        // Redirect when the countdown reaches 0
                        if (seconds <= 0) {
                            clearInterval(countdown);
                            window.location.href = '/sign-in';
                        }
                    }, 1000);
                }
            });
        </script>

    </body>
</html>
