
$(document).ready(function () {
    const loginButton = $('.btn_login');

    // Xử lý khi người dùng click nút Đăng nhập
    loginButton.on('click', function () {
        // Chuyển đến trang đăng nhập
        window.location.href = "login"; // Đường dẫn trang đăng nhập
    });

    // Kiểm tra trạng thái đăng nhập khi trang được tải
    const userId = sessionStorage.getItem("userId");
    const sessionId = sessionStorage.getItem("sessionId");

    if (userId && sessionId) {
        $('.btn_login').hide();
    }

    const tieptuc = $('.btn_shopping');
    tieptuc.on('click', function () {
        window.location.href = 'home';
    })

});



$(document).ready(function () {
    const pay = $('#pay');

    pay.on('click', function (event) { // ✅ Thêm tham số event
        event.preventDefault(); // Ngăn hành động mặc định ngay từ đầu

        let isLoggedIn = sessionStorage.getItem("userId") && sessionStorage.getItem("sessionId");

        if (!isLoggedIn) {
            alert("Bạn cần đăng nhập trước!");
            return;
        }

        const optionIds = [];

        $('.product_checked:checked').each(function () {
            optionIds.push($(this).val());
        });

        if (optionIds.length === 0) {
            alert('Chọn ít nhất một sản phẩm để thanh toán!');
            return;
        }

        const body = optionIds.join(',');
        window.location.href = "checkout?optionIds=" + encodeURIComponent(body);
    });

    //xu  ly chua dang nhap




    //     Increase/Decrease quantity

    const product = $('.product-item')

    window.onload = function () {
        product.each(function () {

            let price = $(this).find('.price');
            let quantity = ($(this).find('.num'));
            let increment = $(this).find('#increment');
            let decrement = $(this).find('#decrement');
            let remove = $(this).find('.remove');

            let stock = $(this).attr('data-stock');
            let option_id = parseInt($(this).attr('data-id'));



            updatePrice(price, quantity);

            increment.on('click', function () {
                increaseQuantity($(this), quantity, price, stock, option_id);
            })

            decrement.on('click', function () {
                decreaseQuantity($(this), quantity, price, option_id);
            })

            remove.on('click', function () {

                let productItem = $(this).closest('.product-item');
                let optionId = parseInt(productItem.attr('data-id'));

                removeItem(optionId, productItem);
            })




        })

        updateBill();




    }


    function updatePrice(price, quantity) {
        let price_value = parseInt(price.attr('data-price'));
        let quantity_value = parseInt(quantity.attr('data-quantity'));
        let total = price_value * quantity_value;

        let formatted = new Intl.NumberFormat('vi-VN').format(total);
        price.text(formatted + ' VND');

        console.log("updatePrice: ", price);


        updateBill();

    }


    function increaseQuantity(product, quantity, price, stock, option_id) {
        let newQuantity = parseInt(quantity.attr('data-quantity'));



        console.log("option_id: ", option_id);

        if (newQuantity < stock) {
            newQuantity += 1;
            quantity.attr('data-quantity', newQuantity);
            quantity.text(newQuantity);

            updatePrice(price, quantity);
            updateQuantity(option_id, newQuantity);
        }

        else {
            console.log("stock: ", stock);
            alert("Đã đạt số lượng tối đa")
        }





    }



    function decreaseQuantity(product, quantity, price, option_id) {
        let newQuantity = parseInt(quantity.attr('data-quantity'));

        if (newQuantity > 1) {
            newQuantity -= 1;

            quantity.attr('data-quantity', newQuantity);
            quantity.text(newQuantity);

            updateQuantity(option_id, newQuantity);
            updatePrice(price, quantity);

        }

        updateBill();


    }



    function updateQuantity(optionId, quantity) {
        $.ajax({
            url: 'cart/update-quantity',
            method: 'POST',
            data: {
                optionId: optionId,
                quantity: quantity
            },
            success: function (result) {
                console.log(result);
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
            }


        })
    }



    function removeItem(optionId, productItem) {

        if (confirm('Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?')) {
            $.ajax({
                url: 'cart/remove',
                method: 'POST',
                data: {
                    optionId: optionId,
                },
                success: function (result) {
                    console.log(result);
                    productItem.remove();
                    location.reload();
                },
                error: function (xhr, status, error) {
                    console.log(xhr.responseText);
                    alert('Xóa sản phẩm không thành công. Vui lòng thử lại!');
                }
            });
        }


        updateBill();



    }



    function updateBill() {
        const productItems = $('.product-item');

        let totalPrice = 0;
        let total = $('#total');
        let VAT = $('#VAT');
        let before_tax = $('#before_tax');

        productItems.each(function () {
            const isChecked = $(this).find('.product_checked').is(':checked');
            if (isChecked) {
                let priceText = $(this).find('.price').text();
                let priceValue = parseInt(priceText.replace(' VND', '').replaceAll('.', ''));
                if (!isNaN(priceValue)) {
                    totalPrice += priceValue;
                }
            }
        });

        const tax = totalPrice * 10 / 100;
        const b_t = totalPrice - tax;

        total.text(Intl.NumberFormat('vi-VN').format(totalPrice) + ' VND');
        VAT.text(Intl.NumberFormat('vi-VN').format(tax) + ' VND');
        before_tax.text(Intl.NumberFormat('vi-VN').format(b_t) + ' VND');
    }

    // Lắng nghe sự kiện thay đổi của checkbox để cập nhật lại hóa đơn
    $(document).on('change', '.product_checked', function () {
        updateBill();
    });



})