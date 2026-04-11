<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%-- Created by IntelliJ IDEA. User: vinhp Date: 12/24/2025 Time: 1:55 PM To change this template use File |
        Settings | File Templates. --%>
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
            <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">

                    <title>Chi tiết sản phẩm</title>
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                    <link rel="stylesheet"
                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
                    <link rel="stylesheet"
                        href="${pageContext.request.contextPath}/static/style-component/product-detail/Product-detail.css">
                    <link rel="stylesheet"
                        href="${pageContext.request.contextPath}/static/style-component/product-detail/Product-detail-item.css">
                    <script
                        src="${pageContext.request.contextPath}/static/style-component/product-detail/Product-detail-item.js"
                        defer></script>
                    <link rel="stylesheet"
                        href="${pageContext.request.contextPath}/static/style-component/product-detail/Product-buying-tool.css">
                    <script
                        src="${pageContext.request.contextPath}/static/style-component/product-detail/Product-buying-tool.js"></script>
                    <link rel="stylesheet"
                        href="${pageContext.request.contextPath}/static/style-component/product-detail/review.css" />
                    <script
                        src="${pageContext.request.contextPath}/static/style-component/product-detail/review.js"></script>
                </head>

                <body>
                    <div class="cart_header">
                        <jsp:include page="/home/header.jsp" />
                    </div>
                    <div class="container">
                        <div class="section1">
                            <div class="carousel-container">


                                <div class="carousel-container">

                                    <img id="mainImage" src="${primaryImageUrl}" alt="Carousel Image"
                                        class="carousel-image">
                                    <!-- Navigation Arrows -->
                                    <div class="nav-arrow left" onclick="prevImage()">&#10094;</div>
                                    <div class="nav-arrow right" onclick="nextImage()">&#10095;</div>

                                    <!-- Thumbnails -->
                                    <div class="thumbnails">
                                        <div class="thumbnail">
                                            <c:if test="${not empty images}">
                                                <c:forEach var="image" items="${images}">
                                                    <img src="${image}" alt="Thumbnail " />
                                                </c:forEach>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>

                        <%-- product này là gì--%>
                            <div class="section1">
                                <div id="product" data-id="${product.id}" data-option-default="${product.optionId}"
                                    class="container-product-Bt">
                                    <div class="product-title">
                                        ${product.name}
                                    </div>

                                    <%-- product này là gì--%>
                                        <%-- VARIANT --%>
                                            <c:if test="${not empty optionVariant  && not empty variants}">
                                                <c:forEach items="${variants}" var="type">

                                                    <div class="wrap_variant ">
                                                        <div class="option-title">Chọn ${type}:</div>

                                                        <c:forEach items="${optionVariant}" var="op">
                                                            <c:if test="${op.variantName eq type  }">
                                                                <div class="option-item" data-option-id="${op.id}"
                                                                    data-price="${op.price}"> ${op.variantValue}</div>
                                                            </c:if>
                                                        </c:forEach>
                                                    </div>

                                                </c:forEach>

                                            </c:if>


                                            <div id="price" class="price">
                                                <c:choose>
                                                    <c:when test="${not empty productPrice}">

                                                        <fmt:formatNumber value="${productPrice}" pattern="#,###" /> VND
                                                    </c:when>
                                                    <c:otherwise>
                                                        Đang câp nhật
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>


                                            <div class="product-features">
                                                <ul>

                                                    <!-- Lặp qua danh sách descriptions -->
                                                    <c:forEach var="desc" items="${descriptions}">
                                                        <li>${desc}</li>
                                                    </c:forEach>
                                                </ul>
                                            </div>



                                            <div class="button-group">
                                                <a id="add-to-cart" href="#">
                                                    <button class="btn-add-to-cart btn add">Thêm vào giỏ hàng</button>
                                                </a>

                                                <a id="buy-now" href="#">
                                                    <button class="btn-buy-now btn buy"> Mua ngay</button>
                                                </a>
                                            </div>

                                            <div id="cart-notification" class="notification hidden">
                                                <i class="fa-solid fa-circle-check"></i>
                                                <span>Thêm vào giỏ hàng thành công</span>
                                            </div>

                                </div>
                            </div>

                    </div>
                    <%--summary__list--%>
                        <div class="summary__list">
                            <div class="summary__item">
                                <img src="${pageContext.request.contextPath}/static/image/img-detail/pediatrics_24dp_000000_FILL0_wght400_GRAD0_opsz24.png"
                                    alt="Icon 1" class="summary__icon">
                                <p class="summary__text">Thương Hiệu Abbott</p>
                            </div>
                            <div class="summary__divider"></div>
                            <div class="summary__item">
                                <img src="${pageContext.request.contextPath}/static/image/img-detail/replay_10_24dp_000000_FILL0_wght400_GRAD0_opsz24.png"
                                    alt="Icon 2" class="summary__icon">
                                <p class="summary__text">Độ Tuổi Phù Hợp cho trẻ từ 1-10 tuổi</p>
                            </div>
                            <div class="summary__divider"></div>
                            <div class="summary__item">
                                <img src="${pageContext.request.contextPath}/static/image/img-detail/cool_to_dry_24dp_000000_FILL0_wght400_GRAD0_opsz24.png"
                                    alt="Icon 3" class="summary__icon">
                                <p class="summary__text">Bảo quản nơi khô ráo, hộp đã mở sử dụng trong vòng 3 tuần</p>
                            </div>
                        </div>
                        <div class="Section-Pt">
                            <img src="${pageContext.request.contextPath}/static/image/img-detail/img00.1.jpg"
                                alt="Section1">
                        </div>
                        <%--fix nay--%>
                            <%--<div class="text1">--%>
                                <%-- <h2>Thiết kế phẳng hiện đại, hoàn hảo mọi gian bếp</h2>--%>
                                    <%-- <p>Nâng tầm không gian bếp với thiết kế thời thượng từ tủ lạnh Samsung thế hệ
                                        mới. Thiết kế phẳng giảm thiểu--%>
                                        <%-- chi--%>
                                            <%-- tiết đem lại sự tao nhã sang trọng, cùng chất liệu cao cấp bền đẹp theo
                                                thời gian.</p>--%>
                                                <%--< /div>--%>
                                                    <div class="feature-benefit">
                                                        <div class="feature-benefit__text">
                                                            <h3>Hướng Dẫn Sử Dụng</h3>
                                                            <ul>
                                                                <li>Để có 225 ml PediaSure BA pha chuẩn, cho 190ml nước
                                                                    chín để nguội (≤ 37 độ C) vào ly.</li>
                                                                <li>Vừa cho từ từ 5 muỗng gạt ngang bột PediaSure BA
                                                                    (muỗng có sẵn trong hộp) vừa khuấy cho tan đều.</li>
                                                                <li>Khi pha đúng theo hướng dẫn, 1ml PediaSure BA cung
                                                                    cấp 1 kcal hoặc 4.18 KJ.</li>
                                                                <li>Sữa vừa pha dùng ngay hay đậy kín, cho vào tủ lạnh
                                                                    và dùng trong vòng 24 giờ.</li>
                                                                <li>Để bổ sung dinh dưỡng: 2 ly/ngày (trẻ 1 - 8 tuổi),
                                                                    2-3 ly/ngày (trẻ 9 - 10 tuổi) hoặc theo hướng dẫn
                                                                    của
                                                                    chuyên viên dinh dưỡng.
                                                                </li>
                                                                <li>Không dùng lò vi sóng để hâm nóng sữa.</li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                    <script> const contextPath = "${pageContext.request.contextPath}"; </script>
                                                    <script
                                                        src="${pageContext.request.contextPath}/static/style-component/product-detail/Product-detail.js"></script>
                </body>

                </html>