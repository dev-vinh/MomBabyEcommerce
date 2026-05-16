$(document).ready(function () {


    const userId = sessionStorage.getItem("userId");
    const sessionId = sessionStorage.getItem("sessionId");

    if (userId && sessionId) {
        $('.btn_login').hide();
    }

    const tieptuc = $('.btn_shopping');
    tieptuc.on('click', function () {
        window.location.href = 'home';
    });

    const pay = $('#pay');

    pay.on('click', function (event) {
        event.preventDefault();

        let isLoggedIn = sessionStorage.getItem("userId") && sessionStorage.getItem("sessionId");

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

        const body = optionIds.join(',');
        window.location.href = "checkout?optionIds=" + encodeURIComponent(body);
    });

    initCartItems();
    updateBill();

    $(document).on('change', '.product_checked', function () {
        updateBill();
    });
});

function initCartItems() {
    $('.product-item').each(function () {
        let productItem = $(this);

        let price = productItem.find('.price');
        let quantity = productItem.find('.num');

        // Hỗ trợ cả trường hợp bạn đã sửa thành class hoặc vẫn còn id cũ
        let increment = productItem.find('.increment, #increment');
        let decrement = productItem.find('.decrement, #decrement');

        let remove = productItem.find('.remove');

        let stock = parseInt(productItem.attr('data-stock'), 10) || 0;
        let optionId = parseInt(productItem.attr('data-id'), 10);

        updatePrice(price, quantity);
        updateQuantityButtons(productItem);

        increment.off('click').on('click', function () {
            increaseQuantity(productItem, quantity, price, stock, optionId);
        });

        decrement.off('click').on('click', function () {
            decreaseQuantity(productItem, quantity, price, optionId);
        });

        remove.off('click').on('click', function () {
            let optionId = parseInt(productItem.attr('data-id'), 10);
            removeItem(optionId, productItem);
        });
    });
}

function updateQuantityButtons(productItem) {
    let quantitySpan = productItem.find('.num');

    let incrementBtn = productItem.find('.increment, #increment');
    let decrementBtn = productItem.find('.decrement, #decrement');

    let quantity = parseInt(quantitySpan.attr('data-quantity'), 10) || 1;
    let stock = parseInt(productItem.attr('data-stock'), 10) || 0;

    if (stock <= 0) {
        incrementBtn.prop('disabled', true);
        decrementBtn.prop('disabled', true);
        return;
    }

    decrementBtn.prop('disabled', quantity <= 1);
    incrementBtn.prop('disabled', quantity >= stock);
}

function increaseQuantity(productItem, quantity, price, stock, optionId) {
    let currentQuantity = parseInt(quantity.attr('data-quantity'), 10) || 1;

    if (stock <= 0) {
        showToast("Sản phẩm đã hết hàng", "error");
        updateQuantityButtons(productItem);
        return;
    }

    if (currentQuantity >= stock) {
        showToast("Số lượng trong kho chỉ còn " + stock + " sản phẩm", "error");
        updateQuantityButtons(productItem);
        return;
    }

    let newQuantity = currentQuantity + 1;

    updateQuantity(optionId, newQuantity, function () {
        quantity.attr('data-quantity', newQuantity);
        quantity.text(newQuantity);

        updatePrice(price, quantity);
        updateQuantityButtons(productItem);
        updateBill();
    });
}

function decreaseQuantity(productItem, quantity, price, optionId) {
    let currentQuantity = parseInt(quantity.attr('data-quantity'), 10) || 1;

    if (currentQuantity <= 1) {
        showToast("Số lượng tối thiểu là 1", "error");
        updateQuantityButtons(productItem);
        return;
    }

    let newQuantity = currentQuantity - 1;

    updateQuantity(optionId, newQuantity, function () {
        quantity.attr('data-quantity', newQuantity);
        quantity.text(newQuantity);

        updatePrice(price, quantity);

        // Quan trọng: sau khi giảm từ 2 xuống 1, bật lại nút +
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
                showToast(result.message || "Cập nhật số lượng thất bại", "error");
                setTimeout(function () {
                    location.reload();
                }, 1200);
            }
        },
        error: function (xhr) {
            console.log(xhr.responseText);
            showToast("Không thể cập nhật số lượng. Vui lòng thử lại!", "error");

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
                showToast(result.message || "Xóa sản phẩm không thành công", "error");
                return;
            }

            productItem.remove();
            updateBill();
            showToast("Đã xóa sản phẩm khỏi giỏ hàng", "success");

            if ($('.product-item').length === 0) {
                setTimeout(function () {
                    location.reload();
                }, 1200);
            }
        },
        error: function (xhr) {
            console.log(xhr.responseText);
            showToast("Xóa sản phẩm không thành công. Vui lòng thử lại!", "error");
        }
    });
}

function updatePrice(price, quantity) {
    let priceValue = parseInt(price.attr('data-price'), 10) || 0;
    let quantityValue = parseInt(quantity.attr('data-quantity'), 10) || 1;

    let total = priceValue * quantityValue;
    let formatted = new Intl.NumberFormat('vi-VN').format(total);

    price.text(formatted + ' VND');

    updateBill();
}

function updateBill() {
    const productItems = $('.product-item');

    let totalPrice = 0;
    let total = $('#total');
    let VAT = $('#VAT');
    let beforeTax = $('#before_tax');

    productItems.each(function () {
        const isChecked = $(this).find('.product_checked').is(':checked');

        if (isChecked) {
            let priceText = $(this).find('.price').text();

            let priceValue = parseInt(
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
        }
    });

    const tax = totalPrice * 10 / 100;
    const beforeTaxValue = totalPrice - tax;

    total.text(new Intl.NumberFormat('vi-VN').format(totalPrice) + ' VND');
    VAT.text(new Intl.NumberFormat('vi-VN').format(tax) + ' VND');
    beforeTax.text(new Intl.NumberFormat('vi-VN').format(beforeTaxValue) + ' VND');
}