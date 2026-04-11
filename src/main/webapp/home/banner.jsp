<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 12/30/2025
  Time: 11:25 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-home/banner.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

</head>
<button class="pause-play-btn" id="pausePlayBtn">
    <i class="fa-solid fa-pause"></i>
</button>

<div class="indicator" id="indicator1">
    <div class="progress-loader" >

        <div class="progress"></div>
    </div>
    <div class="progress-loader" >

        <div class="progress"></div>
    </div>
    <div class="progress-loader" >

        <div class="progress"></div>
    </div>
    <div class="progress-loader" >

        <div class="progress"></div>
    </div>
</div>

<div class="banner-container">
    <div class="banner-slide active" id="slide-1"
         style="background-image: url('${pageContext.request.contextPath}/static/image/thudongbanner.jpg');">
        <div class="overlay"></div>
        <div class="banner-content">
            <h2>Bộ sưu tập Thu Đông 2025</h2>
            <p>Dòng sữa công thức mới - Tăng cường miễn dịch</p>
            <!--            <button class="buy-now-btn">Mua ngay</button>-->
        </div>
    </div>

    <div class="banner-slide" id="slide-2" style="background-image: url('${pageContext.request.contextPath}/static/image/giamgia.png');">
        <div class="overlay"></div>
        <div class="banner-content">
            <h2>Tuần lễ Bỉm sữa - Giảm giá đến 50%</h2>
            <p>Chào hè rực rỡ - Sale hết cỡ đồ sơ sinh, váy áo cho mẹ bầu</p>
            <!--            <button class="buy-now-btn">Mua ngay</button>-->
        </div>
    </div>

    <div class="banner-slide" id="slide-3" style="background-image: url('${pageContext.request.contextPath}/static/image/spuachuong.png');">
        <div class="overlay"></div>
        <div class="banner-content">
            <h2>Top sản phẩm dược ưu chuộng</h2>
            <p>Hàng chính hãng 100% - An toàn cho bé - Dịch vụ tận tâm</p>
            <!--            <button class="buy-now-btn">Mua ngay</button>-->
        </div>
    </div>

    <div class="banner-slide" id="slide-4" style="background-image: url('${pageContext.request.contextPath}/static/image/tabanner.jpg');">
        <div class="overlay"></div>
        <div class="banner-content">
            <h2>Chăm sóc dịu nhẹ cho bé</h2>
            <p>Thấm hút vượt trội - Mềm mại thoáng khí - Chống hăm hiệu quả</p>
            <!--            <button class="buy-now-btn">Mua ngay</button>-->
        </div>
    </div>

    <button class="nav-btn prev-btn">❮</button>
    <button class="nav-btn next-btn">❯</button>


</div>
</body>
<script src="${pageContext.request.contextPath}/static/style-component/style-home/banner.js"></script>


