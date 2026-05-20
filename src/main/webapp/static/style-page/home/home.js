document.addEventListener("DOMContentLoaded", async () => {
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
            if (res.status === 401) {
                return null;
            }
            if (!res.ok) {
                return null;
            }
            const data = await res.json();

            return res.ok && data.statusCode === 200 ? data.data : null;
        } catch (err) {
            return null;
        }
    }

    const currentUser = await checkSession();
    const isLoggedIn = currentUser !== null;


    // Hiệu ứng hover menu
    menuItems.forEach((item) => {
        let timeout;
        item.addEventListener("mouseenter", () => {
            clearTimeout(timeout);
            menuItems.forEach((i) => i.classList.remove("active"));
            item.classList.add("active");
        });
        item.addEventListener("mouseleave", () => {
            timeout = setTimeout(() => item.classList.remove("active"), 200);
        });
        const submenu = item.querySelector(".submenu");
        if (submenu) {
            submenu.addEventListener("mouseenter", () => clearTimeout(timeout));
            submenu.addEventListener("mouseleave", () => {
                timeout = setTimeout(() => item.classList.remove("active"), 200);
            });
        }
    });

    if (userLoginIcon && userPopup) {

        let popupTimeout;

        userLoginIcon.addEventListener("mouseenter", () => {
            clearTimeout(popupTimeout);
            userPopup.style.display = "block";
        });
        userLoginIcon.addEventListener("mouseleave", () => {
            popupTimeout = setTimeout(() => userPopup.style.display = "none", 200);
        });
        userPopup.addEventListener("mouseenter", () => {
            clearTimeout(popupTimeout);
            userPopup.style.display = "block";
        });
        userPopup.addEventListener("mouseleave", () => {
            popupTimeout = setTimeout(() => userPopup.style.display = "none", 200);
        });


    }
    window.addEventListener("message", (event) => {
        if (event.data.type === "navigate") {
            iframe.src = event.data.url;
            history.pushState({ page: "checkout" }, "Thanh toán", "checkout");
        }
    });


});
