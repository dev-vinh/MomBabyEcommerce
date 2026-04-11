document.addEventListener("DOMContentLoaded", function () {
    const addToCartButtons = document.querySelectorAll(".btn.add");
    const notifications = document.querySelectorAll(".notification");

    addToCartButtons.forEach((button, index) => {
        button.addEventListener("click", function () {
            const notification = notifications[index] || document.getElementById("cart-notification");
            if (notification) {
                notification.classList.remove("hidden");
                notification.classList.add("show");

                setTimeout(() => {
                    notification.classList.remove("show");
                    notification.classList.add("hidden");
                }, 3000);
            }
        });
    });
});
