function toggleDropdown() {
    const dropdown = document.getElementById("popular-keywords");
    if (dropdown) {
        dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
    }
}

document.addEventListener("click", function(event) {
    const searchContainer = document.querySelector(".search-container");
    const dropdown = document.getElementById("popular-keywords");
    if (dropdown && !searchContainer.contains(event.target)) {
        dropdown.style.display = "none";
    }
});




let debounceTimeout;

document.getElementById('search-input').addEventListener('input', () => {
    const searchInput = document.getElementById('search-input').value.trim();
    const searchIconEl = document.querySelector('.search-bar-icon');


    searchIconEl.classList.remove('fa-magnifying-glass');
    searchIconEl.classList.add('fa-spinner', 'fa-spin');


    clearTimeout(debounceTimeout);

    debounceTimeout = setTimeout(() => {
        if (searchInput) {
            fetch(`${contextPath}/home/products/search?name=${encodeURIComponent(searchInput)}`)
                .then(response => response.json())
                .then(data => {
                    if (data.status === 'success') {
                        updateSuggestions(data.data);
                    } else {
                        clearSuggestions();
                        console.log(data.message || 'Không tìm thấy sản phẩm phù hợp.');
                    }
                })
                .catch(err => {
                    console.error('Lỗi khi tìm kiếm sản phẩm:', err);
                })
                .finally(() => {
                    searchIconEl.classList.remove('fa-spinner', 'fa-spin');
                    searchIconEl.classList.add('fa-magnifying-glass');
                });
        } else {
            clearSuggestions();
            // Khôi phục lại icon nếu input rỗng
            searchIconEl.classList.remove('fa-spinner', 'fa-spin');
            searchIconEl.classList.add('fa-magnifying-glass');
        }
    }, 500);
});




function updateSuggestions(products) {
    const suggestionBox  = document.getElementById("suggestion-box");
    const suggestionList = document.getElementById("suggestion-list");
    const suggestionFooter = document.getElementById("suggestion-footer");

    if (!suggestionBox || !suggestionList) {
        console.error("Không tìm thấy suggestion-box hoặc suggestion-list trong DOM");
        return;
    }

    suggestionList.innerHTML = "";

    if (!products || products.length === 0) {
        suggestionBox.style.display = "none";
        if (suggestionFooter) suggestionFooter.style.display = "none";
        return;
    }

    const displayed = products.slice(0, 5);

    displayed.forEach(product => {
        const price = product.price
            ? product.price.toLocaleString("vi-VN") + "đ"
            : "Đang cập nhật";

        const li = document.createElement("li");
        li.innerHTML = `
            <a href="${contextPath}/product-detail?id=${encodeURIComponent(product.id)}"
               class="suggestion-item">
                <div class="suggestion-info">
                    <span class="suggestion-name">
                        ${product.name || "Sản phẩm chưa có tên"}
                    </span>
                    <span class="suggestion-price">${price}</span>
                </div>
                <img class="suggestion-img"
                     src="${product.imageUrl}"
                     alt="${product.name}"
                     onerror="this.src='${contextPath}/static/image/placeholder.png'"/>
            </a>
        `;
        suggestionList.appendChild(li);
    });

    // Xem thêm nếu có nhiều hơn 5
    if (products.length > 5) {
        const keyword = document.getElementById("search-input").value.trim();
        const li = document.createElement("li");
        li.innerHTML = `
            <a class="suggestion-view-more"
               href="${contextPath}/search-results?name=${encodeURIComponent(keyword)}">
                Xem thêm ${products.length - 5} sản phẩm →
            </a>
        `;
        suggestionList.appendChild(li);
    }

    if (suggestionFooter) {
        const keyword = document.getElementById("search-input").value.trim();
        const countEl = suggestionFooter.querySelector(".footer-count");
        const linkEl  = suggestionFooter.querySelector(".footer-link");
        if (countEl) countEl.textContent = `${products.length} kết quả cho "${keyword}"`;
        if (linkEl)  linkEl.href = `${contextPath}/search-results?name=${encodeURIComponent(keyword)}`;
        suggestionFooter.style.display = "flex";
    }

    suggestionBox.style.display = "block";
}

function clearSuggestions() {
    const suggestionList = document.getElementById("suggestion-list");
    const suggestionBox  = document.getElementById("suggestion-box");
    if (suggestionList) suggestionList.innerHTML = "";
    if (suggestionBox)  suggestionBox.style.display = "none";
}
function performSearch() {
    const searchInput = document.getElementById('search-input').value.trim();
    if (searchInput) {
        const searchURL = `${contextPath}/search-results?name=${encodeURIComponent(searchInput)}`;
        window.open(searchURL, '_blank');
    } else {
        alert("Vui lòng nhập từ khóa tìm kiếm!");
    }
}
function searchKeyword(keyword) {
    const inputEl = document.getElementById("search-input");
    inputEl.value = keyword;
    inputEl.focus();
    inputEl.dispatchEvent(new Event("input"));
}