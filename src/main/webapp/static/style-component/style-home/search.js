document.addEventListener("DOMContentLoaded", () => {
    const searchIcon    = document.getElementById("search-icon");
    const searchOverlay = document.getElementById("search-overlay");
    const closeBtn      = document.getElementById("close-search-overlay");
    const searchInput   = document.getElementById("search-input");

    function openOverlay() {
        if (!searchOverlay) return;
        searchOverlay.classList.add("open");
        if (searchInput) searchInput.focus();
    }

    function closeOverlay() {
        if (!searchOverlay) return;
        searchOverlay.classList.remove("open");
        if (searchInput) searchInput.value = "";
        clearSuggestions();
    }

    if (searchIcon) {
        searchIcon.addEventListener("click", (e) => {
            e.preventDefault();
            openOverlay();
        });
    }

    if (closeBtn) {
        closeBtn.addEventListener("click", closeOverlay);
    }

    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape") closeOverlay();
    });

    if (searchOverlay) {
        searchOverlay.addEventListener("click", (e) => {
            const container = searchOverlay.querySelector(".search-container");
            if (container && !container.contains(e.target)) closeOverlay();
        });
    }

    window.showSearchOverlay  = openOverlay;
    window.closeSearchOverlay = closeOverlay;

    window.toggleDropdown = function () {
        const dropdown = document.getElementById("popular-keywords");
        if (dropdown) {
            dropdown.style.display =
                dropdown.style.display === "block" ? "none" : "block";
        }
    };

    document.addEventListener("click", (event) => {
        const searchContainer = document.querySelector(".search-container");
        const dropdown = document.getElementById("popular-keywords");
        if (dropdown && searchContainer && !searchContainer.contains(event.target)) {
            dropdown.style.display = "none";
        }
    });

    let debounceTimeout;

    if (searchInput) {
        searchInput.addEventListener("input", () => {
            const keyword = searchInput.value.trim();
            const iconEl  = document.querySelector(".search-bar-icon");

            if (iconEl) {
                iconEl.classList.remove("fa-magnifying-glass");
                iconEl.classList.add("fa-spinner", "fa-spin");
            }

            clearTimeout(debounceTimeout);

            debounceTimeout = setTimeout(() => {
                if (keyword) {
                    fetch(`${contextPath}/home/products/search?name=${encodeURIComponent(keyword)}`)
                        .then((res) => {
                            if (!res.ok) throw new Error(`Lỗi server: ${res.status}`);
                            return res.json();
                        })
                        .then((data) => {
                            updateSuggestions(data.status === "success" ? data.data : []);
                        })
                        .catch((err) => {
                            console.error("Lỗi khi tìm kiếm:", err.message);
                            updateSuggestions([]);
                        })
                        .finally(() => {
                            if (iconEl) {
                                iconEl.classList.remove("fa-spinner", "fa-spin");
                                iconEl.classList.add("fa-magnifying-glass");
                            }
                        });
                } else {
                    clearSuggestions();
                    if (iconEl) {
                        iconEl.classList.remove("fa-spinner", "fa-spin");
                        iconEl.classList.add("fa-magnifying-glass");
                    }
                }
            }, 500);
        });
    }

    function updateSuggestions(products) {
        const suggestionBox    = document.getElementById("suggestion-box");
        const suggestionList   = document.getElementById("suggestion-list");
        const suggestionFooter = document.getElementById("suggestion-footer");
        const noResult         = document.getElementById("no-result");

        if (!suggestionBox || !suggestionList) {
            console.error("Không tìm thấy suggestion-box hoặc suggestion-list trong DOM");
            return;
        }

        suggestionList.innerHTML = "";

        if (!products || products.length === 0) {
            suggestionBox.style.display  = "block";
            suggestionList.style.display = "none";
            if (noResult)         noResult.style.display         = "block";
            if (suggestionFooter) suggestionFooter.style.display = "none";
            return;
        }

        suggestionList.style.display = "block";
        if (noResult) noResult.style.display = "none";

        products.slice(0, 5).forEach((product) => {
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
                </a>`;
            suggestionList.appendChild(li);
        });

        if (products.length > 5) {
            const keyword = searchInput ? searchInput.value.trim() : "";
            const li = document.createElement("li");
            li.innerHTML = `
                <a class="suggestion-view-more"
                   href="${contextPath}/search-results?name=${encodeURIComponent(keyword)}">
                    Xem thêm ${products.length - 5} sản phẩm →
                </a>`;
            suggestionList.appendChild(li);
        }

        if (suggestionFooter) {
            const keyword = searchInput ? searchInput.value.trim() : "";
            const countEl = suggestionFooter.querySelector(".footer-count");
            const linkEl  = suggestionFooter.querySelector(".footer-link");
            if (countEl) countEl.textContent = `${products.length} kết quả cho "${keyword}"`;
            if (linkEl)  linkEl.href = `${contextPath}/search-results?name=${encodeURIComponent(keyword)}`;
            suggestionFooter.style.display = "flex";
        }

        suggestionBox.style.display = "block";
    }

    function clearSuggestions() {
        const suggestionList   = document.getElementById("suggestion-list");
        const suggestionBox    = document.getElementById("suggestion-box");
        const noResult         = document.getElementById("no-result");
        const suggestionFooter = document.getElementById("suggestion-footer");
        if (suggestionList)   suggestionList.innerHTML       = "";
        if (suggestionBox)    suggestionBox.style.display    = "none";
        if (noResult)         noResult.style.display         = "none";
        if (suggestionFooter) suggestionFooter.style.display = "none";
    }

    window.updateSuggestions = updateSuggestions;
    window.clearSuggestions  = clearSuggestions;


    window.performSearch = function () {
        const keyword = searchInput ? searchInput.value.trim() : "";
        if (keyword) {
            window.open(
                `${contextPath}/search-results?name=${encodeURIComponent(keyword)}`,
                "_blank"
            );
        } else {
            alert("Vui lòng nhập từ khóa tìm kiếm!");
        }
    };

    window.searchKeyword = function (keyword) {
        if (!searchInput) return;
        searchInput.value = keyword;
        searchInput.focus();
        searchInput.dispatchEvent(new Event("input"));
    };

});