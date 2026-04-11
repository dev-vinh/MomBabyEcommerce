<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 12/30/2025
  Time: 12:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <title>Title</title>
</head>
<body>

<div id="search_body">

    <div class="wrap_img">
        <img id="image" src="${pageContext.request.contextPath}/static/image/sp1.png" alt="" height="225"
             width="225"/>
    </div>

    <div class="infor mid_align col ">
        <div id="name" class="bold f16">
            <a href="#"> Body SS DT UR2079 gấu ghi Uala Rogo 22ST06</a>
        </div>



        <div id="price" class="bold f22">
            190.000 ₫

            <span id="ratting" class="" style="padding: 0 5px">
                5 (153)
              <i class="fa-solid fa-star" style="color: #FFD43B;"></i>
            </span>

        </div>


        <div id="description">
            <ul class="list_descriptions">
                <li class="desc_item f12">Bộ quần áo dài êm mềm giúp giữ ấm cơ thể bé, đặc biệt là lưng và bụng</li>
                <li class=" desc_item f12">Áo cổ tròn, phom suông giúp ba mẹ dễ dàng thay ra mặc vào cho bé</li>
                <li class="desc_item f12">Hoạ tiết độc đáo, giúp vẻ ngoài của bé thêm đáng yêu và năng động</li>
            </ul>
        </div>

    </div>


    <div class="operation col">
        <button id="buy_now" onclick="checkLoginAndBuy(event)">Mua ngay</button>
        <button class="btn add">Thêm vào giỏ hàng</button>
    </div>
    <div id="cart-notification" class="notification hidden">
        <i class="fa-solid fa-circle-check"></i>
        <span>Thêm vào giỏ hàng thành công</span>
    </div>


</div>
<%--<script>--%>
<%--    function checkLoginAndBuy(event) {--%>
<%--        event.preventDefault();--%>
<%--        const sessionId = sessionStorage.getItem("session_id");--%>
<%--        if (!sessionId) {--%>
<%--            alert("Bạn cần đăng nhập trước khi mua hàng!");--%>
<%--            return;--%>
<%--        }--%>

<%--        // Gọi API BuyNowController--%>
<%--        fetch("buy-now", {--%>
<%--            method: "POST",--%>
<%--            headers: {--%>
<%--                "Content-Type": "application/x-www-form-urlencoded",--%>
<%--            },--%>
<%--            // xem lại ben controller--%>
<%--            body: `product_id=${product.id}&option_id=${product.optionId}&session_id=${sessionId}`--%>
<%--        })--%>
<%--            .then(response => response.json())--%>
<%--            .then(data => {--%>
<%--                if (data.success) {--%>
<%--                    window.location.href = "/checkout/checkout.jsp";--%>
<%--                } else {--%>
<%--                    alert(data.message || "Có lỗi xảy ra khi xử lý đơn hàng");--%>
<%--                }--%>
<%--            })--%>
<%--            .catch(error => {--%>
<%--                console.log(error);--%>
<%--                alert("Có lỗi xảy ra. Vui lòng thử lại sau!");--%>
<%--            });--%>
<%--    }--%>
<%--</script>--%>
<script src="${pageContext.request.contextPath}/static/style-component/style_product/TopProduct.js"></script>


</body>