<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 3/15/2026
  Time: 5:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_profile/UserProfileDetail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-page/user/UserProfile.css">
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

            <div id="head_title" class=" col mid_align">
                <div class="header_container mid_align  col">
                    <span class="title f22 ">Hồ sơ</span>
                    <span class="description">Quản lý thông tin hồ sơ để giữ an toàn cho tài khoản của bạn</span>
                </div>
            </div>


            <c:if test="${not empty user}">
                <div id="content_body">
                    <div class="body_container row">

                        <div class="info_left">

                            <div class="form_infor row mid_align">
                                <div class="avatar">
                                    <c:if   test="${not empty user.avatarUrl}">
                                        <img id="avatar" src="${user.avatarUrl}"/>
                                    </c:if>

                                    <c:if   test="${empty user.avatarUrl}">
                                        <img id="avatar" src="${pageContext.request.contextPath}/static/image/medium%20(1).png" height="500" width="500"/>
                                    </c:if>



                                    <i id="btn_upload" class="fa-solid fa-camera"></i>
                                    <input type="file" name="" id="upload_avatar" accept="image/*" style="display: none;"/>
                                </div>

                                <div class="base_infor col">
                                    <span class=name_title>Họ và Tên :</span>

                                    <c:if test="${not empty user.fullName}">
                                        <input id="name" type="text" class="name" placeholder="Full Name" value="${user.fullName}">
                                    </c:if>

                                    <span class=name_title>Tên hiển thị :</span>
                                    <c:if test="${not empty user.displayName}">
                                        <input id="displayName" type="text" class="name" placeholder="Display name" value="${user.displayName}">
                                    </c:if>

                                    <div class="gender row ">
                                        <div class="gender_title">
                                            <span>Giới tính :</span>
                                        </div>

                                        <div class="gender_radio mid_align">
                                            <label>
                                                <input type="radio" name="gender" value="male"
                                                       <c:if test="${user.gender == 'Male'}">checked</c:if>>
                                                Nam
                                            </label>
                                            <label>
                                                <input type="radio" name="gender" value="female"
                                                       <c:if test="${user.gender == 'Female'}">checked</c:if>>
                                                Nữ
                                            </label>
                                            <label>
                                                <input type="radio" name="gender" value="other"
                                                       <c:if test="${user.gender == 'Other'}">checked</c:if>>
                                                Khác
                                            </label>
                                        </div>

                                    </div>
                                </div>


                            </div>

                            <div class="personal_infor">
                                <div class="birth row">
                                    <div class="birth_title">
                                        <span>Ngày sinh : </span>
                                    </div>



                                    <c:set var="birthDay" value="${user.dOB.dayOfMonth}" />
                                    <c:set var="birthMonth" value="${user.dOB.monthValue}" />
                                    <c:set var="birthYear" value="${user.dOB.year}" />



                                    <div class="birth_form mid_align">
                                        <div class="col">
                                            <select id="day">
                                                <option value="" disabled selected>Ngày</option>
                                                <c:forEach var="i" begin="1" end="31">
                                                    <option value="${i}" <c:if test="${i == birthDay}">selected</c:if>>${i}</option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <div class="col">
                                            <select id="month">
                                                <option value="" disabled selected>Tháng</option>
                                                <c:forEach var="i" begin="1" end="12">
                                                    <option value="${i}" <c:if test="${i == birthMonth}">selected</c:if>>${i}</option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <div class="col">
                                            <select id="year">
                                                <option value="" disabled selected>Năm</option>
                                                <c:forEach var="i" begin="1900" end="2025">
                                                    <option value="${i}" <c:if test="${i == birthYear}">selected</c:if>>${i}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                </div>

                            </div>

                            <div class="mid_align bottom">
                                <button id="save" class="save_btn">
                                    Lưu
                                </button>
                            </div>


                        </div>


                        <div class="info_right">

                            <!-- Contact infor -->
                            <div class="contact col ">
                                <span>Thông tin liên hệ</span>

                                <div class="contact_item row mid_align">
                                    <i class="fa-solid fa-phone"></i>

                                    <span id="phone" class="item_text"  >
                             <c:if test="${not empty user.phoneNumber}">
                                 ${user.phoneNumber}
                             </c:if>
                        </span>

                                    <button type="button" class="update_btn" data-field="phone" >Cập nhật</button>
                                </div>

                                <div class="contact_item row mid_align">
                                    <i class="fa-regular fa-envelope"></i>
                                    <c:if test="${not empty user.email}">
                                        <span id="email" class="item_text">${user.email} </span>
                                    </c:if>

                                    <button type="button" class="update_btn"  data-field="email" >Cập nhật</button>
                                </div>


                            </div>


                            <!-- Social Link-->
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

                                <div class="contact_item row mid_align">
                                    <img src="${pageContext.request.contextPath}/static/image/Zalo.png" alt="">
                                    <span class="item_text ">Zalo</span>
                                    <button type="button" class="update_btn">Xóa</button>
                                </div>


                            </div>


                        </div>

                    </div>
                </div>
            </c:if>


            <c:if test="${empty user}">
                <p>Không tìm thấy người dung</p>
            </c:if>


        </div>


    </div>
</div>


<div id="footer"></div>


<script src="${pageContext.request.contextPath}/static/style-page/user/UserProfile.js"></script>
<script src="${pageContext.request.contextPath}/static/style-component/style-user_profile/UserProfileDetail.js"></script>



</body>
</html>
