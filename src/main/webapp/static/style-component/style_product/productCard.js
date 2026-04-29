function renderProductCard(p) {
    const outOfStock = p.stock === 0;

    return `
        <div class="sp-card ${outOfStock ? 'out-of-stock' : ''}" 
             onclick="goToDetail(${p.id})">

            <div class="sp-card-img">
                ${
        p.imageUrl
            ? `<img src="${p.imageUrl}" alt="${esc(p.name)}"/>`
            : `<i class="fa-solid fa-image no-img"></i>`
    }
            </div>

            <div class="sp-card-body">

                <div class="sp-card-name">
                    ${esc(p.name)}
                </div>

                <div class="sp-card-rating">
                    ${renderStars(p.rating || 4)}
                </div>

                <div class="sp-card-price">
                    ${fmtPrice(p.price)}đ
                </div>

                <button class="btn-add-cart"
                        onclick="event.stopPropagation(); goToDetail(${p.id})">
                    <i class="fa-solid fa-cart-shopping"></i>
                    Thêm vào giỏ
                </button>

            </div>
        </div>
    `;
}