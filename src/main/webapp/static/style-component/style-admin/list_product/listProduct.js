document.addEventListener("DOMContentLoaded", function () {

    // Submit form khi đổi số mục hiển thị
    function submitFilterForm(size) {
        var form = document.getElementById('filterForm');
        var sizeInput = document.createElement('input');
        sizeInput.type = 'hidden';
        sizeInput.name = 'size';
        sizeInput.value = size;
        form.appendChild(sizeInput);
        form.submit();
    }
    window.submitFilterForm = submitFilterForm;

    const modal = document.getElementById('confirmDeleteModal');
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    const cancelBtn = document.getElementById('cancelDeleteBtn');
    const msgEl = document.getElementById('confirmDeleteMsg');

    let pendingProductId = null;

    // Mở modal
    function openDeleteModal(productId, productName) {
        pendingProductId = productId;
        msgEl.textContent = 'Bạn có chắc chắn muốn xóa sản phẩm "' + productName + '"? Sản phẩm sẽ bị tắt hoạt động.';
        modal.style.display = 'flex';
    }

    // Đóng modal
    function closeDeleteModal() {
        pendingProductId = null;
        modal.style.display = 'none';
    }

    // Gắn sự kiện cho các nút xóa trong bảng
    document.querySelectorAll('.delete-icon').forEach(function(icon) {
        icon.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');
            const row = this.closest('tr');
            const name = row.querySelector('td:nth-child(2) p')?.textContent?.trim() || 'sản phẩm này';
            openDeleteModal(productId, name);
        });
    });

    // Xác nhận xóa
    confirmBtn.addEventListener('click', function() {
        if (pendingProductId === null) return;
        const productId = pendingProductId;
        closeDeleteModal();

        fetch('delete-product', {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productId: productId })
        })
            .then(function(res) { return res.json(); })
            .then(function(data) {
                if (data.statusCode === 200) {
                    alert(data.message || 'Xóa sản phẩm thành công!');
                    window.location.reload();
                } else {
                    alert(data.message || 'Có lỗi xảy ra.');
                }
            })
            .catch(function() {
                alert('Không thể kết nối đến máy chủ!');
            });
    });

    // Huỷ
    cancelBtn.addEventListener('click', closeDeleteModal);
    modal.addEventListener('click', function(e) {
        if (e.target === modal) closeDeleteModal();
    });
});
