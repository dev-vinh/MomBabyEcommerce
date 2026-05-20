$(document).ready(async function () {

    const currentUser = await checkSession();
    const isLoggedIn = currentUser !== null;

    const userId = sessionStorage.getItem("userId");
    const sessionId = sessionStorage.getItem("sessionId");

    if (isLoggedIn) {
        $('.btn_login').hide();
    }

    bindContinueShopping();
    bindCheckout(isLoggedIn);

    initCartItems();
    updateBill();

    $(document).on('change', '.product_checked', function () {
        updateBill();
    });
});



async function checkSession() {
    try {
        const response = await fetch("/api/check-session", {
            method: "GET",
            credentials: "include"
        });
        if (response.status === 401) {
            return null;
        }

        const result = await response.json();

        if (response.ok && result.statusCode === 200) {
            return result.data;
        }

        return null;

    } catch (error) {
        console.error("Check session error:", error);
        return null;
    }
}


function bindContinueShopping() {
    $('.btn_shopping').on('click', function () {
        window.location.href = 'home';
    });
}

function bindCheckout(isLoggedIn) {

    $('#pay').on('click', function (event) {

        event.preventDefault();

        if (!isLoggedIn) {
            showToast("Bạn cần đăng nhập trước!", "error");
            return;
        }

        const optionIds = [];

        $('.product_checked:checked').each(function () {
            optionIds.push($(this).val());
        });

        if (optionIds.length === 0) {
            showToast("Chọn ít nhất một sản phẩm để thanh toán!", "error");
            return;
        }

        const query = encodeURIComponent(optionIds.join(','));

        window.location.href = "checkout?optionIds=" + query;
    });
}


function initCartItems() {

    $('.product-item').each(function () {

        const productItem = $(this);

        const price = productItem.find('.price');
        const quantity = productItem.find('.num');

        const incrementBtn = productItem.find('.increment');
        const decrementBtn = productItem.find('.decrement');

        const removeBtn = productItem.find('.remove');

        const stock = parseInt(productItem.attr('data-stock'), 10) || 0;
        const optionId = parseInt(productItem.attr('data-id'), 10);

        updatePrice(price, quantity);
        updateQuantityButtons(productItem);

        incrementBtn.off('click').on('click', function () {
            increaseQuantity(
                productItem,
                quantity,
                price,
                stock,
                optionId
            );
        });

        decrementBtn.off('click').on('click', function () {
            decreaseQuantity(
                productItem,
                quantity,
                price,
                optionId
            );
        });

        removeBtn.off('click').on('click', function () {
            removeItem(optionId, productItem);
        });
    });
}


function updateQuantityButtons(productItem) {

    const quantitySpan = productItem.find('.num');

    const incrementBtn = productItem.find('.increment');
    const decrementBtn = productItem.find('.decrement');

    const quantity = parseInt(
        quantitySpan.attr('data-quantity'),
        10
    ) || 1;

    const stock = parseInt(
        productItem.attr('data-stock'),
        10
    ) || 0;

    if (stock <= 0) {
        incrementBtn.prop('disabled', true);
        decrementBtn.prop('disabled', true);
        return;
    }

    decrementBtn.prop('disabled', quantity <= 1);
    incrementBtn.prop('disabled', quantity >= stock);
}


function increaseQuantity(
    productItem,
    quantity,
    price,
    stock,
    optionId
) {

    const currentQuantity = parseInt(
        quantity.attr('data-quantity'),
        10
    ) || 1;

    if (stock <= 0) {
        showToast("Sản phẩm đã hết hàng", "error");
        updateQuantityButtons(productItem);
        return;
    }

    if (currentQuantity >= stock) {
        showToast(
            "Số lượng trong kho chỉ còn " + stock + " sản phẩm",
            "error"
        );

        updateQuantityButtons(productItem);
        return;
    }

    const newQuantity = currentQuantity + 1;

    updateQuantity(optionId, newQuantity, function () {

        quantity.attr('data-quantity', newQuantity);
        quantity.text(newQuantity);

        updatePrice(price, quantity);
        updateQuantityButtons(productItem);
        updateBill();
    });
}

function decreaseQuantity(
    productItem,
    quantity,
    price,
    optionId
) {

    const currentQuantity = parseInt(
        quantity.attr('data-quantity'),
        10
    ) || 1;

    if (currentQuantity <= 1) {
        showToast("Số lượng tối thiểu là 1", "error");
        updateQuantityButtons(productItem);
        return;
    }

    const newQuantity = currentQuantity - 1;

    updateQuantity(optionId, newQuantity, function () {

        quantity.attr('data-quantity', newQuantity);
        quantity.text(newQuantity);

        updatePrice(price, quantity);
        updateQuantityButtons(productItem);
        updateBill();
    });
}


function updateQuantity(optionId, quantity, onSuccess) {

    $.ajax({
        url: 'cart/update-quantity',
        method: 'POST',
        dataType: 'json',

        data: {
            optionId: optionId,
            quantity: quantity
        },

        success: function (result) {

            if (result.success) {

                if (typeof onSuccess === 'function') {
                    onSuccess(result);
                }

            } else {

                showToast(
                    result.message || "Cập nhật số lượng thất bại",
                    "error"
                );

                setTimeout(function () {
                    location.reload();
                }, 1200);
            }
        },

        error: function (xhr) {

            console.error(xhr.responseText);

            showToast(
                "Không thể cập nhật số lượng. Vui lòng thử lại!",
                "error"
            );

            setTimeout(function () {
                location.reload();
            }, 1200);
        }
    });
}


function removeItem(optionId, productItem) {

    $.ajax({
        url: 'cart/remove',
        method: 'POST',
        dataType: 'json',

        data: {
            optionId: optionId
        },

        success: function (result) {

            if (result.success === false) {

                showToast(
                    result.message || "Xóa sản phẩm không thành công",
                    "error"
                );

                return;
            }

            productItem.remove();

            updateBill();

            showToast(
                "Đã xóa sản phẩm khỏi giỏ hàng",
                "success"
            );

            if ($('.product-item').length === 0) {

                setTimeout(function () {
                    location.reload();
                }, 1200);
            }
        },

        error: function (xhr) {

            console.error(xhr.responseText);

            showToast(
                "Xóa sản phẩm không thành công. Vui lòng thử lại!",
                "error"
            );
        }
    });
}


function updatePrice(price, quantity) {

    const priceValue = parseInt(
        price.attr('data-price'),
        10
    ) || 0;

    const quantityValue = parseInt(
        quantity.attr('data-quantity'),
        10
    ) || 1;

    const total = priceValue * quantityValue;

    const formatted = new Intl.NumberFormat('vi-VN')
        .format(total);

    price.text(formatted + ' VND');

    updateBill();
}


function updateBill() {

    const productItems = $('.product-item');

    let totalPrice = 0;

    const total = $('#total');
    const VAT = $('#VAT');
    const beforeTax = $('#before_tax');

    productItems.each(function () {

        const isChecked = $(this)
            .find('.product_checked')
            .is(':checked');

        if (!isChecked) {
            return;
        }

        const priceText = $(this)
            .find('.price')
            .text();

        const priceValue = parseInt(
            priceText
                .replace(' VND', '')
                .replaceAll('.', '')
                .replaceAll(',', '')
                .trim(),
            10
        );

        if (!isNaN(priceValue)) {
            totalPrice += priceValue;
        }
    });

    const tax = totalPrice * 0.1;
    const beforeTaxValue = totalPrice - tax;

    total.text(formatCurrency(totalPrice));
    VAT.text(formatCurrency(tax));
    beforeTax.text(formatCurrency(beforeTaxValue));
}


function formatCurrency(value) {
    return new Intl.NumberFormat('vi-VN')
        .format(value) + ' VND';
}