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

                        <!-- LEFT -->
                        <div class="info_left">

                            <!-- Avatar + Base info -->
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

                                            <label>
                                                <input type="radio" name="gender" value="Other"
                                                       <c:if test="${user.gender == 'Other'}">checked</c:if>>
                                                Khác
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- DOB -->
                            <div class="personal_infor">

                                <div class="birth row">
                                    <div class="birth_title">
                                        <span>Ngày sinh :</span>
                                    </div>

                                    <c:if test="${not empty user.dOB}">
                                        <c:set var="birthDay" value="${user.dOB.dayOfMonth}"/>
                                        <c:set var="birthMonth" value="${user.dOB.monthValue}"/>
                                        <c:set var="birthYear" value="${user.dOB.year}"/>
                                    </c:if>

                                    <div class="birth_form mid_align">

                                        <select id="day" name="day">
                                            <option value="">Ngày</option>
                                            <c:forEach var="i" begin="1" end="31">
                                                <option value="${i}"
                                                        <c:if test="${i == birthDay}">selected</c:if>>
                                                        ${i}
                                                </option>
                                            </c:forEach>
                                        </select>

                                        <select id="month" name="month">
                                            <option value="">Tháng</option>
                                            <c:forEach var="i" begin="1" end="12">
                                                <option value="${i}"
                                                        <c:if test="${i == birthMonth}">selected</c:if>>
                                                        ${i}
                                                </option>
                                            </c:forEach>
                                        </select>

                                        <select id="year" name="year">
                                            <option value="">Năm</option>
                                            <c:forEach var="i" begin="1900" end="2026">
                                                <option value="${i}"
                                                        <c:if test="${i == birthYear}">selected</c:if>>
                                                        ${i}
                                                </option>
                                            </c:forEach>
                                        </select>

                                    </div>
                                </div>
                            </div>

                            <!-- SAVE -->
                            <div class="mid_align bottom">
                                <button id="save" class="save_btn">Lưu</button>
                            </div>

                        </div>

                        <!-- RIGHT -->
                        <div class="info_right">

                            <div class="contact col">
                                <span>Thông tin liên hệ</span>

                                <div class="contact_item row mid_align">
                                    <span id="phone">${user.phoneNumber}</span>
                                </div>

                                <div class="contact_item row mid_align">
                                    <span id="email">${user.email}</span>
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

<script>
    $("#save").click(function () {

        const day = $("#day").val();
        const month = $("#month").val();
        const year = $("#year").val();

        const data = {
            fullName: $("#name").val(),
            displayName: $("#displayName").val(),
            gender: $("input[name='gender']:checked").val(),
            dOB: `${year}-${month}-${day}`
        };

        $.ajax({
            url: "${pageContext.request.contextPath}/updateUser",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function () {
                alert("Cập nhật thành công");
            },
            error: function () {
                alert("Lỗi khi cập nhật");
            }
        });
    });
</script>

</body>
</html>