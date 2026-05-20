document.addEventListener("DOMContentLoaded", function () {

    const addProductBtn = document.querySelector('.add-product-btn');
    if (addProductBtn) {
        addProductBtn.addEventListener('click', () => {
            window.location.href = 'add-product';
        });
    }

    document.querySelectorAll('.delete-icon').forEach(icon => {
        icon.addEventListener('click', function (e) {
            e.stopPropagation();
            const productId = this.getAttribute('data-product-id');
            if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
                deleteProduct(productId);
            }
        });
    });

    document.addEventListener('click', function () {
        document.querySelectorAll('.dropdown-content').forEach(d => {
            d.style.display = 'none';
        });
    });
});

function toggleDropdown(btn) {
    const content = btn.nextElementSibling;
    const isOpen  = content.style.display === 'block';
    document.querySelectorAll('.dropdown-content').forEach(d => d.style.display = 'none');
    content.style.display = isOpen ? 'none' : 'block';
    event.stopPropagation();
}

function deleteProduct(productId) {
    fetch('delete-product', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId: productId })
    })
        .then(res => res.json())
        .then(data => {
            if (data.status === "success") {
                alert(data.message);
                window.location.reload();
            } else {
                alert(data.message);
            }
        })
        .catch(() => {
            alert('Không thể kết nối đến máy chủ!');
        });
}