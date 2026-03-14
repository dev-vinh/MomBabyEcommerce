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
    const searchIcon = document.querySelector('.search-icon i');

    // Thay đổi icon thành loader khi người dùng nhập từ khóa
    searchIcon.classList.remove('fa-magnifying-glass');
    searchIcon.classList.add('fa-spinner', 'fa-spin');

    // Xóa timeout cũ nếu người dùng tiếp tục nhập
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
                    // Khôi phục lại icon tìm kiếm sau khi hoàn thành
                    searchIcon.classList.remove('fa-spinner', 'fa-spin');
                    searchIcon.classList.add('fa-magnifying-glass');
                });
        } else {
            clearSuggestions();
            // Khôi phục lại icon nếu input rỗng
            searchIcon.classList.remove('fa-spinner', 'fa-spin');
            searchIcon.classList.add('fa-magnifying-glass');
        }
    }, 500); // 500ms debounce
});




function updateSuggestions(products) {
    const suggestionsContainer = document.querySelector('.product-suggestions');
    suggestionsContainer.innerHTML = ''; // Xóa kết quả cũ

    if (products && products.length > 0) {
        products.forEach(product => {
            const productDiv = document.createElement('div');
            productDiv.classList.add('product');

            // Hiển thị giá sản phẩm nếu có, nếu không thì hiển thị "Đang cập nhật"
            const price = product.price ? product.price.toLocaleString() : 'Đang cập nhật';

            // Cập nhật nội dung của productDiv để hiển thị giá, tên và ảnh
            productDiv.innerHTML = `
                <img class="" src="${product.imageUrl}">
                <div class="product-content"> 
                    <a href="product-detail?id=${encodeURIComponent(product.id)}" class="product-name" >
                        ${product.name || 'Sản phẩm chưa có tên'}
                     </a>
                    <span class="product-price" >${price} VND</span>
                </div>
            `;

            // productDiv.addEventListener('click', () => {
            //     const url = `${window.location.origin}/backend_war/product-detail?id=${encodeURIComponent(product.id)}`;
            //     console.log("Đang mở URL:", url); // Kiểm tra URL xem có đúng không
            //     window.open(url, '_blank');
            // });
            // Thêm vào danh sách kết quả
            suggestionsContainer.appendChild(productDiv);
        });
    } else {
        suggestionsContainer.innerHTML = '<p>Không tìm thấy sản phẩm nào phù hợp.</p>';
    }
}

function clearSuggestions() {
    const suggestionsContainer = document.querySelector('.product-suggestions');
    suggestionsContainer.innerHTML = ''; // Xóa kết quả hiển thị
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
