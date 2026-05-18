const registerButton = document.querySelector(".register-button");
const loginButton = document.querySelector(".login-button");
const container = document.querySelector(".auth-container");
const togglePasswords = document.querySelectorAll(".toggle-password");

// Chuyển đổi giao diện giữa đăng ký và đăng nhập
registerButton.addEventListener("click", () => {
    container.classList.add("right-panel-active");
});
loginButton.addEventListener("click", () => {
    container.classList.remove("right-panel-active");
});

// Hiển thị/Ẩn mật khẩu
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

// Ẩn thông báo lỗi khi người dùng nhập lại
document.getElementById("emails").addEventListener("input", function () {
    document.getElementById("email-error").style.display = "none";
});

document.querySelector(".sign-up-container form").addEventListener("submit", async (e) => {
    e.preventDefault(); // Ngăn gửi form truyền thống

    const fullName = document.getElementById("fullName").value;
    const displayName = document.getElementById("displayName").value;
    const email = document.getElementById("emails").value;
    const password = document.getElementById("passwordd").value;
    const confirmPassword = document.getElementById("conf").value;
    const fullNameError = document.getElementById("fullNameError");
    const displayNameError = document.getElementById("displayNameError");
    const emailError = document.getElementById("email-error");


    // reset lỗi
    fullNameError.innerText = "";
    displayNameError.innerText = "";


    let isValid = true;


    if (!fullName.trim()) {
        fullNameError.innerText = "Không được để trống";
        isValid = false;
    } else if (fullName.length > 50) {
        fullNameError.innerText = "Tối đa 50 ký tự";
        isValid = false;
    }


    if (!displayName.trim()) {
        displayNameError.innerText = "Không được để trống";
        isValid = false;
    } else if (displayName.length > 20) {
        displayNameError.innerText = "Tối đa 20 ký tự";
        isValid = false;
    }

    if (!isValid) return;
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

            console.log("Response data:", data);
            alert("Đăng ký thành công! Vui lòng vào mail để xác nhận.");
            window.location.reload();
        } else {
            const errorData = await response.json();
            console.log("Error response:", errorData);
            // Hiển thị lỗi dưới input email
            if (emailError) {
                emailError.textContent = "Email đã tồn tại";
                emailError.style.display = "block";
            }
        }
    } catch (error) {
        console.error("Lỗi khi đăng ký:", error);
        alert("Đã xảy ra lỗi! Vui lòng thử lại.");
    }
});

document.querySelector(".sign-in-container form")
    .addEventListener("submit", async (e) => {

        e.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const errorBox = document.getElementById("auth-error-message");
        const btn = document.getElementById("signInButton");
        btn.innerText = "Đang xử lý...";
        btn.disabled = true;

        try {
            const response = await fetch("login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email,
                    password,
                }),
            });

            const data = await response.json();
            if (response.ok) {
                sessionStorage.setItem("userId", data.data.id);
                sessionStorage.setItem("sessionId", data.data.sessionId);
                window.location.href = data.data.redirectUrl;
            } else {
                errorBox.innerText = data.message;
                errorBox.style.display = "block";
                btn.innerText = "Đăng nhập";
                btn.disabled = false;
            }
        } catch (error) {
            console.error(error);
            errorBox.innerText = "Lỗi kết nối máy chủ!";
            errorBox.style.display = "block";
            btn.innerText = "Đăng nhập";
            btn.disabled = false;
        }
    });


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
