<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 14/03/2026
  Time: 4:27 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Sản phẩm</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/products/addProduct.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
</head>
<body>
<div class="wrap_header">
  <jsp:include page="header.jsp"/>
</div>

<div class="container">
  <div class="side_bar">
    <jsp:include page="sidebar.jsp"/>
  </div>

  <div class="content">
    <div class="page-header">
        <div class="row align-items-center" style="display: flex; justify-content: space-between; align-items: center;">
            <div class="col-sm mb-2 mb-sm-0">
                <h1 class="page-header-title" id="pageTitle" style="margin: 0;">Sản phẩm</h1>
            </div>

            <div class="import-excel-actions">
                <input type="file" id="excelImportInput" accept=".xlsx, .xls" style="display: none;" />
                <button type="button" id="btnImportExcel" class="browse-files"
                        onclick="document.getElementById('excelImportInput').click()"
                        style="background-color: #107c41; color: white; padding: 10px 16px; border: none; border-radius: 6px; cursor: pointer; font-weight: bold; display: flex; align-items: center; gap: 8px;">
                    <i class="fa-solid fa-file-excel" style="font-size: 18px;"></i> Nhập từ Excel
                </button>
            </div>
        </div>
    </div>

    <div class="main-container">
      <div class="left-column">
        <div class="product-information">
          <h2>Thông tin sản phẩm</h2>
          <form onsubmit="return false;">
            <div class="form-group">
              <label for="productName">Tên <span class="required">*</span></label>
              <input type="text" id="productName" placeholder="VD: Sữa bột Meiji số 0">
            </div>

            <div class="form-row">
              <div class="form-group half-width">
                <label for="sku">Mã SKU <span class="required">*</span></label>
                <input type="text" id="sku" placeholder="VD: MEIJI-0">
              </div>

              <div class="form-group half-width">
                <label for="categoryDropdown">Danh mục <span class="required">*</span></label>
                <select id="categoryDropdown" class="option-select">
                  <option value="">Chọn danh mục</option>
                </select>
              </div>
            </div>

            <div class="form-group">
              <label for="description" id="product-description">Mô tả</label>
              <textarea id="description" placeholder="Nhập mô tả sản phẩm..."></textarea>
            </div>
          </form>
        </div>

        <div class="media-section">
          <div class="media-header">
            <div>
              <h2>Hình ảnh <span class="required">*</span></h2>
              <p class="section-hint">Có thể chọn nhiều ảnh. Ảnh đầu tiên sẽ là ảnh chính của sản phẩm.</p>
            </div>
          </div>

          <div class="media-upload-box" id="mediaUploadBox">
            <div class="upload-icon" id="uploadIcon">
              <img id="previewImage" src="${pageContext.request.contextPath}/static/image/screenshot-1730907930298-removebg-preview.png"
                   height="170" width="130" alt="Preview Image"/>
            </div>

            <div id="imagePreviewContainer" class="image-preview-container"></div>
            <p id="dragDropText">Kéo thả ảnh vào đây hoặc bấm tải ảnh lên</p>
            <form id="uploadForm" enctype="multipart/form-data" onsubmit="return false;">
              <input type="file" id="fileInput" name="file" style="display: none;" accept=".png, .jpg, .jpeg, .webp, .gif" multiple />
              <button type="button" class="browse-files" id="uploadButton">Tải ảnh lên</button>
            </form>
          </div>
        </div>

        <div id="optionsContainer1" class="options-section">
          <div class="variant-header">
            <div>
              <h2>Phiên bản bán & giá <span class="required">*</span></h2>
              <p class="section-hint">
                Mỗi dòng là một phiên bản bán riêng. Giá do quản lý sản phẩm cập nhật; tồn kho chỉ hiển thị để xem, việc nhập/xuất kho nên xử lý ở module quản lý kho.
              </p>
            </div>
            <button type="button" class="add-variant-button" id="addOptionRowButton">+ Thêm phiên bản</button>
          </div>

          <div id="optionRows" class="option-rows"></div>
        </div>
      </div>

      <div class="right-column">
        <div class="section organization-section">
          <h2>Tổ chức</h2>
          <label for="vendor">Nhà cung cấp <span class="required">*</span></label>
          <select id="vendor">
            <option value="">Chọn nhà cung cấp</option>
          </select>

          <label for="statusSelect">Trạng thái hoạt động <span class="required">*</span></label>
          <select id="statusSelect">
            <option value="true">Hoạt động</option>
            <option value="false">Không hoạt động</option>
          </select>
        </div>

        <div class="save">
          <button type="button" id="saveButton">Lưu</button>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/static/style-component/style-admin/products/addProduct.js?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
