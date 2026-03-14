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
        wrap_variant.each(function () {
            const group_options = $(this).find('.option-item');
            const groupFirst = group_options.first();
            group_options.removeClass('selected');
            groupFirst.addClass('selected');
        });

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
        addToCart(product_id, currentOptionId);
    });

    $('.option-item').on('click', function () {
        const group = $(this).closest('.wrap_variant');
        group.find('.option-item').removeClass('selected');
        $(this).addClass('selected');

        // Update price based on selected item
        const selectedPrice = $(this).attr("data-price");
        if (selectedPrice) {
            $('#price').text(Number(selectedPrice).toLocaleString('vi-VN') + ' VND');
        }

        // Update option id for buttons
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
                const notification = document.getElementById('cart-notification');
                if (notification) {
                    notification.classList.remove('hidden');
                    setTimeout(() => notification.classList.add('hidden'), 2000);
                }
            }
        })
        .catch(error => console.log(error));
}
