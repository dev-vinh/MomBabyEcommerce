document.addEventListener("DOMContentLoaded", function () {
    const entriesDropdown = document.getElementById("entries");
    const productTableBody = document.getElementById("product-table-body");
    const paginationContainer = document.querySelector(".pagination");
    const prevButton = document.querySelector(".prev-btn");
    const nextButton = document.querySelector(".next-btn");
    const addProductBtn = document.querySelector('.add-product-btn');

    let allRows = Array.from(productTableBody.rows); // Lưu tất cả các dòng sản phẩm vào mảng
    let currentPage = 1;
    let entriesPerPage = parseInt(entriesDropdown.value, 10);

    // Hàm hiển thị các dòng sản phẩm tương ứng với trang hiện tại
    function showPage(page) {
        //todo
    }

    // Hàm cập nhật nút phân trang
    function updatePaginationButtons(page) {
        //todo
    }

    // Lắng nghe sự kiện khi người dùng thay đổi giá trị dropdown
    entriesDropdown.addEventListener("change", function () {
        entriesPerPage = parseInt(entriesDropdown.value, 10);
        showPage(1); // Luôn hiển thị trang đầu tiên khi thay đổi số mục hiển thị
    });

    // Xử lý sự kiện khi nhấn nút Trước
    prevButton.addEventListener("click", function () {
        if (currentPage > 1) {
            showPage(currentPage - 1);
        }
    });

    // Xử lý sự kiện khi nhấn nút Tiếp
    nextButton.addEventListener("click", function () {
        const totalPages = Math.ceil(allRows.length / entriesPerPage);
        if (currentPage < totalPages) {
            showPage(currentPage + 1);
        }
    });

    // Hàm sắp xếp bảng
    let currentSortColumn = null;
    let currentSortOrder = 'asc';

    function sortTable(columnIndex) {
        //todo
    }

    // Xử lý thêm sản phẩm
    addProductBtn.addEventListener('click', () => {
        // const contextPath = '/backend_war';
        const addProductUrl = `add-product`;
        window.location.href = addProductUrl;
    });

    // Khởi tạo hiển thị mặc định
    showPage(1);
});


document.addEventListener("DOMContentLoaded", function() {
    const deleteIcons = document.querySelectorAll('.delete-icon');

    deleteIcons.forEach(icon => {
        icon.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');
            if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
                deleteProduct(productId);
            }
        });
    });
});
function deleteProduct(productId) {
    const url = 'delete-product';  // URL của Servlet xử lý yêu cầu

    const requestData = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ productId: productId })
    };

    fetch(url, requestData)
        .then(response => response.json())  // Chuyển phản hồi thành JSON
        .then(data => {
            if (data.status === "success") {
                alert(data.message);  // Hiển thị thông báo thành công
                window.location.reload()
                const row = document.querySelector(`tr[data-product-id="${productId}"]`);

                if (row) {
                    row.remove();
                }
            } else {
                alert(data.message);  // Hiển thị thông báo lỗi
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Không thể kết nối đến máy chủ!');
        });
}


