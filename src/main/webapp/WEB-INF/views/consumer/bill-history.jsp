<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Bill History</title>
		<style>
			#active-bill {
				background: var(--c-menu-link-selected);
				transition: .5s;
			}
		</style>
	</head>
	<body>
		<%@ include file="consumer-navbar.jsp"%>

		<div class="container">
	        <div class="table-container" style="margin: 3em 10em;">
				<h1>Bill History</h1>

				<div class="search-box">
					<input type="text" id="searchText" placeholder="BillNum (Last 7 digits) / Status" style="width:250px" oninput="searchData(0, 13, 'billHistTable')"/>
				</div>

				<table border="1" id="billHistTable">
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
							<td colspan="13"><span class="no-data-row">No Data Available</span></td>
						</tr>
					</c:if>

					<c:set var="count" value="0" />
					<c:set var="showRemaining" value="false" />

					<c:forEach var="bill" items="${bills}" varStatus="loop">
						<c:choose>
							<c:when test="${count < 5}">
								<tr>
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
								 <c:set var="count" value="${count + 1}" />
							</c:when>
							<c:otherwise>
								<c:set var="showRemaining" value="true" />
								<tr class="hidden-row" style="display: none;">
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
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:if test="${showRemaining}">
						<tr id="view-more-row">
							<td colspan="13"><label id="view-more-label" onclick="toggleRows()">View More</label></td>
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
			</div>
		</div>
	</body>
</html>
