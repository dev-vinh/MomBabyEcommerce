<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 12/30/2025
  Time: 12:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style_product/SearchProductItem.css">--%>
    <%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/fontawesome/css/all.css">--%>
</head>
<body>
<div class="wrap mid_align row">

    <div class="img_section">
        <img src="${pageContext.request.contextPath}/static/image/sp1.png"  alt=""/>
    </div>


    <div class="infor_section">

        <div class="infor_name bold f22" id="name">
            <span>Body SS DT UR2079 gấu ghi Uala Rogo 22ST06</span>
        </div>


        <div class="infor_color col">
            <span  class="bold f16">Màu Sắc: <span class="normal f16" >Xám</span></span>

            <div class="choose_color row">
                <div class="col_item" id="pink"></div>
                <div class="col_item" id="gray"></div>
                <div class="col_item" id="yellow"></div>
            </div>


        </div>

        <div class="rating row mid_align">

            <span class="star" >
                <span class="star_empty"></span>
                <span class="star_fill"></span>
            </span>


            <span class="star" >
                <span class="star_empty "></span>
                <span class="star_fill"></span>
            </span>

            <span class="star " >
                <span class="star_empty"></span>
                <span class="star_fill"></span>
            </span>

            <span class="star" >
                <span class="star_empty"></span>
                <span class="star_fill"></span>
            </span>


            <span class="star" >
                <span class="star_empty"></span>
                <span class="star_fill"></span>
            </span>

            <span id="ratting" class="bold" style="padding: 0 5px"> 5 </span>
            <span id="noOfRatting"  class="bold" >(153)</span>

        </div>


        <div id="description">
            <ul class="list_descriptions">
                <li class="desc_item f14" >Bộ quần áo dài êm mềm giúp giữ ấm cơ thể bé, đặc biệt là lưng và bụng</li>
                <li class=" desc_item f14">Áo cổ tròn, phom suông giúp ba mẹ dễ dàng thay ra mặc vào cho bé</li>
                <li class="desc_item f14">Hoạ tiết độc đáo, giúp vẻ ngoài của bé thêm đáng yêu và năng động </li>
            </ul>
        </div>

    </div>



    <div class="rec_vertical"></div>



    <div class="section_right col">
        <div class="price">
            <span class="bold f22">190.000 VND</span>
        </div>

        <div class="service">
            <div class="service_item">
                <i class="fa-solid fa-gift"></i>
                <span>Sản phẩm được đảm bảo chất lượng tốt</span>
            </div>

            <div class="service_item">
                <i class="fa-solid fa-truck"></i>
                <span>Miễn Phí Vận Chuyển Toàn Quốc</span>
            </div>

            <div class="service_item">
                <i class="fa-solid fa-box-open"></i>
                <span>Đổi trả trong 14 ngày </span>
            </div>


        </div>



        <div class="wrap_btn col">
            <button class="btn buy" id="buy-now-btn">Mua Ngay</button>
            <button class="btn add">Thêm vào giỏ hàng</button>
        </div>

        <div id="cart-notification" class="notification hidden">
            <i class="fa fa-check-circle"></i>
            <span>Thêm vào giỏ hàng thành công</span>
        </div>

    </div>








</div>

<script src="${pageContext.request.contextPath}/static/style-component/style_product/SearchProductItem.js"></script>
</body>
