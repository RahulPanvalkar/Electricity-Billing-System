<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.ebs.entities.Bill"%>
<%@page import="com.ebs.entities.User"%>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Pending Bills</title>
		<style>
			#active-bill{
				background: var(--c-menu-link-selected);
				transition: .5s;
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

		<div class="container">
			<div class="table-container">
				<h1>Pending Bills</h1>
				<table border="1">
					<tr>
						<th>Bill No</th>
						<th>Bill Date</th>
						<th>Consumer Number</th>
						<th>Meter Number</th>
						<th>Bill For Month</th>
						<th>Total Units</th>
						<th>Current Bill Amount</th>
						<th>Previous Balance</th>
						<th>Total Amount</th>
						<th>Due Date</th>
						<th>Status</th>
						<th>Action</th>
					</tr>

					<c:choose>
						<c:when test="${currentBill == null}">
							<tr>
								<td colspan="12"><span class="no-data-row">Pending Bill Not Found</span></td>
							</tr>
						</c:when>

						<c:otherwise>
						<tr>
							<td><c:out value="${currentBill.billNo}" /></td>
							<td><fmt:formatDate value="${currentBill.billDate}" pattern="dd/MM/yyyy" /></td>
							<td><c:out value="${currentBill.consumerNum}" /></td>
							<td><c:out value="${currentBill.meterNum}" /></td>
							<td><c:out value="${currentBill.month}" /></td>
							<td><c:out value="${currentBill.totalUnits}" /></td>
							<td><c:out value="${currentBill.currentAmount}" /></td>
							<td><c:out value="${currentBill.previousBalance}" /></td>
							<td><c:out value="${currentBill.totalAmount}" /></td>
							<td><fmt:formatDate value="${currentBill.dueDate}" pattern="dd/MM/yyyy" /></td>
							<td><c:out value="${currentBill.status}" /></td>
							<td>
								<form action="pending-bill/payment" method="post">
		                            <input type="hidden" name="billNo" value="${currentBill.billNo}" />
		                            <input type="hidden" name="consumerNum" value="${currentBill.consumerNum}" />
		                            <input type="hidden" name="totalAmount" value="${currentBill.totalAmount}" />
		                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		                            <button class="pay-btn" type="submit">Pay</button>
		                        </form>
		                    </td>
						</tr>
						</c:otherwise>
					</c:choose>
				</table>
			</div>
		</div>

	</body>
</html>
