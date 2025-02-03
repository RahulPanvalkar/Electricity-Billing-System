<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Update Cost Per Unit</title>
		<style>
			a.active-update-cost {
				background: var(--c-menu-link-selected);
				transition: .5s;
			}
		</style>
	</head>
	<body>

		<!-- navbar-->
		<%@ include file="admin-navbar.jsp"%>

		<div class="container">
			<div class="form-container">
				<h1>Update Cost</h1>
				<form action="update-cost" method="post" id="updateCostForm">
					<div class="form-group">
						<label for="unitsZeroToHundred">Units 0-100</label>
						<input type="number" step="0.01" required oninput="checkInput(this)" placeholder="${costPerUnit.getUnitsZeroToHundred()}"
						maxlength="6" name="unitsZeroToHundred" id="unitsZeroToHundred">
					</div>
					<div class="form-group">
						<label for="unitsOneHundredOneToThreeHundred">Units 101-300</label>
						<input type="number" step="0.01" required oninput="checkInput(this)" placeholder="${costPerUnit.getUnitsOneHundredOneToThreeHundred()}"
							maxlength="6" name="unitsOneHundredOneToThreeHundred" id="unitsOneHundredOneToThreeHundred">
					</div>
					<div class="form-group">
						<label for="unitsThreeHundredOneToFiveHundred">Units 301-500</label>
						<input type="number" step="0.01" required oninput="checkInput(this)" placeholder="${costPerUnit.getUnitsThreeHundredOneToFiveHundred()}"
							maxlength="6" name="unitsThreeHundredOneToFiveHundred" id="unitsThreeHundredOneToFiveHundred">
					</div>
					<div class="form-group">
						<label for="unitsFiveHundredOneAndAbove">Units 501 and above</label>
						<input type="number" step="0.01" required oninput="checkInput(this)" placeholder="${costPerUnit.getUnitsFiveHundredOneAndAbove()}"
							maxlength="6" name="unitsFiveHundredOneAndAbove" id="unitsFiveHundredOneAndAbove">
					</div>

					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

					<div class="form-group">
						<button type="button" onClick="costValidation()">Update Cost</button>
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
			function costValidation() {
			    console.log("inside costValidation...");
			    let elements = document.querySelectorAll('input[type="number"]');
			    let isFieldEmpty = false;
			    let isCostChanged = false;
			    let issues = [];

			    for (let i = 0; i < elements.length; i++) {
			        let value = elements[i].value;
			        let placeholder = elements[i].placeholder;
			        console.log("value: " + value + " & placeholder: " + placeholder);

			        if (!value) {
			            isFieldEmpty = true;
			        } else if (value !== placeholder) {
			            isCostChanged = true;
			        }
			    }

			    if (isFieldEmpty) {
			        issues.push("One or more fields are empty!");
			    }

			    if (!isFieldEmpty && isCostChanged) {
			        document.getElementById("updateCostForm").submit();
			    } else if (!isFieldEmpty) {
			        issues.push("No changes made in cost!");
			    }

			    if (issues.length > 0) {
			        alert(issues.join("\n"));
			    }
			}
		</script>

	</body>
</html>