<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 12/03/2026
  Time: 6:56 PM
  To chang this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <meta charset="UTF-8">
    <title>Quản trị</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/sidebar/Admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">


</head>
<body>
<div class="wrap col">


    <div id="header" class="row">
        <div class="logo">
            <img class="logo" src="${pageContext.request.contextPath}/static/image/logo_web.jpg" alt="Logo">
        </div>
        <div class="search-bar">
            <input type="text" placeholder="Tìm kiếm...">
            <button type="submit"><i class="fa-solid fa-search"></i></button>
        </div>
        <div class="menu row mid_align">
            <i class="fa-regular fa-bell"></i>
            <i class="fa-regular fa-envelope"></i>
            <div class="avatar" id="avatar">
                <img src="${pageContext.request.contextPath}/static/image/avatar.jpg" alt="Avatar" height="1024" width="1024"/>
                <div class="user-popup">
                    <a
                            class="nav_item"
                            href="#" onclick="loadPage('my_profile')"
                            id="my-page-link"
                    >Trang của tôi</a
                    >
<%--                    <a href="${pageContext.request.contextPath}/auth/auth.jsp">Đăng xuất</a>--%>
                </div>
            </div>
        </div>
    </div>



    <div id="body" class="row">
        <nav id="sidebar" class="col">
            <ul>
                <!-- Bảng điều khiển -->
                <li class="menu_item " onclick="loadPage('dashboard')" >
                    <div class="wrap_menu_item">
                        <i class="fa-solid fa-house"></i>
                        <span>Tổng quan</span>
                    </div>
                </li>



                <!--Sản phẩm-->
                <li class="menu_item" >
                    <div class="wrap_menu_item">
                        <i class="fa-solid fa-box"></i>
                        <span>Sản phẩm</span>
                        <i class="fa-solid fa-chevron-down toggle-arrow"></i>
                    </div>

                    <ul class="submenu">
                        <li class="submenu_item" onclick="loadPage('list_products')" > Danh sách sản phẩm </li>
                        <!--                        <li onclick="loadPage('product_details')" ><a href="#" >Chi tiết sản phẩm</a></li>-->
                        <li class="submenu_item" onclick="loadPage('add_product')" > Thêm sản phẩm </li>
                    </ul>
                </li>

                <!-- Đơn hàng -->
                <li class="menu_item"  onclick="loadPage('orders')" >
                    <div class="wrap_menu_item">
                        <i class="fa-solid fa-cart-shopping"></i>
                        <span>Đơn hàng</span>
                    </div>

                </li>

                <!-- Khách hàng -->
                <li class="menu_item" onclick="loadPage('customers')" >
                    <div class="wrap_menu_item">
                        <i class="fa-solid fa-users"></i>
                        <span>Khách hàng</span>
                    </div>

                </li>



                <!-- Hồ sơ -->
                <li class="menu_item" onclick="loadPage('my_profile')" >
                    <div class="wrap_menu_item">
                        <i class="fa-solid fa-user"></i>
                        <span>Hồ sơ</span>
                    </div>
                </li>




                <!-- Cài đặt -->
                <li class="menu_item" onclick="loadPage('setting')" >
                    <div class="wrap_menu_item">
                        <i class="fa-solid fa-gear"></i>
                        <span>  Cài đặt </span>
                    </div>
                </li>



            </ul>
        </nav>

        <div id="content">
            <iframe src="${pageContext.request.contextPath}/admin/dashboard.jsp" frameborder="0"></iframe>
        </div>
    </div>
</div>
</body>

<script src="${pageContext.request.contextPath}/static/style-component/style-admin/sidebar/Admin.js"></script>
