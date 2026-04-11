<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 12/30/2025
  Time: 11:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Image</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/product-detail/Product-detail-item.css">
    <script src="${pageContext.request.contextPath}/static/style-component/product-detail/Product-detail-item.js" defer></script>
</head>
<body>

<!-- Carousel Container -->
<div class="carousel-container">
    <!-- Main Image Display -->
    <img id="mainImage"
         src="${pageContext.request.contextPath}/static/image/img-detail/product_cart_1.png"
         alt="Carousel Image"
         class="carousel-image"
         data-context-path="${pageContext.request.contextPath}">

    <!-- Navigation Arrows -->
    <div class="nav-arrow left" onclick="prevImage()">&#10094;</div>
    <div class="nav-arrow right" onclick="nextImage()">&#10095;</div>

    <!-- Thumbnails -->
    <div class="thumbnails">
        <c:forEach var="image" items="${images}" varStatus="status">
            <img src="${image.url}"
                 alt="Thumbnail ${status.index + 1}"
                 class="thumbnail <c:if test='${status.index == 0}'>active</c:if>"
                 onclick="showImage(${status.index})">
        </c:forEach>
    </div>
</div>

</body>
</html>