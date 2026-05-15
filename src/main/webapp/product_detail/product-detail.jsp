<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>${product.name}</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/product-detail/Product-detail.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">

</head>

<body>

<div class="cart_header">
    <jsp:include page="/home/header.jsp"/>
</div>

<!-- BREADCRUMB -->
<div class="breadcrumb">
    <a href="${pageContext.request.contextPath}/home">
        Trang chủ
    </a>

    <span>/</span>

    <a href="#">
        Sản phẩm
    </a>

    <span>/</span>

    <span class="active">
        ${product.name}
    </span>

</div>

<div class="container">

    <!-- LEFT -->
    <div class="section1">

        <div class="carousel-container">

            <!-- MAIN IMAGE -->
            <img id="mainImage"
                 src="${primaryImageUrl}"
                 data-context-path="${pageContext.request.contextPath}"
                 alt="${product.name}"
                 class="carousel-image">

            <!-- NAV -->
            <div class="nav-arrow left" onclick="prevImage()">&#10094;</div>
            <div class="nav-arrow right" onclick="nextImage()">&#10095;</div>

            <div class="thumbnails">
                <c:if test="${not empty images}">
                    <c:forEach var="image" items="${images}" varStatus="loop">
                        <img class="thumbnail" src="${image}" data-index="${loop.index}" alt="Thumbnail"/>
                    </c:forEach>
                </c:if>
            </div>

        </div>

    </div>

    <!-- RIGHT -->
    <div class="section1">

        <div id="product"
             data-id="${product.id}"
             data-option-default="${product.optionId}"
             class="container-product-Bt">

            <!-- TITLE -->
            <div class="product-title">
                ${product.name}
            </div>

            <!-- META -->
            <div class="product-meta">

                <div class="product-rating">

                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>
                    <i class="fa-solid fa-star"></i>

                    <span>
                        (128 đánh giá)//todo
                    </span>

                </div>

                <div class="product-view">

                    <i class="fa-solid fa-eye"></i>

                    ${product.noOfViews} lượt xem

                </div>

            </div>

            <!-- OPTION -->
            <c:if test="${not empty productOptions}">

                <div class="variant-wrapper">

                    <div class="option-title">
                        Chọn phân loại:
                    </div>

                    <div class="wrap_variant">

                        <c:forEach items="${productOptions}" var="op">

                            <div class="option-item <c:if test="${op.stock <= 0}">out-of-stock</c:if>"
                                 data-option-id="${op.optionId}"
                                 data-price="${op.price}"
                                 data-stock="${op.stock}">

                                <span>${op.variantText}</span>

                                <c:if test="${op.stock <= 0}">
                                    <small class="option-stock">Hết hàng</small>
                                </c:if>

                            </div>

                        </c:forEach>

                    </div>

                </div>

            </c:if>

            <!-- PRICE -->
            <div id="price" class="price">

                <c:choose>

                    <c:when test="${not empty productPrice}">

                        <fmt:formatNumber value="${productPrice}" pattern="#,###"/>
                        VND

                    </c:when>

                    <c:otherwise>

                        Đang cập nhật

                    </c:otherwise>

                </c:choose>

            </div>

            <!-- SHORT DESC -->
            <div class="short-description">

                <c:if test="${not empty descriptions}">
                    ${descriptions[0]}
                </c:if>

            </div>


            <div class="product-info-box">

                <div class="info-item">

                    <i class="fa-solid fa-truck"></i>

                    <span>
                        Giao hàng toàn quốc
                    </span>

                </div>

                <div class="info-item">

                    <i class="fa-solid fa-shield"></i>

                    <span>
                        Cam kết chính hãng
                    </span>

                </div>

            </div>

            <!-- QUANTITY -->
            <div class="quantity-wrapper">

                <div class="quantity-title">
                    Số lượng
                </div>

               <div class="quantity-box">
                <button type="button" class="qty-btn minus">-</button>
                   <input type="number" id="quantity" value="1" min="1">
                <button type="button" class="qty-btn plus">+</button>
            </div>

            </div>

            <div class="button-group">
                    <button id="add-to-cart" class="btn-add-to-cart btn add">
                        <i class="fa-solid fa-cart-shopping"></i> Thêm vào giỏ hàng
                    </button>
            </div>

            <div id="cart-notification" class="notification hidden">

                <i class="fa-solid fa-circle-check"></i>

                <span>
                    Thêm vào giỏ hàng thành công
                </span>

            </div>

        </div>

    </div>

</div>

<!-- PRODUCT DETAIL -->
<div class="product-detail-wrapper">

    <!-- TAB -->
    <div class="product-tabs">

        <button class="tab-btn active" data-tab="description">
            Mô tả sản phẩm
        </button>

        <button class="tab-btn" data-tab="guide">
            Hướng dẫn sử dụng
        </button>

        <button class="tab-btn" data-tab="info">
            Thông tin sản phẩm
        </button>

    </div>

    <!-- DESCRIPTION -->
    <div class="tab-content active" id="description">

        <div class="description-content">

            <h3>
                ${product.name}
            </h3>

            <p>
                ${product.name} là dòng sản phẩm dinh dưỡng chất lượng cao,
                hỗ trợ phát triển toàn diện cho bé.
            </p>

            <ul>

                <c:forEach var="desc" items="${descriptions}">

                    <li>${desc}</li>

                </c:forEach>

            </ul>

        </div>

    </div>

    <!-- GUIDE -->
    <div class="tab-content" id="guide">

        <div class="guide-content">

            <ul>

                <li>
                    Rửa tay và dụng cụ pha sữa thật sạch trước khi sử dụng.
                </li>

                <li>
                    Đun sôi nước và để nguội khoảng 40°C trước khi pha.
                </li>

                <li>
                    Pha đúng liều lượng theo hướng dẫn của nhà sản xuất.
                </li>

                <li>
                    Khuấy đều đến khi sữa tan hoàn toàn.
                </li>

                <li>
                    Sữa nên sử dụng ngay sau khi pha.
                </li>

                <li>
                    Không sử dụng phần sữa thừa sau 2 giờ.
                </li>

            </ul>

        </div>

    </div>

    <!-- PRODUCT INFO -->
    <div class="tab-content" id="info">

        <div class="product-information-table">

            <div class="info-row">

                <div class="info-label">
                    Mã sản phẩm
                </div>

                <div class="info-value">
                    #${product.id}
                </div>

            </div>

            <div class="info-row">

                <div class="info-label">
                    Tên sản phẩm
                </div>

                <div class="info-value">
                    ${product.name}
                </div>

            </div>

            <div class="info-row">

                <div class="info-label">
                    Giá
                </div>

                <div class="info-value">

                    <fmt:formatNumber value="${productPrice}" pattern="#,###"/>
                    VND

                </div>

            </div>

            <div class="info-row">

                <div class="info-label">
                    Lượt xem
                </div>

                <div class="info-value">
                    ${product.noOfViews}
                </div>

            </div>

            <div class="info-row">

                <div class="info-label">
                    Tồn kho
                </div>

                <div class="info-value">

                    <c:choose>

                        <c:when test="${product.stock > 0}">
                            Còn hàng
                        </c:when>

                        <c:otherwise>
                            Hết hàng
                        </c:otherwise>

                    </c:choose>

                </div>

            </div>

        </div>

    </div>

</div>
<div id="footer">
    <jsp:include page="/home/footer.jsp" />
</div>

<script
        src="${pageContext.request.contextPath}/static/style-component/product-detail/Product-detail.js"></script>
</body>

</html>