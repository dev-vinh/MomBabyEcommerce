<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 12/03/2026
  Time: 7:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/sidebar/sidebar.css">
</head>
<body>
<div id="body" class="row">
    <nav id="sidebar" class="col">
        <ul>
            <!-- Bảng điều khiển -->
            <li class="menu_item ">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-house"></i>
                    <a href="dashboard">Tổng quan</a>
                </div>
            </li>


            <!--Sản phẩm-->
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-box"></i>
                    <span>Sản phẩm</span>
                    <i class="fa-solid fa-chevron-down toggle-arrow"></i>
                </div>

                <ul class="submenu">
                    <li class="submenu_item">
                        <a href="list-product">Danh sách sản phẩm</a>
                    </li>
                    <li class="submenu_item">
                        <a href="add-product">Thêm sản phẩm</a>
                    </li>
                </ul>
            </li>

            <!-- Đơn hàng -->
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-cart-shopping"></i>
                    <a href="orders">Đơn hàng</a>
                </div>

            </li>

            <!-- Khách hàng -->
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-users"></i>
                    <a href="customers">Khách hàng</a>
                </div>

            </li>


            <!-- Hồ sơ -->
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-user"></i>
                    <a href="profile">Hồ sơ</a>
                </div>
            </li>


            <!-- Danh mục -->
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-warehouse"></i>
                    <a href="category">Danh mục</a>
                </div>
            </li>

            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-warehouse"></i>
                    <a href="brand">Nhà sản xuất</a>
                </div>
            </li>


        </ul>
    </nav>

</div>
<script src="${pageContext.request.contextPath}/static/style-component/style-admin/sidebar/Admin.js"></script>

</body>
</html>
