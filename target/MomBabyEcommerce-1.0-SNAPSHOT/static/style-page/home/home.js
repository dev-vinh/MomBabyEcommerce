document.addEventListener("DOMContentLoaded", () => {
    const menuItems = document.querySelectorAll(".navbar ul li");
    const userLoginIcon = document.querySelector(".user-login");
    const userPopup = document.querySelector(".user-popup");
    const iframe = document.querySelector("#body iframe");

    let isLoggedIn = sessionStorage.getItem("userId") && sessionStorage.getItem("sessionId");

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


    // Xử lý nút tìm kiếm
    const searchIcon = document.getElementById("search-icon");
    const searchOverlay = document.getElementById("search-overlay");
    const closeSearchOverlay = document.getElementById("close-search-overlay");

    if (searchIcon) {
        searchIcon.addEventListener("click", () => {
            searchOverlay.style.display = "flex";
        });
    }

    if (closeSearchOverlay) {
        closeSearchOverlay.addEventListener("click", () => {
            searchOverlay.style.display = "none";
        });
    }


    // Xử lý nút "Trang của tôi"
    document.getElementById("my-page-link").addEventListener("click", (event) => {
        event.preventDefault();
        if (!isLoggedIn) {
            alert("Bạn cần đăng nhập trước!");
        } else {
            // iframe.src = "/web-programming/frontEnd/src/pages/UserProfile.html";
            // history.pushState({ page: "user-profile" }, "Trang của tôi", "user-profile");
            window.location.href = 'user-profile';
        }
    });

    window.addEventListener("message", (event) => {
        if (event.data.type === "navigate") {
            iframe.src = event.data.url;
            history.pushState({ page: "checkout" }, "Thanh toán", "checkout");
        }
    });

    // Xử lý trạng thái đăng nhập
    const loginLink = document.getElementById("login-link");
    if (loginLink) {
        if (isLoggedIn) {
            loginLink.textContent = "Đăng xuất";
            loginLink.addEventListener("click", (event) => {
                event.preventDefault();
                sessionStorage.removeItem("userId");
                sessionStorage.removeItem("sessionId");
                sessionStorage.removeItem("role");
                // alert("Bạn đã đăng xuất!");
                window.location.href = 'login';
            });
        } else {
            loginLink.textContent = "Đăng nhập/Đăng ký";
            loginLink.addEventListener("click", (event) => {
                event.preventDefault();
                window.location.href = 'login';
            });
        }
    }
});


function showSearchOverlay() {
    // Hiển thị overlay tìm kiếm bằng cách thay đổi style
    document.getElementById("search-overlay").style.display = "block";
}

// Đóng overlay tìm kiếm khi bấm vào nút đóng
document.getElementById("close-search-overlay").addEventListener("click", function () {
    document.getElementById("search-overlay").style.display = "none";
});


// Hàm đọc tham số từ URL
function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

// Lấy loại sản phẩm từ URL
const productType = getQueryParam('product');

// Hiển thị nội dung theo loại sản phẩm
if (productType) {
    const titleElement = document.querySelector('#list_product .product_item > span');
    const productMap = {
        'tu-lanh': 'Tủ Lạnh Bán Chạy',
        'may-giat': 'Máy Giặt Thông Minh',
        'may-lanh': 'Máy Lạnh Hiện Đại',
        'dung-cu-nha-bep': 'Dụng Cụ Nhà Bếp'
    };

    // Thay đổi tiêu đề danh sách sản phẩm
    titleElement.textContent = productMap[productType] || 'Sản Phẩm Nổi Bật';
}

