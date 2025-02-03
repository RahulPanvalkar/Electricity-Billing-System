<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.ebs.entities.Admin" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>View Connections</title>
		<style>
			#active-connection {
				background: var(--c-menu-link-selected);
				transition: .5s;
			}
		</style>
	</head>
	<body>
		<%@ include file="admin-navbar.jsp"%>

		<div class="container">
          	<div class="table-container">

				<h1>View Connections</h1>

				<div class="search-box">
					<input type="text" id="searchText" placeholder="Search by ConsumerNum / Name" style="width:250px"  oninput="searchData(0, 2, 'consumerTable')"/>
				</div>

				<table border="1" id="consumerTable">
					<tr>
						<th>Connection ID</th>
						<th>Consumer Number</th>
						<th>Meter Number</th>
						<th>Full Name</th>
						<th>Mobile Number</th>
						<th>Address</th>
						<th>Start Date</th>
						<th>Type</th>
					</tr>

					<c:if test="${empty connections}">
						<tr>
							<td colspan="10"><span class="no-data-row">No Data Available</span></td>
						</tr>
					</c:if>

					<c:set var="count" value="0" />
					<c:set var="showRemaining" value="false" />

					<c:forEach var="con" items="${connections}">
						<c:choose>
							<c:when test="${count < 5}">
                                <tr onClick="showActionButtons('hidden-row${count}')">
                                    <td><c:out value="${con.connId}" /></td>
                                    <td><c:out value="${con.consumerNum}" /></td>
                                    <td><c:out value="${con.meterNum}" /></td>
                                    <td><c:out value="${con.fullName}" /></td>
                                    <td><c:out value="${con.mobNumber}" /></td>
                                    <td><c:out value="${con.address}" /></td>
                                    <td><fmt:formatDate value="${con.startDate}" pattern="dd/MM/yy" /></td>
                                    <td><c:out value="${con.type}" /></td>
                                </tr>
                                <tr hidden id="hidden-row${count}">
                                    <td colspan="8" style="text-align:right">
                                        <a class="action-btn" href="connection/edit/${con.connId}">Edit</a>
                                        <a class="action-btn" href="#" onclick="removeConnection('${con.fullName}','${con.connId}')">Remove</a>
                                    </td>
                                </tr>
                                <c:set var="count" value="${count + 1}" />
                            </c:when>

							<c:otherwise>
								<c:set var="showRemaining" value="true" />

								<tr onClick="showActionButtons('hidden-row${count}')" class="hidden-row" style="display: none;">
									<td><c:out value="${con.connId}" /></td>
									<td><c:out value="${con.consumerNum}" /></td>
									<td><c:out value="${con.meterNum}" /></td>
									<td><c:out value="${con.fullName}" /></td>
									<td><c:out value="${con.mobNumber}" /></td>
									<td><c:out value="${con.address}" /></td>
									<td><fmt:formatDate value="${con.startDate}" pattern="dd/MM/yy" /></td>
									<td><c:out value="${con.type}" /></td>
								</tr>
								<tr hidden id="hidden-row${count}">
                                    <td colspan="8" style="text-align:right">
                                        <a class="action-btn" href="connection/edit/${con.connId}">Edit</a>
                                        <a class="action-btn" href="#" onclick="removeConnection('${con.fullName}','${con.connId}')">Remove</a>
                                    </td>
                                </tr>
                                <c:set var="count" value="${count + 1}" />
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:if test="${showRemaining}">
						<tr  id="view-more-row">
							<td colspan="8"><label id="view-more-label" onclick="toggleRows()">View More</label></td>
						</tr>
					</c:if>
				</table>

				<c:if test="${!empty connections}">
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

			function removeConnection(fullName, connId){
				console.log("inside removeConnection..",connId);
				let confirmValue =  confirm("Are you sure ? All the connection details of  "+fullName+" will be permanently deleted");
				console.log("confirmValue : "+confirmValue);
				if(confirmValue){
					deleteRecord("connection", connId);
				}
			}

		</script>
	</body>
</html>
