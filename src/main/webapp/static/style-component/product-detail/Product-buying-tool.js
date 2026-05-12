$(document).ready(function () {
    const buy_now = $('#buy-now');
    const add_to_cart = $('#add-to-cart');
    const product = $('#product');
    const qtyInput = $('#quantity');
    const btnMinus = $('.qty-btn.minus');
    const btnPlus = $('.qty-btn.plus');

    const product_id = product.attr('data-id');
    const option_id_default = product.attr('data-option-default');
    const maxStock = parseInt(product.attr('data-stock')) || 99;

    const option_items = $('.option-item');
    const firstOption = option_items.first();

    if (firstOption.length > 0) {
        option_items.removeClass('selected');
        firstOption.addClass('selected');
        const initialPrice = firstOption.attr("data-price");
        if (initialPrice) {
            $('#price').text(Number(initialPrice).toLocaleString('vi-VN') + ' VND');
        }
    }

    let currentOptionId = firstOption.attr('data-option-id') || option_id_default;

    function validateQuantity(val) {
        let n = parseInt(val);
        if (isNaN(n) || n < 1) return 1;
        if (n > maxStock) return maxStock;
        return n;
    }

    function updateButtons(optionId) {
        currentOptionId = optionId;
        const qty = qtyInput.val();
        buy_now.attr('href', `${contextPath}/buy-now?productId=${product_id}&optionId=${currentOptionId}&quantity=${qty}`);
    }

    btnMinus.on('click', function() {
        let currentQty = parseInt(qtyInput.val()) || 1;
        if (currentQty > 1) {
            qtyInput.val(currentQty - 1);
            updateButtons(currentOptionId);
        }
    });

    btnPlus.on('click', function() {
        let currentQty = parseInt(qtyInput.val()) || 1;
        if (currentQty < maxStock) {
            qtyInput.val(currentQty + 1);
            updateButtons(currentOptionId);
        } else {
            alert("Số lượng trong kho chỉ còn " + maxStock + " sản phẩm");
        }
    });

    qtyInput.on('change', function() {
        $(this).val(validateQuantity($(this).val()));
        updateButtons(currentOptionId);
    });

    updateButtons(currentOptionId);

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

    add_to_cart.on('click', function (e) {
        e.preventDefault();
        const qty = qtyInput.val();
        if (!currentOptionId || currentOptionId === 'undefined') {
            alert("Vui lòng chọn phân loại sản phẩm");
            return;
        }
        addToCart(product_id, currentOptionId, qty);
    });
});

function addToCart(productId, optionId, quantity) {
    fetch(`${contextPath}/add-cart`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `productId=${productId}&optionId=${optionId}&quantity=${quantity}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showCartToast();
                const notification = document.getElementById('cart-notification');
                if (notification) {
                    notification.classList.remove('hidden');
                    setTimeout(() => notification.classList.add('hidden'), 2000);
                }
            }
        })
        .catch(error => console.error(error));
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

let productImages = [];
let productImageIndex = 0;

document.addEventListener('DOMContentLoaded', () => {
    const mainImage = document.getElementById('mainImage');
    if (!mainImage) return;

    const contextPathAttr = mainImage.dataset.contextPath;
    const thumbnails = document.querySelectorAll('.thumbnail');

    productImages = Array.from(thumbnails).map((thumbnail) => {
        return `${contextPathAttr}${thumbnail.getAttribute('src').replace(contextPathAttr, '')}`;
    });

    showImage(productImageIndex);
});

function showImage(index) {
    const mainImage = document.getElementById('mainImage');
    const thumbnails = document.querySelectorAll('.thumbnail');

    if (mainImage && index >= 0 && index < productImages.length) {
        mainImage.src = productImages[index];
        productImageIndex = index;

        thumbnails.forEach((thumbnail, i) => {
            if (i === index) {
                thumbnail.classList.add('active');
            } else {
                thumbnail.classList.remove('active');
            }
        });
    }
}

function nextImage() {
    if (productImages.length === 0) return;
    productImageIndex = (productImageIndex + 1) % productImages.length;
    showImage(productImageIndex);
}

function prevImage() {
    if (productImages.length === 0) return;
    productImageIndex = (productImageIndex - 1 + productImages.length) % productImages.length;
    showImage(productImageIndex);
}

document.addEventListener('DOMContentLoaded', () => {
    const stars = document.querySelectorAll('#user-rating .stars span');
    const ratingInput = document.getElementById('rating-value');

    stars.forEach(star => {
        star.addEventListener('click', () => {
            ratingInput.value = star.dataset.value;
            stars.forEach(s => s.classList.remove('selected'));
            star.classList.add('selected');
        });

        star.addEventListener('mouseover', () => {
            stars.forEach(s => s.classList.remove('hover'));
            star.classList.add('hover');
            let prev = star.previousElementSibling;
            while (prev) {
                prev.classList.add('hover');
                prev = prev.previousElementSibling;
            }
        });

        star.addEventListener('mouseout', () => {
            stars.forEach(s => s.classList.remove('hover'));
        });
    });

    const reviewForm = document.getElementById('review-form');
    if (reviewForm) {
        reviewForm.addEventListener('submit', e => {
            e.preventDefault();
            alert('Cảm ơn bạn đã gửi đánh giá!');
            reviewForm.reset();
            stars.forEach(s => s.classList.remove('selected'));
            ratingInput.value = 0;
        });
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const photoInput = document.getElementById('review-photo');
    const photoPreview = document.getElementById('photo-preview');

    if (photoInput && photoPreview) {
        photoInput.addEventListener('change', () => {
            const files = Array.from(photoInput.files);
            photoPreview.innerHTML = '';

            files.forEach((file, index) => {
                const reader = new FileReader();
                reader.onload = (e) => {
                    const imgContainer = document.createElement('div');
                    imgContainer.className = 'remove-photo';

                    const img = document.createElement('img');
                    img.src = e.target.result;

                    imgContainer.appendChild(img);
                    photoPreview.appendChild(imgContainer);

                    imgContainer.addEventListener('click', () => {
                        const updatedFiles = Array.from(photoInput.files).filter((_, i) => i !== index);
                        const dataTransfer = new DataTransfer();
                        updatedFiles.forEach(f => dataTransfer.items.add(f));
                        photoInput.files = dataTransfer.files;
                        imgContainer.remove();
                    });
                };
                reader.readAsDataURL(file);
            });
        });
    }
});