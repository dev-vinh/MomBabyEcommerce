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
    const pageButtons = document.querySelectorAll(".page-number");
    const prevButton = document.querySelector(".prev-btn");
    const nextButton = document.querySelector(".next-btn");
    const tableBody = document.getElementById("product-table-body");
    const rowsPerPage = 10;
    const rows = Array.from(tableBody.rows);
    const totalPages = Math.ceil(rows.length / rowsPerPage);
    let currentPage = 1;

    function renderTable(page) {
        const start = (page - 1) * rowsPerPage;
        const end = page * rowsPerPage;

        rows.forEach((row, index) => {
            row.style.display = index >= start && index < end ? "table-row" : "none";
        });

        pageButtons.forEach((btn, index) => {
            btn.classList.toggle("active", index + 1 === page);
        });

        prevButton.disabled = page === 1;
        nextButton.disabled = page === totalPages;
    }

    pageButtons.forEach((button, index) => {
        button.addEventListener("click", () => {
            currentPage = index + 1;
            renderTable(currentPage);
        });
    });

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
    });

    // ==== Thêm danh mục ====
    const addCateBtn = document.querySelector(".add-cate-btn");
    const inputField = document.querySelector(".input-field");

    addCateBtn.addEventListener("click", async () => {
        const categoryName = inputField.value.trim();

        if (!categoryName) {
            alert("Vui lòng nhập tên danh mục!");
            return;
        }

        try {
            const response = await fetch('add-category', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name: categoryName }),
                redirect: "manual"
            }).then(res => {
                console.log("Status:", res.status);
                if (res.status === 0) {
                    alert("Your session may have expired. Redirecting...");
                    window.location.href = "login"
                    return Promise.reject("Session expired");
                }

                return res.json();
            }).then(data => {
                console.log(data);
                if (data && data.success) {
                    currentRole = newRole;
                    select.setAttribute("data-role-current", newRole);
                    alert("Success!");
                }
            });


        } catch (error) {
            console.error(error);
        }
    });

    // ==== Cập nhật trạng thái danh mục (toggle) ====
    document.querySelectorAll(".toggle-icon").forEach(icon => {
        icon.addEventListener("click", () => {
            const categoryId = icon.dataset.id;
            const isActive = icon.dataset.active === 'true';

            fetch(`${contextPath}/admin/api/categories/${categoryId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ isActive: !isActive })
            })
                .then(res => res.json())
                .then(data => {
                    if (data.status === "success") {
                        const row = icon.closest("tr");
                        const statusEl = row.querySelector(".category-status-toggle");

                        // Cập nhật trạng thái chữ và class
                        statusEl.classList.toggle("active", !isActive);
                        statusEl.classList.toggle("deactive", isActive);
                        statusEl.textContent = !isActive ? "Hoạt động" : "Không hoạt động";

                        // Cập nhật biểu tượng
                        const iconEl = icon.querySelector("i");
                        iconEl.className = `fa-solid ${!isActive ? 'fa-trash' : 'fa-eye-slash'}`;

                        // Cập nhật thuộc tính data-active
                        icon.dataset.active = (!isActive).toString();
                    } else {
                        alert("Cập nhật trạng thái thất bại!");
                    }
                })
                .catch(err => {
                    console.error("Lỗi khi cập nhật trạng thái category:", err);
                });
        });
    });


});

