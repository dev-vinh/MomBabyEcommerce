<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 16/03/2026
  Time: 11:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Brand Management</title>
    <script> const contextPath = "${pageContext.request.contextPath}"; </script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/brand/brands.css">
    
    <script src="${pageContext.request.contextPath}/static/style-component/style-admin/brand/brands.js" defer></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
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
            <div id="add-category-box" class="hidden">
                <h3>Thêm Danh Mục</h3>
                <input type="text" id="category-name" name="categoryName" placeholder="Nhập tên nhà sản xuất"
                       class="input-field brand-input-field " required/>
                <div id="error-message" class="error hidden">Tên nhà sản xuất không được để trống</div>
                <div class="action-buttons">
                    <button class="add-btn add-brand-btn" id="add-category-btn">Thêm</button>
                    <button class="discard-btn" id="discard-category-btn">Hủy</button>
                </div>
            </div>

            <button class="add-product-btn">+ Thêm</button>
        </div>
        <%--            // mới --%>
        <div class="row">
            <div class="entries-dropdown">
                <label for="entries">Hiển thị</label>
                <select id="entries" name="entries">
                    <option value="10">10</option>
                    <option value="25">25</option>
                    <option value="50">50</option>
                    <option value="100">100</option>
                </select>
                mục
            </div>

        </div>
        <table class="product-table">
            <thead>
            <tr>
                <th><label>
                    <input type="checkbox">
                </label></th>
                <th data-sort="string" onclick="sortTable(1)">
                    <div class="header-content">
                        <span class="header-text">Tên nhà sản xuất</span>
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
            <c:if test="${empty brands}">
                <tr>
                    <td colspan="4">Không có nhà sản xuất nào được tìm thấy.</td>
                </tr>
            </c:if>
            <c:if test="${not empty brands}">
                <c:forEach items="${brands}" var="b">
                    <tr>
                        <td>
                            <label><input type="checkbox" class="checkbox"></label>
                        </td>
                        <td>
                            <div class="product">
                                <p>${b.name}</p>
                            </div>
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
                                            <i class="fa-solid fa-pencil-alt" style="padding: 5px;"></i>
                                            Sửa
                                        </span>

                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>

        <div class="pagination page-numbers">
            <button class="prev-btn">Trước</button>
            <button class="page-number active">1</button>
            <button class="page-number">2</button>
            <button class="next-btn">Tiếp Theo</button>
        </div>

    </div>

</div>
</body>
</html>

