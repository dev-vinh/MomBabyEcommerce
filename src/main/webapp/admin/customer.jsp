<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 1/7/2026
  Time: 11:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quản lí Khách Hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/customer.css">
</head>
<body>

<div class="header">
    <jsp:include page="header.jsp"/>
</div>

<div class="container">

    <div class="side_bar">
        <jsp:include page="sidebar.jsp"/>
    </div>


    <div class="content">
        <h1 class="header-title">Khách Hàng</h1>

        <div class="export-container">
            <button class="export-btn">Xuất <i class="fas fa-chevron-down"></i></button>
            <div class="export-menu">
                <div class="options">
                    <p>OPTIONS</p>
                    <button class="export-option"><i class="fas fa-copy"></i> Copy</button>
                    <button class="export-option"><i class="fas fa-print"></i> Print</button>
                </div>
                <div class="divider"></div>
                <div class="download-options">
                    <p>DOWNLOAD OPTIONS</p>
                    <button class="export-option"><i class="fas fa-file-excel"></i> Excel</button>
                    <button class="export-option"><i class="fas fa-file-csv"></i> .CSV</button>
                    <button class="export-option"><i class="fas fa-file-pdf"></i> PDF</button>
                </div>
            </div>
        </div>

        <div class="table-container">
            <div class="search-bar">
                <form method="get" action="${pageContext.request.contextPath}/admin/customers" style="display: flex; align-items: center; gap: 10px;">
                    <button type="submit" style="background: none; border: none; padding: 0; cursor: pointer;">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                             stroke="currentColor" class="size-6" style="width: 24px; height: 24px;">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                  d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"/>
                        </svg>
                    </button>
                    <input type="text" name="name" placeholder="Tìm theo tên" value="${param.name}" />
                </form>
            </div>
            <table>
                <thead>
                <tr>
                    <th><input type="checkbox"></th>
                    <th>Khách Hàng</th>
                    <th>Số Điện Thoại</th>
                    <th>Email</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${not empty customers}">
                    <c:forEach items="${customers}" var="c">
                        <tr class="order-row">
                            <td><input type="checkbox"></td>
                            <td>
                                <a href="customer?id=${c.id}" class="name">
                                    <c:if test="${not empty c.avatar_url}">
                                        <img src="${c.avatar_url}" alt="Avatar"
                                             style="width:50px; height:50px; border-radius:50%; margin-right:10px;">
                                    </c:if>

                                    <c:if test="${empty c.avatar_url}">
                                        <img src="${pageContext.request.contextPath}/static/image/default-avatar.png" alt="Avatar"
                                             style="width:50px; height:50px; border-radius:50%; margin-right:10px;">
                                    </c:if>

                                        ${c.fullName}
                                </a>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${not empty c.phoneNumber }">
                                        ${c.phoneNumber}
                                    </c:when>
                                    <c:otherwise>
                                        Chưa cập nhật
                                    </c:otherwise>
                                </c:choose>


                            </td>
                            <td>${c.email} </td>
                        </tr>
                    </c:forEach>

                </c:if>
                </tbody>
            </table>
        </div>
    </div>

</div>


<script src="${pageContext.request.contextPath}/static/style-component/style-admin/customer.js"></script>
</body>
</html>
