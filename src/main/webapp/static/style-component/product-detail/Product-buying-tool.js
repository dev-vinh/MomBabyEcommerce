// Sử lý variant
$(document).ready(function () {

    const buy_now = $('#buy-now');
    const add_to_cart = $('#add-to-cart');
    const product = $('#product');
    const product_id = product.attr('data-id');
    const option_id_default = product.attr('data-option-default');

    const wrap_variant = $('.wrap_variant');

    // Mac dinh select option dau tien
    const option_items = $('.option-item');
    const firstOption = option_items.first();

    if (firstOption.length > 0) {
        option_items.removeClass('selected');
        firstOption.addClass('selected');

        const priceDisplay = $('#price');
        const initialPrice = firstOption.attr("data-price");

        if (initialPrice) {
            priceDisplay.text(Number(initialPrice).toLocaleString('vi-VN') + ' VND');
        }
    }

    let currentOptionId = firstOption.attr('data-option-id') || option_id_default;

    function updateButtons(optionId) {
        currentOptionId = optionId;
        buy_now.attr('href', `${contextPath}/buy-now?productId=${product_id}&optionId=${currentOptionId}`);
    }

    // Initialize buttons
    updateButtons(currentOptionId);

    add_to_cart.on('click', function (e) {
        e.preventDefault();
        if (!currentOptionId || currentOptionId === 'undefined') {
            alert("Vui lòng chọn phân loại sản phẩm");
            return;
        }
        addToCart(product_id, currentOptionId);
    });

    $('.option-item').on('click', function () {
        $('.option-item').removeClass('selected');
        $(this).addClass('selected');

        const selectedPrice = $(this).attr("data-price");
        if (selectedPrice) {
            $('#price').text(Number(selectedPrice).toLocaleString('vi-VN') + ' VND');
        }

        const optionId = $(this).attr('data-option-id');
        updateButtons(optionId);
    });
});

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
                showCartToast();
                const notification = document.getElementById('cart-notification');
                if (notification) {
                    notification.classList.remove('hidden');
                    setTimeout(() => notification.classList.add('hidden'), 2000);
                }
            }
        })
        .catch(error => console.log(error));
}
function showCartToast() {
    const toast = document.getElementById('cart-notification');
    if (!toast) return;

    clearTimeout(toast._hideTimer);
    toast.classList.remove('hidden');

    requestAnimationFrame(() => {
        requestAnimationFrame(() => {
            toast.classList.add('show');
        });
    });

    toast._hideTimer = setTimeout(() => {
        toast.classList.remove('show');
        toast.addEventListener('transitionend', () => {
            toast.classList.add('hidden');
        }, { once: true });
    }, 3000);
}
