document.addEventListener("DOMContentLoaded", () => {
    // ==== Sắp xếp bảng ====
    let currentSortColumn = null;
    let currentSortOrder = 'asc';

    function sortTable(columnIndex) {
        const tableBody = document.getElementById("product-table-body");
        const rows = Array.from(tableBody.rows);
        const header = document.querySelectorAll("th")[columnIndex];
        const columnType = header.getAttribute("data-sort");

        if (currentSortColumn === columnIndex) {
            currentSortOrder = currentSortOrder === 'asc' ? 'desc' : 'asc';
        } else {
            currentSortOrder = 'asc';
        }
        currentSortColumn = columnIndex;

        const sortedRows = rows.sort((a, b) => {
            const valA = a.cells[columnIndex].innerText.trim();
            const valB = b.cells[columnIndex].innerText.trim();

            return columnType === "number"
                ? (currentSortOrder === 'asc' ? valA - valB : valB - valA)
                : (currentSortOrder === 'asc' ? valA.localeCompare(valB) : valB.localeCompare(valA));
        });

        tableBody.innerHTML = "";
        sortedRows.forEach(row => tableBody.appendChild(row));

        document.querySelectorAll(".sort-arrow").forEach(arrow => arrow.classList.remove("active"));
        header.querySelector(`.${currentSortOrder}`).classList.add("active");
    }

    document.querySelectorAll("th[data-sort]").forEach((header, index) => {
        header.addEventListener("click", () => sortTable(index));
    });

    // ==== Phân trang ====
    const prevButton = document.querySelector(".prev-btn");
    const nextButton = document.querySelector(".next-btn");
    const pageNumbersContainer = document.querySelector(".page-numbers");
    const tableBody = document.getElementById("product-table-body");
    const rowsPerPage = 10;
    const rows = Array.from(tableBody.rows);
    let totalPages = Math.ceil(rows.length / rowsPerPage);
    let currentPage = 1;

    function renderPageNumbers() {
        pageNumbersContainer.innerHTML = "";
        totalPages = Math.ceil(rows.length / rowsPerPage);
        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement("button");
            btn.className = "page-number" + (i === currentPage ? " active" : "");
            btn.textContent = i;
            btn.addEventListener("click", () => {
                currentPage = i;
                renderTable(currentPage);
            });
            pageNumbersContainer.appendChild(btn);
        }
    }

    function renderTable(page) {
        const start = (page - 1) * rowsPerPage;
        const end = page * rowsPerPage;

        rows.forEach((row, index) => {
            row.style.display = index >= start && index < end ? "table-row" : "none";
        });

        renderPageNumbers();

        prevButton.disabled = page === 1;
        nextButton.disabled = page === totalPages;
    }

    prevButton.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            renderTable(currentPage);
        }
    });

    nextButton.addEventListener("click", () => {
        if (currentPage < totalPages) {
            currentPage++;
            renderTable(currentPage);
        }
    });

    renderTable(1); // Hiển thị trang đầu tiên

    // ==== Hiển thị/Ẩn form thêm ====
    const addProductBtn = document.querySelector(".add-product-btn");
    const addCategoryBox = document.getElementById("add-category-box");
    const discardBtn = document.querySelector(".discard-btn");

    addProductBtn.addEventListener("click", () => {
        addCategoryBox.classList.toggle("hidden");
    });

    discardBtn.addEventListener("click", () => {
        addCategoryBox.classList.add("hidden");
        document.getElementById("category-name").value = "";
        document.getElementById("brand-status").value = "true";
    });

    // ==== Thêm nhà sản xuất (gồm trạng thái) ====
    const addBtn = document.getElementById("add-category-btn");
    const nameInput = document.getElementById("category-name");

    addBtn.addEventListener("click", async () => {
        const name = nameInput.value.trim();

        if (!name) {
            alert("Vui lòng nhập tên nhà sản xuất!");
            return;
        }

        try {
            const response = await fetch(`${contextPath}/admin/api/brand`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name })
            });

            const result = await response.json();
            if (response.ok) {
                alert("Thêm thành công!");
                location.reload();
            } else {
                alert(result.message || "Thêm thất bại.");
            }
        } catch (err) {
            console.error("Lỗi khi thêm brand:", err);
            alert("Không thể kết nối đến máy chủ.");
        }
    });

    // ==== Cập nhật trạng thái brand (toggle) ====
    document.querySelectorAll(".toggle-icon").forEach(icon => {
        icon.addEventListener("click", () => {
            const brandId = icon.dataset.id;
            const isActive = icon.dataset.active === "true";

            fetch(`${contextPath}/admin/api/brand/${brandId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ id: brandId, isActive: !isActive })
            })
                .then(res => res.json())
                .then(data => {
                    if (data.status === "success" || data.success === true) {
                        const row = icon.closest("tr");
                        const statusEl = row.querySelector(".brand-status-toggle");

                        statusEl.classList.toggle("active", !isActive);
                        statusEl.classList.toggle("deactive", isActive);
                        statusEl.textContent = !isActive ? "Hoạt động" : "Không hoạt động";

                        icon.dataset.active = (!isActive).toString();
                        icon.querySelector("i").className = `fa-solid ${!isActive ? 'fa-trash' : 'fa-eye-slash'}`;
                    } else {
                        alert("Cập nhật trạng thái thất bại!");
                    }
                })
                .catch(err => {
                    console.error("Lỗi toggle trạng thái:", err);
                    alert("Lỗi kết nối!");
                });
        });
    });
});
