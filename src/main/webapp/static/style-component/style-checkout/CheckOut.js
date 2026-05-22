let shipFee = 0;

$(document).ready(async function () {
    await calculateBill();
    bindCheckout();
});

async function calculateBill() {
    const productItems = $('.product-item');
    let totalPrice = 0;
    const items = [];

    productItems.each(function () {
        const quantity = parseInt($(this).data('quantity')) || 1;
        const price = parseInt($(this).data('price')) || 0;
        totalPrice += quantity * price;

        const height = parseInt($(this).data('height')) || 10;
        const width = parseInt($(this).data('width')) || 15;
        const length = parseInt($(this).data('length')) || 20;
        const weight = parseInt($(this).data('weight')) || 500;

        items.push({
            name: "Sản phẩm",
            quantity: quantity,
            height: height,
            width: width,
            length: length,
            weight: weight
        });
    });
    const addressEl = $('.address-item.active');

    const address = {
        districtId: addressEl.data('district-id'),
        communeId: addressEl.data('commune-id')
    };

    console.log("Items:", items);
    console.log("Address:", address);

    shipFee = await getShipFee(items, address);

    const vat = totalPrice * 0.1;
    const finalTotal = totalPrice + vat + shipFee;

    $('#before_tax').text(formatCurrency(totalPrice));
    $('#VAT').text(formatCurrency(vat));
    $('#ship_fee').text(formatCurrency(shipFee));
    $('#total').text(formatCurrency(finalTotal));
}

async function getShipFee(items, address) {
    if (!address.districtId || !address.communeId) {
        console.error("Thiếu districtId hoặc communeId thực tế:", address);
        return 0;
    }

    const payload = {
        "from_district_id": 3695,
        "service_type_id": 2,
        "to_district_id": parseInt(address.districtId),
        "to_ward_code": String(address.communeId),
        "weight": 500,
        "items": items
    };

    console.log("GHN Payload:", payload);

    try {
        const response = await fetch(
            'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee',
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Token': '676e7671-116a-11f0-95d0-0a92b8726859',
                    'ShopId': 196324
                },
                body: JSON.stringify(payload)
            }
        );

        const data = await response.json();
        console.log("GHN Response:", data);

        if (data.code === 200) {
            return data.data.total;
        }
        return 0;
    } catch (error) {
        console.error("Lỗi API tính phí ship GHN:", error);
        return 0;
    }
}

function bindCheckout() {
    $('#pay').on('click', function () {
        const paymentMethod = $('input[name="payment-method"]:checked').val();
        let paymentText = "COD";
        if(paymentMethod === "VNPAY"){
            paymentText = "Thanh toán VNPAY";
        }
        $('#paymentText').text(paymentText);

        $('#confirmModal').fadeIn();
    });
    // chọn chờ chút
    $('#cancelPay').on('click', function () {
        $('#confirmModal').fadeOut();
    });
    // chọn Xác nhận
    $('#confirmPay').on('click', async function () {
        $('#confirmModal').fadeOut();
        const addressId = $('.address-item.active').data('address-id');
        const paymentMethod = $('input[name="payment-method"]:checked').val();
        const products = [];

        $('.product-item').each(function () {
            const productId = $(this).data('product-id');
            const optionId = $(this).data('option-id');
            const quantity = $(this).data('quantity');
            const price = $(this).data('price');

            products.push({
                id: productId,
                optionId: optionId,
                quantity: quantity,
                total: quantity * price
            });
        });

        const formData = {
            address_id: addressId,
            paymentMethod: paymentMethod,
            products: products,
            ship_fee: shipFee
        };

        console.log("Dữ liệu submit thanh toán:", formData);

        if(!addressId) {
            alert("Vui lòng bổ sung địa chỉ nhận hàng trước khi thanh toán!");
            return;
        }

        try {
            const response = await fetch('checkout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            const result = await response.json();
            console.log(result);

            if (result.success) {
                if(result.paymentUrl){
                    window.location.href = result.paymentUrl;
                }else{
                    window.location.href = "success";
                }
            } else {
                alert(result.message);
            }
        } catch (error) {
            console.error(error);
            alert('Có lỗi xảy ra trong quá trình thanh toán');
        }
    });
}

function formatCurrency(value) {
    return new Intl.NumberFormat('vi-VN').format(value) + ' VND';
}