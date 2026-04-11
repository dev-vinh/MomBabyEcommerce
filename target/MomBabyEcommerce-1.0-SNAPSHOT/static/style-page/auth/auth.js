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
togglePasswords.forEach((togglePassword) => {togglePasswords
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
    e.preventDefault();

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
            alert("Đăng ký thành công! Vui lòng vào mail để xác nhận.");
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

                sessionStorage.setItem("sessionId", data.data.sessionId);
                sessionStorage.setItem("userId", data.data.id);
                sessionStorage.setItem("roleType", data.data.roleType);
                sessionStorage.setItem("roleId", data.data.roleId);
                sessionStorage.setItem("permissions", JSON.stringify(data.data.permissions));

                if(data.data.status === "BANNED"){
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

function validatePassword(password) {
    const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return passwordRegex.test(password);
}
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

function validateForm() {
    const password = document.getElementById('passwordd').value;
    const confirmPassword = document.getElementById('conf').value;

    if (!validatePassword(password)) {
        document.getElementById('password-error').style.display = 'block';
        return false;
    }
    if (password !== confirmPassword) {
        document.getElementById('conf-error').style.display = 'block';
        return false;
    }

    return true;
}

document.querySelector('form').addEventListener('submit', function(event) {
    if (!validateForm()) {
        event.preventDefault();
    }
    // todo
});