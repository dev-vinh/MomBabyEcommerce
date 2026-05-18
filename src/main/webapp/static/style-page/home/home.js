document.addEventListener("DOMContentLoaded",async () => {
    const menuItems = document.querySelectorAll(".navbar ul li");
    const userLoginIcon = document.querySelector(".user-login");
    const userPopup = document.querySelector(".user-popup");
    const iframe = document.querySelector("#body iframe");

    async function checkSession() {
        try {
            const res = await fetch("/api/check-session", {
                method: "GET",
                credentials: "include"
            });
            const data = await res.json();
            return res.ok && data.status === 200 ? data.data : null;
        } catch (err) {
            return null;
        }
    }
    const currentUser = await checkSession();
    const isLoggedIn = currentUser !== null;

    // Hiệu ứng hover cho menu
    menuItems.forEach((item) => {
        let timeout;

        item.addEventListener("mouseenter", () => {
            clearTimeout(timeout);
            menuItems.forEach((i) => i.classList.remove("active"));
            item.classList.add("active");
        });

        item.addEventListener("mouseleave", () => {
            timeout = setTimeout(() => {
                item.classList.remove("active");
            }, 200);
        });

        const submenu = item.querySelector(".submenu");
        if (submenu) {
            submenu.addEventListener("mouseenter", () => {
                clearTimeout(timeout);
            });

            submenu.addEventListener("mouseleave", () => {
                timeout = setTimeout(() => {
                    item.classList.remove("active");
                }, 200);
            });
        }
    });

    userLoginIcon.addEventListener("mouseenter", () => {
        userPopup.style.display = "block";
    });

    userLoginIcon.addEventListener("mouseleave", () => {
        setTimeout(() => {
            userPopup.style.display = "none";
        }, 200);
    });

    userPopup.addEventListener("mouseenter", () => {
        userPopup.style.display = "block";
    });

    userPopup.addEventListener("mouseleave", () => {
        userPopup.style.display = "none";
    });


    window.addEventListener("message", (event) => {
        if (event.data.type === "navigate") {
            iframe.src = event.data.url;
            history.pushState({ page: "checkout" }, "Thanh toán", "checkout");
        }
    });
        const loginLink = document.getElementById("login-link");
        if (loginLink) {
            if (isLoggedIn) {
                loginLink.textContent = "Đăng xuất";

                loginLink.addEventListener("click", (event) => {
                    event.preventDefault();
                    fetch("logout", {
                        method: "GET",
                        credentials: "include"
                    }).then(() => {
                        sessionStorage.clear();
                        window.location.href = "home";
                    });
                });

            }
            else {
                loginLink.addEventListener("click", (event) => {
                    event.preventDefault();
                    if (modal) {
                        modal.style.display = "block";
                    } else {
                        window.location.href = "login";
                    }
                });
            }
        }
});






