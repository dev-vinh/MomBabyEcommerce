document.addEventListener("DOMContentLoaded", function () {
    const rows = document.querySelectorAll("tbody tr");
    const rowsPerPage = 8;
    const totalPages = Math.ceil(rows.length / rowsPerPage);
    const paginationLinks = document.querySelectorAll(".pagination .page-link");
    let currentPage = 1;

    function showPage(page) {
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        rows.forEach((row, index) => {
            row.style.display = (index >= start && index < end) ? "" : "none";
        });

        document.querySelector('.pagination .page-link[data-action="prev"]').parentElement.classList.toggle("disabled", page === 1);
        document.querySelector('.pagination .page-link[data-action="next"]').parentElement.classList.toggle("disabled", page === totalPages);

        paginationLinks.forEach(link => {
            link.classList.remove("active");
            if (!link.getAttribute('data-action') && parseInt(link.textContent) === page) {
                link.classList.add("active");
            }
        });
    }

    paginationLinks.forEach(link => {
        link.addEventListener("click", function (e) {
            e.preventDefault();
            const action = link.getAttribute('data-action');
            if (action === "prev" && currentPage > 1) currentPage--;
            else if (action === "next" && currentPage < totalPages) currentPage++;
            else if (!action) currentPage = parseInt(link.textContent);

            showPage(currentPage);
        });
    });

    showPage(1);
    document.querySelectorAll('.actions-btn').forEach(button => {
        button.addEventListener('click', function (e) {
            e.stopPropagation();
            const menu = this.nextElementSibling;
            menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
        });
    });

    document.addEventListener('click', function () {
        document.querySelectorAll('.actions-menu').forEach(menu => {
            menu.style.display = 'none';
        });
    });
    document.querySelector('.status-select').addEventListener('change', function () {
        const selected = this.value.toLowerCase();
        document.querySelectorAll('tbody tr').forEach(row => {
            const status = row.querySelector('.status')?.innerText.toLowerCase() || '';
            row.style.display = selected === "mặc định" || status.includes(selected) ? '' : 'none';
        });
    });
    document.querySelectorAll('.order-row').forEach(row => {
        row.addEventListener('click', function () {
            const url = this.getAttribute('data-url');
            if (url) window.location.href = url;
        });
    });

    const exportBtn = document.getElementById("exportBtn");
    if (exportBtn) {
        exportBtn.addEventListener("click", function (e) {
            e.preventDefault();
            try {
                if (!pageContextPath) throw new Error("pageContextPath is not defined");
                const exportUrl = `${pageContextPath}/admin/export-products`;

                this.disabled = true;
                const original = this.innerHTML;
                this.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xuất...';

                const form = document.createElement('form');
                form.method = 'GET';
                form.action = exportUrl;
                document.body.appendChild(form);
                form.submit();
                form.remove();

                setTimeout(() => {
                    this.disabled = false;
                    this.innerHTML = original;
                }, 2000);
            } catch (err) {
                alert("Xuất thất bại: " + err.message);
            }
        });
    }

    const searchInput = document.getElementById("order-search-input");
    const tbody = document.querySelector("tbody");
    const allRows = Array.from(document.querySelectorAll(".order-row"));

    searchInput.addEventListener("input", function () {
        const keyword = this.value.toLowerCase().trim();

        const matchedRows = allRows
            .map(row => {
                const id = row.querySelector(".order-id")?.innerText.toLowerCase() || "";
                const name = row.querySelector(".order-name")?.innerText.toLowerCase() || "";
                const date = row.querySelector(".order-date")?.innerText.toLowerCase() || "";
                const status = row.querySelector(".status")?.innerText.toLowerCase() || "";

                const fullText = `${id} ${name} ${date} ${status}`;
                const matched = fullText.includes(keyword);

                return { row, matched };
            });

        tbody.innerHTML = "";

        matchedRows.forEach(({ row, matched }) => {
            if (keyword === "" || matched) {
                row.style.display = "";
                tbody.appendChild(row);
            }
        });
    });
});
