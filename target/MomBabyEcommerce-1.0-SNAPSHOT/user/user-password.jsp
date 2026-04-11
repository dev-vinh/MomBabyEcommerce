<%--
  Created by IntelliJ IDEA.
  User: fileh
  Date: 1/3/2026
  Time: 12:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Password</title>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_profile/Password.css">
</head>
<body>

<div class="header">
    <jsp:include page="/home/header.jsp"/>
</div>


<div class="container">
    <div class="sidebar">
        <jsp:include page="user-sidebar.jsp"/>
    </div>


    <div class="content">


        <div id="password_header" class="mid_align">
            <span>Mật khẩu</span>
        </div>

        <div id="password_body" class="mid_align col">
            <div class="title mid_align">
                <span>Đổi mật khẩu</span>
            </div>

            <form class="form" action="">
                <label class="item">
                    <span>Mật khẩu hiện tại<span class="asterisk">*</span></span>
                    <div class="input">
                        <input id="current_pass" type="password" required autocomplete="current-password">
                        <i class="toggle_pass fa-regular fa-eye"></i>
                    </div>
                </label>

                <label class="item">
                    <span>Mật khẩu mới<span class="asterisk">*</span></span>
                    <div class="input">
                        <input id="new_pass" type="password" required autocomplete="new-password">
                        <i class="toggle_pass fa-regular fa-eye"></i>
                    </div>
                </label>

                <label class="item">
                    <span>Xác nhận mật khẩu <span class="asterisk">*</span></span>
                    <div class="input">
                        <input id="confirm_pass" type="password" required autocomplete="new-password">
                        <i class="toggle_pass fa-regular fa-eye"></i>
                    </div>
                </label>

                <div class="password-requirements">
                    <strong>Yêu cầu mật khẩu:</strong>
                    <p>Đảm bảo các yêu cầu sau được đáp ứng:</p>
                    <ul>
                        <li>Tối thiểu 8 ký tự - càng dài càng tốt</li>
                        <li>Ít nhất một ký tự viết thường</li>
                        <li>Ít nhất một ký tự viết hoa</li>
                        <li>Ít nhất một ký tự số, ký hiệu hoặc khoảng trắng</li>
                    </ul>
                </div>


                <button class="save_btn" type="submit">Lưu</button>
            </form>
        </div>


    </div>

</div>


<script src="${pageContext.request.contextPath}/static/style-component/style-user_profile/Password.js"></script>
</body>
</html>
