<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - MomBaby Ecommerce</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/dashboard/Dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
</head>
<body>

<div class="wrap_header">
    <jsp:include page="header.jsp"/>
</div>

<div class="container">

    <div class="side_bar">
        <jsp:include page="/admin/sidebar.jsp"/>
    </div>

    <div class="main_content">
        <div class="dashboard-filter">
            <form method="get" action="${pageContext.request.contextPath}/admin/dashboard">
                <div class="period_tabs">
                    <button type="submit" name="period" value="WEEK"
                            class="period_tab ${period == 'WEEK' ? 'active' : ''}">
                        <i class="fa-regular fa-calendar-week"></i> Tuần này
                    </button>
                    <button type="submit" name="period" value="MONTH"
                            class="period_tab ${period == 'MONTH' ? 'active' : ''}">
                        <i class="fa-regular fa-calendar"></i> Tháng này
                    </button>
                    <button type="submit" name="period" value="YEAR"
                            class="period_tab ${period == 'YEAR' ? 'active' : ''}">
                        <i class="fa-regular fa-calendar-days"></i> Năm nay
                    </button>
                </div>
            </form>
        </div>
        <div class="stats_grid">

            <div class="stat_card revenue">
                <div class="stat_icon">
                    <i class="fa-solid fa-dollar-sign"></i>
                </div>
                <div class="stat_info">
                    <span class="stat_label">
                        Doanh Thu
                        <c:choose>
                            <c:when test="${period == 'WEEK'}">Tuần</c:when>
                            <c:when test="${period == 'YEAR'}">Năm</c:when>
                            <c:otherwise>Tháng</c:otherwise>
                        </c:choose>
                    </span>
                    <span class="stat_value">
                        <fmt:formatNumber value="${not empty currentRevenue ? currentRevenue : 0}" pattern="#,###"/>đ
                    </span>
                    <div class="stat_footer">
                        <span id="revenueGrowthBadge" class="growth_badge"></span>
                        <span class="stat_compare">so với kỳ trước</span>
                    </div>
                </div>
            </div>

            <div class="stat_card orders">
                <div class="stat_icon">
                    <i class="fa-solid fa-cart-shopping"></i>
                </div>
                <div class="stat_info">
                    <span class="stat_label">
                        Đơn Hàng
                        <c:choose>
                            <c:when test="${period == 'WEEK'}">Tuần</c:when>
                            <c:when test="${period == 'YEAR'}">Năm</c:when>
                            <c:otherwise>Tháng</c:otherwise>
                        </c:choose>
                    </span>
                    <span class="stat_value">${not empty currentOrders ? currentOrders : 0}</span>
                    <div class="stat_footer">
                        <span id="ordersGrowthBadge" class="growth_badge"></span>
                        <span class="stat_compare">so với kỳ trước</span>
                    </div>
                </div>
            </div>

            <div class="stat_card products">
                <div class="stat_icon">
                    <i class="fa-solid fa-box"></i>
                </div>
                <div class="stat_info">
                    <span class="stat_label">Tổng Sản Phẩm</span>
                    <span class="stat_value">${not empty totalProducts ? totalProducts : 0}</span>
                    <span class="stat_change increase">
                        <i class="fa-solid fa-check"></i> Đang bán
                    </span>
                </div>
            </div>

            <div class="stat_card customers">
                <div class="stat_icon">
                    <i class="fa-solid fa-users"></i>
                </div>
                <div class="stat_info">
                    <span class="stat_label">Khách Hàng</span>
                    <span class="stat_value">${not empty totalCustomers ? totalCustomers : 0}</span>
                    <span class="stat_change increase">
                        <i class="fa-solid fa-user-plus"></i> Đăng ký
                    </span>
                </div>
            </div>
        </div>
        <c:if test="${lowStockCount > 0 || outOfStockCount > 0}">
            <div class="inventory_alert">
                <div class="alert_header">
                    <div class="alert_title">
                        <i class="fa-solid fa-triangle-exclamation"></i>
                        <div>
                            <h3>Sản phẩm sắp hết</h3>
                            <p class="alert_subtitle">
                                <c:if test="${outOfStockCount > 0}">
                                    <span class="alert_count danger">${outOfStockCount} hết hàng</span>
                                </c:if>
                            </p>
                        </div>
                    </div>
                    <div class="alert_header_right">
                        <a href="${pageContext.request.contextPath}/admin/inventory" class="alert_link">Xem chi tiết</a>
                    </div>
                </div>
                <div class="alert_list">
                    <c:forEach var="product" items="${lowStockProducts}">
                        <div class="alert_item ${product.stock == 0 ? 'danger' : 'warning'}">
                            <div class="alert_product">
                                <img src="${product.imageUrl}" alt="${product.name}">
                                <div class="alert_product_info">
                                    <span class="alert_product_name">${product.name}</span>
                                    <span class="alert_product_sku">SKU: ${product.sku}</span>
                                </div>
                            </div>
                            <div class="alert_stock">
                                <span class="stock_count ${product.stock == 0 ? 'danger' : 'warning'}">${product.stock}</span>
                                <span class="stock_label">${product.stock == 0 ? 'HẾT HÀNG' : 'còn lại'}</span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>

        <div class="content_grid">
            <div class="chart_section">
                <div class="section_header">
                    <h3>Thống Kê Doanh Thu</h3>
                </div>
                <div class="chart_container">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>

            <div class="recent_orders">
                <div class="section_header">
                    <h3>Đơn Hàng Gần Đây</h3>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="view_all">Xem tất cả</a>
                </div>
                <table class="orders_table">
                    <thead>
                    <tr>
                        <th>Mã ĐH</th>
                        <th>Khách Hàng</th>
                        <th>Ngày Đặt</th>
                        <th>Tổng Tiền</th>
                        <th>Trạng Thái</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${recentOrders}">
                        <tr>
                            <td>#DH${order.id}</td>
                            <td>${order.userName}</td>
                            <td>${order.createAt}</td>
                            <td><fmt:formatNumber value="${not empty order.total ? order.total : 0}" pattern="#,###"/>đ</td>
                            <td>
                                <c:choose>
                                    <c:when test="${order.orderStatus == 'DELIVERED'}">
                                        <span class="status completed">Hoàn Thành</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == 'SHIPPING'}">
                                        <span class="status shipping">Đang Giao</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == 'PROCESSING'}">
                                        <span class="status processing">Đang Xử Lý</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == 'CANCELLED'}">
                                        <span class="status cancelled">Đã Hủy</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status pending">Chờ Xác Nhận</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty recentOrders}">
                        <tr>
                            <td colspan="5" style="text-align: center; color: #888; padding: 20px;">Chưa có đơn hàng nào</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <c:if test="${not empty top10}">
            <div class="top_products_section">
                <div class="section_header">
                    <h3>Top Sản Phẩm Bán Chạy</h3>
                </div>
                <div id="table_product">
                    <table id="table">
                        <thead>
                        <tr>
                            <td>Top</td>
                            <td class="product_col">Sản Phẩm</td>
                            <td>Lượt Mua</td>
                            <td>Còn Lại</td>
                            <td>Lượt Xem</td>
                            <td>Đánh Giá</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="p" items="${top10}" varStatus="status">
                            <tr class="product_item">
                                <td class="rank">
                                    <i class="fa-solid fa-medal fa-xl
                                        <c:choose>
                                           <c:when test="${status.index == 0}">top1</c:when>
                                           <c:when test="${status.index == 1}">top2</c:when>
                                           <c:when test="${status.index == 2}">top3</c:when>
                                        </c:choose>"
                                    ></i>
                                </td>
                                <td class="product">
                                    <img class="product_image" src="${p.imageUrl}"/>
                                    <span class="product_name">${p.name}</span>
                                </td>
                                <td class="sold">${p.noOfSold}</td>
                                <td class="remaining">${p.stock}</td>
                                <td class="view">${p.noOfViews}</td>
                                <td class="rating">
                                    <i class="fa-solid fa-star" style="color: #FFD43B;"></i>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>

    </div>
</div>

<script>
    window.chartLabels = [
        <c:if test="${not empty revenueChart}">
        <c:forEach var="stat" items="${revenueChart}" varStatus="s">
        "${stat.label}"<c:if test="${!s.last}">,</c:if>
        </c:forEach>
        </c:if>
    ];
    window.chartData = [
        <c:if test="${not empty revenueChart}">
        <c:forEach var="stat" items="${revenueChart}" varStatus="s">
        ${not empty stat.revenue ? stat.revenue : 0}<c:if test="${!s.last}">,</c:if>
        </c:forEach>
        </c:if>
    ];
    window.revenueGrowth = ${not empty revenueGrowth ? revenueGrowth : 0};
    window.ordersGrowth  = ${not empty ordersGrowth  ? ordersGrowth  : 0};
</script>
<script src="${pageContext.request.contextPath}/static/style-component/style-admin/dashboard/dashboard.js"></script>
</body>
</html>
