<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 4/30/2026
  Time: 9:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/banner/banner.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <title>Quản lý Banner</title>
    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>
</head>
<body>
<div class="container">
    <div class="left">
        <div class="side_bar">
            <jsp:include page="sidebar.jsp"/>
        </div>
    </div>
    <div class="center">
        <div class="wrap_header">
            <jsp:include page="header.jsp"/>
        </div>

        <div class="content">
            <div class="toolbar">
                <button class="add-banner-btn">+ Thêm Banner</button>
            </div>
            <div id="overlay" class="hidden"></div>

            <div id="add-banner-box" class="hidden">
                <h3>Thêm Banner</h3>
                <select id="banner-status" class="input-field">
                    <option value="ACTIVE">Hoạt động</option>
                    <option value="INACTIVE">Ngưng</option>
                </select>

                <div class="form-group">
                    <label for="banner-imageFile" class="custom-file-label">
                        <i class="fa-solid fa-upload"></i> Chọn ảnh banner
                    </label>
                    <input type="file" id="banner-imageFile"
                           accept="image/png, image/jpeg"
                           class="input-field"
                           style="display:none" />

                    <div id="image-preview-wrapper" style="display:none; margin-top: 10px;">
                        <img id="banner-image-preview"
                             class="preview-image"
                             src=""
                             alt="Ảnh preview"
                             style="max-width:120px; border-radius:8px;" />
                    </div>
                </div>

                <input type="date" id="banner-startDate" class="input-field">
                <input type="date" id="banner-endDate" class="input-field">

                <div class="action-buttons">
                    <button id="submit-banner-btn" class="add-btn">Thêm</button>
                    <button id="discard-banner-btn" class="discard-btn">Hủy</button>
                </div>
            </div>
            <div class="table-wrapper">
                <table class="product-table">
                    <thead>
                    <tr>
                        <th onclick="sortTable(0)">
                            <div class="header-content">
                                <span class="header-text">ID</span>
                                <span class="sort-arrows">
                                    <span class="sort-arrow asc">▲</span>
                                    <span class="sort-arrow desc">▼</span>
                                 </span>
                            </div>
                        </th>
                        <th>Ảnh</th>
                        <th onclick="sortTable(2)">
                            <div class="header-content">
                                <span class="header-text">Tên banner</span>
                                <span class="sort-arrows">
                                    <span class="sort-arrow asc">▲</span>
                                    <span class="sort-arrow desc">▼</span>
                                </span>
                            </div>
                        </th>
                        <th onclick="sortTable(3)">
                            <div class="header-content">
                                <span class="header-text">Mô tả</span>
                                <span class="sort-arrows">
                                    <span class="sort-arrow asc">▲</span>
                                    <span class="sort-arrow desc">▼</span>
                                </span>
                            </div>
                        </th>
                        <th onclick="sortTable(4)">
                            <div class="header-content">
                                <span class="header-text">Ngày bắt đầu</span>
                                <span class="sort-arrows">
                                    <span class="sort-arrow asc">▲</span>
                                    <span class="sort-arrow desc">▼</span>
                                </span>
                            </div>
                        </th>
                        <th onclick="sortTable(5)">
                            <div class="header-content">
                                <span class="header-text">Ngày kết thúc</span>
                                <span class="sort-arrows">
                                    <span class="sort-arrow asc">▲</span>
                                    <span class="sort-arrow desc">▼</span>
                                </span>
                            </div>
                        </th>
                        <th onclick="sortTable(6)">
                            <div class="header-content">
                                <span class="header-text">Trạng thái</span>
                                <span class="sort-arrows">
                                    <span class="sort-arrow asc">▲</span>
                                    <span class="sort-arrow desc">▼</span>
                                </span>
                            </div>
                        </th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody id="banner-table-body">
                    <c:if test="${empty banners}">
                        <tr>
                            <td colspan="8">Không có banner nào được tìm thấy.</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty banners}">
                        <c:forEach items="${banners}" var="b">
                            <tr>
                                <td>${b.id}</td>
                                <td>
                                    <img src="${imageMap[b.imageId]}" alt="Banner" class="banner-image" style="width: 100px; border-radius: 8px;"/>
                                </td>
                                <td>${b.startDate}</td>
                                <td>${b.endDate}</td>
                                <td>
                  <span class="status ${b.isActive ? 'active' : 'deactive'}">
                          ${b.isActive ? 'Hoạt động' : 'Không hoạt động'}
                  </span>
                                </td>
                                <td>
                                    <div class="action-icons">
                    <span class="icon toggle-icon"
                          data-id="${b.id}"
                          data-active="${b.isActive}">
                      <i class="fa-solid ${b.isActive ? 'fa-trash' : 'fa-eye-slash'}"></i>
                    </span>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
        </div>
    </div>
        <script src="${pageContext.request.contextPath}/static/style-component/style-admin/banner/banner.js" defer></script>
</div>
</div>
    <div id="modal-image-viewer" class="modal-image-viewer" style="display:none;">
        <span class="close-modal" id="close-modal-image">&times;</span>
        <img id="modal-preview-image" src="" alt="Xem ảnh lớn" />
    </div>
</body>
</html>
