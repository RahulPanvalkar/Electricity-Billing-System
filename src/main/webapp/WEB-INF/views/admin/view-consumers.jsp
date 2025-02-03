<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="com.ebs.entities.Admin"%>
<%@page import="com.ebs.entities.Consumer"%>

<html>
   <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>View Consumers</title>
      <style>
         #active-customer {
	         background: var(--c-menu-link-selected);
	         transition: .5s;
         }
      </style>
   </head>

   <body>
      <%@ include file="admin-navbar.jsp"%>
      <div class="container">
         <div class="table-container">
            <h1>View Consumers</h1>
            <div class="search-box">
               <input type="text" id="searchText" placeholder="Search by ConsumerNum / Name" style="width:250px" oninput="searchData(0, 1, 'consumerTable')" />
            </div>
            <table border="1" id="consumerTable">
               <tr>
                  <th>Consumer Number</th>
                  <th>Full Name</th>
                  <th>Email Id</th>
                  <th>Mobile No</th>
                  <th>Address</th>
                  <th>Connection</th>
                  <th>Add Date</th>
               </tr>

               <c:if test="${empty consumers}">
                  <tr>
                     <td colspan="7"><span class="no-data-row">No Data Available</span></td>
                  </tr>
               </c:if>

               <c:set var="count" value="0" />
               <c:set var="showRemaining" value="false" />
               <c:forEach var="consumer" items="${consumers}">
                  <c:choose>
                     <c:when test="${count < 10}">
                        <!-- Display the first 10 consumers -->
                        <tr onClick="showActionButtons('hidden-row${count}')">
                           <td>
                              <c:out value="${consumer.consumerNum}" />
                           </td>
                           <td>
                              <c:out value="${consumer.fullName}" />
                           </td>
                           <td>
                              <c:out value="${consumer.emailId}" />
                           </td>
                           <td>
                              <c:out value="${consumer.mobNumber}" />
                           </td>
                           <td>
                              <c:out value="${consumer.address}" />
                           </td>
                           <td>
                              <c:out value="${consumer.connId}" />
                           </td>
                           <td>
                              <fmt:formatDate value="${consumer.addDate}" pattern="dd-MM-yy" />
                           </td>
                        </tr>
                        <tr hidden id="hidden-row${count}">
                           <td colspan="7" style="text-align:right">
                              <a class="action-btn" href="edit-consumer/${consumer.consumerNum}">Edit</a>
                              <a class="action-btn" href="#" onclick="removeConsumer('${consumer.consumerNum}','${consumer.fullName}')">Remove</a>
                           </td>
                        </tr>
                        <c:set var="count" value="${count + 1}" />
                     </c:when>
                     <c:otherwise>
                        <!-- If more than 5 consumers, set flag to show remaining -->
                        <c:set var="showRemaining" value="true" />
                        <tr class="hidden-row" style="display: none;" onClick="showActionButtons('hidden-row${count}')">
                           <td>
                              <c:out value="${consumer.consumerNum}" />
                           </td>
                           <td>
                              <c:out value="${consumer.fullName}" />
                           </td>
                           <td>
                              <c:out value="${consumer.emailId}" />
                           </td>
                           <td>
                              <c:out value="${consumer.mobNumber}" />
                           </td>
                           <td>
                              <c:out value="${consumer.address}" />
                           </td>
                           <td>
                              <c:out value="${consumer.connId}" />
                           </td>
                           <td>
                              <fmt:formatDate value="${consumer.addDate}" pattern="dd-MM-yy" />
                           </td>
                        </tr>
                        <tr hidden id="hidden-row${count}">
                           <td colspan="7" style="text-align:right">
                              <a class="action-btn" href="edit-consumer/${consumer.consumerNum}">Edit</a>
                              <a class="action-btn" href="#" onclick="removeConsumer('${consumer.consumerNum}','${consumer.fullName}')">Remove</a>
                           </td>
                        </tr>
                        <c:set var="count" value="${count + 1}" />
                     </c:otherwise>
                  </c:choose>
               </c:forEach>

               <c:if test="${showRemaining}">
                  <tr id="view-more-row">
                     <td colspan="7"><label id="view-more-label" onclick="toggleRows()">View More</label></td>
                  </tr>
               </c:if>
            </table>

			<c:if test="${!empty consumers}">
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
         function removeConsumer(consumerNum, fullName) {
         	console.log("inside removeConsumer >> consumerNum::",consumerNum," && fullName:: ",fullName);
         	let confirmValue = confirm("Are you sure ? All the details of " + fullName + " will be permanently deleted");
         	console.log("confirmValue : " + confirmValue);
         	if (confirmValue) {
         		deleteRecord("consumer", consumerNum);
         	}
         }
      </script>
   </body>
</html>