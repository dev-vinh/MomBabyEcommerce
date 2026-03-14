<%-- Created by IntelliJ IDEA. User: aho4dot0 Date: 27/12/2025 Time: 10:30 To change this template use File | Settings |
    File Templates. --%>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <title>Giỏ Hàng của bạn</title>
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                    <link rel="stylesheet"
                        href="${pageContext.request.contextPath}/static/style-component/style-cart/Cart.css">
                    <link rel="stylesheet"
                        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
                    <script src="${pageContext.request.contextPath}/static/style-component/style-cart/Cart.js"></script>
                    <link rel="stylesheet"
                        href="${pageContext.request.contextPath}/static/style-component/style-cart/CartItem.css">
                </head>

                <body>
                    <div id="wrap_cart">

                        <div class="cart_header">
                            <jsp:include page="/home/header.jsp" />
                        </div>
                        <div id="cart_body" class="mid_align col">


                            <%-- CHƯA CÓ CONTROLLẺR SETATTRIBUTẺ CHO productCarts--%>
                                <c:if test="${empty productCarts}">
                                    <div id="empty_cart" class="mid_align col">
                                        <i class="fa-solid fa-cart-plus"></i>
                                        <h1>Giỏ Hàng Đang Trống</h1>
                                        <span>Đăng nhập vào tài khoản của bạn để xem các mục đã lưu hoặc tiếp tục mua
                                            sắm</span>

                                        <div class="btn row">
                                            <button class="btn_shopping">Tiếp tục mua sắm</button>
                                            <button class="btn_login">Đăng nhập</button>
                                        </div>
                                    </div>
                                </c:if>
                                <%-- CHƯA CÓ CONTROLLẺR SETATTRIBUTẺ CHO productCarts--%>
                                    <c:if test="${not empty productCarts }">


                                        <div class="content row">
                                            <div class="list_item ">
                                                <%-- <span>Bạn đang có 4 sản phâm trong giỏ hàng</span>--%>


                                                    <c:forEach items="${productCarts}" var="p">

                                                        <div class="wrap mid_align row product-item"
                                                            data-stock="${p.stock}" data-id="${p.optionId}">
                                                            <input type="checkbox" checked class="product_checked"
                                                                value="${p.optionId}">
                                                            <div class="image">
                                                                <c:choose>
                                                                    <c:when test="${empty p.imageUrl}">
                                                                        <img src="${pageContext.request.contextPath}static/image/default_img.jpg"
                                                                            alt="" />
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img src="${p.imageUrl}" alt="" />
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>


                                                            <div class="description mid_align col  ">
                                                                <div class="title ">${p.name}</div>

<%--                                                                <div class="color">--%>
<%--                                                                    <span class="color_name">Màu Sắc: Đen </span>--%>
<%--                                                                </div>--%>



                                                                <div class="status">
                                                                    <span class="status_type">Còn hàng</span>
                                                                </div>

                                                            </div>


                                                            <div class="section_price mid_align col  ">
                                                                <span class="price" data-price="${p.price}">
                                                                    <fmt:formatNumber value="${p.price}"
                                                                        pattern="#,###" />

                                                                </span>



                                                                <div class="quantity mid_align row">
                                                                    <button id="decrement"><i
                                                                            class="fa-solid fa-minus"></i></button>
                                                                    <span class="num mid_align"
                                                                        data-quantity="${p.quantity}">
                                                                        ${p.quantity}
                                                                    </span>

                                                                    <button id="increment"><i
                                                                            class="fa-solid fa-plus"></i></button>

                                                                </div>

                                                                <div class="remove">
                                                                    <i class=" del fa-solid fa-trash-can"></i>
                                                                </div>


                                                            </div>


                                                        </div>

                                                    </c:forEach>


                                            </div>


                                            <div class="bill mid_align rol ">
                                                <div class="discount">
                                                    <span class="title">Nhập Mã Khuyến Mãi</span>

                                                    <div class="wrap_input">
                                                        <input id="voucher" type="text"
                                                            placeholder="Voucher hoặc gift code ">
                                                        <label for="voucher"></label>
                                                        <button type="submit">Áp Dụng</button>
                                                    </div>

                                                </div>


                                                <div class="summary col">
                                                    <span class="title">Bản Tóm Tắt</span>
                                                    <div class="item_price">
                                                        <span>Tổng giá trước thuế</span>
                                                        <span id="before_tax" class="value">0 VND</span>
                                                    </div>

                                                    <div class="tax item_price">
                                                        <span>Thuế GTGT (10%)</span>
                                                        <span id="VAT" class="value">0 VND</span>
                                                    </div>
                                                </div>


                                                <div class="wrap_total">
                                                    <div class="total_label">
                                                        <span>Tổng cộng</span>
                                                        <span id="total">0 VND</span>
                                                    </div>
                                                    <span class="note">Đẫ bao gồm thuế GTGT</span>
                                                </div>


                                                <button type="submit" id="pay">
                                                    Tiếp tục
                                                </button>

                                                <div class="term_condition">
                                                    <span>Bằng cách gửi đơn đặt hàng,
                                                        bạn đồng ý với <a href="#">Điều khoản & điều kiện</a> và
                                                        chúng tôi sẽ sử dụng dữ liệu cá nhân của bạn theo
                                                        <a href="#">Chính sách quyền riêng tư</a> của chúng tôi.</span>
                                                </div>


                                                <div class="ads col">
                                                    <div class="ads_item">
                                                        <i class="fa-solid fa-medal"></i>
                                                        <span>Cam kết giá</span>

                                                    </div>

                                                    <div class="ads_item">
                                                        <i class="fa-solid fa-truck-fast"></i>
                                                        <span>Giao hàng miễn phí toàn quốc </span>

                                                    </div>
                                                    <div class="ads_item">
                                                        <i class="fa-solid fa-percent"></i>
                                                        <span>Gói trả góp 0%</span>

                                                    </div>

                                                    <div class="ads_item">
                                                        <i class="fa-solid fa-rotate"></i>
                                                        <span>Đổi sản phẩm theo chính sách quy định trong vòng 14
                                                            ngày</span>

                                                    </div>

                                                </div>


                                            </div>


                                        </div>


                                    </c:if>


                        </div>

                    </div>
                </body>

                </html>