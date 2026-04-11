<%-- Created by IntelliJ IDEA. User: vinhp Date: 1/27/2026 Time: 11:06 AM To change this template use File | Settings |
    File Templates. --%>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

                <head>
                    <title>Kết quả tìm kiếm</title>
                    <link rel="stylesheet"
                        href="${pageContext.request.contextPath}/static/style-component/style-home/search-results.css">
                </head>

                <body>

                    <div class="search_header">
                        <jsp:include page="../home/header.jsp" />
                    </div>

                    <div id="list_product">
                        <c:choose>
                            <c:when test="${not empty products}">
                                <div class="product-row">
                                    <c:forEach var="product" items="${products}">
                                        <div class="search_body">
                                            <div class="wrap_img">
                                                <img src="${pageContext.request.contextPath}${product.imageUrl}"
                                                    alt="${product.name}" />
                                            </div>
                                            <div class="infor col">
                                                <div class="product-title">
                                                    <a
                                                        href="${pageContext.request.contextPath}/product-detail?id=${product.id}">
                                                        <c:out value="${product.name}" />
                                                    </a>
                                                </div>
                                                <div class="infor mid_align col">
                                                    <div id="top_name" class="bold f16">
                                                        <a
                                                            href="${pageContext.request.contextPath}/product-detail?id=${product.id}">
                                                            <c:out value="${product.name}" />
                                                        </a>
                                                    </div>
                                                    <div id="price" class="bold f22">
                                                        <fmt:formatNumber value="${product.price}" pattern="#,###" />
                                                        VND
                                                        <span id="ratting" class="" style="padding: 0 5px">
                                                            5 (153)
                                                            <i class="fa-solid fa-star" style="color: #FFD43B;"></i>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="operation">
                                                <button class="buy-now">Mua ngay</button>
                                                <button class="btn add">Thêm vào giỏ hàng</button>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="no-results">Không tìm thấy sản phẩm nào phù hợp.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </body>
