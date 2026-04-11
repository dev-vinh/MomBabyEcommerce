const iframe = document.querySelector("#body iframe");
const addToCartButtons = document.querySelectorAll(".btn.add");
const notification = document.getElementById("cart-notification");


document.addEventListener("DOMContentLoaded", function () {
    const addToCartButtons = document.querySelectorAll(".btn.add");
    // In top-product.jsp, notification has id top_cart-notification or cart-notification

    addToCartButtons.forEach(button => {
        button.addEventListener("click", function () {
            const container = this.closest('#search_body');
            const notification = container.querySelector('.notification');
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
