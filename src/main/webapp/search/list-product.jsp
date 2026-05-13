<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

<head>
    <title>Danh sách sản phẩm</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style_product/ListProduct.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style_product/ListProduct.css">
</head>

<body>
<div class="search_header">
    <jsp:include page="../home/header.jsp" />
</div>

<div class="sp-page">
    <aside class="sp-sidebar" id="sidebar" data-category="${categoryId}">
        <div class="sidebar-title">
            <i class="fa-solid fa-sliders"></i>
            Bộ lọc tìm kiếm
        </div>
        <div class="divider"></div>
        <div class="filter-section">
            <div class="filter-label">
                Khoảng giá
            </div>
            <div class="filter-option">
                <input type="radio" name="price" id="price0">
                <label for="price0">Tất cả</label>
            </div>
            <div class="filter-option">
                <input type="radio" name="price" id="price1" data-max="300000">
                <label for="price1">Dưới 300.000đ</label>
            </div>
            <div class="filter-option">
                <input type="radio" name="price" id="price2" data-min="300000" data-max="500000">
                <label for="price2">300.000đ – 500.000đ</label>
            </div>
            <div class="filter-option">
                <input type="radio" name="price" id="price3" data-min="500000" data-max="700000">
                <label for="price3">500.000đ – 700.000đ</label>
            </div>
            <div class="filter-option">
                <input type="radio" name="price" id="price4" data-min="700000" data-max="1000000">
                <label for="price4">700.000đ – 1.000.000đ</label>
            </div>
            <div class="filter-option">
                <input type="radio" name="price" id="price5" data-min="1000000">
                <label for="price5">Trên 1.000.000đ</label>
            </div>
        </div>

        <div class="divider"></div>

        <div class="filter-section">
            <div class="collapse-filter" data-target="filter-brand">
                <i class="fa-solid fa-copyright"></i>
                Thương hiệu
                <i class="fa-solid fa-chevron-down arrow"></i>
            </div>

            <div id="filter-brand" class="filter-content">

                <c:forEach var="b" items="${brands}">
                    <div class="filter-option">
                        <input type="radio" name="brand" value="${b.id}">
                        <label>${b.name}</label>
                    </div>
                </c:forEach>

            </div>
        </div>


        <div class="divider"></div>


        <button class="btn-apply" id="apply_btn">Áp dụng</button>
    </aside>

    <main class="sp-main">
        <div class="sp-main-header">
            <h1 class="sp-main-title">
                <c:choose>
                    <c:when test="${not empty categoryName}">${categoryName}</c:when>
                    <c:otherwise>Sản phẩm</c:otherwise>
                </c:choose>
            </h1>
        </div>

        <div class="sp-grid" id="product_list"></div>
        <div class="sp-pagination" id="sp-pagination"></div>
    </main>
</div>
<div id="footer">
    <jsp:include page="/home/footer.jsp" />
</div>


</body>
<script src="${pageContext.request.contextPath}/static/style-component/style_product/productCard.js"></script>
<script src="${pageContext.request.contextPath}/static/style-component/style_product/ListProduct.js"></script>


</html>