<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 3/15/2026
  Time: 5:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-user_profile/UserSideBar.css">
</head>
<body>
<div id="side_bar" class="container  col">
    <div class=" infor row mid_align  ">
        <div class="profile_avatar">
            <img src="${pageContext.request.contextPath}/static/image/medium%20(1).png" height="500" width="500"/>
        </div>
        <div class="name">
            <span>User</span>
        </div>

    </div>

    <div class="rec_horizontal"></div>

    <div class=" wrap_item  col">

        <div class="item  ">

            <i class="fa-solid fa-user-large"></i>
            <span>Tài Khoản</span>
            <i class="fa-solid fa-caret-down icon_down"></i>

        </div>


        <div class="menu_sub_item  ">
            <ul>
                <li class="sub_items  nav ">
                    <i class="fa-solid fa-address-card"></i>
                    <a href="user-profile" class="item_link" >
                        Hồ sơ</a>
                </li>

                <li class="sub_items nav">
                    <i class="fa-regular fa-credit-card"></i>
                    <a href="user-card" class="item_link" >
                        Thanh toán</a>
                </li>

                <li class="sub_items nav">
                    <i class="fa-solid fa-location-dot"></i>
                    <a href="user-address" class="item_link">
                        Địa chỉ</a>
                </li>

                <li class="sub_items nav">
                    <i class="fa-solid fa-key"></i>
                    <a href="user-password" class="item_link" >
                        Bảo mật</a>
                </li>

                <li class="sub_items nav">
                    <i class="fa-regular fa-bell"></i>
                    <a href="user-noti.jsp" class="item_link" >
                        Thông báo</a>
                </li>
            </ul>
        </div>
    </div>


    <div class="item nav ">
        <i class="fa-solid fa-cart-shopping"></i>
        <span>  <a href="user-order" class="item_link"   >Đơn hàng</a></span>

    </div>


    <div class="item  ">
        <i class="fa-brands fa-rocketchat"></i>
        <span>  <a href="#" class="item_link"   >Tin nhắn</a></span>

    </div>


</div>

<script src="${pageContext.request.contextPath}/static/style-component/style-user_profile/UserSidebar.js"></script>

</body>
</html>
