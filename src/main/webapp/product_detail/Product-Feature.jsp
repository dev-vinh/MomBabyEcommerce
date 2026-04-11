    <%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 12/27/2025
  Time: 10:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Feature</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/style-component/product-detail/Product-Feature.css">
</head>
<body>
<div class="feature-benefit">
    <div class="feature-benefit__text">
        <h2> Bộ quần áo dài êm mềm giúp giữ ấm cơ thể bé, đặc biệt là lưng và bụng</h2>
        <h3> Chun quần mềm mại, co giãn tốt cho bé thoả sức vui chơi, vận động</h3>
        <p>Chất liệu 100% vải cotton mềm mại, an toàn và có khả năng thấm hút mồ hôi vượt trội.
        </p>
        <ul>
            <li>Sợi bông có khả năng hút ẩm vượt trội, giúp thấm mồ hôi nhanh</li>
            <li>Cotton tự nhiên mềm nhẹ như bông bảo vệ làn da của bé</li>
        </ul>
    </div>
    <div class="feature-benefit__img">
        <img src="<%= request.getContextPath() %>/static/image/sp1.png" alt="Carousel Image1" class="imgFeature1">
    </div>
<%--    them vai cai nua--%>
</div>
</body>
</html>
