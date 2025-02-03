<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.ebs.entities.CostPerUnit"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Edit Bill</title>
		<style>
			#active-bill {
                background: var(--c-menu-link-selected);
                transition: .5s;
            }
		</style>
		<script src="${pageContext.request.contextPath}/views/script/AddEntity.js"></script>
	</head>
	<body>

		<!-- navbar-->
		<%@ include file="admin-navbar.jsp"%>

		<div class="container">
	        <div class="add-form-container">
	            <h1>Edit Bill</h1>
	            <form id="addConnectionForm" action="/admin/bill/edit" method="post">
	                <div class="form-grid">
	                    <div class="form-group">
                            <label>Bill No</label>
                            <input type="text" name="billNo-hidden" id="billNo" value="${bill.billNo}" disabled>
                            <input type="hidden" name="billNo" value="${bill.billNo}" required>
                        </div>

	                    <div class="form-group">
	                        <label>Consumer Number</label>
                            <input type="text" name="consumerNum-hidden" id="consumerNum" value="${bill.consumerNum}" disabled>
                            <input type="hidden" name="consumerNum" value="${bill.consumerNum}" required>
	                    </div>

	                    <div class="form-group">
	                        <label>Bill For Month</label>
                            <select id="month" name="month" value="${bill.month}" required>
                                <option value="January">January</option>
                                <option value="February">February</option>
                                <option value="March">March</option>
                                <option value="April">April</option>
                                <option value="May">May</option>
                                <option value="June">June</option>
                                <option value="July">July</option>
                                <option value="August">August</option>
                                <option value="September">September</option>
                                <option value="October">October</option>
                                <option value="November">November</option>
                                <option value="December">December</option>
                            </select>
	                    </div>

	                    <div class="form-group">
	                        <label>Current Reading</label>
                            <input type="number" name="currentReading" id="currentReading" placeholder="Enter Current Reading" required
                                value="${bill.currentReading}" oninput="checkInputLength(this)" maxlength="8">
	                    </div>

	                    <div class="form-group">
                            <label>Previous Reading</label>
                            <input type="number" name="previousReading" id="previousReading" placeholder="Enter Previous Reading" required
                                value="${bill.previousReading}" oninput="checkInputLength(this)" maxlength="8">
                        </div>

	                    <div class="form-group">
	                        <label>Total Unit</label>
                            <input type="number" id="totalUnits" name="totalUnits" placeholder="Total Units" required
                                value="${bill.totalUnits}" maxlength="8" readonly>
	                    </div>

						<div class="form-group">
                            <label>Current Amount</label>
                            <input type="number" id="currentAmount" name="currentAmount" placeholder="Current Amount"
                                value="${bill.currentAmount}" step="0.01" required oninput="checkInput(this)" maxlength="10" readonly>
                        </div>

	                    <div class="form-group">
	                        <label>Previous Balance</label>
                            <input type="number" id="previousBalance" name="previousBalance" placeholder="Previous Balance" readonly
                                value="${bill.previousBalance}" step="0.01" required maxlength="10">
	                    </div>

	                    <div class="form-group">
	                        <label>Total Amount</label>
                            <input type="number" id="totalAmount" name="totalAmount" placeholder="Final Amount"
                                value="${bill.totalAmount}" step="0.01" required oninput="checkInput(this)" maxlength="10" readonly>
	                    </div>

	                    <div class="form-group">
                            <label>Due Date</label>
                            <input type="date" name="dueDate" id="dueDate" placeholder="Due Date" value="${bill.dueDate}" required>
                        </div>

						<input type="hidden" name="billDate" id="billDate" value="getMinDate()" required>
						<input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	                </div>

	                <button type="submit" class="add-button">Update</button>

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


		<%
		    CostPerUnit cost = (CostPerUnit) request.getAttribute("costPerUnit");
			//CostPerUnit cost = new CostPerUnit(3.45,5.55,7.45,8.55);
		    // HashMap to store the cost values
			Map<String, Double> costMap = new HashMap<>();
		    if(cost != null){
			    costMap.put("unitsZeroToHundred", cost.getUnitsZeroToHundred());
			    costMap.put("unitsOneHundredOneToThreeHundred", cost.getUnitsOneHundredOneToThreeHundred());
			    costMap.put("unitsThreeHundredOneToFiveHundred", cost.getUnitsThreeHundredOneToFiveHundred());
			    costMap.put("unitsFiveHundredOneAndAbove", cost.getUnitsFiveHundredOneAndAbove());
			}
		%>

		<script>
			let currentDate = getMinDate();
			console.log("getMinDate >> currentDate :: ",currentDate);
		    // Set the minimum attribute of the due date input to the current date
		    let dueDateElement = document.getElementById('dueDate');
		    dueDateElement.setAttribute('min', currentDate);
		    dueDateElement.value=getMinDate();

		    // setting billDate
		    document.getElementById('billDate').value = currentDate;

			// Set the current month when the page loads
			var currDate = new Date();
			let idx = currDate.getMonth();
	        document.getElementById('month').options[idx].selected = true;
		</script>

		<script>
			//Attach the costMap variable to the window object
			window.costMap = new Map();

			// Populate the costMap with values from the HashMap
			<% for (Map.Entry<String, Double> entry : costMap.entrySet()) { %>
			    window.costMap.set('<%= entry.getKey() %>', <%= entry.getValue() %>);
			<% } %>

			// Attach the calculateBillAmount function to the input field's input event
			document.getElementById('currentReading').addEventListener('blur', calculateBillAmount);
			document.getElementById('previousReading').addEventListener('blur', calculateBillAmount);
			document.getElementById('previousBalance').addEventListener('blur', calculateBillAmount);
		</script>

	</body>

</html>
