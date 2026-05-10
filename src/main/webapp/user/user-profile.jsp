<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_profile/UserProfileDetail.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-page/user/UserProfile.css">
</head>

<body>

<div class="header">
    <jsp:include page="/home/header.jsp"/>
</div>

<div id="body" class="mid_align">
    <div class="container mid_align wrap_body">

        <div class="side_bar">
            <jsp:include page="user-sidebar.jsp"/>
        </div>

        <div class="content">

            <div id="head_title" class="col mid_align">
                <div class="header_container mid_align col">
                    <span class="title f22">Hồ sơ</span>
                    <span class="description">
                        Quản lý thông tin hồ sơ để giữ an toàn cho tài khoản của bạn
                    </span>
                </div>
            </div>

            <c:if test="${not empty user}">
                <div id="content_body">
                    <div class="body_container row">
                        <div class="info_left">
                            <div class="form_infor row mid_align">

                                <div class="avatar">
                                    <c:choose>
                                        <c:when test="${not empty user.avatarUrl}">
                                            <img id="avatar" src="${user.avatarUrl}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img id="avatar"
                                                 src="${pageContext.request.contextPath}/static/image/medium%20(1).png"/>
                                        </c:otherwise>
                                    </c:choose>

                                    <i id="btn_upload" class="fa-solid fa-camera"></i>
                                    <input type="file" id="upload_avatar" accept="image/*" style="display:none;"/>
                                </div>

                                <div class="base_infor col">

                                    <span class="name_title">Họ và Tên :</span>
                                    <input id="name" name="name" type="text"
                                           value="${user.fullName}" placeholder="Full Name">

                                    <span class="name_title">Tên hiển thị :</span>
                                    <input id="displayName" name="displayName" type="text"
                                           value="${user.displayName}" placeholder="Display name">

                                    <div class="gender row">
                                        <div class="gender_title">
                                            <span>Giới tính :</span>
                                        </div>

                                        <div class="gender_radio mid_align">
                                            <label>
                                                <input type="radio" name="gender" value="Male"
                                                       <c:if test="${user.gender == 'Male'}">checked</c:if>>
                                                Nam
                                            </label>

                                            <label>
                                                <input type="radio" name="gender" value="Female"
                                                       <c:if test="${user.gender == 'Female'}">checked</c:if>>
                                                Nữ
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- SAVE -->
                            <div class="mid_align bottom">
                                <button type="button" id="save" class="save_btn">
                                    Lưu
                                </button>
                            </div>

                        </div>

                        <div class="info_right">

                            <div class="contact col">
                                <span>Thông tin liên hệ</span>

                                <div class="contact_item row mid_align">
                                    <i class="fa-solid fa-phone"></i>

                                    <input id="phone" class="item_text" placeholder="Vui lòng cập nhật số điện thoại." value="${user.phoneNumber}"/>

                                </div>

                                <div class="contact_item row mid_align">
                                    <i class="fa-regular fa-envelope"></i>

                                    <c:if test="${not empty user.email}">
                                        <span id="email" class="item_text"> ${user.email}</span>
                                    </c:if>


                                </div>
                            </div>
                            <div class="contact social col ">
                                <span>Liên kết</span>

                                <div class="contact_item row mid_align">
                                    <img src="${pageContext.request.contextPath}/static/image/facebook.svg" alt="">
                                    <span class="item_text mid_align">Facebook </span>
                                    <button type="button" class="update_btn">Liên kết</button>
                                </div>

                                <div class="contact_item row mid_align">
                                    <img src="${pageContext.request.contextPath}/static/image/google.svg" alt="">
                                    <span class="item_text ">Google</span>
                                    <button type="button" class="update_btn">Xóa</button>
                                </div>


                            </div>

                        </div>

                    </div>
                </div>
            </c:if>

            <c:if test="${empty user}">
                <p>Không tìm thấy người dùng</p>
            </c:if>

        </div>
    </div>
</div>
<div id="footer"></div>

<script src="${pageContext.request.contextPath}/static/style-page/user/UserProfile.js"></script>
<script src="${pageContext.request.contextPath}/static/style-component/style-user_profile/UserProfileDetail.js"></script>

</body>
</html>