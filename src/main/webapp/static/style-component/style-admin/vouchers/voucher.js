document.addEventListener("DOMContentLoaded", function () {
    console.log("voucher.js loaded");

    const searchInput = document.getElementById("searchInput");
    const tableBody = document.getElementById("voucherTableBody");

    if (!searchInput || !tableBody) {
        return;
    }

    searchInput.addEventListener("input", function () {
        const keyword = this.value.toLowerCase().trim();
        const rows = tableBody.querySelectorAll("tr");
        rows.forEach(row => {
            const voucherCode =
                row.cells[0]?.textContent.toLowerCase() || "";
            if (voucherCode.includes(keyword)) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });

    });

});
document.addEventListener("click", function (e) {
    const deleteBtn = e.target.closest(".delete-btn");
    if (!deleteBtn) {
        return;
    }

    const confirmDelete = confirm("Bạn có chắc muốn vô hiệu hóa voucher này không?");

    if (!confirmDelete) {
        e.preventDefault();
    }
});
const addVoucherBtn = document.getElementById("addVoucherBtn");
addVoucherBtn?.addEventListener("click", () => {
    window.location.href = "/admin/vouchers/add";
});