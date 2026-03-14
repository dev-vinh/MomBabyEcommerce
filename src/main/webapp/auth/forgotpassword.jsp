<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 3/14/2026
  Time: 3:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="container">
    <div class="hear">
        <div class="ft1">Đặt lại mật khẩu</div>
        <%--        pbha sửa tạo link--%>
        <a href="https://linkhelp " target="_blank" rel="noopener noreferrer" class="help">Bạn cần giúp đỡ?</a>
    </div>
    <!-- Email Reset Box -->
    <div id="resetBox" class="reset-box">
        <div class="header">
            <span class="back-arrow" onclick="goBackToHome()">&#8592;</span> <!-- Mũi tên quay lại -->
            <h2>Đặt lại mật khẩu</h2>
        </div>
        <div class="infield">
            <input
                    type="email"
                    id="emailInput"
                    placeholder=" "
                    name="email"
                    required
            />
            <label for="emailInput">Nhập lại email <span class="required">*</span> </label>
        </div>
        <div id="errorMessage" class="error-message">Email không hợp lệ</div>
        <button onclick="validateEmail()">Tiếp theo</button>
    </div>

    <!-- OTP Verification Box -->
    <div id="otpBox" class="reset-box" style="display: none;">
        <div class="header">
            <span class="back-arrow" onclick="goBackToReset()">&#8592;</span> <!-- Mũi tên quay lại -->
            <h2>Nhập mã xác nhận</h2>
        </div>
        <p>Mã xác minh của bạn sẽ được gửi qua email.</p>
        <div class="otp-inputs">
            <input type="text" maxlength="1" id="otp1" oninput="moveToNext(this, 'otp2')">
            <input type="text" maxlength="1" id="otp2" oninput="moveToNext(this, 'otp3')">
            <input type="text" maxlength="1" id="otp3" oninput="moveToNext(this, 'otp4')">
            <input type="text" maxlength="1" id="otp4" oninput="moveToNext(this, 'otp5')">
            <input type="text" maxlength="1" id="otp5">
        </div>
        <div id="otpErrorMessage" class="error-message" style="display: none;"></div>
        <p class="timer-text">Vui lòng chờ <span id="timer">60</span> giây để gửi lại.</p>
        <button onclick="confirmOTP()">Tiếp theo</button>
    </div>

    <!-- New Password Box -->
    <div id="passwordBox" class="reset-box" style="display: none;">
        <div class="header">
            <span class="back-arrow" onclick="goBackToOTP()">&#8592;</span> <!-- Mũi tên quay lại -->
            <h2>Nhập mật khẩu mới </h2>
        </div>
        <input type="password" id="passwordInput" name="newPassword"  placeholder="Nhập mật khẩu mới" required >
        <input type="password" id="confirmPasswordInput" name="confirmPassword" placeholder="Nhập lại mật khẩu mới" required>
        <div id="passwordErrorMessage" class="error-message">Mật khẩu không khớp</div>
        <button onclick="submitPassword()">Đặt lại mật khẩu</button>
    </div>

</div>
</body>
</html>
