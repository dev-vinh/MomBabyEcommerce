<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 5/20/2026
  Time: 11:57 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>QL Đơn Hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/orders/order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
</head>
<body>
<div class="header">
    <jsp:include page="header.jsp"/>
</div>
<div class="container">
    <div class="left">
        <div class="side_bar">
            <jsp:include page="sidebar.jsp"/>
        </div>
    </div>

    <div class="center">
        <div class="wrap_content">
            <div class="row">
                <h1 class="header-title">Đơn Hàng</h1>
            </div>
            <div class="content">
                <div class="header-container">
                    <div class="search-bar">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                             stroke="currentColor" class="size-6">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                  d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"/>
                        </svg>
                        <input type="text" placeholder="Tìm kiếm" id="order-search-input">
                    </div>

                    <select class="status-select" id="statusFilter">
                        <option value="ALL">Mặc Định</option>
                        <option value="PENDING">Chờ xác nhận</option>
                        <option value="CONFIRMED">Đã xác nhận</option>
                        <option value="PROCESSING">Đang đóng gói</option>
                        <option value="SHIPPING">Đang giao hàng</option>
                        <option value="DELIVERED">Đã giao hàng</option>
                        <option value="CANCELLED">Đã huỷ</option>
                    </select>

                    <div class="header-actions">
                        <div class="export-container">
                            <a href="orders?action=export" class="export-btn">
                                <i class="fas fa-file-excel"></i>
                                Xuất Excel
                            </a>
                        </div>
                    </div>
                </div>

                <div class="table-container">
                    <table>
                        <thead>
                        <tr>
                            <th><input type="checkbox"></th>
                            <th>Mã Đơn Hàng</th>
                            <th>Tên Khách Hàng</th>
                            <th>Ngày</th>
                            <th>Thanh Toán</th>
                            <th>Tổng Tiền</th>
                            <th>Trạng Thái</th>
                            <th>Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${not empty orders}">
                            <c:forEach items="${orders}" var="o">
                                <tr class="order-row"  data-status="${o.orderStatus}">
                                    <td><input type="checkbox"></td>
                                    <td><a href="order-detail?orderId=${o.id}" class="order-id">#${o.id}</a></td>
                                    <td class="order-name">${o.userName}</td>
                                    <td class="order-date">${o.createAt}</td>
                                    <td>
                                        <c:if test="${o.paymentStatus == 'PAID'}">
                                            <span class="status payment-status-paid">Đã Thanh Toán</span>
                                        </c:if>
                                        <c:if test="${o.paymentStatus == 'PENDING'}">
                                            <span class="status payment-status-pending">Chưa Thanh Toán</span>
                                        </c:if>
                                    </td>
                                    <td class="order-total">
                                        <fmt:formatNumber value="${o.total + o.shippingFee}" pattern="#,###"/> VND
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${o.orderStatus == 'PENDING'}">
                                                <span class="status order-status-pending">Chờ xác nhận</span>
                                            </c:when>
                                            <c:when test="${o.orderStatus == 'CONFIRMED'}">
                                                <span class="status order-status-pending">Đã xác nhận</span>
                                            </c:when>
                                            <c:when test="${o.orderStatus == 'PROCESSING'}">
                                                <span class="status order-status-in-progress">Đang đóng gói</span>
                                            </c:when>
                                            <c:when test="${o.orderStatus == 'SHIPPING'}">
                                                <span class="status order-status-shipped">Đang giao hàng</span>
                                            </c:when>
                                            <c:when test="${o.orderStatus == 'DELIVERED'}">
                                                <span class="status order-status-delivered">Đã giao hàng</span>
                                            </c:when>
                                            <c:when test="${o.orderStatus == 'CANCELLED'}">
                                                <span class="status order-status-failed">Đã huỷ</span>
                                            </c:when>
                                            <c:when test="${o.orderStatus == 'RETURNED'}">
                                                <span class="status order-status-failed">Trả hàng</span>
                                            </c:when>
                                            <c:when test="${o.orderStatus == 'FAILED'}">
                                                <span class="status order-status-failed">Giao hàng thất bại</span>
                                            </c:when>
                                            <c:when test="${o.orderStatus == 'CANCEL_ERROR'}">
                                                <span class="status order-status-failed">Huỷ đơn hàng thất bại</span>
                                            </c:when>
                                            <c:when test="${o.orderStatus == 'ORDER_CREATE_ERROR'}">
                                                <span class="status order-status-failed">Tạo đơn hàng thất bại</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status order-status-failed">Đang cập nhật</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                    <td>
                                        <div class="action-buttons">

                                            <a href="order-detail?orderId=${o.id}"
                                               class="btn-view">

                                                <i class="fa-solid fa-eye"></i>
                                            </a>

                                            <button type="button"
                                                    class="btn-edit"
                                                    data-id="${o.id}"
                                                    data-status="${o.orderStatus}">
                                                Chỉnh sửa
                                            </button>

                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                </div>

                <div class="footer-container">
                    <nav class="mt-2 mt-md-0">
                        <ul class="pagination mb-0">
                            <li class="page-item"><a class="page-link" href="#!" data-action="prev">Quay lại</a></li>
                            <li class="page-item"><a class="page-link active" href="#!">1</a></li>
                            <li class="page-item"><a class="page-link" href="#!">2</a></li>
                            <li class="page-item"><a class="page-link" href="#!">3</a></li>
                            <li class="page-item"><a class="page-link" href="#!" data-action="next">Tiếp theo</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="editOrderModal" class="modal">

    <div class="modal-content">

        <div class="modal-header">

            <h3>Cập nhật trạng thái</h3>
            <span class="close" id="closeModalBtn">&times;</span>
        </div>

        <form action="${pageContext.request.contextPath}/admin/order-detail"
              method="post">

            <input type="hidden"
                   name="action"
                   value="update-status">

            <input type="hidden"
                   name="orderId"
                   id="edit-order-id">

            <div class="form-group">

                <label>Trạng thái</label>

                <select name="status"
                        id="edit-order-status">

                    <option value="PENDING">
                        Chờ xác nhận
                    </option>

                    <option value="CONFIRMED">
                        Đã xác nhận
                    </option>

                    <option value="PROCESSING">
                        Đang đóng gói
                    </option>

                    <option value="SHIPPING">
                        Đang giao hàng
                    </option>

                    <option value="DELIVERED">
                        Đã giao hàng
                    </option>

                    <option value="CANCELLED">
                        Đã hủy
                    </option>
                </select>
            </div>
            <button type="submit"
                    class="save-btn">

                Cập nhật
            </button>
        </form>
    </div>
</div>
<script src="${pageContext.request.contextPath}/static/style-component/style-admin/orders/order.js"></script>
</body>
</html>