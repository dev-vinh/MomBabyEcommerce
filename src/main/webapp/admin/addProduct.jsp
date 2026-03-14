<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 14/03/2026
  Time: 4:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/products/addProduct.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

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
      <div class="row align-items-center">
        <div class="col-sm mb-2 mb-sm-0">
          <h1 class="page-header-title">Sản phẩm</h1>
        </div>
      </div>
    </div>
    <div class="main-container">
      <div class="left-column">
        <div class="product-information">
          <h2>Thông tin sản phẩm</h2>
          <form>
            <div class="form-group">
              <label for="productName">Tên  <span class="required">*</span> </label>
              <input type="text" id="productName" placeholder="Quần áo,bình sữa, ect.">
            </div>

            <div class="form-row">
              <div class="form-group half-width">
                <label for="sku">Mã SKU</label>
                <input type="text" id="sku"  disabled>
              </div>

              <div class="form-group half-width">
                <label  for="categoryDropdown" >Danh mục  <span class="required">*</span> </label>
                <select id="categoryDropdown" class="option-select" data-url="<%= request.getContextPath() %>categories">
                  <option value="">Chọn danh mục</option>
                  <!-- Danh mục sẽ được load ở đây -->
                </select>

              </div>


            </div>

            <div class="form-group">
              <label for="description" id="product-description">Mô tả (Tùy chọn)</label>

              <div class="toolbar">
                <button type="button" class="toolbar-btn"><i class="fa-solid fa-bold"></i></button>
                <button type="button" class="toolbar-btn"><i class="fa-solid fa-italic"></i></button>
                <button type="button" class="toolbar-btn"><i class="fa-solid fa-underline"></i></button>

                <button type="button" class="toolbar-btn"><i class="fa-solid fa-strikethrough"></i></button>

                <button type="button" class="toolbar-btn"><i class="fa-solid fa-link"></i></button>
                <button type="button" class="toolbar-btn"><i class="fa-solid fa-image"></i></button>
                <button type="button" class="toolbar-btn"><i class="fa-solid fa-quote-right"></i></button>

                <button type="button" class="toolbar-btn"><i class="fa-solid fa-code"></i></button>
                <button type="button" class="toolbar-btn"><i class="fa-solid fa-list-ul"></i></button>
              </div>


              <textarea id="description" placeholder="Nhập mô tả của bạn..."></textarea>
            </div>

          </form>
        </div>

        <div class="media-section">
          <div class="media-header">
            <h2>Hình ảnh <span class="required">*</span></h2>
          </div>

          <div class="media-upload-box">
            <div class="upload-icon" id="uploadIcon">
              <img id="previewImage" src="${pageContext.request.contextPath}/static/image/screenshot-1730907930298-removebg-preview.png"
                   height="170" width="130" alt="Preview Image"/>
            </div>

            <div id="imagePreviewContainer" class="image-preview-container"></div>
            <p id="dragDropText">Kéo và thả tệp của bạn vào đây</p>
            <span>hoặc</span>
            <form id="uploadForm" enctype="multipart/form-data">
              <input type="file" id="fileInput" name="file" style="display: none;" accept=".png, .jpg, .jpeg" multiple />
              <button type="button" class="browse-files" id="uploadButton">Tải ảnh lên</button>
            </form>
          </div>
        </div>


        <div id="optionsContainer1" class="options-section">
          <div class="variant-header">
            <h2>Biến thể <span class="required">*</span> </h2 >
            <button class="add-variant-button" onclick="addVariant()">+ Thêm biến thể</button>
          </div>

          <div class="variant-group">
            <div id="optionsContainer" class="options-container">
              <div class="pricing-section">
                <div class="form-group half-width">
                  <input type="text" id="price" placeholder="Giá">
                </div>
                <div class="form-group half-width">
                  <input type="text" id="total" placeholder="Số lượng" >
                </div>

              </div>

              <label for="productName">Thuộc tính</label>
              <div class="option-group">
                <select class="option-select" id="variant-select" onchange="fetchVariantValues(this.value)">
                  <option value="">Chọn danh mục</option>
                </select>

                <!-- Variant Value Dropdown -->
                <select class="option-select" id="variant-value-select">
                  <option value="">Chọn giá trị</option>
                </select>

                <button class="remove-option-button" onclick="removeOptionGroup(this)">×</button>
              </div>
            </div>
            <button class="add-option-button" onclick="addOptionGroup('optionsContainer')">+ Thêm thuộc tính
            </button>
          </div>

        </div>
      </div>


      <div class="right-column">
        <div class="section organization-section">
          <h2>Tổ chức</h2>
          <label for="vendor">Nhà cung cấp <span class="required">*</span> </label>
          <select id="vendor"  data-url="<%= request.getContextPath() %>categories">
            <option value="winter">Chọn nhà cung cấp</option>
          </select>

          <%--                    <label for="tags">Thẻ</label>--%>
          <%--                    <input type="text" id="tags" placeholder="Nhập thẻ tại đây">--%>
        </div>


        <div class="save">
          <button id="saveButton" onclick="saveProductDetails()"> Lưu</button>
        </div>

      </div>

    </div>
  </div>

</div>


</div>


<script src="${pageContext.request.contextPath}/static/style-component/style-admin/products/addProduct.js"></script>
</body>
</html>
