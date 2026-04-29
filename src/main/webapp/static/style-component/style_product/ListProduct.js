var PAGE_SIZE = 16;
var allProducts = [];
var filteredList = [];
var currentPage = 1;

(function init() {
    try {
        var dataEl = document.getElementById('product-data');
        if (!dataEl) {
            console.error("Không tìm thấy product data");
            return;
        }
        allProducts = JSON.parse(dataEl.textContent.trim());
    } catch (e) {
        console.error("Lỗi parse JSON:", e);
        allProducts = [];
    }
    filteredList = allProducts.slice();
    render();
})();

function render() {
    renderGrid();
    renderPagination();
    renderCount();
}

function renderGrid() {
    var grid = document.getElementById('product_list');
    if (!grid) return;

    var start = (currentPage - 1) * PAGE_SIZE;
    var end = Math.min(start + PAGE_SIZE, filteredList.length);
    var page = filteredList.slice(start, end);

    if (filteredList.length === 0) {
        grid.innerHTML = `
            <div class="sp-empty">
                <i class="fa-solid fa-box-open"></i>
                <p>Không có sản phẩm nào phù hợp.</p>
            </div>`;
        return;
    }

    grid.innerHTML = page.map(function (p) {
        var outOfStock = p.stock === 0;

        var imgHtml = p.imageUrl
            ? `<img src="${p.imageUrl}" alt="${esc(p.name)}" loading="lazy"/>`
            : `<i class="fa-solid fa-mug-hot no-img"></i>`;

        var btnHtml = outOfStock
            ? `<button class="btn-add-cart" disabled>
                <i class="fa-solid fa-ban"></i> Hết hàng
               </button>`
            : `<button class="btn-add-cart"
                    onclick="addToCart(${p.id}, ${p.optionId})">
                <i class="fa-solid fa-cart-shopping"></i> Thêm vào giỏ
               </button>`;

        return `
            <div class="sp-card ${outOfStock ? 'out-of-stock' : ''}">
                <div class="sp-card-img">${imgHtml}</div>
                <div class="sp-card-body">
                    <div class="sp-card-name">
                        <a href="product-detail?id=${p.id}">${esc(p.name)}</a>
                    </div>
                    <div class="sp-card-cat">${esc(p.categoryName || '')}</div>
                    <div class="sp-card-price">${fmtPrice(p.price)}đ</div>
                    ${btnHtml}
                </div>
            </div>`;
    }).join('');
}

function renderPagination() {
    var wrap = document.getElementById('sp-pagination');
    if (!wrap) return;

    var totalPages = Math.ceil(filteredList.length / PAGE_SIZE);
    if (totalPages <= 1) {
        wrap.innerHTML = '';
        return;
    }

    var html = '';

    html += currentPage > 1
        ? `<a href="#" class="pg-btn" data-page="${currentPage - 1}">
             <i class="fa-solid fa-chevron-left"></i>
           </a>`
        : `<span class="pg-btn disabled">
             <i class="fa-solid fa-chevron-left"></i>
           </span>`;

    buildPageRange(currentPage, totalPages).forEach(function (p) {
        if (p === '...') {
            html += `<span class="dots">…</span>`;
        } else {
            html += `<a href="#" class="pg-btn ${p === currentPage ? 'active' : ''}" data-page="${p}">
                        ${p}
                     </a>`;
        }
    });

    html += currentPage < totalPages
        ? `<a href="#" class="pg-btn" data-page="${currentPage + 1}">
             <i class="fa-solid fa-chevron-right"></i>
           </a>`
        : `<span class="pg-btn disabled">
             <i class="fa-solid fa-chevron-right"></i>
           </span>`;

    wrap.innerHTML = html;

    wrap.querySelectorAll('a.pg-btn').forEach(function (a) {
        a.addEventListener('click', function (e) {
            e.preventDefault();
            goToPage(parseInt(this.dataset.page));
        });
    });
}

function renderCount() {

    var total = filteredList.length;
    var start = total === 0 ? 0 : (currentPage - 1) * PAGE_SIZE + 1;
    var end = Math.min(currentPage * PAGE_SIZE, total);

}

function goToPage(page) {
    var totalPages = Math.ceil(filteredList.length / PAGE_SIZE);
    if (page < 1 || page > totalPages) return;

    currentPage = page;
    render();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function buildPageRange(cur, total) {
    if (total <= 7) {
        return Array.from({ length: total }, (_, i) => i + 1);
    }
    var pages = [1];
    if (cur > 3) pages.push('...');
    for (var i = Math.max(2, cur - 1); i <= Math.min(total - 1, cur + 1); i++) {
        pages.push(i);
    }
    if (cur < total - 2) pages.push('...');
    pages.push(total);
    return pages;
}


document.addEventListener("DOMContentLoaded", function () {

    document.getElementById('btn_all')?.addEventListener('click', function () {
        document.querySelectorAll('input[name="price"]').forEach(r => r.checked = false);
        document.getElementById('price0').checked = true;
        document.getElementById('sort_select').value = 'default';

        filteredList = allProducts.slice();
        currentPage = 1;
        render();
    });
});

function esc(str) {
    return String(str || '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function fmtPrice(n) {
    return Number(n).toLocaleString('vi-VN');
}