<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Lịch Sử Kho</title>
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
      <h2>Lịch Sử Nhập / Xuất Kho</h2>
    </div>

    <form method="get" action="inventory-log" style="margin-bottom: 16px;">
      <div style="display:flex;gap:10px;flex-wrap:wrap;align-items:center;">

        <div class="entries-dropdown">
          <label for="productId">Sản phẩm</label>
          <input type="number" id="productId" name="productId"
                 value="${filterProductId}" placeholder="ID sản phẩm"
                 style="padding:6px 10px;border:1px solid #ddd;border-radius:6px;width:120px;">
        </div>

        <div class="entries-dropdown">
          <label for="actionType">Loại thao tác</label>
          <select id="actionType" name="actionType" style="padding:6px 10px;border:1px solid #ddd;border-radius:6px;">
            <option value="">-- Tất cả --</option>
            <option value="IMPORT" ${filterActionType == 'IMPORT' ? 'selected' : ''}>Nhập kho</option>
            <option value="EXPORT" ${filterActionType == 'EXPORT' ? 'selected' : ''}>Xuất kho</option>
            <option value="ADJUSTMENT" ${filterActionType == 'ADJUSTMENT' ? 'selected' : ''}>Điều chỉnh</option>
          </select>
        </div>

        <div class="entries-dropdown">
          <label for="fromDate">Từ ngày</label>
          <input type="date" id="fromDate" name="fromDate"
                 value="${filterFromDate}"
                 style="padding:6px 10px;border:1px solid #ddd;border-radius:6px;">
        </div>

        <div class="entries-dropdown">
          <label for="toDate">Đến ngày</label>
          <input type="date" id="toDate" name="toDate"
                 value="${filterToDate}"
                 style="padding:6px 10px;border:1px solid #ddd;border-radius:6px;">
        </div>

        <button type="submit" style="padding:7px 16px;background:#0d6efd;color:#fff;border:none;border-radius:6px;cursor:pointer;">
          <i class="fa-solid fa-magnifying-glass"></i> Lọc
        </button>
        <a href="inventory-log" style="padding:7px 16px;background:#6c757d;color:#fff;border:none;border-radius:6px;text-decoration:none;display:inline-block;">
          Xoá lọc
        </a>
      </div>
    </form>

    <div class="row">
      <div class="entries-dropdown">
        <label for="entries">Hiển thị</label>
        <select id="entries" name="entries"
                onchange="window.location.href='inventory-log?page=1&size='+this.value+'&productId=${filterProductId}&actionType=${filterActionType}&fromDate=${filterFromDate}&toDate=${filterToDate}'">
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
        <th>Thời gian</th>
        <th>Sản phẩm</th>
        <th>Biến thể</th>
        <th>Loại</th>
        <th>SL thay đổi</th>
        <th>Tồn trước</th>
        <th>Tồn sau</th>
        <th>Lý do</th>
        <th>Người thực hiện</th>
      </tr>
      </thead>
      <tbody>

      <c:choose>
        <c:when test="${empty logs}">
          <tr>
            <td colspan="9" style="text-align: center; color: #999; padding: 30px;">
              Không có bản ghi nào.
            </td>
          </tr>
        </c:when>
        <c:otherwise>
          <c:forEach var="log" items="${logs}">
            <tr>
              <td>
                ${log.createdAt}
              </td>
              <td>
                <strong>${log.productName}</strong>
                <c:if test="${not empty log.productId}">
                  <br><span style="font-size:12px;color:#888;">#${log.productId}</span>
                </c:if>
              </td>
              <td style="color:#666;">
                <c:choose>
                  <c:when test="${not empty log.variantLabel && log.variantLabel != ': '}">${log.variantLabel}</c:when>
                  <c:otherwise><span style="color:#bbb;">—</span></c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${log.actionType == 'IMPORT'}">
                    <span style="color:#198754;font-weight:bold;">
                      <i class="fa-solid fa-arrow-down"></i> Nhập
                    </span>
                  </c:when>
                  <c:when test="${log.actionType == 'EXPORT'}">
                    <span style="color:#dc3545;font-weight:bold;">
                      <i class="fa-solid fa-arrow-up"></i> Xuất
                    </span>
                  </c:when>
                  <c:otherwise>
                    <span style="color:#0d6efd;font-weight:bold;">
                      <i class="fa-solid fa-pen"></i> Điều chỉnh
                    </span>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${log.quantityChange > 0}">
                    <span style="color:#198754;">+${log.quantityChange}</span>
                  </c:when>
                  <c:when test="${log.quantityChange < 0}">
                    <span style="color:#dc3545;">${log.quantityChange}</span>
                  </c:when>
                  <c:otherwise>
                    <span style="color:#888;">0</span>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>${log.stockBefore}</td>
              <td>${log.stockAfter}</td>
              <td style="max-width:150px;">
                <c:choose>
                  <c:when test="${not empty log.reason}">
                    <span title="${log.reason}">${log.reason}</span>
                  </c:when>
                  <c:otherwise><span style="color:#bbb;">—</span></c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:choose>
                  <c:when test="${not empty log.userName}">${log.userName}</c:when>
                  <c:otherwise><span style="color:#bbb;">Hệ thống</span></c:otherwise>
                </c:choose>
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
          <a href="inventory-log?page=${currentPage - 1}&size=${size}&productId=${filterProductId}&actionType=${filterActionType}&fromDate=${filterFromDate}&toDate=${filterToDate}">
            <button class="prev-btn">Trước</button>
          </a>
        </c:when>
        <c:otherwise>
          <button class="prev-btn" disabled>Trước</button>
        </c:otherwise>
      </c:choose>

      <c:if test="${currentPage > 3}">
        <a href="inventory-log?page=1&size=${size}&productId=${filterProductId}&actionType=${filterActionType}&fromDate=${filterFromDate}&toDate=${filterToDate}">
          <button class="page-number">1</button>
        </a>
        <c:if test="${currentPage > 4}">
          <span class="page-dots">...</span>
        </c:if>
      </c:if>

      <c:forEach begin="1" end="${totalPages}" var="i">
        <c:if test="${i >= currentPage - 2 && i <= currentPage + 2}">
          <a href="inventory-log?page=${i}&size=${size}&productId=${filterProductId}&actionType=${filterActionType}&fromDate=${filterFromDate}&toDate=${filterToDate}">
            <button class="page-number ${i == currentPage ? 'active' : ''}">${i}</button>
          </a>
        </c:if>
      </c:forEach>

      <c:if test="${currentPage < totalPages - 2}">
        <c:if test="${currentPage < totalPages - 3}">
          <span class="page-dots">...</span>
        </c:if>
        <a href="inventory-log?page=${totalPages}&size=${size}&productId=${filterProductId}&actionType=${filterActionType}&fromDate=${filterFromDate}&toDate=${filterToDate}">
          <button class="page-number">${totalPages}</button>
        </a>
      </c:if>

      <c:choose>
        <c:when test="${currentPage < totalPages}">
          <a href="inventory-log?page=${currentPage + 1}&size=${size}&productId=${filterProductId}&actionType=${filterActionType}&fromDate=${filterFromDate}&toDate=${filterToDate}">
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

</body>
</html>
