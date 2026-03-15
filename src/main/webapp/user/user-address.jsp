<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 3/15/2026
  Time: 5:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hcmuaf.fit.mombabyecommerce.model.User" %>
<%@ page import="hcmuaf.fit.mombabyecommerce.model.Address" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_profile/Address.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <script
            src="${pageContext.request.contextPath}/static/style-component/style-user_profile/Address.js"></script>

</head>
<body>
<div class="header">
    <jsp:include page="/home/header.jsp" />
</div>

<div class="container">

    <div class="sidebar">
        <jsp:include page="user-sidebar.jsp" />
    </div>

    <div class="content">

        <div id="address_header" class="row mid_align">
            <span class="title">Địa Chỉ</span>
            <div class="add_btn mid_align">
                <i class="fa-solid fa-plus"></i>
                <a href="#">Thêm </a>
            </div>
            <div id="addAddressFormContainer" style="display: none;">
                <h2>Thêm Địa Chỉ</h2>

                <form method="POST"
                      action="${pageContext.request.contextPath}/AddAddressController">

                    <label for="fullName">Họ tên người nhận:</label>
                    <input type="text" id="fullName" name="fullName"
                           placeholder="Nhập họ tên người nhận" required />

                    <label for="phoneNumber">Số điện thoại:</label>
                    <input type="text" id="phoneNumber" name="phoneNumber"
                           placeholder="Nhập số điện thoại" required />

                    <label for="street">Địa chỉ chi tiết:</label>
                    <input type="text" id="street" name="street"
                           placeholder="Số nhà, tên đường..." required />

                    <label for="city">Quận / Huyện:</label>
                    <input type="text" id="city" name="city" placeholder="Nhập quận/huyện"
                           required />

                    <label for="state">Tỉnh / Thành phố:</label>
                    <input type="text" id="state" name="state" placeholder="Nhập tỉnh/thành phố"
                           required />

                    <label for="country">Quốc gia:</label>
                    <input type="text" id="country" name="country" value="Việt Nam" required />

                    <label>
                        <input type="checkbox" name="isDefault" value="true" />
                        Đặt làm địa chỉ mặc định
                    </label>

                    <input type="hidden" name="userId" value="${userId}" />

                    <div class="btn-group">
                        <button type="submit">Lưu</button>
                        <button type="reset">Hủy</button>
                    </div>
                </form>

            </div>


        </div>
        <div id="card_body">
            <% User user=(User) request.getAttribute("user"); List<Address> addresses = (List
                    <Address>) request.getAttribute("addresses");
                if (user != null && addresses != null) {
                    for (Address address : addresses) {
            %>
            <div class="address_item row">
                <div class="icon mid_align">
                    <i class="fa-solid <%= (address.getAddressType() != null && address.getAddressType().equals("house")) ? "fa-house" : "fa-building" %>"></i>
                </div>

                <div class="infor">
                    <div class="item_header row mid_align">
                                                        <span class="name">
                                                            <%= address.getFullName() %>
                                                        </span>
                        <div class="rec_vertical"></div>
                        <span class="phone">
                                                            <%= address.getPhoneNumber() %>
                                                        </span>
                        <% if (Boolean.TRUE.equals(address.getDefault())) { %>
                        <div class="default">Mặc định</div>
                        <% } %>
                    </div>

                    <div class="item_body">
                        <div class="address_detail">
                                                            <span>
                                                                <%= address.getStreet() %>
                                                            </span>
                        </div>
                        <div class="location">
                                                            <span>
                                                                <%= address.getState() %>, <%= address.getCity() %>
                                                            </span>
                        </div>
                    </div>
                </div>

                <div class="manage mid_align col">
                    <button class="update_btn">Thay đổi</button>
                    <% if (!Boolean.TRUE.equals(address.getDefault())) { %>
                    <button class="set_default_btn">Đặt làm mặc định</button>
                    <% } else { %>
                    <button class="set_default_btn disabled" disabled>Đặt làm
                        mặc định</button>
                    <% } %>
                    <button class="delete_btn">Xóa</button>
                </div>
            </div>
            <% } } %>
        </div>
    </div>


</div>
</body>
</html>
