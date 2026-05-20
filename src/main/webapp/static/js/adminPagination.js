function initPagination(options) {
    var tbodyId      = options.tbodyId      || 'product-table-body';
    var paginationId = options.paginationId || 'paginationContainer';
    var entriesId    = options.entriesId    || 'entries';
    var rowsPerPage  = options.rowsPerPage  || 10;

    var tbody      = document.getElementById(tbodyId);
    var pagination = document.getElementById(paginationId);
    var entriesSel = document.getElementById(entriesId);

    if (!tbody || !pagination) {
        console.warn('adminPagination: không tìm thấy tbody hoặc pagination container.');
        return;
    }

    var currentPage = 1;
    function getRows() {
        return Array.from(tbody.rows);
    }

    function showPage(page) {
        var rows = getRows();
        var total = Math.ceil(rows.length / rowsPerPage);

        if (page < 1) page = 1;
        if (page > total) page = total;
        currentPage = page;

        var start = (page - 1) * rowsPerPage;
        var end   = start + rowsPerPage;

        rows.forEach(function(row, index) {
            row.style.display = (index >= start && index < end) ? '' : 'none';
        });

        renderPageNumbers(total);
    }

    function renderPageNumbers(total) {
        pagination.innerHTML = '';

        var prevBtn = document.createElement('button');
        prevBtn.className = 'prev-btn';
        prevBtn.textContent = 'Trước';
        prevBtn.disabled = currentPage === 1;
        prevBtn.addEventListener('click', function() { showPage(currentPage - 1); });
        pagination.appendChild(prevBtn);

        var startPage = Math.max(1, currentPage - 2);
        var endPage   = Math.min(total, startPage + 4);
        if (endPage - startPage < 4) {
            startPage = Math.max(1, endPage - 4);
        }
        if (startPage > 1) {
            pagination.appendChild(createPageBtn(1, total));
            if (startPage > 2) {
                var dots = document.createElement('span');
                dots.textContent = '...';
                dots.className = 'page-dots';
                pagination.appendChild(dots);
            }
        }

        for (var i = startPage; i <= endPage; i++) {
            pagination.appendChild(createPageBtn(i, total));
        }

        if (endPage < total) {
            if (endPage < total - 1) {
                var dots2 = document.createElement('span');
                dots2.textContent = '...';
                dots2.className = 'page-dots';
                pagination.appendChild(dots2);
            }
            pagination.appendChild(createPageBtn(total, total));
        }

        var nextBtn = document.createElement('button');
        nextBtn.className = 'next-btn';
        nextBtn.textContent = 'Tiếp Theo';
        nextBtn.disabled = currentPage === total || total === 0;
        nextBtn.addEventListener('click', function() { showPage(currentPage + 1); });
        pagination.appendChild(nextBtn);
    }

    function createPageBtn(pageNum, total) {
        var btn = document.createElement('button');
        btn.className = 'page-number' + (pageNum === currentPage ? ' active' : '');
        btn.textContent = pageNum;
        btn.addEventListener('click', function() { showPage(pageNum); });
        return btn;
    }

    if (entriesSel) {
        entriesSel.addEventListener('change', function() {
            rowsPerPage = parseInt(this.value, 10);
            showPage(1);
        });
    }

    showPage(1);
    return {
        refresh: function() { showPage(1); },
        goTo:    function(page) { showPage(page); }
    };
}