//pbha chưa checkkĩ
$(document).ready(function () {
    const category_id = $('#sidebar').data('category');
    console.log("Category ID on sidebar:", category_id);
    const apply_btn = $('#apply_btn');
    apply_btn.on('click', function () {

        // Radio filtering for price
        let selectedPrice = $('input[name="price"]:checked');
        console.log("Selected Price element:", selectedPrice);

        let minPrice = selectedPrice.length ? selectedPrice.data('min') : null;
        let maxPrice = selectedPrice.length ? selectedPrice.data('max') : null;

        console.log("Filter parameters:", { category_id, minPrice, maxPrice });

        fetch(`${contextPath}/product/filter`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            body: JSON.stringify({
                category_id: category_id,
                minPrice: minPrice,
                maxPrice: maxPrice,
            })
        }).then(response => response.json())
            .then(data => {
                console.log("Filtered products data: ", data);
                if (!Array.isArray(data)) {
                    console.error("API response is not an array:", data);
                    return;
                }
                reRenderProducts(data);
            })
            .catch(error => {
                console.error("Error fetching filtered products:", error);
            });

    })
})

function reRenderProducts(products) {
    const product_list = $('#product_list');
    product_list.empty();

    if (products.length === 0) {
        product_list.append('<p class="no-results" style="text-align: center; width: 100%; padding: 20px;">Không tìm thấy sản phẩm nào phù hợp.</p>');
        return;
    }

    products.forEach(p => {
        // Safe path joining for image
        let imgPath = p.imageUrl || '';
        if (imgPath && !imgPath.startsWith('http') && !imgPath.startsWith(contextPath)) {
            // If missing context path and not absolute, prepend it
            // Ensure exactly one slash between them if needed, but contextPath usually has leading / and imgPath should too
            if (!imgPath.startsWith('/')) imgPath = '/' + imgPath;
            imgPath = contextPath + imgPath;
        }

        const itemHtml = `
                    <div class="product_item col" data-stock="${p.stock}">
                        <div class="wrap mid_align row">
                            <div class="img_section">
                                ${imgPath ? `<img src="${imgPath}" alt="${p.name || ''}"/>` : '<div class="no-image">No Image</div>'}
                            </div>
                            <div class="infor_section">
                                <div class="infor_name bold f22" id="name">
                                    <a href="${contextPath}/product-detail?id=${p.id}"> ${p.name || 'Sản phẩm'}</a>
                                </div>
                                <div class="rating row mid_align">
                                    <span id="noOfRatting" class="bold" style="padding: 0 5px">
                                        4.7 (153)
                                        <i class="fa-solid fa-star" style="color: #FFD43B;"></i>
                                    </span>
                                </div>
                                <div id="description">
                                    <p class="desc_item f14">${p.description || ''}</p>
                                </div>
                            </div>
                            <div class="rec_vertical"></div>
                            <div class="section_right col">
                                <div class="price">
                                    <span class="bold f22">${p.price ? p.price.toLocaleString() : '0'} VND</span>
                                </div>
                                <div class="service">
                                    <div class="service_item">
                                        <i class="fa-solid fa-gift"></i>
                                        <span>Luôn sẵn sàng cho khách hàng</span>
                                    </div>
                                    <div class="service_item">
                                        <i class="fa-solid fa-truck"></i>
                                        <span>Miễn Phí Vận Chuyển Toàn Quốc</span>
                                    </div>
                                    <div class="service_item">
                                        <i class="fa-solid fa-box-open"></i>
                                        <span>Đổi trả trong 14 ngày</span>
                                    </div>
                                </div>
                                <div class="wrap_btn col">
                                     <a href="${contextPath}/buy-now?productId=${p.id}&optionId=${p.optionId}" class="btn buy buy-now-btn">Mua Ngay</a>
                                    <button onclick="addToCart(${p.id},${p.optionId})" class="btn add">
                                        Thêm vào giỏ hàng
                                    </button>
                                </div>
                                <div id="cart-notification" class="notification hidden">
                                    <i class="fa fa-check-circle"></i>
                                    <span>Thêm vào giỏ hàng thành công</span>
                                </div>
                            </div>
                        </div>
                    </div>`;
        product_list.append(itemHtml);
    });
}
