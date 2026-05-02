document.addEventListener("DOMContentLoaded", function () {
    const rows = document.querySelectorAll("tbody tr");
    const rowsPerPage = 8;
    const totalPages = Math.ceil(rows.length / rowsPerPage);
    const paginationContainer = document.getElementById("pagination");
    let currentPage = 1;

    function renderPagination() {
        paginationContainer.innerHTML = "";

        const prev = document.createElement("li");
        prev.className = "page-item";
        prev.innerHTML = `<a class="page-link" href="#" data-action="prev">Quay lại</a>`;
        paginationContainer.appendChild(prev);

        for (let i = 1; i <= totalPages; i++) {
            const pageItem = document.createElement("li");
            pageItem.className = `page-item ${i === 1 ? "active" : ""}`;
            pageItem.innerHTML = `<a class="page-link" href="#">${i}</a>`;
            paginationContainer.appendChild(pageItem);
        }

        const next = document.createElement("li");
        next.className = "page-item";
        next.innerHTML = `<a class="page-link" href="#" data-action="next">Tiếp theo</a>`;
        paginationContainer.appendChild(next);
    }

    function showPage(page) {
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        rows.forEach((row, index) => {
            row.style.display = (index >= start && index < end) ? "" : "none";
        });

        const pageItems = paginationContainer.querySelectorAll("li");
        pageItems.forEach(item => item.classList.remove("disabled", "active"));

        if (page === 1) pageItems[0].classList.add("disabled");
        if (page === totalPages) pageItems[pageItems.length - 1].classList.add("disabled");

        if (page >= 1 && page <= totalPages) {
            pageItems[page].classList.add("active");
        }

        currentPage = page;
    }

    paginationContainer.addEventListener("click", function (e) {
        e.preventDefault();
        if (!e.target.classList.contains("page-link")) return;

        const action = e.target.getAttribute("data-action");
        const pageItems = paginationContainer.querySelectorAll("li");

        if (action === "prev" && currentPage > 1) {
            showPage(currentPage - 1);
        } else if (action === "next" && currentPage < totalPages) {
            showPage(currentPage + 1);
        } else if (!action) {
            const page = parseInt(e.target.textContent);
            if (!isNaN(page)) showPage(page);
        }
    });

    renderPagination();
    showPage(1);
});
