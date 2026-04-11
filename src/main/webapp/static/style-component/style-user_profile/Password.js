
document.querySelectorAll('.toggle_pass')
    .forEach( function(eyeIcon) {
        eyeIcon.addEventListener('click', function (e) {
            e.preventDefault();

            const pass = eyeIcon.parentElement.getElementsByTagName('input')[0];
            if (pass && pass.type === 'password') {
                pass.type = 'text';
                eyeIcon.classList.remove('fa-eye');
                eyeIcon.classList.add('fa-eye-slash');
            } else if (pass) {
                pass.type = 'password';
                eyeIcon.classList.remove('fa-eye-slash');
                eyeIcon.classList.add('fa-eye');
            }
        })
    });

document.querySelector("form").addEventListener("submit", async (event) => {
    event.preventDefault();

    const formData = {
        currentPassword: document.getElementById("current_pass").value,
        newPassword: document.getElementById("new_pass").value,
        confirmPassword: document.getElementById("confirm_pass").value,
    };

    try {
        const response = await fetch("change-password", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
        });

        // Kiểm tra trạng thái HTTP trước khi parse JSON
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        if (data.statusCode === 200) {
            alert("Thay đổi password thành công! Vui lòng đăng nhập lại...");
            window.location.href = "login";
        } else {
            alert(data.message);
        }
    } catch (error) {
        console.error("Lỗi:", error.message);
        alert("Có lỗi xảy ra: " + error.message);
    }
});


