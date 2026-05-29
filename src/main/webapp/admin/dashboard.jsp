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
    <script src="${pageContext.request.contextPath}/static/style-component/style-admin/dashboard/dashboard.js"></script>
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
        <div class="welcome_section">
            <div class="welcome_text">
                <p>Tổng quan MomBaby Ecommerce</p>
            </div>
            <div class="current_date">
                <i class="fa-solid fa-calendar-days"></i>
                <span id="currentDate"></span>
            </div>
        </div>

        <div class="stats_grid">
            <div class="stat_card revenue">
                <div class="stat_icon">
                    <i class="fa-solid fa-dollar-sign"></i>
                </div>
                <div class="stat_info">
                    <span class="stat_label">Tổng Doanh Thu</span>
                    <span class="stat_value">168.500.000đ</span>
                    <span class="stat_change increase">
                        <i class="fa-solid fa-arrow-up"></i> 12.2%
                    </span>
                </div>
            </div>

            <div class="stat_card orders">
                <div class="stat_icon">
                    <i class="fa-solid fa-cart-shopping"></i>
                </div>
                <div class="stat_info">
                    <span class="stat_label">Tổng Đơn Hàng</span>
                    <span class="stat_value">342</span>
                    <span class="stat_change increase">
                        <i class="fa-solid fa-arrow-up"></i> 8.5%
                    </span>
                </div>
            </div>

            <div class="stat_card products">
                <div class="stat_icon">
                    <i class="fa-solid fa-box"></i>
                </div>
                <div class="stat_info">
                    <span class="stat_label">Sản Phẩm</span>
                    <span class="stat_value">1.256</span>
                    <span class="stat_change decrease">
                        <i class="fa-solid fa-arrow-down"></i> 3.2%
                    </span>
                </div>
            </div>

            <div class="stat_card customers">
                <div class="stat_icon">
                    <i class="fa-solid fa-users"></i>
                </div>
                <div class="stat_info">
                    <span class="stat_label">Khách Hàng</span>
                    <span class="stat_value">2.847</span>
                    <span class="stat_change increase">
                        <i class="fa-solid fa-arrow-up"></i> 15.8%
                    </span>
                </div>
            </div>
        </div>

        <div class="inventory_alert">
            <div class="alert_header">
                <div class="alert_title">
                    <i class="fa-solid fa-triangle-exclamation"></i>
                    <h3>Cảnh Báo Kho Hàng</h3>
                </div>
                <div class="alert_header_right">
                    <span class="alert_count danger">sản phẩm sắp hết</span>
                    <a href="inventory" class="alert_link">Xem chi tiết</a>
                </div>
            </div>
            <div class="alert_list">

            </div>
        </div>

        <div class="content_grid">
            <div class="chart_section">
                <div class="section_header">
                    <h3>Thống Kê Đơn Hàng</h3>
                    <div class="chart_filters">
                        <button class="chart-btn active">Theo Tháng</button>
                        <button class="chart-btn">Theo Tuần</button>
                        <button class="chart-btn">Theo Ngày</button>
                    </div>
                </div>
                <div class="chart_container">
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>

            <div class="recent_orders">
                <div class="section_header">
                    <h3>Đơn Hàng Gần Đây</h3>
                    <a href="orders" class="view_all">Xem tất cả</a>
                </div>
                <table class="orders_table">
                    <thead>
                        <tr>
                        </tr>
                    </thead>
                    <tbody>

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
                                <td>Xu Hướng</td>
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
                                        4.8
                                        <i class="fa-solid fa-star" style="color: #FFD43B;"></i>
                                    </td>
                                    <td class="wrap_trend_up">
                                        <i class="fa-solid fa-arrow-trend-up"></i>
                                        <span>4.5%</span>
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
</body>
</html>
