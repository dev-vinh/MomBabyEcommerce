function openEditModal(optionId, currentStock, label) {
    document.getElementById('modalOptionId').value = optionId;
    document.getElementById('modalQuantity').value = currentStock;
    document.getElementById('modalLocation').value = '';
    document.getElementById('modalTitle').textContent = 'Cập nhật kho: ' + label;
    document.getElementById('modalOverlay').classList.add('active');
}

function closeModal() {
    document.getElementById('modalOverlay').classList.remove('active');
}

function saveStock() {
    const optionId = document.getElementById('modalOptionId').value;
    const quantity = parseInt(document.getElementById('modalQuantity').value);
    const location = document.getElementById('modalLocation').value.trim();

    if (isNaN(quantity) || quantity < 0) {
        showToast('Số lượng không hợp lệ.', 'error');
        return;
    }

    fetch(contextPath + '/admin/api/inventory/' + optionId, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ quantity: quantity, location: location || null })
    })
        .then(function(res) { return res.json(); })
        .then(function(data) {
            if (data.statusCode === 200) {
                showToast('Cập nhật kho thành công!', 'success');
                closeModal();
                setTimeout(function() { location.reload(); }, 800);
            } else {
                showToast(data.message || 'Có lỗi xảy ra.', 'error');
            }
        })
        .catch(function() {
            showToast('Không thể kết nối đến máy chủ!', 'error');
        });
}



function searchInventory() {
    var keyword = document.getElementById('searchInput').value.toLowerCase();
    var rows = document.querySelectorAll('#inventoryTableBody tr');

    rows.forEach(function(row) {
        var text = row.textContent.toLowerCase();
        row.style.display = text.includes(keyword) ? '' : 'none';
    });
}

// Đóng modal khi click ra ngoài
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('modalOverlay').addEventListener('click', function(e) {
        if (e.target === this) {
            closeModal();
        }
    });
});
