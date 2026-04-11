<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 12/30/2025
  Time: 12:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Page</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/style-component/product-detail/Product-buying-tool.css">
    <script src="<%= request.getContextPath() %>/static/style-component/product-detail/Product-buying-tool.js"></script>
</head>
<body>

<div class="container">
    <div class="product-title">
        <%= "Pediasure hương vani 1.6kg cho bé từ 1-10 tuổi" %>
    </div>
    <a href="<%= request.getContextPath() %>/link-Đánh giá" class="product-rating">
        ★★★☆☆ <span>(<%= "3.0 / 10 đánh giá" %>)</span>
    </a>

    <div class="product-features">
        <ul>
            <li>
                <div>Tình trạng: Còn hàng</div>
                <div>Mã sản phẩm: 105215</div>
                <div>Thương hiệu: Pediasure</div>
                <div>Dòng sản phẩm: Sữa bột trẻ em</div>
                <div>Liên hệ với tư vấn viên online để biết thêm chi tiết về ưu đãi hoặc để nhận hướng dẫn mua hàng.
                </div>
            </li>
<%--            <li>*Khuyến mại trên áp dụng khi mua hàng trực tiếp trên Website và không áp dụng FREESHIP--%>
<%--                đối với sản phẩm ngày vàng / preorder--%>
<%--            </li>--%>
        </ul>
    </div>

<%--    <div class="option-title">Chọn Màu Sắc</div>--%>
<%--    <div class="color-options">--%>
<%--        <div class="color-option black"></div>--%>
<%--        <div class="color-option silver"></div>--%>
<%--    </div>--%>

    <div class="option-title">Chọn Khối Lượng</div>
    <div class="capacity-options">
        <div class="capacity-option">350g</div>
        <div class="capacity-option">600g</div>
        <div class="capacity-option">800g</div>
    </div>
    <div class="price">
        <%= " 1.139.000 VND" %>
    </div>
    <!--    <button class="btn-add-to-cart">Thêm vào giỏ hàng</button>-->
</div>

</body>
</html>