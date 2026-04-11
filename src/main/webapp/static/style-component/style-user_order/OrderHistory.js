$(document).ready(function () {

    const back_btn = $('#back_btn');
    back_btn.on('click', function () {

        const message = {
            type: 'openUserProfile',
            src: $(back_btn).attr('data-src'),
        }

        console.log(message);
        window.parent.postMessage(message, '*')
    })


    const price = $('#product_price');
    const VAT = $('#VAT');
    const total_charge = $('#total_charge');
    const total_charge_value = parseInt(total_charge.text().replaceAll('.', '').replaceAll('VND', ''));


    const newVat = total_charge_value * 10 / 100;
    const newPrice = total_charge_value - newVat;

    price.text(Intl.NumberFormat('vi-VN').format(newPrice) + ' VND');

    VAT.text(Intl.NumberFormat('vi-VN').format(newVat) + ' VND');

    console.log(total_charge_value)
    console.log(newVat)
    console.log(newPrice)


//     Review


    const stars = $('.star');
    const ratingInput = $('#rating-value');

    stars.on('click', function () {
        if ($(this).hasClass('disabled')) return;

        const selectedValue = $(this).data('value');
        ratingInput.val(selectedValue);

        stars.each(function () {
            const starValue = $(this).data('value');
            if (starValue <= selectedValue) {
                $(this).find('i').css('color', 'gold');
            } else {
                $(this).find('i').css('color', 'gray');
            }


        });
        console.log(ratingInput.val())
    });


    const btn_submit = $('#btn_submit');
    btn_submit.on('click', function (e) {
        e.preventDefault();

        const review_text = $('#review-text');
        const order_item= $('.order_item');
        const productIds =[];

        order_item.each(function () {
            const product_id = $(this).data('product-id');
            productIds.push(product_id);
        })



        const order_id = $('.order_id').data('order-id');

        const data = {
            productIds: productIds,
            order_id: order_id,
            rating: ratingInput.val(),
            description: review_text.val(),
        };

        fetch('add-review', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json',
            }
        }).then(function (response) {

            if (response.status === 200) {
                stars.each(function () {
                    console.log($(this));
                    $(this).addClass('disabled');
                    $(this).off('click');
                    $(this).find('i').css('color', 'gold');
                })

                review_text.prop('disabled', true);
                btn_submit.prop('disabled', true);
            }
            return response.json();

        }) .catch(function (error) {
            console.log(error);
        })

        console.log(review_text.val());
        console.log('Rating: ', ratingInput.val());
        console.log("productIds: " ,productIds);
        console.log("order_item: " ,order_item);
        console.log("order_id:",order_id);

    })


});
