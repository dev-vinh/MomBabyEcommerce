<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 16/03/2026
  Time: 3:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Category Management</title>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/static/style-component/style-admin/categories/categories.css">
  <script
          src="${pageContext.request.contextPath}/static/style-component/style-admin/categories/categories1.js"
          defer></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>

<body>

<div class="header">
  <jsp:include page="header.jsp" />
</div>

<div class="container">
  <div class="side_bar">
    <jsp:include page="/admin/sidebar.jsp" />
  </div>

  <div class="content">
    <div class="toolbar">

      <div id="add-category-box" class="hidden">
        <h3>Thêm Danh Mục</h3>
        <input type="text" id="category-name" name="categoryName" placeholder="Nhập tên danh mục"
               class="input-field" required/>
        <div id="error-message" class="error hidden">Tên danh mục không được để trống</div>
        <div class="action-buttons">
          <button class="add-btn add-cate-btn" id="add-category-btn">Thêm</button>
          <button class="discard-btn" id="discard-category-btn">Hủy</button>
        </div>
      </div>

      <button class="add-product-btn">+ Thêm</button>
    </div>
    <div class="table-wrapper">
      <table class="product-table">
        <thead>
        <tr>
          <th data-sort="string" onclick="sortTable(0)">
            <div class="header-content">
              <span class="header-text">ID</span>
              <span class="sort-arrows">
                        <span class="sort-arrow asc">▲</span>
                        <span class="sort-arrow desc">▼</span>
                    </span>
            </div>
          </th>
          <th data-sort="string" onclick="sortTable(1)">
            <div class="header-content">
              <span class="header-text">Tên Danh Mục</span>
              <span class="sort-arrows">
                        <span class="sort-arrow asc">▲</span>
                        <span class="sort-arrow desc">▼</span>
                    </span>
            </div>
          </th>
          <th data-sort="string" onclick="sortTable(2)">
            <div class="header-content">
              <span class="header-text">Tổng Sản Phẩm</span>
              <span class="sort-arrows">
                        <span class="sort-arrow asc">▲</span>
                        <span class="sort-arrow desc">▼</span>
                    </span>
            </div>
          </th>
          <th data-sort="string" onclick="sortTable(3)">
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
        <!-- Hiển thị danh mục -->
        <c:if test="${empty categoriesWithStock}">
          <tr>
            <td colspan="4">Không có danh mục nào được tìm thấy.</td>
          </tr>
        </c:if>
        <c:if test="${not empty categoriesWithStock}">
          <c:forEach items="${categoriesWithStock}" var="category">
            <tr>
              <td>${category.id}</td>

              <td>
                <div class="product">
                  <p>${category.name}</p>
                </div>
              </td>
              <td>${category.totalStock}</td>
              <td>
                <div class="status category-status-toggle ${category.isActive ? 'active' : 'deactive'}"
                     data-id="${category.id}">
                    ${category.isActive ? 'Hoạt động' : 'Không hoạt động'}
                </div>
              </td>
              <td >
                <div class="action-icons">
                                        <span class="icon toggle-icon" data-id="${category.id}" data-active="${category.isActive}">
                                            <i class="fa-solid ${category.isActive ? 'fa-trash' : 'fa-eye-slash'}" style="padding: 5px;"></i>
                                        </span>
                </div>
              </td>


            </tr>
          </c:forEach>
        </c:if>
        </tbody>
      </table>
    </div>
    <div class="pagination">
      <button class="prev-btn">Trước</button>
      <button class="page-number active">1</button>
      <button class="page-number">2</button>
      <button class="next-btn">Tiếp Theo</button>
    </div>
  </div>


</div>
</body>
<script>
  const contextPath = "${pageContext.request.contextPath}";
</script>
</html>
