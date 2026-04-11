<%--
  Created by IntelliJ IDEA.
  User: aho4dot0
  Date: 27/12/2025
  Time: 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>Chi tiết món hàng trong giỏ hàng</title>
</head>
<body>
    <div class="wrap mid_align row">
    <input type="checkbox">
    <div class="image">
        <img src="${pageContext.request.contextPath}static/image/default_img.jpg" alt=""/>

    </div>

<%--    p là biến cho item sản phẩm chi tiết đổi tên--%>
    <div class="description mid_align col  ">
        <div class="title ">${item.name}</div>

<%--        <div class="color">--%>
<%--            <span class="color_name">Màu Sắc: <span>Đen</span></span>--%>
<%--        </div>--%>

        <div class="status">
            <span class="status_type">Còn hàng</span>
        </div>

    </div>


    <div class="section_price mid_align col  ">
        <span class="price">  <fmt:formatNumber value="${item.price}" pattern="#,###"/> VND</span>


        <div class="quantity mid_align row">
            <i class="fa-solid fa-minus"></i>
            <span class="num mid_align">
                ${item.quantity}
            </span>
            <i class="fa-solid fa-plus"></i>

        </div>

        <div class="remove">
            <i class=" del fa-solid fa-trash-can"></i>
        </div>


    </div>


</div>
</body>
</html>
