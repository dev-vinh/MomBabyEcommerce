<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 14/03/2026
  Time: 4:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Sản Phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/list_product/listProduct.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/adminPagination.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
    <script src="${pageContext.request.contextPath}/static/style-component/style-admin/list_product/listProduct.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
</head>
<body>

<div class="header">
    <jsp:include page="header.jsp"/>
</div>

<div class="container">
    <div class="side_bar">
        <jsp:include page="sidebar.jsp"/>
    </div>

    <div class="content">
        <div class="toolbar">
            <button class="add-product-btn" onclick="window.location.href='add-product'">+ Thêm Sản Phẩm</button>
        </div>

        <form method="GET" action="${pageContext.request.contextPath}/admin/list-product" id="filterForm" style="margin-bottom:16px;">
            <div style="display:flex; gap:12px; flex-wrap:wrap; align-items:center;">

                <div style="position:relative; flex:1; min-width:200px;">
                    <input type="text" name="keyword" id="searchInput" placeholder="Tìm theo tên hoặc SKU..."
                           value="${fn:escapeXml(filterKeyword)}"
                           style="width:100%; padding:8px 12px 8px 36px; border:1px solid #ddd; border-radius:6px; font-size:14px; box-sizing:border-box;">
                    <i class="fa-solid fa-search"
                       style="position:absolute; left:12px; top:50%; transform:translateY(-50%); color:#aaa; font-size:14px;"></i>
                </div>

                <select name="categoryId" style="padding:8px 12px; border:1px solid #ddd; border-radius:6px; font-size:14px; min-width:160px;">
                    <option value="">Tất cả Danh mục</option>
                    <c:forEach items="${categories}" var="cat">
                        <option value="${cat.id}" ${filterCategoryId == cat.id ? 'selected' : ''}>${cat.name}</option>
                    </c:forEach>
                </select>

                <select name="status" style="padding:8px 12px; border:1px solid #ddd; border-radius:6px; font-size:14px; min-width:150px;">
                    <option value="">Tất cả Trạng thái</option>
                    <option value="active" ${filterStatus == 'active' ? 'selected' : ''}>Hoạt Động</option>
                    <option value="inactive" ${filterStatus == 'inactive' ? 'selected' : ''}>Không Hoạt Động</option>
                </select>

                <button type="submit" style="padding:8px 18px; background:#6c5ce7; color:#fff; border:none; border-radius:6px; font-size:14px; cursor:pointer;">
                    <i class="fa-solid fa-filter"></i> Lọc
                </button>
                <a href="${pageContext.request.contextPath}/admin/list-product"
                   style="padding:8px 16px; border:1px solid #ddd; border-radius:6px; font-size:14px; color:#666; text-decoration:none;">
                    <i class="fa-solid fa-xmark"></i> Xoá lọc
                </a>
            </div>
        </form>

        <div class="row">
            <div class="entries-dropdown">
                <label for="entries">Hiển thị</label>
                <select id="entries" name="entries"
                        onchange="submitFilterForm(this.value)">
                    <option value="10"  ${size == 10  ? 'selected' : ''}>10</option>
                    <option value="25"  ${size == 25  ? 'selected' : ''}>25</option>
                    <option value="50"  ${size == 50  ? 'selected' : ''}>50</option>
                    <option value="100" ${size == 100 ? 'selected' : ''}>100</option>
                </select>
                mục
            </div>
        </div>

        <table class="product-table">
            <thead>
            <tr>
                <th><label><input type="checkbox"></label></th>
                <th data-sort="string">
                    <div class="header-content">
                        <span class="header-text">Sản Phẩm</span>
                        <span class="sort-arrows">
              <span class="sort-arrow asc">▲</span>
              <span class="sort-arrow desc">▼</span>
            </span>
                    </div>
                </th>
                <th data-sort="string">
                    <div class="header-content">
                        <span class="header-text">Danh Mục</span>
                        <span class="sort-arrows">
              <span class="sort-arrow asc">▲</span>
              <span class="sort-arrow desc">▼</span>
            </span>
                    </div>
                </th>
                <th data-sort="number">
                    <div class="header-content">
                        <span class="header-text">Giá</span>
                        <span class="sort-arrows">
              <span class="sort-arrow asc">▲</span>
              <span class="sort-arrow desc">▼</span>
            </span>
                    </div>
                </th>
                <th data-sort="number">
                    <div class="header-content">
                        <span class="header-text">Số Lượng</span>
                        <span class="sort-arrows">
              <span class="sort-arrow asc">▲</span>
              <span class="sort-arrow desc">▼</span>
            </span>
                    </div>
                </th>
                <th data-sort="string">
                    <div class="header-content">
                        <span class="header-text">Trạng Thái</span>
                        <span class="sort-arrows">
              <span class="sort-arrow asc">▲</span>
              <span class="sort-arrow desc">▼</span>
            </span>
                    </div>
                </th>
                <th>Thao Tác</th>
            </tr>
            </thead>

            <tbody id="product-table-body">
            <c:choose>
                <c:when test="${empty products}">
                    <tr>
                        <td colspan="7" style="text-align: center; color: #999; padding: 30px;">
                            Không có sản phẩm nào.
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${products}" var="p">
                        <tr data-product-id="${p.id}">
                            <td><label><input type="checkbox" class="checkbox"></label></td>
                            <td>
                                <div class="product">
                                    <img src="${p.imageUrl}" alt="${p.name}" class="product-img">
                                    <p>${p.name}</p>
                                </div>
                            </td>
                            <td>${p.categoryName}</td>
                            <td><fmt:formatNumber value="${p.price}" pattern="#,###"/> VND</td>
                            <td>${p.stock}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.active}">
                                        <span class="status active">Hoạt Động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status deactive">Không Hoạt Động</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="action-icons">
                                    <div class="dropdown">
                                        <button onclick="toggleDropdown(this)">
                                            <i class="fa-solid fa-pen-to-square icon-xs" style="padding: 5px;"></i>
                                            <i class="fa-solid fa-chevron-down" style="padding: 5px;"></i>
                                        </button>
                                        <div class="dropdown-content">
                        <span class="icon edit-icon">
                          <a href="${pageContext.request.contextPath}/admin/add-product?id=${p.id}">
                            <i class="fa-solid fa-pen-to-square" style="padding: 5px;"></i> Chỉnh sửa
                          </a>
                        </span>
                                            <span class="icon delete-icon" data-product-id="${p.id}">
                          <i class="fa-solid fa-trash" style="padding: 5px;"></i> Xóa
                        </span>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>

        <div class="pagination">

            <c:choose>
                <c:when test="${currentPage > 1}">
                    <a href="list-product?page=${currentPage - 1}&size=${size}${filterQs}">
                        <button class="prev-btn">Trước</button>
                    </a>
                </c:when>
                <c:otherwise>
                    <button class="prev-btn" disabled>Trước</button>
                </c:otherwise>
            </c:choose>

            <c:if test="${currentPage > 3}">
                <a href="list-product?page=1&size=${size}${filterQs}">
                    <button class="page-number">1</button>
                </a>
                <c:if test="${currentPage > 4}">
                    <span class="page-dots">...</span>
                </c:if>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:if test="${i >= currentPage - 2 && i <= currentPage + 2}">
                    <a href="list-product?page=${i}&size=${size}${filterQs}">
                        <button class="page-number ${i == currentPage ? 'active' : ''}">${i}</button>
                    </a>
                </c:if>
            </c:forEach>

            <c:if test="${currentPage < totalPages - 2}">
                <c:if test="${currentPage < totalPages - 3}">
                    <span class="page-dots">...</span>
                </c:if>
                <a href="list-product?page=${totalPages}&size=${size}${filterQs}">
                    <button class="page-number">${totalPages}</button>
                </a>
            </c:if>

            <c:choose>
                <c:when test="${currentPage < totalPages}">
                    <a href="list-product?page=${currentPage + 1}&size=${size}${filterQs}">
                        <button class="next-btn">Tiếp Theo</button>
                    </a>
                </c:when>
                <c:otherwise>
                    <button class="next-btn" disabled>Tiếp Theo</button>
                </c:otherwise>
            </c:choose>

        </div>
        <div style="text-align: right; font-size: 13px; color: #999; margin-top: 8px;">
            Trang ${currentPage} / ${totalPages}
        </div>

    </div>
</div>

<!-- Confirm Delete Modal -->
<div id="confirmDeleteModal" class="modal-overlay" style="display:none; position:fixed; top:0; left:0; right:0; bottom:0; background:rgba(0,0,0,0.5); z-index:9999; justify-content:center; align-items:center;">
    <div style="background:#fff; border-radius:12px; padding:28px 32px; max-width:400px; width:90%; box-shadow:0 8px 24px rgba(0,0,0,0.15); text-align:center;">
        <div style="width:56px; height:56px; border-radius:50%; background:#fff3f3; margin:0 auto 16px; display:flex; align-items:center; justify-content:center;">
            <i class="fa-solid fa-trash" style="font-size:24px; color:#e74c3c;"></i>
        </div>
        <h3 style="margin:0 0 8px; font-size:18px; color:#333;">Xác nhận xóa sản phẩm</h3>
        <p id="confirmDeleteMsg" style="margin:0 0 24px; font-size:14px; color:#666; line-height:1.6;"></p>
        <div style="display:flex; gap:12px; justify-content:center;">
            <button id="cancelDeleteBtn" style="padding:10px 24px; border:1px solid #ddd; border-radius:6px; background:#fff; color:#333; cursor:pointer; font-size:14px; min-width:100px;">Huỷ bỏ</button>
            <button id="confirmDeleteBtn" style="padding:10px 24px; border:none; border-radius:6px; background:#e74c3c; color:#fff; cursor:pointer; font-size:14px; min-width:100px;">Xóa</button>
        </div>
    </div>
</div>

</body>
</html>
