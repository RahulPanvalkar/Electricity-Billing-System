<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>

<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Sign In</title>
        <style>
            a.active-login {
                background: var(--c-menu-link-selected);
                transition: .5s;
            }
        </style>
    </head>

    <body>
        <!-- navbar-->
        <%@ include file="normal-navbar.jsp"%>

        <div class="container">
            <div class="form-container">

                <h1>Login</h1>
                <form action="login" method="post">
                    <div class="form-group">
                        <label for="username">UserID</label>
                        <input type="text" id="username" name="username" placeholder="Enter your id" maxlength="50">
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Enter your password"
                            required maxlength="20">
                    </div>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="form-group">
                        <button type="submit">Login</button>
                    </div>

                    <c:if test="${param.error != null}">
                        <div class="error">
                            <span>Invalid username or password</span>
                        </div>
                    </c:if>

                    <div class="text-center">
                        <a href="/forget-password">Forgot password?</a>
                        <p>
                            Don't have an account? <a href="/register">Register</a>
                        </p>
                    </div>
                </form>
            </div>
        </div>

    </body>
</html>
