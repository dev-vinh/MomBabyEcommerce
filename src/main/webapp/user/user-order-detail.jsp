<%--
  Created by IntelliJ IDEA.
  User: fileh
  Date: 1/6/2026
  Time: 4:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_order/OrderHistory.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/style-component/style-user_order/OrderHistory.js"></script>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_order/OrderHistoryItem.css">

</head>
<body>

<div class="header">
    <jsp:include page="/home/header.jsp"/>
</div>

<div class="body">
    <div class="sidebar">
        <jsp:include page="user-sidebar.jsp"/>
    </div>

    <c:if test="${ empty order}">
        <p>Chưa có đơn hàng</p>
    </c:if>

    <c:if test="${not empty order}">
        <div class="content">

            <div id="order_header" class="row mid_align">
                <i id="back_btn" class="back_btn fa-solid fa-arrow-left-long"
                   data-src="../../src/main/webapp/fontend/component/user_order/User_order.html"></i>

                <span class="title">Lịch sử đơn hàng</span>
            </div>

            <div id="order_body">


                <div id="order_infor" class=" col">
                    <div class="wrap row">
                        <div class="order_id" data-order-id="${order.id}">Mã đơn hàng: <span>${order.id}</span></div>

                        <c:if test="${order.orderStatus =='DELIVERY'}">
                            <div class="order_status mid_align" style="color: #0a7cff">Đang giao hàng</div>
                        </c:if>

                        <c:if test="${order.orderStatus =='DELIVERED'}">
                            <div class="order_status mid_align">Đã giao hàng</div>
                        </c:if>
                    </div>
                    <!--        <div class="date">22/10/2024</div>-->
                </div>


                <div id="wrap_order_item" class=" col">


                    <c:if test="${empty orderDetails}">
                        <p>Đã xảy ra lỗi</p>
                    </c:if>


                    <c:if test="${not empty orderDetails}">
                        <c:forEach var="od" items="${orderDetails}">

                            <div class="order_item mid_align row" data-product-id="${od.productId}">


                                <div class="image">
                                    <img src="${od.imageUrl}"/>
                                </div>


                                <div class="description mid_align col  ">
                                    <div class="title bold">${od.productName}</div>

                                    <div class="color">
                                        <span class="color_name">Màu Sắc: <span>Đen</span></span>
                                    </div>

                                    <div class="quantity">
                                        <span class="color_name">Số lượng: <span>${od.quantity}</span></span>
                                    </div>


                                </div>


                                <div class="section_price mid_align col  ">

                                    <div class="wrap_price col">
                                        <span class="title">Tổng thanh toán: </span>
                                        <span class="price">
                                         <fmt:formatNumber value="${od.total}" pattern="#,###"/> VND

                                     </span>
                                    </div>
                                </div>


                            </div>

                        </c:forEach>
                    </c:if>


                    <div id="status_detail" class=" row">

                        <div class="item_status section1 col">
                            <i class="fa-solid fa-box-archive"></i>
                            <span>Đặt hàng thành công </span>
                            <span class="status_date">19/10/2024</span>
                        </div>

                        <div class="line"></div>

                        <div class="item_status section2 col">
                            <i class="fa-solid fa-truck-arrow-right"></i>
                            <span>Đã xác nhận đơn</span>
                            <span class="status_date">19/10/2024</span>
                        </div>


                        <div class="line"></div>

                        <div class="item_status section3 col">
                            <i class="fa-solid fa-truck-fast"></i>
                            <span>Đang vận chuyển</span>
                            <span class="status_date">20/10/2024</span>
                        </div>

                        <div class="line"></div>

                        <div class="item_status section4 col">
                            <i class="fa-solid fa-box-open"></i>
                            <span>Đã nhận được hàng</span>
                            <span class="status_date">22/10/2024</span>
                        </div>
                    </div>


                    <div id="payment_infor" class="col">

                        <div class="title">
                            <span>Thông tin thanh toán </span>
                        </div>

                        <div class="content">


                            <div class="content_item">
                                <span class="desc">Giá sản phẩm</span>
                                <span id="product_price" class="value">0 VND</span>
                            </div>


                            <div class="content_item">
                                <span class="desc">Thuế GTGT ( 10% )</span>
                                <span id="VAT" class="value">0 VND</span>
                            </div>

                            <div class="rec_horizontal"></div>

                            <div class="content_item">
                                <span class="desc">Đã thanh toán:</span>
                                <span id="total_charge" class="value">
                                     <fmt:formatNumber value="${order.total}" pattern="#,###"/> VND
                                </span>
                            </div>


                        </div>


                    </div>


                    <c:if test="${not empty user}">
                        <div id="customer" class="col">

                            <div class="title">
                                <span>Thông tin khách hàng</span>
                            </div>

                            <div class="content row">

                                <div class="section_left col">


                                    <div class="content_item full_name">
                                        <i class="fa-regular fa-user"></i>
                                        <span> ${address.fullName} </span>
                                    </div>

                                    <div class="content_item phone">
                                        <i class="fa-solid fa-phone"></i>
                                        <c:if test="${not empty address.phoneNumber}">
                                            <span>${address.phoneNumber}</span>
                                        </c:if>
                                    </div>

                                    <div class="content_item mail">
                                        <i class="fa-regular fa-envelope"></i>
                                        <span>${user.email}</span>
                                    </div>

                                    <div class="content_item address">
                                        <i class="fa-solid fa-location-dot"></i>

                                        <c:if test="${not empty address}">
                                            <span>${address.street}, ${address.city}, ${address.state}, ${address.country}
                                             </span>
                                        </c:if>


                                    </div>


                                </div>


                                <div class="section_right">
                                    <div class="title">
                                        <span>Phương thức thanh toán:</span>
                                    </div>

                                    <div class="content col">

                                        <c:if test="${ not empty card}">
                                            <span>${card.type} : **** ${card.last4}</span>
                                            <span>Thời hạn: ${card.duration}.</span>
                                        </c:if>


                                        <c:if test="${COD == true }">
                                            <span>Thanh toán khi nhận hàng</span>

                                        </c:if>


                                    </div>

                                </div>


                            </div>


                        </div>
                    </c:if>


                </div>
            </div>


            <div class="write-review-section">
                <h2>Viết đánh giá của bạn</h2>
                <form id="review-form" method="post">
                    <div class="input-group">
                        <label for="rating-value"> </label>
                        <div id="user-rating" class="stars">

                            <span class="star" data-value="1">
                                 <i class="fa-solid fa-star"></i>
                            </span>

                            <span class="star" data-value="2">
                                <i class="fa-solid fa-star"></i>
                            </span>

                            <span class="star" data-value="3">
                                <i class="fa-solid fa-star"></i>
                            </span>

                            <span class="star" data-value="4">
                                <i class="fa-solid fa-star"></i>
                            </span>

                            <span class="star" data-value="5">
                                <i class="fa-solid fa-star"></i>
                            </span>


                        </div>
                        <input type="hidden" id="rating-value" name="rating" value="0">
                    </div>

                    <div class="input-group">
                        <label for="review-text">Viết đánh giá:</label>
                        <textarea id="review-text" name="review" rows="4" placeholder="Chia sẻ suy nghĩ của bạn"
                                  required></textarea>
                    </div>

                    <div class="send">
                        <button id="btn_submit">Gửi đánh giá</button>
                    </div>


                </form>
            </div>

        </div>
    </c:if>


</div>

</body>
</html>
