<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>View Bills</title>
		<style>
			#active-bill {
				background: var(--c-menu-link-selected);
				transition: .5s;
			}
		</style>
		</head>
	<body>
		<%@ include file="admin-navbar.jsp"%>

		<div class="container">
	        <div class="table-container" style="margin: 3em 10em;">
				<h1>View Bills</h1>

				<div class="search-box">
					<input type="text" id="searchText" placeholder="Search by Bill No / Meter No" style="width:250px" oninput="searchData(1, 2, 'billTable')"/>
				</div>

				<table border="1" id="billTable">
					<tr>
						<th>Bill No</th>
						<th>Bill Date</th>
						<th>Consumer Number</th>
						<th>Meter Number</th>
						<th>Bill For Month</th>
						<th>Current Reading</th>
						<th>Previous Reading</th>
						<th>Total Units</th>
						<th>Previous Balance</th>
						<th>Current Amount</th>
						<th>Total Amount</th>
						<th>Due Date</th>
						<th>Payment Date</th>
						<th>Status</th>
					</tr>

					<c:if test="${empty bills}">
						<tr>
							<td colspan="14"><span class="no-data-row">No Data Available</span></td>
						</tr>
					</c:if>

					<c:set var="count" value="0" />
					<c:set var="showRemaining" value="false" />

					<c:forEach var="bill" items="${bills}" varStatus="loop">
						<c:choose>
							<c:when test="${count < 5}">
								<tr onClick="showActionButtons('hidden-row${count}')">
									<td><c:out value="${bill.billNo}" /></td>
									<td><fmt:formatDate value="${bill.billDate}" pattern="dd/MM/yy" /></td>
									<td><c:out value="${bill.consumerNum}" /></td>
									<td><c:out value="${bill.meterNum}" /></td>
									<td><c:out value="${bill.month}" /></td>
									<td><c:out value="${bill.currentReading}" /></td>
									<td><c:out value="${bill.previousReading}" /></td>
									<td><c:out value="${bill.totalUnits}" /></td>
									<td><c:out value="${bill.previousBalance}" /></td>
									<td><c:out value="${bill.currentAmount}" /></td>
									<td><c:out value="${bill.totalAmount}" /></td>
									<td><fmt:formatDate value="${bill.dueDate}" pattern="dd/MM/yy" /></td>
									<td><fmt:formatDate value="${bill.paymentDate}" pattern="dd/MM/yy" /></td>
									<td><c:out value="${bill.status}" /></td>
								</tr>
								<tr hidden id="hidden-row${count}">
	                                <td colspan="14" style="text-align:right">
	                                    <a class="action-btn" href="bill/edit/${bill.billNo}">Edit</a>
	                                    <a class="action-btn" href="#" onclick="removeBill('${bill.billNo}')">Remove</a>
	                                </td>
	                            </tr>
								 <c:set var="count" value="${count + 1}" />
							</c:when>
							<c:otherwise>
								<c:set var="showRemaining" value="true" />
								<tr class="hidden-row" style="display: none;" onClick="showActionButtons('hidden-row${count}')">
									<td><c:out value="${bill.billNo}" /></td>
									<td><fmt:formatDate value="${bill.billDate}" pattern="dd/MM/yy" /></td>
									<td><c:out value="${bill.consumerNum}" /></td>
									<td><c:out value="${bill.meterNum}" /></td>
									<td><c:out value="${bill.month}" /></td>
									<td><c:out value="${bill.currentReading}" /></td>
									<td><c:out value="${bill.previousReading}" /></td>
									<td><c:out value="${bill.totalUnits}" /></td>
									<td><c:out value="${bill.previousBalance}" /></td>
									<td><c:out value="${bill.currentAmount}" /></td>
									<td><c:out value="${bill.totalAmount}" /></td>
									<td width="80"><fmt:formatDate value="${bill.dueDate}" pattern="dd/MM/yy" /></td>
									<td><fmt:formatDate value="${bill.paymentDate}" pattern="dd/MM/yy" /></td>
									<td><c:out value="${bill.status}" /></td>
								</tr>
								<tr hidden id="hidden-row${count}">
	                                <td colspan="14" style="text-align:right">
	                                    <a class="action-btn" href="bill/edit/${bill.billNo}">Edit</a>
	                                    <a class="action-btn" href="#" onclick="removeBill('${bill.billNo}')">Remove</a>
	                                </td>
	                            </tr>
	                            <c:set var="count" value="${count + 1}" />
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:if test="${showRemaining}">
						<tr id="view-more-row">
							<td colspan="14"><label id="view-more-label" onclick="toggleRows()">View More</label></td>
						</tr>
					</c:if>

				</table>

				<c:if test="${!empty bills}">
					<div class="pagination">
                        <a href="?page=1"> &laquo; First</a>
                        <c:forEach begin="0" end="${totalPages - 1}" var="i">
                            <a href="?page=${i+1}" class="${i == currentPage-1 ? 'active' : ''}">${i + 1}</a>
                        </c:forEach>
                        <a href="?page=${totalPages}">Last &raquo;</a>
                    </div>
				</c:if>
                <input type="hidden" id="csrf" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			</div>
		</div>

		<script>
			function removeBill(billNo){
				let confirmValue =  confirm("Are you sure ? All the details of billNo : "+billNo+" will be permanently deleted");
				console.log("confirmValue : "+confirmValue);
				if(confirmValue){
					deleteRecord("bill", billNo);
				}
			}
		</script>
	</body>
</html>
