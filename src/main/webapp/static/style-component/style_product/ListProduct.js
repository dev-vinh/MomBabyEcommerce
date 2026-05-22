var PAGE_SIZE = 16;
var filteredList = [];
var currentPage = 1;
var totalPages = 1;
var activeFilters = {
    price: null,   // {label, min, max}
    brands: []     // [{id, name}]
};

function esc(str) {
    return String(str || '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function sanitizeForJson(str) {
    if (!str) return '';
    return str
        .replace(/[\x00-\x1F\x7F]/g, '')
        .replace(/\\/g, '\\\\')
        .replace(/"/g, '\\"')
        .replace(/\n/g, '\\n')
        .replace(/\r/g, '\\r')
        .replace(/\t/g, '\\t');
}

function fmtPrice(n) {
    return Number(n || 0).toLocaleString('vi-VN');
}

function goToDetail(id) {
    window.location.href = window.contextPath + "/product-detail?id=" + id;
}

function renderStars(rating) {
    var html = '';
    for (var i = 1; i <= 5; i++) {
        html += '<i class="fa-solid fa-star ' + (i <= rating ? 'active' : '') + '"></i>';
    }
    return html;
}


function renderFilterTags() {
    var wrap = document.getElementById('active-filters');
    if (!wrap) return;

    var tags = [];

    if (activeFilters.price) {
        tags.push({
            type: 'price',
            label: activeFilters.price.label,
            html: '<span class="filter-tag filter-tag-price">' +
                   esc(activeFilters.price.label) +
                   ' <span class="remove-tag" data-type="price">&times;</span></span>'
        });
    }

    activeFilters.brands.forEach(function (b) {
        tags.push({
            type: 'brand',
            id: b.id,
            label: b.name,
            html: '<span class="filter-tag filter-tag-brand" data-brand-id="' + b.id + '">' +
                   esc(b.name) +
                   ' <span class="remove-tag" data-type="brand" data-brand-id="' + b.id + '">&times;</span></span>'
        });
    });

    if (tags.length === 0) {
        wrap.style.display = 'none';
        wrap.innerHTML = '';
        return;
    }

    var allTags = tags.map(function (t) { return t.html; }).join('');

    wrap.innerHTML =
        '<span class="active-filters-label">Lọc theo:</span>' +
        allTags +
        '<span class="filter-tag-clear-all" id="clear-all-filters">' +
        '<i class="fa-solid fa-xmark"></i> Xóa tất cả' +
        '</span>';

    wrap.style.display = 'flex';

    wrap.querySelectorAll('.remove-tag').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            var type = btn.dataset.type;
            if (type === 'price') {
                activeFilters.price = null;
                document.querySelector('input[name="price"][id="price0"]').checked = true;
            } else if (type === 'brand') {
                var bid = parseInt(btn.dataset.brandId);
                activeFilters.brands = activeFilters.brands.filter(function (b) { return b.id !== bid; });
                var cb = document.querySelector('input[name="brand"][value="' + bid + '"]');
                if (cb) cb.checked = false;
            }
            renderFilterTags();
            document.getElementById('apply_btn').click();
        });
    });

    // Clear all
    document.getElementById('clear-all-filters').addEventListener('click', function () {
        activeFilters = { price: null, brands: [] };
        document.querySelectorAll('input[name="price"]').forEach(function (r) { r.checked = false; });
        document.querySelector('input[name="price"][id="price0"]').checked = true;
        document.querySelectorAll('input[name="brand"]').forEach(function (c) { c.checked = false; });
        renderFilterTags();
        document.getElementById('apply_btn').click();
    });
}

function renderGrid() {
    var grid = document.getElementById('product_list');
    if (!grid) return;

    if (filteredList.length === 0) {
        grid.innerHTML = '<div class="sp-empty">' +
            '<i class="fa-solid fa-box-open"></i>' +
            '<p>Không có sản phẩm nào phù hợp.</p>' +
            '</div>';
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
        ? '<a href="#" class="pg-btn" data-page="' + (currentPage - 1) + '"><i class="fa-solid fa-chevron-left"></i></a>'
        : '<span class="pg-btn disabled"><i class="fa-solid fa-chevron-left"></i></span>';

    buildPageRange(currentPage, totalPages).forEach(function (p) {
        if (p === '...') {
            html += '<span class="dots">…</span>';
        } else {
            html += '<a href="#" class="pg-btn ' + (p === currentPage ? 'active' : '') + '" data-page="' + p + '">' + p + '</a>';
        }
    });

    html += currentPage < totalPages
        ? '<a href="#" class="pg-btn" data-page="' + (currentPage + 1) + '"><i class="fa-solid fa-chevron-right"></i></a>'
        : '<span class="pg-btn disabled"><i class="fa-solid fa-chevron-right"></i></span>';

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
    if (total <= 7) return Array.from({ length: total }, function (_, i) { return i + 1; });
    var pages = [1];
    if (cur > 3) pages.push('...');
    for (var i = Math.max(2, cur - 1); i <= Math.min(total - 1, cur + 1); i++) pages.push(i);
    if (cur < total - 2) pages.push('...');
    pages.push(total);
    return pages;
}

function render() {
    renderGrid();
    renderPagination();
}


document.addEventListener('DOMContentLoaded', function () {

 
    document.querySelectorAll('.collapse-filter').forEach(function (item) {
        item.addEventListener('click', function () {
            var targetId = this.dataset.target;
            var content = document.getElementById(targetId);
            var arrow = this.querySelector('.arrow');
            if (!content) return;
            content.classList.toggle('active');
            if (arrow) arrow.classList.toggle('rotate');
        });
    });

  
    document.getElementById('apply_btn').click();
});

document.getElementById('apply_btn').addEventListener('click', function () {

   
    var selectedPrice = document.querySelector('input[name="price"]:checked');
    var minPrice = selectedPrice && selectedPrice.dataset.min ? parseInt(selectedPrice.dataset.min) : null;
    var maxPrice = selectedPrice && selectedPrice.dataset.max ? parseInt(selectedPrice.dataset.max) : null;
    var priceLabel = selectedPrice && (selectedPrice.dataset.min || selectedPrice.dataset.max)
        ? selectedPrice.nextElementSibling.textContent.trim()
        : null;

    if (minPrice || maxPrice) {
        activeFilters.price = { label: priceLabel, min: minPrice, max: maxPrice };
    } else {
        activeFilters.price = null;
    }

   
    var checkedBrands = document.querySelectorAll('input[name="brand"]:checked');
    var brandIds = [];
    activeFilters.brands = [];
    checkedBrands.forEach(function (cb) {
        var id = parseInt(cb.value, 10);
        if (!isNaN(id)) {
            brandIds.push(id);
            activeFilters.brands.push({
                id: id,
                name: sanitizeForJson(cb.dataset.brandName || cb.nextElementSibling.textContent.trim())
            });
        }
    });

    renderFilterTags();

    var sidebarEl = document.getElementById('sidebar');
    var categoryIdRaw = sidebarEl ? sidebarEl.dataset.category : '0';
    var categoryId = parseInt(categoryIdRaw, 10);
    if (isNaN(categoryId)) categoryId = 0;

    fetch(window.contextPath + '/product/filter', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            category_id: categoryId,
            minPrice: minPrice,
            maxPrice: maxPrice,
            brandIds: brandIds,
            sort: 'default',
            page: currentPage,
            size: PAGE_SIZE
        })
    })
    .then(function (res) { return res.json(); })
    .then(function (res) {
        filteredList = res.products || [];
        totalPages = res.totalPages || 1;
        currentPage = res.currentPage || 1;
        render();
        window.scrollTo({ top: 0, behavior: 'smooth' });
    })
    .catch(function (err) { console.error('Filter error:', err); });
});
