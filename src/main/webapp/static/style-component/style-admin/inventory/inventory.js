var _initialQuantity = '';
var _initialLocation = '';
var _initialReason = '';

function openEditModal(optionId, currentStock, label, currentLocation) {
    document.getElementById('modalOptionId').value = optionId;
    document.getElementById('modalQuantity').value = currentStock;
    document.getElementById('modalLocation').value = currentLocation || '';
    document.getElementById('modalReason').value = '';
    document.getElementById('modalTitle').textContent = 'Cập nhật kho: ' + label;

    _initialQuantity = String(currentStock);
    _initialLocation = currentLocation || '';
    _initialReason = '';

    document.getElementById('modalOverlay').classList.add('active');
    toggleSaveButton();
}

function closeModal() {
    document.getElementById('modalOverlay').classList.remove('active');
}

function toggleSaveButton() {
    const currentQty = document.getElementById('modalQuantity').value;
    const currentLoc = document.getElementById('modalLocation').value.trim();
    const currentReason = document.getElementById('modalReason').value.trim();

    const hasChanged = (currentQty !== _initialQuantity)
        || (currentLoc !== _initialLocation)
        || (currentReason !== _initialReason);

    const saveBtn = document.getElementById('saveBtn');
    if (saveBtn) {
        saveBtn.disabled = !hasChanged;
    }
}

function saveStock() {
    const optionId = document.getElementById('modalOptionId').value;
    const quantity = parseInt(document.getElementById('modalQuantity').value);
    const stockLocation = document.getElementById('modalLocation').value.trim();
    const reason = document.getElementById('modalReason').value.trim();

    if (isNaN(quantity) || quantity < 0) {
        showToast('Số lượng không hợp lệ.', 'error');
        return;
    }

    const saveBtn = document.getElementById('saveBtn');
    if (saveBtn) saveBtn.disabled = true;

    fetch(contextPath + '/admin/api/inventory/' + optionId, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ quantity: quantity, location: stockLocation || null, reason: reason || null })
    })
        .then(function(res) { return res.json(); })
        .then(function(data) {
            if (data.statusCode === 200) {
                showToast('Cập nhật kho thành công!', 'success');
                updateRowUI(optionId, quantity, stockLocation);
                closeModal();
                setTimeout(function() { window.location.reload(); }, 800);
            } else {
                showToast(data.message || 'Có lỗi xảy ra.', 'error');
                if (saveBtn) saveBtn.disabled = false;
            }
        })
        .catch(function() {
            showToast('Không thể kết nối đến máy chủ!', 'error');
            if (saveBtn) saveBtn.disabled = false;
        });
}


function updateRowUI(optionId, quantity, location) {
    const btn = document.querySelector(`.btn-edit-stock[onclick*="openEditModal(${optionId},"]`);
    if (!btn) return;
    const row = btn.closest('tr');

    const stockCell = row.cells[4];

    let stockHtml = '';
    if (quantity === 0) {
        stockHtml = `<span class="status out-of-stock">Hết hàng (0)</span>`;
    } else if (quantity <= 10) {
        stockHtml = `<span class="status low-stock">Sắp hết (${quantity})</span>`;
    } else {
        stockHtml = `<span class="status in-stock">${quantity}</span>`;
    }

    const locationHtml = location
        ? `<div style="font-size:12px;color:#888;margin-top:4px;">
               <i class="fa-solid fa-warehouse"></i> ${location}
           </div>`
        : '';
    stockCell.innerHTML = stockHtml + locationHtml;

    const label = btn.getAttribute('onclick').match(/'([^']+)'/)?.[1] || '';
    btn.setAttribute('onclick',
        `openEditModal(${optionId}, ${quantity}, '${label}', '${location}')`
    );
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

    // Bật/tắt nút Lưu khi gõ
    ['modalQuantity', 'modalLocation', 'modalReason'].forEach(function(id) {
        var el = document.getElementById(id);
        if (el) el.addEventListener('input', toggleSaveButton);
    });

    // Mặc định disable nút Lưu
    var saveBtn = document.getElementById('saveBtn');
    if (saveBtn) saveBtn.disabled = true;
});
