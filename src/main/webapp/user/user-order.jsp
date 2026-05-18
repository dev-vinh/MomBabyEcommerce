<%--
  Created by IntelliJ IDEA.
  User: fileh
  Date: 1/3/2026
  Time: 12:03 PM
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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_order/UserOrder.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_order/OrderHistoryItem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">




</head>
<body>

<div class="header">
    <jsp:include page="/home/header.jsp"/>
</div>

<div class="container">
    <div class="sidebar">
        <jsp:include page="user-sidebar.jsp"/>
    </div>


    <%--    Order History Item--%>
    <div class="content">

        <div id="order_header" class="mid_align row">
            <div class="no_of_order col mid_align">
                <span class="num">
                    ${count}
                </span>
                <span>Đơn hàng</span>
            </div>
        </div>


        <div id="order_menu" class="mid_align row">

            <a class="menu_item ${empty param.status ? 'active' : ''}"
               href="user-order">
                Tất cả
            </a>

            <a class="menu_item ${param.status == 'PENDING' ? 'active' : ''}"
               href="user-order?status=PENDING">
                Chờ xác nhận
            </a>

            <a class="menu_item ${param.status == 'CONFIRMED' ? 'active' : ''}"
               href="user-order?status=CONFIRMED">
                Đã xác nhận
            </a>

            <a class="menu_item ${param.status == 'SHIPPING' ? 'active' : ''}"
               href="user-order?status=SHIPPING">
                Đang vận chuyển
            </a>

            <a class="menu_item ${param.status == 'DELIVERED' ? 'active' : ''}"
               href="user-order?status=DELIVERED">
                Đã giao hàng
            </a>

            <a class="menu_item ${param.status == 'CANCELLED' ? 'active' : ''}"
               href="user-order?status=CANCELLED">
                Đã hủy
            </a>

        </div>


        <div id="order_body">

            <c:if test="${empty orders}">
                <div class="no_order">Hiện tại không có đơn hàng nào.</div>
            </c:if>
            <c:if test="${not empty orders}">
                <c:forEach var="o" items="${orders}">
                    <div id="order_container" class="mid_align row">
                        <div class="image">
                            <img src="${o.productImage}" alt="Hình ảnh sản phẩm"/>
                        </div>

                        <div class="description mid_align col">
                            <div class="title bold">${o.productName}</div>

                            <div class="quantity">
                                <span class="color_name">Số lượng: <span>${o.quantity}</span></span>
                            </div>
                            <div class="status">

                                <c:if test="${o.orderStatus == 'PENDING'}">
                                    <span style="color: orange;">Chờ xác nhận</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'CONFIRMED'}">
                                    <span style="color: #17a2b8;">Đã xác nhận</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'PROCESSING'}">
                                    <span style="color: #6f42c1;">Đang xử lý</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'SHIPPED'}">
                                    <span style="color: #20c997;">Đã bàn giao vận chuyển</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'SHIPPING'}">
                                    <span style="color: #0d6efd;">Đang vận chuyển</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'DELIVERY'}">
                                    <span style="color: #0a7cff;">Đang giao hàng</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'DELIVERED'}">
                                    <span style="color: green;">Đã giao hàng</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'CANCELLED'}">
                                    <span style="color: red;">Đã hủy</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'RETURNED'}">
                                    <span style="color: #dc3545;">Đã hoàn trả</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'FAILED'}">
                                    <span style="color: #b02a37;">Thất bại</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'ORDER_CREATE_ERROR'}">
                                    <span style="color: #e55353;">Lỗi tạo đơn</span>
                                </c:if>

                                <c:if test="${o.orderStatus == 'CANCEL_ERROR'}">
                                    <span style="color: #ff4d4f;">Lỗi hủy đơn</span>
                                </c:if>

                            </div>
                        </div>

                        <div class="section_price mid_align col">
                            <div class="date">
                                <span>${o.createAt}</span>
                            </div>
                            <div class="wrap_price col">
                                <span class="title">Tổng thanh toán: </span>
                                <span class="price">
                                    <fmt:formatNumber value="${o.total}" pattern="#,###"/> VND
                                </span>

                            </div>
                            <div class="btn col">
                                <a href="user-order-detail?orderId=${o.id}">
                                    <button class="btn_detail">Xem chi tiết</button>
                                </a>
                                <button type="button"
                                        class="btn_support"
                                        onclick="cancelOrder(${o.id})">
                                    Hủy đơn
                                </button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>

        </div>


    </div>

    <script src="${pageContext.request.contextPath}/static/style-component/style-user_order/UserOrder.js"></script>
    <script src="${pageContext.request.contextPath}/static/style-component/style-user_order/OrderHistoryItem.js"></script>
</body>
</html>
