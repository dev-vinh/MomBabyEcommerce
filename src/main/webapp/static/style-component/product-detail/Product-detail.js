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
        let maxStock = 0;
        let currentOptionId = option_id_default;

        const option_items = $('.option-item');
        let firstOption = option_items.filter(function () {
            return getStock($(this)) > 0;
        }).first();

        if (firstOption.length === 0) {
            firstOption = option_items.first();
        }

        if (firstOption.length > 0) {
            option_items.removeClass('selected');
            firstOption.addClass('selected');

            currentOptionId = firstOption.attr('data-option-id');
            setPurchaseState(getStock(firstOption));

            const initialPrice = firstOption.attr("data-price");
            if (initialPrice) {
                $('#price').text(Number(initialPrice).toLocaleString('vi-VN') + ' VND');
            }
        }
        function getStock(optionElement) {
            const stock = parseInt(optionElement.attr('data-stock'), 10);
            return Number.isNaN(stock) ? 0 : stock;
        }

        function setPurchaseState(stock) {
            maxStock = stock;

            if (stock <= 0) {
                qtyInput.val(1).prop('disabled', true).attr('max', 0);
                btnMinus.prop('disabled', true);
                btnPlus.prop('disabled', true);
                add_to_cart.prop('disabled', true);
            } else {
                qtyInput.prop('disabled', false).attr('max', stock);
                btnMinus.prop('disabled', false);
                btnPlus.prop('disabled', false);
                add_to_cart.prop('disabled', false);

                let currentQty = parseInt(qtyInput.val(), 10) || 1;
                if (currentQty > stock) currentQty = stock;
                if (currentQty < 1) currentQty = 1;
                qtyInput.val(currentQty);
            }
        }

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

            if (maxStock <= 0) {
                alert('Sản phẩm đã hết hàng');
                return;
            }

            if (currentQty < maxStock) {
                qtyInput.val(currentQty + 1);
                updateButtons(currentOptionId);
            } else {
                alert('Số lượng phân loại này trong kho chỉ còn ' + maxStock + ' sản phẩm');
            }
        });


        updateButtons(currentOptionId);

        $('.option-item').on('click', function () {
            const selectedOption = $(this);

            $('.option-item').removeClass('selected');
            selectedOption.addClass('selected');

            currentOptionId = selectedOption.attr('data-option-id');
            const stock = getStock(selectedOption);

            setPurchaseState(stock);

            const selectedPrice = selectedOption.attr("data-price");
            if (selectedPrice) {
                $('#price').text(Number(selectedPrice).toLocaleString('vi-VN') + ' VND');
            }

            updateButtons(currentOptionId);
        });

        add_to_cart.on('click', function (e) {
            e.preventDefault();
            const qty = parseInt(qtyInput.val()) || 1;

            if (!currentOptionId) {
                alert('Vui lòng chọn phân loại');
                return;
            }
            if (maxStock <= 0) {
                alert('Sản phẩm đã hết hàng');
                return;
            }

            if (qty > maxStock) {
                alert('Số lượng vượt quá tồn kho. Trong kho chỉ còn ' + maxStock + ' sản phẩm');
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
            if (data.success) {
                showCartToast();
            } else {
                alert(data.message || 'Không thể thêm vào giỏ hàng');
            }
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