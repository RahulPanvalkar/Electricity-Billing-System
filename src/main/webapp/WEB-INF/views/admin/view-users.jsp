<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.ebs.entities.Admin"%>

<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>View Users</title>
        <style>
            #active-users {
	            background: var(--c-menu-link-selected);
	            transition: .5s;
            }
        </style>
    </head>
    <body>
        <%@ include file="admin-navbar.jsp"%>
        <div class="container">
            <div class="table-container">
                <h1>View Users</h1>
                <div class="search-box">
                    <input type="text" id="searchText" placeholder="Search by ID / Name" style="width:250px" oninput="searchData(0, 1, 'userTable')" />
                </div>
                <table border="1" id="userTable">
                    <tr>
                        <th>User ID</th>
                        <th>Full Name</th>
                        <th>Email Id</th>
                        <th>Mobile No</th>
                        <th>User Type</th>
                        <th>User Code</th>
                        <th>Add Date</th>
                    </tr>
                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="7"><span class="no-data-row">No Data Available</span></td>
                        </tr>
                    </c:if>

                    <c:set var="count" value="0" />
                    <c:set var="showRemaining" value="true" />
                    <c:forEach var="user" items="${users}">
                        <tr onClick="showActionButtons('hidden-row${count}')">
                            <td>
                                <c:out value="${user.userId}" />
                            </td>
                            <td>
                                <c:out value="${user.name}" />
                            </td>
                            <td>
                                <c:out value="${user.emailId}" />
                            </td>
                            <td>
                                <c:out value="${user.mobNumber}" />
                            </td>
                            <td>
                                <c:out value="${user.userType.displayName}" />
                            </td>
                            <td>
                                <c:out value="${user.userCode}" />
                            </td>
                            <td>
                                <fmt:formatDate value="${user.addDate}" pattern="dd-MM-yy" />
                            </td>
                        </tr>
                        <tr hidden id="hidden-row${count}">
                            <td colspan="7" style="text-align:right">
                                <a class="action-btn" href="edit-user/${user.userId}">Edit</a>
                                <a class="action-btn" href="#" onclick="removeUser('${user.userId}','${user.name}')">Remove</a>
                            </td>
                        </tr>
                        <c:set var="count" value="${count + 1}" />
                    </c:forEach>

                </table>

				<c:if test="${!empty users}">
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
            function removeUser(userId, fullName) {
            	console.log("inside removeConsumer >> userId::",userId," && fullName:: ",fullName);
            	let confirmValue = confirm("Are you sure ? All the details of " + fullName + " will be permanently deleted");
            	console.log("confirmValue : " + confirmValue);
            	if (confirmValue) {
            		deleteRecord("user", userId);
            	}
            }
        </script>
    </body>
</html>