<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 20/05/2026
  Time: 9:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quản Lý Kho</title>
  <script> const contextPath = "${pageContext.request.contextPath}"; </script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/inventory/inventory.css">
  <script src="${pageContext.request.contextPath}/static/style-component/style-admin/inventory/inventory.js" defer></script>
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
      <input type="text" id="searchInput" class="search-bar"
             placeholder="Tìm kiếm sản phẩm..."
             onkeyup="searchInventory()">
    </div>

    <table class="inventory-table">
      <thead>
      <tr>
        <th style="width: 40px">#</th>
        <th>Sản phẩm / Phiên bản</th>
        <th>Biến thể</th>
        <th>Giá bán</th>
        <th>Tồn kho</th>
        <th>Thao tác</th>
      </tr>
      </thead>
      <tbody id="inventoryTableBody">

      <c:set var="stt" value="0"/>
      <c:forEach var="item" items="${inventoryList}">
        <c:set var="stt" value="${stt + 1}"/>

        <%-- Dòng tên sản phẩm --%>
        <tr class="product-group-row">
          <td>${stt}</td>
          <td colspan="4">
            <c:if test="${not empty item.productImage}">
              <img src="${item.productImage}" class="product-img" alt="Ảnh sản phẩm">
            </c:if>
            <strong>${item.productName}</strong>
          </td>
          <td></td>
        </tr>

        <%-- Dòng từng option --%>
        <c:forEach var="opt" items="${item.options}">
          <tr>
            <td></td>
            <td style="padding-left: 30px; color: #666;">
              Option #${opt.id}
            </td>
            <td>
              <c:choose>
                <c:when test="${not empty opt.variantName}">
                  ${opt.variantName}: ${opt.variantValue}
                </c:when>
                <c:otherwise>
                  <span style="color: #bbb;">—</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td>
              <fmt:formatNumber value="${opt.price}" type="number" groupingUsed="true"/>đ
            </td>
            <td>
              <c:choose>
                <c:when test="${opt.stock == 0}">
                  <span class="status out-of-stock">Hết hàng (0)</span>
                </c:when>
                <c:when test="${opt.stock <= 10}">
                  <span class="status low-stock">Sắp hết (${opt.stock})</span>
                </c:when>
                <c:otherwise>
                  <span class="status in-stock">${opt.stock}</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td>
              <div class="action-icons">
                <button class="btn-edit-stock"
                        onclick="openEditModal(${opt.id}, ${opt.stock}, '${item.productName} - Option #${opt.id}')">
                  <i class="fa-solid fa-pen-to-square" style="padding: 5px;"></i> Cập nhật
                </button>
              </div>
            </td>
          </tr>
        </c:forEach>

      </c:forEach>

      </tbody>
    </table>

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

    <div class="modal-actions">
      <button class="discard-btn" onclick="closeModal()">Huỷ</button>
      <button class="add-btn" onclick="saveStock()">Lưu</button>
    </div>
  </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/global-toast.js?v=1"></script>
<script src="${pageContext.request.contextPath}/static/style-component/style-admin/inventory/inventory.js"></script>
</body>
</html>
