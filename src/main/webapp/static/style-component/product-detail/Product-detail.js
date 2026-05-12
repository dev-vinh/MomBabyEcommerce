$(document).ready(function () {
    const buy_now = $('#buy-now');
    const add_to_cart = $('#add-to-cart');
    const product = $('#product');
    const qtyInput = $('#quantity');
    const btnMinus = $('.qty-btn.minus');
    const btnPlus = $('.qty-btn.plus');

    if (product.length > 0) {
        const product_id = product.attr('data-id');
        const option_id_default = product.attr('data-option-default');
        let maxStock = 99;

        const option_items = $('.option-item');
        const firstOption = option_items.first();

        if (firstOption.length > 0) {
            option_items.removeClass('selected');
            firstOption.addClass('selected');
            maxStock = parseInt(firstOption.attr('data-stock')) || 99;
            const initialPrice = firstOption.attr("data-price");
            if (initialPrice) {
                $('#price').text(Number(initialPrice).toLocaleString('vi-VN') + ' VND');
            }
        }

        let currentOptionId = firstOption.attr('data-option-id') || option_id_default;

        function updateButtons(optionId) {
            currentOptionId = optionId;
            const qty = qtyInput.val() || 1;
            if (buy_now.length > 0) {
                buy_now.attr('href', `${contextPath}/buy-now?productId=${product_id}&optionId=${currentOptionId}&quantity=${qty}`);
            }
        }

        btnMinus.on('click', function () {
            let currentQty = parseInt(qtyInput.val()) || 1;
            if (currentQty > 1) {
                qtyInput.val(currentQty - 1);
                updateButtons(currentOptionId);
            }
        });

        btnPlus.on('click', function () {
            let currentQty = parseInt(qtyInput.val()) || 1;
            if (currentQty < maxStock) {
                qtyInput.val(currentQty + 1);
                updateButtons(currentOptionId);
            } else {
                alert("Số lượng phân loại này trong kho chỉ còn " + maxStock + " sản phẩm");
            }
        });

        qtyInput.on('change', function () {
            let n = parseInt($(this).val());
            if (isNaN(n) || n < 1) n = 1;
            if (n > maxStock) n = maxStock;
            $(this).val(n);
            updateButtons(currentOptionId);
        });

        updateButtons(currentOptionId);

        $('.option-item').on('click', function () {
            $('.option-item').removeClass('selected');
            $(this).addClass('selected');

            maxStock = parseInt($(this).attr('data-stock')) || 99;

            let currentQty = parseInt(qtyInput.val()) || 1;
            if (currentQty > maxStock) {
                qtyInput.val(maxStock > 0 ? maxStock : 1);
            }

            const selectedPrice = $(this).attr("data-price");
            if (selectedPrice) {
                $('#price').text(Number(selectedPrice).toLocaleString('vi-VN') + ' VND');
            }
            updateButtons($(this).attr('data-option-id'));
        });

        add_to_cart.on('click', function (e) {
            e.preventDefault();
            const qty = qtyInput.val();
            if (!currentOptionId) {
                alert("Vui lòng chọn phân loại");
                return;
            }
            addToCart(product_id, currentOptionId, qty);
        });
    }

    $('.tab-btn').on('click', function () {
        $('.tab-btn').removeClass('active');
        $('.tab-content').removeClass('active');
        $(this).addClass('active');
        $('#' + $(this).data('tab')).addClass('active');
    });
});

function addToCart(productId, optionId, quantity) {
    if (typeof contextPath === 'undefined') return;
    fetch(`${contextPath}/add-cart`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `productId=${productId}&optionId=${optionId}&quantity=${quantity}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) showCartToast();
        })
        .catch(error => console.error(error));
}

function showCartToast() {
    const toast = document.getElementById('cart-notification');
    if (!toast) return;
    toast.classList.remove('hidden');
    setTimeout(() => toast.classList.add('show'), 10);
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.classList.add('hidden'), 500);
    }, 3000);
}
let productImagesList = [];
let currentImgIdx = 0;

document.addEventListener('DOMContentLoaded', () => {
    const mainImg = document.getElementById('mainImage');
    const thumbs = document.querySelectorAll('.thumbnail');

    if (mainImg && thumbs.length > 0) {
        // Lấy danh sách link ảnh trực tiếp từ các thẻ img phụ
        productImagesList = Array.from(thumbs).map(t => t.getAttribute('src'));

        // Gắn sự kiện click cho từng ảnh phụ
        thumbs.forEach((thumb, index) => {
            thumb.addEventListener('click', function() {
                showImg(index);
            });
        });

        // Mặc định hiển thị ảnh đầu tiên
        showImg(0);
    }
});

function showImg(idx) {
    const mainImg = document.getElementById('mainImage');
    const thumbs = document.querySelectorAll('.thumbnail');

    if (mainImg && idx >= 0 && idx < productImagesList.length) {
        // Đổi ảnh chính
        mainImg.src = productImagesList[idx];
        currentImgIdx = idx;

        // Cập nhật viền cho ảnh phụ đang được chọn
        thumbs.forEach((t, i) => {
            if (i === idx) {
                t.classList.add('active');
            } else {
                t.classList.remove('active');
            }
        });
    }
}

function nextImage() {
    if (productImagesList.length > 0) {
        showImg((currentImgIdx + 1) % productImagesList.length);
    }
}

function prevImage() {
    if (productImagesList.length > 0) {
        showImg((currentImgIdx - 1 + productImagesList.length) % productImagesList.length);
    }
}

const mainToggleButton = document.getElementById("toggle-specs-btn");
const bottomToggleButton = document.getElementById("toggle-specs-btn-bottom");
const specsSection = document.getElementById("specification-section");
let isExpanded = false;

function resizeIframe(iframe) {
    if (!iframe) return;
    const initialHeight = 500;
    try {
        const expandedHeight = iframe.contentWindow.document.body.scrollHeight;
        iframe.style.height = isExpanded ? expandedHeight + 'px' : initialHeight + 'px';
    } catch (e) {
        iframe.style.height = isExpanded ? '1000px' : initialHeight + 'px';
    }
}

function toggleSpecification() {
    const iframe = document.querySelector('.specification .iframe');
    isExpanded = !isExpanded;
    resizeIframe(iframe);
    const btn = document.querySelector('.show-more-btn');
    if (btn) btn.textContent = isExpanded ? "Thu gọn" : "Xem thêm";
}

function toggleSpecifications() {
    if (!specsSection || !mainToggleButton) return;
    if (specsSection.style.display === "none") {
        specsSection.style.display = "block";
        mainToggleButton.style.display = "none";
    } else {
        specsSection.style.display = "none";
        mainToggleButton.style.display = "inline-block";
    }
}

if (mainToggleButton) {
    mainToggleButton.addEventListener("click", toggleSpecifications);
}

if (bottomToggleButton) {
    bottomToggleButton.addEventListener("click", toggleSpecifications);
}