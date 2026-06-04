<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Voucher - Admin Dashboard</title>
    <link rel="stylesheet" href="../static/style-component/style-admin/vouchers/voucher.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <!-- Google Material Icons -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Outlined" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=add_2" />
</head>
<body>
<div class="wrap_header">
    <jsp:include page="header.jsp"/>
</div>
<div class="app-shell">
    <!-- Sidebar -->
    <div class="side_bar">
        <jsp:include page="/admin/sidebar.jsp"/>
    </div>
    <main class="main-content">
        <!-- Header -->
        <div class="content-container"> <!-- Stats Grid -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon bg-blue"><span class="material-icons-outlined">confirmation_number</span>
                    </div>
                    <div class="stat-details"><span class="label">Tổng Voucher</span>
                        <div class="value-row"><span class="value">${totalVoucher}</span> <span class="trend trend-up">+12%</span>
                        </div>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon bg-green"><span class="material-icons-outlined">check_circle</span></div>
                    <div class="stat-details"><span class="label">Đang hoạt động</span>
                        <div class="value-row"><span class="value">${activeVoucher}</span> <span class="trend trend-up">+5%</span>
                        </div>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon bg-orange"><span class="material-icons-outlined">history</span></div>
                    <div class="stat-details"><span class="label">Sắp hết hạn</span>
                        <div class="value-row"><span class="value">1</span>
                    </div>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon bg-red"><span class="material-icons-outlined">block</span></div>
                    <div class="stat-details"><span class="label">Đã hết hạn</span>
                        <div class="value-row"><span class="value">${expiredVoucher}</span> <span class="trend">0%</span></div>
                    </div>
                </div>
            </div> <!-- Filters & Search -->
            <div class="toolbar">
                <div class="search-container">
                    <span class="material-icons-outlined">search</span>
                    <input
                            type="text"
                            id="searchInput"
                            placeholder="Tìm kiếm voucher...">
                </div>
                <div class="action-buttons">
                    <button class="btn btn-secondary"><span class="material-icons-outlined">filter_list</span> Lọc dữ
                        liệu
                    </button>
                    <button class="btn btn-secondary"><span class="material-icons-outlined">file_download</span> Xuất
                        Excel
                    </button>
                </div>
            </div> <!-- Table -->
            <div class="data-table-card">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>MÃ VOUCHER</th>
                        <th>GIẢM GIÁ (%)</th>
                        <th>ĐƠN TỐI THIỂU</th>
                        <th>GIẢM TỐI ĐA (VND)</th>
                        <th>SỐ LƯỢNG</th>
                        <th>NGÀY BẮT ĐẦU</th>
                        <th>NGÀY KẾ THÚC</th>
                        <th>TRẠNG THÁI</th>
                        <th>THAO TÁC</th>
                    </tr>
                    </thead>
                    <tbody id="voucherTableBody">
                    <c:forEach items="${vouchers}" var="voucher">
                        <tr>
                            <td class="font-bold">${voucher.code}</td>

                            <td>${voucher.discountPercent}%</td>

                            <td><fmt:formatNumber value="${voucher.minOrderAmount}" pattern="#,###"/> VND</td>

                            <td><fmt:formatNumber value="${voucher.maxDiscount}" pattern="#,###"/> VND</td>

                            <td>${voucher.quantity}</td>

                            <td>${voucher.startDate.toString().replace('T',' ')}</td>

                            <td>${voucher.endDate.toString().replace('T',' ')}</td>

                            <td>

                            <c:choose>

                                <c:when test="${voucher.status == 'Active'}">
                                    <span class="badge badge-success">
                                        Hoạt động
                                    </span>
                                </c:when>

                                <c:when test="${voucher.status == 'Scheduled'}">
                                    <span class="badge badge-warning">
                                        Chưa bắt đầu
                                    </span>
                                </c:when>

                                <c:when test="${voucher.status == 'Expired'}">
                                    <span class="badge badge-danger">
                                        Hết hạn
                                    </span>
                                </c:when>

                                <c:when test="${voucher.status == 'Out of Stock'}">
                                    <span class="badge badge-secondary">
                                        Hết lượt
                                    </span>
                                </c:when>

                                <c:otherwise>
                                    <span class="badge badge-dark">
                                        Vô hiệu hóa
                                    </span>
                                </c:otherwise>

                            </c:choose>
                            </td>
                            <td>
                                <div class="row-actions">
                                    <a href="${pageContext.request.contextPath}/admin/vouchers/edit?id=${voucher.id}">
                                        <span class="material-icons-outlined">edit</span></a>
                                    <a class="delete-btn" href="${pageContext.request.contextPath}/admin/vouchers/delete?id=${voucher.id}">
                                        <span class="material-icons-outlined">delete</span>
                                    </a>
                                </div>
                            </td>
                        </tr>

                    </c:forEach>
                    </tbody>
                </table>
                <div class="table-footer"><span class="results-info">Hiển thị ${vouchers.size()} của ${totalVoucher} voucher</span>
                    <div class="pagination">
                        <button class="page-nav"><span class="material-icons-outlined">chevron_left</span></button>
                        <button class="page-num active">1</button>
<%--                        <button class="page-num">2</button>--%>
<%--                        <button class="page-num">3</button>--%>
                        <button class="page-nav"><span class="material-icons-outlined">chevron_right</span></button>
                    </div>
                </div>
            </div>
            <div class="toolbar_add">
                <div class="action-buttons">
                    <button class="btn btn-secondary" id="addVoucherBtn"><span class="material-symbols-outlined">add_2</span>
                        Thêm voucher
                    </button>
                </div>
            </div>
        </div>
    </main>
</div>
</body>
<script src="../static/style-component/style-admin/vouchers/voucher.js"></script>
</html>