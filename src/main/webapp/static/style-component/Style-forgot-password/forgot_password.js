// chưa chỉnh lại vĩnh
// // Hàm kiểm tra email và hiển thị hộp nhập OTP
function validateEmail() {
    const emailInput = document.getElementById("emailInput").value.trim();
    const errorMessage = document.getElementById("emailErr");
    const resetBox = document.getElementById("resetBox");
    const otpBox = document.getElementById("otpBox");


    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

    // Nếu ô email trống
    if (emailInput === "") {
        errorMessage.textContent = "Vui lòng nhập email của bạn.";
        errorMessage.style.display = "block";
        return;
    }

    // Nếu email không hợp lệ
    if (!emailRegex.test(emailInput)) {
        errorMessage.textContent = "Vui lòng nhập một email hợp lệ.";
        errorMessage.style.display = "block";
    } else {
        errorMessage.style.display = "none";
        resetBox.style.display = "none";
        otpBox.style.display = "block";
        startTimer();
    }
}


// Điều hướng quay lại
function goBackToReset() {
    document.getElementById("otpBox").style.display = "none";
    document.getElementById("resetBox").style.display = "block";

}

function goBackToOTP() {
    document.getElementById("passwordBox").style.display = "none";
    document.getElementById("otpBox").style.display = "block";
}

function goBackToHome() {
    window.location.href = '../../pages/auth.html';
    document.querySelector('.back-arrow').addEventListener('click', goBackToHome);
}

// Hàm đếm ngược thời gian cho OTP
let timeLeft = 60;

function startTimer() {
    const timerElement = document.getElementById("timer");
    const timerInterval = setInterval(() => {
        if (timeLeft > 0) {
            timeLeft--;
            timerElement.textContent = timeLeft;
        } else {
            clearInterval(timerInterval);
            timerElement.textContent = "0";
        }
    }, 1000);
}
document.addEventListener("DOMContentLoaded", () => {
    const resetBox = document.getElementById("resetBox");
    const otpBox = document.getElementById("otpBox");
    const passwordBox = document.getElementById("passwordBox");
    const errorMessage = document.getElementById("errorMessage");
    const passwordErrorMessage = document.getElementById("passwordErrorMessage");

    // Chỉ cho phép nhập số trong ô OTP
    const otpInput = document.getElementById("otpInput");
    if (otpInput) {
        otpInput.addEventListener("input", function () {
            this.value = this.value.replace(/\D/g, "").slice(0, 6);
        });
    }

    // Toggle hiển thị/ẩn mật khẩu
    const togglePasswordIcons = document.querySelectorAll(".toggle-password");
    togglePasswordIcons.forEach(icon => {
        icon.addEventListener("click", function () {
            const targetId = this.getAttribute("data-toggle");
            const targetInput = document.querySelector(targetId);
            if (targetInput) {
                if (targetInput.type === "password") {
                    targetInput.type = "text";
                    this.classList.remove("fa-eye");
                    this.classList.add("fa-eye-slash");
                } else {
                    targetInput.type = "password";
                    this.classList.remove("fa-eye-slash");
                    this.classList.add("fa-eye");
                }
            }
        });
    });

    function validateEmail() {
        const email = document.getElementById("emailInput").value.trim();
        if (!email) {
            errorMessage.textContent = "Vui lòng nhập email.";
            errorMessage.style.display = "block";
            return;
        }
        fetch("forgot-password", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `email=${encodeURIComponent(email)}`,
        })
            .then((response) => {
                return response.text().then(text => {
                    console.log("Validate email response:", response.status, text);
                    if (response.ok && text === "success") {
                        resetBox.style.display = "none";
                        otpBox.style.display = "block";
                        startTimer();
                    } else {
                        errorMessage.textContent = text || "Email không tồn tại trong hệ thống.";
                        errorMessage.style.display = "block";
                    }
                });
            })
            .catch(() => {
                errorMessage.textContent = "Đã xảy ra lỗi, vui lòng thử lại.";
                errorMessage.style.display = "block";
            });
    }

    function confirmOTP() {
        const otpInput = document.getElementById("otpInput");
        const otp = (otpInput && otpInput.value ? otpInput.value : "").trim();
        const otpErrorMessage = document.getElementById("otpErrorMessage");
        if (otpErrorMessage) otpErrorMessage.style.display = "none";
        console.log("Submitting OTP:", otp);

        if (otp.length !== 6 || !/^\d{6}$/.test(otp)) {
            if (otpErrorMessage) {
                otpErrorMessage.textContent = "Mã OTP phải có đúng 6 chữ số.";
                otpErrorMessage.style.display = "block";
            } else {
                errorMessage.textContent = "Mã OTP phải có đúng 6 chữ số.";
                errorMessage.style.display = "block";
            }
            return;
        }
        fetch("verify-otp", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `otp=${encodeURIComponent(otp)}`,
        })
            .then((response) => {
                return response.text().then(text => {
                    console.log("Verify OTP response:", response.status, text);
                    if (response.ok && text === "success") {
                        console.log("OTP verified! Switching to password box");
                        otpBox.style.display = "none";
                        passwordBox.style.display = "block";
                    } else {
                        console.log("OTP failed, showing error");
                        const otpErrEl = document.getElementById("otpErrorMessage");
                        if (otpErrEl) {
                            otpErrEl.textContent = text || "Mã OTP không chính xác.";
                            otpErrEl.style.display = "block";
                        } else {
                            errorMessage.textContent = text || "Mã OTP không chính xác.";
                            errorMessage.style.display = "block";
                        }
                    }
                });
            })
            .catch((err) => {
                console.error("Fetch error:", err);
                const otpErrEl = document.getElementById("otpErrorMessage");
                if (otpErrEl) {
                    otpErrEl.textContent = "Đã xảy ra lỗi, vui lòng thử lại.";
                    otpErrEl.style.display = "block";
                } else {
                    errorMessage.textContent = "Đã xảy ra lỗi, vui lòng thử lại.";
                    errorMessage.style.display = "block";
                }
            });
    }

    // Hàm hiển thị thông báo lỗi OTP
    function showOtpError(message) {
        const otpErrorMessage = document.getElementById("otpErrorMessage");
        otpErrorMessage.textContent = message;
        otpErrorMessage.style.display = "block";
        otpErrorMessage.style.color = "#d32f2f";
        otpErrorMessage.style.backgroundColor = "#ffebee";
        otpErrorMessage.style.padding = "10px 15px";
        otpErrorMessage.style.borderRadius = "4px";
        otpErrorMessage.style.marginTop = "10px";
        otpErrorMessage.style.marginBottom = "10px";
        otpErrorMessage.style.border = "1px solid #ffcdd2";
    }

    // Hàm hiển thị thông báo OTP với trạng thái khác nhau
    function showOtpMessage(message, type) {
        const otpErrorMessage = document.getElementById("otpErrorMessage");
        otpErrorMessage.textContent = message;
        otpErrorMessage.style.display = "block";
        otpErrorMessage.style.padding = "10px 15px";
        otpErrorMessage.style.borderRadius = "4px";
        otpErrorMessage.style.marginTop = "10px";
        otpErrorMessage.style.marginBottom = "10px";

        switch(type) {
            case "success":
                otpErrorMessage.style.color = "#2e7d32";
                otpErrorMessage.style.backgroundColor = "#e8f5e9";
                otpErrorMessage.style.border = "1px solid #c8e6c9";
                break;
            case "info":
                otpErrorMessage.style.color = "#1565c0";
                otpErrorMessage.style.backgroundColor = "#e3f2fd";
                otpErrorMessage.style.border = "1px solid #bbdefb";
                break;
            default:
                otpErrorMessage.style.color = "#d32f2f";
                otpErrorMessage.style.backgroundColor = "#ffebee";
                otpErrorMessage.style.border = "1px solid #ffcdd2";
        }
    }
    function goBack() {
        window.history.back();
    }
    // Hàm validate
    function validateStrongPassword(password) {
        // Ít nhất 8 ký tự, 1 ký tự viết hoa, 1 số, 1 ký tự đặc biệt
        const strongPasswordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        return strongPasswordRegex.test(password);
    }

    function submitPassword() {
        const newPassword = document.getElementById("passwordInput").value;
        const confirmPassword = document.getElementById("confirmPasswordInput").value;

        // Validate mật khẩu
        if (!newPassword) {
            passwordErrorMessage.textContent = "Vui lòng nhập mật khẩu mới.";
            passwordErrorMessage.style.display = "block";
            return;
        }

        if (!validateStrongPassword(newPassword)) {
            passwordErrorMessage.textContent = "Mật khẩu phải có ít nhất 8 ký tự, bao gồm một ký tự viết hoa, một ký tự số và một ký tự đặc biệt.";
            passwordErrorMessage.style.display = "block";
            return;
        }

        if (newPassword !== confirmPassword) {
            passwordErrorMessage.textContent = "Mật khẩu không khớp.";
            passwordErrorMessage.style.display = "block";
            return;
        }

        fetch("reset-password", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `newPassword=${encodeURIComponent(newPassword)}&confirmPassword=${encodeURIComponent(confirmPassword)}`,
        })
            .then((response) => {
                return response.text().then(text => {
                    if (response.ok && text === "success") {
                        alert("Mật khẩu của bạn đã được đặt lại thành công!");
                        window.location.href = contextPath + "/login";
                    } else {
                        console.log("Error response:", text);
                        passwordErrorMessage.textContent = text || "Không thể đặt lại mật khẩu.";
                        passwordErrorMessage.style.display = "block";
                    }
                });
            })
            .catch(() => {
                passwordErrorMessage.textContent = "Đã xảy ra lỗi, vui lòng thử lại.";
                passwordErrorMessage.style.display = "block";
            });
    }

    // Bind events
    window.validateEmail = validateEmail;
    window.confirmOTP = confirmOTP;
    window.submitPassword = submitPassword;
    window.goBackToHome = goBackToHome;
    window.goBackToReset = goBackToReset;
    window.goBackToOTP = goBackToOTP;
});


function onBack() {
    const screen1 = document.getElementById("screen1");
    const screen2 = document.getElementById("screen2");
    const screen3 = document.getElementById("screen3");

    const isVisible = (el) => el && window.getComputedStyle(el).display !== "none";

    // Password → OTP
    if (isVisible(screen3)) {
        fetch(contextPath + "/forgot-password?action=backToOtp")
            .then(() => location.reload());
    }

    // OTP → Email
    else if (isVisible(screen2)) {
        fetch(contextPath + "/forgot-password?action=backToEmail")
            .then(() => location.reload());
    }

    // Step đầu → back browser
    else {
        window.history.back();
    }
}