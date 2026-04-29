var PAGE_SIZE = 16;
var filteredList = [];
var currentPage = 1;
var totalPages = 1;


function render() {
    renderGrid();
    renderPagination();
}

function renderGrid() {
    var grid = document.getElementById('product_list');
    if (!grid) return;

    if (filteredList.length === 0) {
        grid.innerHTML = `
            <div class="sp-empty">
                <i class="fa-solid fa-box-open"></i>
                <p>Không có sản phẩm nào phù hợp.</p>
            </div>`;
        return;
    }

    grid.innerHTML = filteredList.map(renderProductCard).join('');
}

function renderPagination() {
    var wrap = document.getElementById('sp-pagination');
    if (!wrap) return;
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


function goToPage(page) {
    if (page < 1 || page > totalPages) return;
    currentPage = page;
    document.getElementById('apply_btn').click();
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

    document.querySelectorAll('.collapse-filter').forEach(item => {
        item.addEventListener('click', function () {

            const targetId = this.dataset.target;
            const content = document.getElementById(targetId);
            const arrow = this.querySelector('.arrow');

            if (!content) return;

            content.classList.toggle('active');
            if (arrow) {
                arrow.classList.toggle('rotate');
            }
        });
        document.getElementById('apply_btn')?.click();
    });

});



document.getElementById('apply_btn')?.addEventListener('click', function () {


    let selectedPrice = document.querySelector('input[name="price"]:checked');

    let minPrice = selectedPrice ? selectedPrice.dataset.min : null;
    let maxPrice = selectedPrice ? selectedPrice.dataset.max : null;


    let selectedBrand = document.querySelector('input[name="brand"]:checked');
    let brandId = selectedBrand ? selectedBrand.value : null;

    console.log("Filter:", { minPrice, maxPrice, brandId });

    fetch(window.contextPath + '/product/filter', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            category_id: document.getElementById('sidebar').dataset.category,
            minPrice: minPrice ? parseInt(minPrice) : null,
            maxPrice: maxPrice ? parseInt(maxPrice) : null,
            brandId: brandId ? parseInt(brandId) : null,
            page: currentPage,
            size: PAGE_SIZE
        })
    })
        .then(res => res.json())
        .then(res => {
            console.log("Dữ liệu từ Server:", res);
            console.log("Số lượng sản phẩm nhận được:", res.products.length);
            console.log("Tổng số trang:", res.totalPages);
            filteredList = res.products;
            totalPages = res.totalPages;
            currentPage = res.currentPage;

            render();
            window.scrollTo({ top: 0, behavior: 'smooth' });
        })
        .catch(err => console.error("Filter error:", err));
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

function goToDetail(id) {
    window.location.href = window.contextPath + "/product-detail?id=" + id;
}

function renderStars(rating) {
    let html = '';
    for (let i = 1; i <= 5; i++) {
        html += `<i class="fa-solid fa-star ${i <= rating ? 'active' : ''}"></i>`;
    }
    return html;
}

