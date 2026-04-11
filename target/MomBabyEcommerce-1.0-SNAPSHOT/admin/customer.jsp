account_settings.jsp
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="hcmuaf.fit.mombabyecommerce.model.User" %><%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 1/7/2026
  Time: 11:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
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
        <div class="action-buttons">

            <button class="export">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round"
                          d="M12 16.5V9.75m0 0 3 3m-3-3-3 3M6.75 19.5a4.5 4.5 0 0 1-1.41-8.775 5.25 5.25 0 0 1 10.233-2.33 3 3 0 0 1 3.758 3.848A3.752 3.752 0 0 1 18 19.5H6.75Z"/>
                </svg>
                Xuất
            </button>
            <div class="more-options">
                Tùy Chọn
            </div>

        </div>


        <div class="table-container">
            <div class="search-bar">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round"
                          d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"/>
                </svg>
                <input type="text" placeholder="Tìm kiếm">
            </div>
            <table>
                <thead>
                <tr>
                    <th><input type="checkbox"></th>
                    <th>Khách Hàng</th>
                    <th>Số Điện Thoại</th>
                    <th>Email</th>
                    <th>Vị trí</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<User> users = (List<User>) request.getAttribute("users");
                    Map<Integer, String> userAddresses = (Map<Integer, String>) request.getAttribute("userAddresses");
                    for (User user : users) {
                %>
                <tr class="order-row">
                    <td><input type="checkbox"></td>
                    <td>
                        <%--                        <%= user.getFullName() %>--%>
                        <img src="<%= user.getAvatarUrl() %>" alt="Avatar" style="width:50px; height:50px; border-radius:50%; margin-right:10px;">
                        <%= user.getFullName() %>
                    </td>
                    <td><%= user.getPhoneNumber() %></td>
                    <td><%= user.getEmail() %></td>
                    <td><%= userAddresses.getOrDefault(user.getId(), "N/A") %></td>
                </tr>
                <%
                    }
                %>
                </tbody>



            </table>
        </div>
        <div class="footer-container">
            <nav class="mt-2 mt-md-0">
                <ul class="pagination mb-0">
                    <li class="page-item"><a class="page-link" href="#!" data-action="prev">Quay lại</a></li>
                    <li class="page-item"><a class="page-link active" href="#!">1</a></li>
                    <li class="page-item"><a class="page-link" href="#!">2</a></li>
                    <li class="page-item"><a class="page-link" href="#!">3</a></li>
                    <li class="page-item"><a class="page-link" href="#!" data-action="next">Tiếp theo</a></li>
                </ul>
            </nav>
        </div>
    </div>

</div>


<script src="${pageContext.request.contextPath}/static/style-component/style-admin/customer.js"></script>
</body>
</html>
