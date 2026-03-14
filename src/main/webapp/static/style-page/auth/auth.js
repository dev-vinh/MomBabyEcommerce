// To do
const registerButton = document.querySelector(".register-button");
const loginButton = document.querySelector(".login-button");
const container = document.querySelector(".container");
const togglePasswords = document.querySelectorAll(".toggle-password");

registerButton.addEventListener("click", () => {
    container.classList.add("right-panel-active");
});
loginButton.addEventListener("click", () => {
    container.classList.remove("right-panel-active");
});
togglePasswords.forEach((togglePassword) => {
    togglePassword.addEventListener("click", function () {
        const passwordInput = document.querySelector(
            this.getAttribute("data-toggle")
        );

        const type =
            passwordInput.getAttribute("type") === "password" ? "text" : "password";
        passwordInput.setAttribute("type", type);

        this.classList.toggle("fa-eye-slash");
    });
});

document.querySelector(".sign-up-container form").addEventListener("submit", async (e) => {
    e.preventDefault(); // Ngăn gửi form truyền thống

    const fullName = document.getElementById("fullName").value;
    const displayName = document.getElementById("displayname").value;
    const email = document.getElementById("emails").value;
    const password = document.getElementById("passwordd").value;
    const confirmPassword = document.getElementById("conf").value;

    if (password !== confirmPassword) {
        alert("Mật khẩu và xác nhận mật khẩu không khớp!");
        return;
    }

    try {
        const response = await fetch("register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                fullName,
                displayName,
                email,
                password,
                confirmPassword
            }),
        });

        if (response.ok) {
            const data = await response.json();
            alert("Đăng ký thành công! Vui lòng vào mail để xác nhận kích hoạt tài khoản.");
            window.location.reload();
        } else {
            const errorData = await response.json();
            alert("Lỗi đăng ký: " + errorData.message);
        }
    } catch (error) {
        console.error("Lỗi khi đăng ký:", error);
        alert("Đã xảy ra lỗi! Vui lòng thử lại.");
    }
});

document.querySelector(".sign-in-container form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const recaptchaResponse = grecaptcha.getResponse();
    if (!recaptchaResponse) {
        alert("Vui lòng xác nhận bạn không phải là robot.");
        return;
    }

    try {
        const response = await fetch("login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                credentials: "include",
                email,
                password,
                recaptcha: recaptchaResponse
            }),
        });

        if (response.ok) {
            const data = await response.json();
            console.log("Dữ liệu trả về từ server:", data);

            if (data && data.data) {
                console.log("Session ID:", data.data.sessionId);
                console.log("User ID:", data.data.id);
                console.log("Role Type:", data.data.roleType);
                console.log("Role ID:", data.data.roleId);
                console.log("Permissions:", data.data.permissions);

                // Lưu vào sessionStorage
                sessionStorage.setItem("sessionId", data.data.sessionId);
                sessionStorage.setItem("userId", data.data.id);
                sessionStorage.setItem("roleType", data.data.roleType);
                sessionStorage.setItem("roleId", data.data.roleId);

                // Lưu permissions dưới dạng JSON string
                sessionStorage.setItem("permissions", JSON.stringify(data.data.permissions));

                if(data.data.status === "BANNED"){
                    // alert("test" + data.data.status)
                    window.location.href = "home"
                }
                if (data.data.roleType !== "USER" && data.data.status === "ACTIVE" ) {
                    window.location.href = "admin/dashboard";
                }
                else {
                    window.location.href = "home";
                }
            } else {
                alert("Email hoặc mật khẩu không chính xác!");
            }
        } else {
            const errorData = await response.json();
            alert("Lỗi đăng nhập: " + errorData.message);
        }
    } catch (error) {
        console.error("Lỗi khi đăng nhập:", error);
        alert("Đã xảy ra lỗi! Vui lòng thử lại.");
    }
});

// Helper function to check if user has specific permission
function hasPermission(permissionType) {
    const permissions = JSON.parse(sessionStorage.getItem("permissions") || "[]");
    return permissions.includes(permissionType);
}

// Helper function to get all user permissions
function getUserPermissions() {
    return JSON.parse(sessionStorage.getItem("permissions") || "[]");
}


// Hàm kiểm tra mật khẩu
function validatePassword(password) {
    const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return passwordRegex.test(password);
}

// Hàm kiểm tra khi người dùng nhập mật khẩu
document.getElementById('passwordd').addEventListener('input', function () {
    const password = document.getElementById('passwordd').value;
    if (!validatePassword(password)) {
        document.getElementById('password-error').style.display = 'block';
    } else {
        document.getElementById('password-error').style.display = 'none';
    }
});

document.getElementById('conf').addEventListener('input', function () {
    const password = document.getElementById('passwordd').value;
    const confirmPassword = document.getElementById('conf').value;
    if (confirmPassword && confirmPassword !== password) {
        document.getElementById('conf-error').style.display = 'block';
    } else {
        document.getElementById('conf-error').style.display = 'none';
    }
});

// Hàm kiểm tra khi người dùng cố gắng đăng ký
function validateForm() {
    const password = document.getElementById('passwordd').value;
    const confirmPassword = document.getElementById('conf').value;

    // Kiểm tra mật khẩu hợp lệ
    if (!validatePassword(password)) {
        document.getElementById('password-error').style.display = 'block';
        return false;
    }
    // Kiểm tra mật khẩu và xác nhận mật khẩu có khớp không
    if (password !== confirmPassword) {
        document.getElementById('conf-error').style.display = 'block';
        return false;
    }

    return true;
}

// Thêm sự kiện submit để kiểm tra form khi người dùng nhấn nút đăng ký
document.querySelector('form').addEventListener('submit', function(event) {
    if (!validateForm()) {
        event.preventDefault(); // Ngừng gửi form nếu không hợp lệ
    }
});



document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.querySelector('.sign-in-container form');
    const rememberCheckbox = document.getElementById('remember-checkbox');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const signInButton = document.getElementById('signInButton');

    checkSavedCredentials();
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const email = emailInput.value.trim();
        const password = passwordInput.value;

        if (rememberCheckbox.checked) {
            saveCredentials(email, password);
        } else {
            clearSavedCredentials();
        }
        // login(email, password, recaptchaResponse);
        const recaptchaResponse = grecaptcha.getResponse();
        if (!recaptchaResponse) {
            alert("Vui lòng xác nhận bạn không phải là robot.");
            return;
        }

        login(email, password, recaptchaResponse);
    });

    function saveCredentials(email, password) {
        const encodedPassword = btoa(password);
        localStorage.setItem('remembered_email', email);
        localStorage.setItem('remembered_password', encodedPassword);
        localStorage.setItem('remember_me', 'true');
    }

    function clearSavedCredentials() {
        localStorage.removeItem('remembered_email');
        localStorage.removeItem('remembered_password');
        localStorage.removeItem('remember_me');
    }

    function checkSavedCredentials() {
        const rememberedEmail = localStorage.getItem('remembered_email');
        const rememberedPassword = localStorage.getItem('remembered_password');
        const rememberMe = localStorage.getItem('remember_me');

        if (rememberMe === 'true' && rememberedEmail && rememberedPassword) {
            emailInput.value = rememberedEmail;
            // Decode the password
            passwordInput.value = atob(rememberedPassword);
            rememberCheckbox.checked = true;
        }
    }

});