<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quản Lý Kho</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/adminPagination.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/inventory/inventory.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
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
      <h2>Quản Lý Kho Hàng</h2>
    </div>

    <div class="row">
      <div class="entries-dropdown">
        <label for="entries">Hiển thị</label>
        <select id="entries" name="entries"
                onchange="window.location.href='inventory?page=1&size='+this.value">
          <option value="10"  ${size == 10  ? 'selected' : ''}>10</option>
          <option value="25"  ${size == 25  ? 'selected' : ''}>25</option>
          <option value="50"  ${size == 50  ? 'selected' : ''}>50</option>
          <option value="100" ${size == 100 ? 'selected' : ''}>100</option>
        </select>
        mục
      </div>
    </div>

    <table class="inventory-table">
      <thead>
      <tr>
        <th style="width: 60px">ID</th>
        <th>Sản phẩm / Phiên bản</th>
        <th>Biến thể</th>
        <th>Giá bán</th>
        <th>Tồn kho</th>
        <th>Thao tác</th>
      </tr>
      </thead>
      <tbody id="inventoryTableBody">

      <c:forEach var="item" items="${inventoryList}">
        <tr class="product-group-row" data-product-id="${item.productId}">
          <td><strong>${item.productId}</strong></td>
          <td colspan="5">
            <c:if test="${not empty item.productImage}">
              <img src="${item.productImage}" class="product-img" alt="Ảnh sản phẩm">
            </c:if>
            <strong>${item.productName}</strong>
          </td>
        </tr>

        <c:forEach var="opt" items="${item.options}">
          <tr data-option-id="${opt.id}">
            <td></td>
            <td style="padding-left: 30px; color: #666;">Option #${opt.id}</td>
            <td>
              <c:choose>
                <c:when test="${not empty opt.variantName}">${opt.variantName}: ${opt.variantValue}</c:when>
                <c:otherwise><span style="color: #bbb;">—</span></c:otherwise>
              </c:choose>
            </td>
            <td><fmt:formatNumber value="${opt.price}" type="number" groupingUsed="true"/>đ</td>
            <td>
              <c:choose>
                <c:when test="${opt.stock == 0}"><span class="status out-of-stock">Hết hàng (0)</span></c:when>
                <c:when test="${opt.stock <= 10}"><span class="status low-stock">Sắp hết (${opt.stock})</span></c:when>
                <c:otherwise><span class="status in-stock">${opt.stock}</span></c:otherwise>
              </c:choose>
              <c:if test="${not empty opt.warehouseLocation}">
                <div style="font-size: 12px; color: #888; margin-top: 4px;">
                  <i class="fa-solid fa-warehouse"></i> ${opt.warehouseLocation}
                </div>
              </c:if>
            </td>
            <td>
              <div class="action-icons">
                <button class="btn-edit-stock"
                        onclick="openEditModal(${opt.id}, ${opt.stock}, '${item.productName} - Option #${opt.id}', '${opt.warehouseLocation}')">
                  <i class="fa-solid fa-pen-to-square" style="padding: 5px;"></i> Cập nhật
                </button>
              </div>
            </td>
          </tr>
        </c:forEach>
      </c:forEach>

      <c:if test="${empty inventoryList}">
        <tr>
          <td colspan="6" style="text-align: center; color: #999; padding: 30px;">
            Không có dữ liệu kho hàng.
          </td>
        </tr>
      </c:if>

      </tbody>
    </table>

    <div class="pagination">

      <c:choose>
        <c:when test="${currentPage > 1}">
          <a href="inventory?page=${currentPage - 1}&size=${size}">
            <button class="prev-btn">Trước</button>
          </a>
        </c:when>
        <c:otherwise>
          <button class="prev-btn" disabled>Trước</button>
        </c:otherwise>
      </c:choose>

      <c:if test="${currentPage > 3}">
        <a href="inventory?page=1&size=${size}">
          <button class="page-number">1</button>
        </a>
        <c:if test="${currentPage > 4}">
          <span class="page-dots">...</span>
        </c:if>
      </c:if>

      <c:forEach begin="1" end="${totalPages}" var="i">
        <c:if test="${i >= currentPage - 2 && i <= currentPage + 2}">
          <a href="inventory?page=${i}&size=${size}">
            <button class="page-number ${i == currentPage ? 'active' : ''}">${i}</button>
          </a>
        </c:if>
      </c:forEach>

      <c:if test="${currentPage < totalPages - 2}">
        <c:if test="${currentPage < totalPages - 3}">
          <span class="page-dots">...</span>
        </c:if>
        <a href="inventory?page=${totalPages}&size=${size}">
          <button class="page-number">${totalPages}</button>
        </a>
      </c:if>

      <c:choose>
        <c:when test="${currentPage < totalPages}">
          <a href="inventory?page=${currentPage + 1}&size=${size}">
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

<%-- Modal cập nhật kho --%>
<div class="modal-overlay" id="modalOverlay">
  <div class="modal-box">
    <h3 id="modalTitle">Cập nhật tồn kho</h3>
    <input type="hidden" id="modalOptionId">

    <div class="modal-field">
      <label>Số lượng tồn kho</label>
      <input type="number" id="modalQuantity" min="0" placeholder="Nhập số lượng...">
    </div>

    <div class="modal-field">
      <label>Vị trí kho (tuỳ chọn)</label>
      <input type="text" id="modalLocation" placeholder="VD: Kho HCM">
    </div>

    <div class="modal-field">
      <label>Ghi chú / Lý do</label>
      <input type="text" id="modalReason" placeholder="VD: Nhập hàng mới, Kiểm kho...">
    </div>

    <div class="modal-actions">
      <button class="discard-btn" onclick="closeModal()">Huỷ</button>
      <button class="add-btn" id="saveBtn" onclick="saveStock()" disabled>Lưu</button>
    </div>
  </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/global-toast.js?v=1"></script>
<script src="${pageContext.request.contextPath}/static/style-component/style-admin/inventory/inventory.js?v=2"></script>
</body>
</html>
