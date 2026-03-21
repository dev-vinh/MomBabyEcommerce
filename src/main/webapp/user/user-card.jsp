<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 3/21/2026
  Time: 7:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_profile/Payment.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_profile/CardComponent.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_profile/AddCard.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

</head>
<body>

<div class="header">
    <jsp:include page="/home/header.jsp"/>
</div>


<div class="wrap ">
    <div class="side_bar">
        <jsp:include page="user-sidebar.jsp"/>
    </div>

    <div class="content">

        <div id="content_header" class="mid_align row">
            <span class="title">Quản lý thanh toán</span>


            <div class="add_btn mid_align" onclick="openAddCardOverlay()">
                <i class="fa-solid fa-plus"></i>
                <a href="#">Thêm</a>
            </div>

        </div>

        <div id="content_body" class="mid_align">



            <c:if   test="${not empty cards}">

                <c:forEach items="${cards}" var="c">
                    <div id="card"
                         style="background-image: url('${pageContext.request.contextPath}/static/image/card_bg.jpg');">
                        <div class="overlay"></div>
                        <div id="card_header" class="mid_align">

                            <div class="infor col">
                                <div class="card_infor row">
                                    <span class="title">Số thẻ :</span>
                                    <span class="last4"> **** ${c.last4}</span>

                                </div>
                                <span class="card_holder">${user.fullName}</span>
                            </div>


                            <div class="type_card">
                                <img src="${pageContext.request.contextPath}/static/image/visa.png" alt="">
                            </div>


                        </div>

                        <div id="card_body">
                            <div class="card_date">
                                <span> Date: ${c.duration}</span>
                            </div>


                        </div>


                        <div id="footer" class="mid_align">
                            <c:if  test="${c.isDefault}">
                                <div id="default">Mặc định</div>
                            </c:if>
                            <div class="edit_card mid_align">
                                <a href="#" class="delete_btn">Xóa</a>

                            </div>
                        </div>


                    </div>
                </c:forEach>

            </c:if>




        </div>


        <div id="add-card-overlay">
            <div class="add-card-container">


                <div class="card-form">
                    <h2>Thêm thẻ</h2>
                    <form id="card_form"  >
                        <label for="name">Tên trên thẻ:</label>
                        <input type="text" id="name" name="name" required><br>

                        <label for="card-number">Số thẻ:</label>
                        <input type="text" id="card-number" name="cardNumber" required><br>

                        <label for="expiry">Ngày hết hạn:</label>
                        <input type="text" id="expiry" name="expiry" placeholder="MM/YYYY" required><br>

                        <label for="cvv">Mã CVV:</label>
                        <input type="password" id="cvv" name="cvv" required><br>

                        <div class="checkbox-group">
                            <input type="checkbox" id="primary-card">
                            <label for="primary-card">Đặt làm thẻ chính</label>
                        </div>

                        <button type="submit">Lưu thẻ</button>
                    </form>

                </div>


                <button class="close-btn" onclick="closeAddCardOverlay()">
                    <i class="fas fa-times"></i>
                </button>
            </div>
        </div>

    </div>
</div>


<script src="${pageContext.request.contextPath}/static/style-component/style-user_profile/AddCard.js"></script>
</body>
</html>
