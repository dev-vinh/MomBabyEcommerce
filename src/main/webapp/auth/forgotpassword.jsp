<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Boolean otpVerified = (Boolean) session.getAttribute("otpVerified");
    String userEmail    = (String)  session.getAttribute("userEmail");

    String screen1Style  = "block";
    String screen2Style  = "none";
    String screen3Style  = "none";
    String screen4Style  = "none";

    String step1Class = "active";
    String step2Class = "";
    String step3Class = "";

    if (userEmail != null && otpVerified != null && otpVerified) {
        screen1Style = "none"; screen3Style = "block";
        step1Class = "done"; step2Class = "done"; step3Class = "active";
    } else if (userEmail != null) {
        screen1Style = "none"; screen2Style = "block";
        step1Class = "done"; step2Class = "active";
    }

    String headerTitle = "Đặt lại mật khẩu";
    if ("done".equals(step2Class) && "active".equals(step3Class)) {
        headerTitle = "Mật khẩu mới";
    } else if ("active".equals(step2Class)) {
        headerTitle = "Nhập mã OTP";
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/Style-forgot-password/forgot_password.css">

</head>
<body>

<div class="container">
    <div class="reset-box">

        <div class="card-header">
            <h2 id="cardTitle"><%= headerTitle %></h2>
        </div>

        <div class="card-body">


            <div class="screen" id="screen1" style="display: <%= screen1Style %>;">
                <div class="field-group">
                    <label for="emailInput">Địa chỉ email</label>
                    <input type="email" id="emailInput" placeholder="example@email.com" autocomplete="email"/>
                    <div class="error-message" id="emailErr">Vui lòng nhập email hợp lệ.</div>
                </div>
                <button class="primary-btn" onclick="validateEmail()">Tiếp theo</button>
            </div>

            <div class="screen" id="screen2" style="display: <%= screen2Style %>;">
                <button class="back-btn" onclick="goStep(1)">
                    <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="2"
                         stroke-linecap="round" stroke-linejoin="round">
                        <path d="M10 3L5 8l5 5"/>
                    </svg>
                    Quay lại
                </button>

                <div class="field-group">
                    <div class="field-group">
                        <input type="text"
                               id="otpInput"
                               placeholder="Nhập 6 số OTP"
                               maxlength="6"
                               inputmode="numeric"
                               autocomplete="one-time-code" />

                        <div class="error-message" id="otpErr">Mã OTP không hợp lệ.</div>
                    </div>
                    <div class="error-message" id="otpErr">Mã OTP không hợp lệ. Vui lòng thử lại.</div>
                </div>

                <div class="resend-row">
                    <span class="timer-text">Gửi lại sau <b id="timerNum">60</b>s</span>
                    <button class="resend-btn" id="resendBtn" onclick="resendOtp()">Gửi lại mã</button>
                </div>

                <button class="primary-btn" onclick="confirmOTP()" style="margin-top:20px;">Xác nhận →</button>
            </div>

            <!-- Screen 3: New password -->
            <div class="screen" id="screen3" style="display: <%= screen3Style %>;">
                <button class="back-btn" onclick="goStep(2)">
                    <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="2"
                         stroke-linecap="round" stroke-linejoin="round">
                        <path d="M10 3L5 8l5 5"/>
                    </svg>
                    Quay lại
                </button>

                <div class="field-group">
                    <label for="passwordInput">Mật khẩu mới</label>
                    <div class="pw-wrap">
                        <input type="password" id="passwordInput" name="newPassword"
                               placeholder="Tối thiểu 8 ký tự" required/>
                        <button type="button" class="eye-btn toggle-password"
                                data-toggle="#passwordInput" aria-label="Hiện/ẩn mật khẩu">
                            <svg viewBox="0 0 24 24">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                                <circle cx="12" cy="12" r="3"/>
                            </svg>
                        </button>
                    </div>
                    <div class="pw-strength">
                        <div class="pw-strength-fill" id="strFill"></div>
                    </div>
                    <div class="hint" id="strLabel">Độ mạnh mật khẩu</div>
                </div>

                <div class="field-group">
                    <label for="confirmPasswordInput">Xác nhận mật khẩu</label>
                    <div class="pw-wrap">
                        <input type="password" id="confirmPasswordInput" name="confirmPassword"
                               placeholder="Nhập lại mật khẩu mới" required/>
                        <button type="button" class="eye-btn toggle-password"
                                data-toggle="#confirmPasswordInput" aria-label="Hiện/ẩn mật khẩu">
                            <svg viewBox="0 0 24 24">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                                <circle cx="12" cy="12" r="3"/>
                            </svg>
                        </button>
                    </div>
                    <div class="error-message" id="passwordErrorMessage">Mật khẩu không khớp.</div>
                </div>

                <button class="primary-btn" onclick="submitPassword()">Đặt lại mật khẩu</button>
            </div>

            <!-- Screen 4: Success -->
            <div class="screen" id="screen4" style="display: <%= screen4Style %>;">
                <div class="success-wrap">
                    <div class="success-icon">
                        <svg viewBox="0 0 24 24">
                            <polyline points="20 6 9 17 4 12"/>
                        </svg>
                    </div>
                    <div class="success-title">Đặt lại thành công!</div>
                    <div class="success-sub">Mật khẩu của bạn đã được cập nhật.<br>Bạn có thể đăng nhập ngay bây giờ.</div>
                    <a href="${pageContext.request.contextPath}/login">
                        <button class="primary-btn" style="margin-top:28px;">Về trang đăng nhập</button>
                    </a>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/static/style-component/Style-forgot-password/forgot_password.js"></script>
</body>
</html>
