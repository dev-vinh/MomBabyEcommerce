
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
let timerInterval = null;

function startTimer(seconds) {
    //xóa time cũ nếu đang chạy
    if (timerInterval) clearInterval(timerInterval);

    const resendBtn = document.getElementById("resendBtn");
    const timerText = document.querySelector(".timer-text");
    const timerElement = document.getElementById("timerNum");
    timeLeft = seconds;

    if (timeLeft <= 0) {
        clearInterval(timerInterval);
        timerInterval = null;
        resendBtn.disabled = false;
        timerText.style.display = "none";
        timerElement.textContent = "0";
        return;
    }

    resendBtn.disabled = true;
    timerText.style.display = "inline";
    timerElement.textContent = timeLeft;


    timerInterval = setInterval(() => {
            timeLeft--;
            timerElement.textContent  = timeLeft;
        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            timerInterval = null;
            resendBtn.disabled = false;
            timerText.style.display = "none";
        }
    }, 1000);
}
document.addEventListener("DOMContentLoaded", () => {
    const resetBox = document.getElementById("resetBox");
    const otpBox = document.getElementById("otpBox");
    const passwordBox = document.getElementById("passwordBox");
    const errorMessage = document.getElementById("emailErr");
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
        const emailErr   = document.getElementById("emailErr");
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
                    if (response.ok && text === "success") {
                        sessionStorage.setItem("fpEmail", email);
                        resetBox.style.display = "none";
                        otpBox.style.display = "block";
                        startTimer(60);
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
    function backToEmail() {
        fetch("forgot-password?action=backToEmail")
            .then(() => {
                location.reload();
            });
    }
    function backToOtp() {
        fetch("forgot-password?action=backToOtp")
            .then(() => location.reload());
    }
    const isOtpVisible = window.getComputedStyle(otpBox).display !== "none";

    if (otpBox && isOtpVisible) {
        const remaining = window.remainingSecondsOnLoad || 0;

        if (remaining > 0) {
            startTimer(remaining);
        } else {
            document.getElementById("resendBtn").disabled = false;
            document.querySelector(".timer-text").style.display = "none";
        }
    }

    function confirmOTP() {
        const otpInput = document.getElementById("otpInput");
        const otp = (otpInput && otpInput.value ? otpInput.value : "").trim();
        const otpErr = document.getElementById("otpErr");
        if (otpErr) otpErr.style.display = "none";
        console.log("Submitting OTP:", otp);

        if (!/^\d{6}$/.test(otp)) {
            otpErr.textContent = "Mã OTP phải có đúng 6 chữ số.";
            otpErr.style.display = "block";
                return;
        }


        fetch("verify-otp", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `otp=${encodeURIComponent(otp)}`,
        })
            .then(res => res.text().then(text => ({ status: res.status, text })))
            .then(({ status, text }) => {
                    console.log("Verify OTP response:", status, text);
                    if (status === 200 && text === "success") {
                        if (timerInterval) clearInterval(timerInterval);
                        document.getElementById("otpBox").style.display = "none";
                        document.getElementById("passwordBox").style.display = "block";
                        document.getElementById("cardTitle").textContent = "Mật khẩu mới";
                    } else if (status === 410)  {
                        otpErr.textContent = text;
                        if (timerInterval) clearInterval(timerInterval);
                        timerInterval = null;
                        timeLeft = 0;
                        document.getElementById("timerNum").textContent = "0";
                        document.getElementById("resendBtn").disabled = false;
                        document.querySelector(".timer-text").style.display = "none";
                    }else{
                        otpErr.textContent = text || "Mã OTP không chính xác.";
                        otpErr.style.display = "block";
                    }
                })
            .catch((err) => {
                otpErr.textContent = "Đã xảy ra lỗi, vui lòng thử lại.";
                otpErr.style.display = "block";
            });
    }

    function resendOtp() {
        const email  = sessionStorage.getItem("fpEmail") || document.getElementById("emailInput").value.trim();
        const otpErr = document.getElementById("otpErr");
        otpErr.style.display = "none";

        if (!email) {
            otpErr.textContent = "Không tìm thấy email. Vui lòng quay lại bước đầu.";
            otpErr.style.display = "block";
            return;
        }
        fetch("forgot-password", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `email=${encodeURIComponent(email)}`
        })
            .then(res => res.text().then(text => ({ status: res.status, text })))
            .then(({ status, text }) => {
                if (status === 200 && text === "success") {
                    document.getElementById("otpInput").value = "";
                    startTimer(60);
                } else if (status === 429) {
                    otpErr.textContent = text;
                    otpErr.style.display = "block";
                } else {
                    otpErr.textContent = text || "Không thể gửi lại OTP.";
                    otpErr.style.display = "block";
                }
            })
            .catch(() => {
                otpErr.textContent = "Đã xảy ra lỗi, vui lòng thử lại.";
                otpErr.style.display = "block";
            });
    }

    function goBack() {
        window.history.back();
    }
    // Hàm validate
    function validateStrongPassword(password) {
        const strongPasswordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;
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
    window.resendOtp      = resendOtp;
    window.backToEmail      = backToEmail;
    window.backToOtp      = backToOtp;

});

