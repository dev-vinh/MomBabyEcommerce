<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 3/21/2026
  Time: 7:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-user_order/OrderSuccess.css">
</head>
<body>
<div id="header" class="mid_align row">


    <div class="cart_header">
        <jsp:include page="/home/header.jsp"/>
    </div>


</div>

<div class="container mid_align  col">
    <div class="title">Đặt hàng thành công</div>
    <div class="image">
        <img alt="" src="${pageContext.request.contextPath}/static/image/delivery.jpg" height="7730" width="7730"/>
    </div>
    <a  id="btn"  href="home"   >Tiếp tục mua sắm</a>
<%--    điều hướng về trang home để mua tiếp -- to do--%>
<%--    chưa viết js--%>
</div>
</body>
</html>
