<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 3/14/2026
  Time: 1:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Auth</title>
    <script src="https://www.google.com/recaptcha/api.js?hl=vi" async defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-page/auth/auth.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>
<div class="container" id="container">
    <!-- Đăng ký -->
    <div class="form-container sign-up-container">
        <form action="#">
            <h1>Tạo tài khoản</h1>
            <div class="social-container">
                <a href="https://www.facebook.com/?locale=vi_VN" class="social"><i class="fa-brands fa-facebook-f"></i></a>
                <a href="https://accounts.google.com/" class="social"><i class="fa-brands fa-google-plus-g"></i></a>
                <a href="https://www.linkedin.com/" class="social"><i class="fa-brands fa-linkedin-in"></i></a>
                <%--                <a href="#" onclick="handleFacebookAuth('register')" class="social">h<i class="fa-brands fa-facebook-f"></i></a>--%>
                <%--                <a href="${pageContext.request.contextPath}/register-google" class="social"><i class="fa-brands fa-google"></i></a>--%>
                <%--                <a href="https://www.linkedin.com/" class="social"><i class="fa-brands fa   -linkedin-in"></i></a>--%>
            </div>
            <span>hoặc sử dụng email của bạn để đăng ký</span>
            <div class="infield">
                <input id="fullName" placeholder=" " required />
                <label for="fullName">Tên đầy đủ <span class="required">*</span> </label>
            </div>
            <div class="infield">
                <input id="displayname" placeholder=" " required />
                <label for="displayname">Tên hiển thị <span class="required">*</span> </label>
            </div>
            <div class="infield">
                <input type="email" id="emails" placeholder=" " name="email" required />
                <label for="emails">Email <span class="required">*</span> </label>
            </div>

            <div class="infield password">
                <input type="password" id="passwordd" placeholder=" " required>
                <label for="passwordd">Mật khẩu <span class="required">*</span> </label>
                <%--                <i class="fas fa-eye toggle-password" data-toggle="#passwordd"></i>--%>
                <i class="fa-solid fa-eye toggle-password" data-toggle="#passwordd"></i>
            </div>
            <div id="password-error" style="color: red; display: none;">
                Mật khẩu phải chứa ít nhất 8 ký tự, bao gồm một ký tự viết hoa, một ký tự số và một ký tự đặc biệt.
            </div>

            <div class="infield password">
                <input type="password" id="conf" placeholder=" " required />
                <label for="conf">Xác nhận mật khẩu <span class="required">*</span> </label>
                <i class="fa-solid fa-eye toggle-password" data-toggle="#conf"></i>
            </div>
            <div id="conf-error" style="color: red; display: none;">
                Mật khẩu xác nhận không khớp với mật khẩu.
            </div>

            <div class="infield terms">
                <input type="checkbox" id="terms-checkbox" required />
                <label for="terms-checkbox">
                    Tôi đồng ý với điều khoản và điều kiện khi đăng ký dịch vụ,
                    và xác nhận rằng tôi đã đọc chính sách quyền riêng tư.
                </label>
            </div>
            <button>Đăng ký</button>
        </form>
    </div>

    <!-- Đăng nhập -->
    <div class="form-container sign-in-container">
        <form action="#">
            <h1>Đăng nhập</h1>
            <div class="social-container">
                <a href="https://www.facebook.com/?locale=vi_VN" class="social"><i class="fa-brands fa-facebook-f"></i></a>
                <a href="https://accounts.google.com/" class="social"><i class="fa-brands fa-google-plus-g"></i></a>
                <a href="https://www.linkedin.com/" class="social"><i class="fa-brands fa-linkedin-in"></i></a>
            </div>
            <span>hoặc sử dụng tài khoản của bạn</span>
            <div class="infield">
                <input type="email" id="email" placeholder=" " name="email" required />
                <label for="email">Email <span class="required">*</span> </label>
            </div>
            <div class="infield password">
                <input type="password" id="password" placeholder=" " required>
                <label for="password">Mật khẩu <span class="required">*</span> </label>
                <i class="fa-solid fa-eye toggle-password" data-toggle="#password"></i>
            </div>
            <div class="remember-forgot-container">
                <div class="infield remember-me">
                    <input type="checkbox" id="remember-checkbox" />
                    <label for="remember-checkbox">Ghi nhớ đăng nhập</label>
                </div>
                <a href="${pageContext.request.contextPath}/auth/forgotpassword.jsp" class="forgot">Quên mật khẩu?</a>
            </div>


            <button type="submit" id="signInButton"
            >Đăng nhập</button>
        </form>
    </div>

    <div class="overlay-container" id="overlayCon">
        <div class="overlay">
            <div class="overlay-panel overlay-left">
                <h1>Chào mừng trở lại!</h1>
                <p>
                    Để tiếp tục kết nối với chúng tôi, vui lòng đăng nhập bằng thông tin cá nhân của bạn.
                </p>
                <button class="ghost login-button">Đăng nhập</button>
            </div>
            <div class="overlay-panel overlay-right">
                <h1>Xin chào, bạn mới!</h1>
                <p>Nhập thông tin cá nhân của bạn và bắt đầu hành trình cùng chúng tôi</p>
                <button class="ghost register-button">Đăng ký</button>
            </div>
        </div>
    </div>
</div>
<%--<main>--%>
<%--<script>--%>
<%--    function handleFacebookAuth(mode) {--%>
<%--        // Chuyển hướng trực tiếp đến servlet--%>
<%--        window.location.href = '${pageContext.request.contextPath}/login-facebook?mode=' + mode;--%>
<%--    }--%>
<%--</script>--%>
<%--</main>--%>
<%--<script>--%>
<%--    const contextPath = '${pageContext.request.contextPath}';--%>
<%--</script>--%>
<main>
    <script src="${pageContext.request.contextPath}/static/style-page/auth/auth.js"></script>
</main>

<% if (request.getAttribute("errorMessage") != null) { %>
<p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
<% } %>
</body>
</html>
