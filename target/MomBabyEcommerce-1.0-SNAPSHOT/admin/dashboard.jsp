<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 12/03/2026
  Time: 7:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/dashboard/Dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>

<div class="wrap_header">
    <jsp:include page="header.jsp"/>
</div>

<div class="container">

    <div class="side_bar">
        <jsp:include page="/admin/sidebar.jsp"/>
    </div>

    <div class=" wrap">
        <div class="title ">
            <h2>Tổng quan tháng 12 </h2>
        </div>

        <div class="report row mid_align">


            <div class="report_item col">
                    <span class="title row">Tổng Doanh Thu
                        <div class="status increase row ">
                            <i class="fa-solid fa-arrow-trend-up"></i>
                            <span>12.2%</span>
                        </div>
                    </span>
                <span class="value">0 <span class="f16">VND</span></span>
            </div>
            <div class="report_item col">
            <span class="title row">Tổng Lượt Truy Cập
                <div class="status decrease ">
                <i class="fa-solid fa-arrow-trend-down"></i>
                <span>8.2%</span>
            </div>
            </span>
                <span class="value">320  </span>

            </div>

            <div class="report_item col">
            <span class="title row ">
                Số Lượng Đơn Hàng
                 <div class="status increase ">
                    <i class="fa-solid fa-arrow-trend-up"></i>
                    <span>4.5%</span>
                </div>

            </span>
                <span class="value">57 </span>

            </div>

            <div class="report_item col">
            <span class="title">
                Khách Hàng mới
                 <div class="status increase ">
                    <i class="fa-solid fa-arrow-trend-up"></i>
                    <span>10.0%</span>
                 </div>

            </span>
                <span class="value">231 </span>

            </div>
        </div>

        <c:if test="${not empty top10}">

            <div id="table_product">

                <table id="table">
                    <thead>

                    <tr>
                        <td>Top</td>
                        <td class="product_col">Sản Phẩm</td>
                        <td> Lượt Mua</td>
                        <td>Còn Lại</td>
                        <td>Số Lượt Xem</td>
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
        </c:if>


    </div>
</div>

</body>
</html>
