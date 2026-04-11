function addToCart(productId, optionId) {
    fetch(`${contextPath}/add-cart`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `productId=${productId}&optionId=${optionId}`
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            if (data.success) {
                // Show notification - try both possible IDs
                const notification = document.getElementById('cart-notification');
                const topNotification = document.getElementById('top_cart-notification');

                if (notification) {
                    notification.classList.remove('hidden');
                    setTimeout(() => notification.classList.add('hidden'), 2000);
                }
                if (topNotification) {
                    topNotification.classList.remove('hidden');
                    setTimeout(() => topNotification.classList.add('hidden'), 2000);
                }
            }
        }).catch(error => console.log(error));
}

// Redirect directly for Buy Now to use the GET controller
function buyNow(productId, optionId) {
    window.location.href = `${contextPath}/buy-now?productId=${productId}&optionId=${optionId}`;
}
