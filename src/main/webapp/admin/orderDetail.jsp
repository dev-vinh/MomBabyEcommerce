<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 1/7/2026
  Time: 12:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi Tiết Đơn Hàng</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/orderDetail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
</head>
<body>

<div class="header">
    <jsp:include page="/home/header.jsp"/>
</div>

<div class="container">


    <div class="sidebar">
        <jsp:include page="sidebar.jsp"/>
    </div>


    <c:if   test="${not empty order}">
        <div class="content">
            <div class="row">
                <h2 class="header-title">Chi Tiết Đơn Hàng</h2>
            </div>

            <div class="order-details">


                <div class="order-header">
                    <div class="order-progress">
                        <c:set var="statusStep">
                            <c:choose>
                                <c:when test="${order.orderStatus == 'PENDING'}">1</c:when>
                                <c:when test="${order.orderStatus == 'CONFIRMED'}">2</c:when>
                                <c:when test="${order.orderStatus == 'PROCESSING'}">2</c:when>
                                <c:when test="${order.orderStatus == 'SHIPPED'}">3</c:when>
                                <c:when test="${order.orderStatus == 'DELIVERED'}">4</c:when>
                                <c:when test="${order.orderStatus == 'CANCELLED'}">-1</c:when>
                                <c:when test="${order.orderStatus == 'RETURNED'}">99</c:when>
                                <c:when test="${order.orderStatus == 'FAILED'}">99</c:when>

                            </c:choose>
                        </c:set>
                        <div class="step ${statusStep >= 1 ? 'completed' : ''} ">
                            <div class="circle"></div>
                            <span class="label">Đặt hàng thành công</span>
                        </div>

                        <div class="line"></div>

                        <div class="step ${statusStep >= 2 ? 'completed' : ''} ">
                            <div class="circle "></div>
                            <span class="label">Chuẩn bị hàng</span>
                        </div>
                        <div class="line  "></div>

                        <div class="step ${statusStep >= 3 ? 'completed' : ''} ">
                            <div class="circle "></div>
                            <span class="label">Đang vận chuyển</span>
                        </div>
                        <div class="line  "></div>
                        <div class="step
                                <c:choose>
                                    <c:when test='${statusStep < 0}'>failed </c:when>
                                    <c:when test='${statusStep == 99}'>failed</c:when>
                                    <c:when test='${statusStep == 4}'>completed</c:when>
                                </c:choose>"
                        >

                            <div class="circle "></div>
                            <span class="label">
                                     <c:choose>
                                         <c:when test="${order.orderStatus == 'CANCELLED'}">Đơn hàng đã bị hủy</c:when>
                                         <c:when test="${order.orderStatus == 'RETURNED'}">Đã trả hàng</c:when>
                                         <c:when test="${order.orderStatus == 'FAILED'}">Giao hàng không thành công</c:when>
                                         <c:when test="${order.orderStatus == 'CANCELLED'}">Đơn hàng đã bị hủy</c:when>
                                         <c:otherwise>Đã nhận được hàng</c:otherwise>
                                     </c:choose>
                                </span>
                        </div>
                    </div>
                </div>

                <div class="order-summary-container">

                    <div class="order-info">
                        <h3>Mã Đơn Hàng: ${order.id}</h3>
                        <p>Ngày Đặt Hàng: ${order.createAt}
                            <c:if test="${order.paymentStatus =='PAID'}">
                                <span class="status-paid">Đã Thanh Toán</span>
                            </c:if>
                        </p>

                        <table class="products-table">
                            <thead>
                            <tr>
                                <th>Sản Phẩm</th>
                                <th>Mã Sản Phẩm</th>
                                <th>Số Lượng</th>
                                <th>Số Tiền</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${not empty orderDetails}">
                                <c:forEach var="od" items="${orderDetails}">

                                    <tr>
                                        <td style="text-align: left;">
                                            <div class="product-info">
                                                <img src="${od.imageUrl}"
                                                     alt="${od.productName}" class="product-image">
                                                <span>${od.productName}</span><br>

                                            </div>
                                        </td>
                                        <td>${od.productId}</td>
                                        <td>${od.quantity}</td>
                                        <td>
                                            <fmt:formatNumber value="${od.total}" pattern="#,###"/> VND
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div>

                    <div class="summary-details">

                        <div class="order">

                            <h3>Tóm Tắt Đơn Hàng</h3>
                            <p> Tổng giá trị:
                                <span id="before_tax"><fmt:formatNumber value="${order.total}" pattern="#,###"/> VND</span>
                            </p>

                            <p> Phí vận chuyển:
                                <span><fmt:formatNumber value="${order.shippingFee}" pattern="#,###"/> VND</span>
                            </p>

                            <p class="total-amount">Tổng Số Tiền:
                                <span id="total_charge" data-total="${order.total }">
                                    <fmt:formatNumber value="${order.total  + order.shippingFee}" pattern="#,###"/> VND
                                </span>
                            </p>
                        </div>

                        <c:if test="${not empty user}">
                            <div class="payment">
                                <h3>Chi Tiết Thanh Toán</h3>
                                <p>Trạng thái thanh toán:
                                    <c:if test="${order.paymentStatus =='PAID'}">
                                        <span class="payment-status-paid    ">Đã Thanh Toán</span>
                                    </c:if>

                                    <c:if test="${order.paymentStatus =='PENDING'}">
                                        <span class="payment-status-pending">Chưa Thanh Toán</span>
                                    </c:if>
                                </p>

                                <p>Phương Thức Thanh Toán:
                                    <c:if test="${order.COD == false}">
                                        <span> Thẻ Tín Dụng</span>
                                    </c:if>

                                    <c:if test="${order.COD == true}">
                                        <span>Thanh toán khi nhận hàng</span>
                                    </c:if>

                                </p>
                                <p>Tên khách hàng: <span>${user.fullName}</span></p>
                                <p>Email: <span>${user.email}</span></p>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

</div>
<script src="${pageContext.request.contextPath}/static/style-component/style-admin/orderDetail.js"></script>
</body>
</html>
