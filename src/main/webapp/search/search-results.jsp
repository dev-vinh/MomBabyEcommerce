<%-- Created by IntelliJ IDEA. User: vinhp Date: 1/27/2026 Time: 11:06 AM To change this template use File | Settings |
    File Templates. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<head>
    <title>Kết quả tìm kiếm</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-home/search-results.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
</head>

<body>
<div class="search_header">
    <jsp:include page="../home/header.jsp"/>
</div>
<div class="sr-heading">
    <h2>Tìm kiếm</h2>
    <p>Có <strong>${productCount} sản phẩm</strong> cho tìm kiếm</p>
    <div class="sr-divider"></div>
</div>


<c:if test="${not empty keyword}">
    <p class="sr-keyword">Kết quả tìm kiếm cho <span>"${keyword}"</span>.</p>
</c:if>

<div id="list_product">
    <c:choose>
        <c:when test="${not empty products}">
            <div class="product-row">
                <c:forEach var="product" items="${products}">
                    <div class="search_body"
                         onclick="location.href='${pageContext.request.contextPath}/product-detail?id=${product.id}'">


                        <div class="wrap_img">
                            <img src="${pageContext.request.contextPath}${product.imageUrl}"
                                 alt="${product.name}"
                                 onerror="this.src='${pageContext.request.contextPath}/static/image/placeholder.png'"/>
                        </div>

                        <div class="infor">
                            <div class="product-name">
                                <c:out value="${product.name}"/>
                            </div>

                            <div class="product-price">
                                <c:choose>
                                    <c:when test="${product.price != null}">
                                        <fmt:formatNumber value="${product.price}" pattern="#,###"/>đ
                                    </c:when>
                                    <c:otherwise>
                                        Đang cập nhật
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </div>

                    </div>
                </c:forEach>
            </div>
        </c:when>

        <c:otherwise>
            <div class="empty-state">
                <i class="fa-solid fa-magnifying-glass"></i>
                <p>Không tìm thấy sản phẩm nào phù hợp.</p>
                <span>Hãy thử tìm với từ khoá khác.</span>
            </div>
        </c:otherwise>
    </c:choose>
</div>

</body>
