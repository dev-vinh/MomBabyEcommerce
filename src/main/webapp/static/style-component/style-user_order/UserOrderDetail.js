$(document).ready(function () {
    const back_btn = $('#back_btn');
    back_btn.on('click', function () {
        window.history.back();
    });
});

// Gắn sự kiện click cho từng form đánh giá
$(document).on('click', '.review-form .star', function () {
    const form = $(this).closest('.review-form');
    const value = parseInt($(this).data('value'));
    // const ratingInput = form.find('input[name="rating"]');
    const ratingInput = $("input[name='rating']", form);

    if (!ratingInput.length) {
        console.error("Không tìm thấy input[name='rating']");
        return;
    }

    ratingInput.val(value); // GÁN GIÁ TRỊ

    form.find('.star').each(function () {
        const starValue = parseInt($(this).data('value'));
        $(this).find('i').css('color', starValue <= value ? 'gold' : 'gray');
    });

    console.log("Đã chọn sao:", value, "=> rating =", ratingInput.val());
});


$(document).on('submit', '.review-form', function (e) {
    e.preventDefault();

    const form = $(this);
    const productId = form.closest('.order_item').data('product-id');
    const rating = parseInt(form.find('input[name="rating"]').val()) || 0;
    const description = form.find('textarea[name="review"]').val().trim();
    const orderId = $('.order_id').data('order-id');

    if (rating === 0 || description === "") {
        alert("Vui lòng chọn sao và nhập đánh giá.");
        return;
    }

    const data = {
        productIds: [productId],
        order_id: orderId,
        rating: rating,
        description: description
    };

    console.log("Sending review: ", data);

    fetch('add-review', {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(res => res.json())
        .then(json => {
            if (json.status === 'success') {
                alert('Đánh giá thành công!');
                location.reload();
            } else {
                alert('Lỗi: ' + json.message);
            }
        })
        .catch(err => {
            console.error(err);
            alert('Lỗi kết nối máy chủ.');
        });
});
