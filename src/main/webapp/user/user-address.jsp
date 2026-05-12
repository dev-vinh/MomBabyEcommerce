<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Địa Chỉ</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/style-component/style-user_profile/Address.css">
</head>
<body>

<div class="header">
    <jsp:include page="/home/header.jsp"/>
</div>

<div class="container">

    <div class="sidebar">
        <jsp:include page="user-sidebar.jsp"/>
    </div>

    <div class="content">

        <div id="address_header" class="row mid_align">
            <span class="title">Địa chỉ của tôi</span>
            <button type="button" class="add_btn mid_align open-add-address-btn">
                <i class="fa-solid fa-plus"></i>
                <span>Thêm địa chỉ</span>
            </button>
        </div>

        <c:if test="${param.requireAddress eq 'true'}">
            <div style="color: red; font-weight: bold; margin: 10px 0;">
                Vui lòng thêm địa chỉ để tiếp tục thanh toán.
            </div>
        </c:if>

        <div class="overlay"></div>

        <div id="addAddressFormContainer" style="display: none;">
            <span class="close-icon">&times;</span>
            <h2>Thêm Địa Chỉ</h2>

            <form id="addAddressForm"
                  action="${pageContext.request.contextPath}/AddAddressController"
                  method="post">

                <label for="name">Tên người nhận:<span style="color:red;">*</span></label>
                <input type="text" id="name" name="fullName" autocomplete="name" placeholder="Nhập tên người nhận" required/>

                <label for="phone">Số điện thoại:<span style="color:red;">*</span></label>
                <input type="text" id="phone" name="phoneNumber" autocomplete="phone" placeholder="Nhập số điện thoại"
                       maxlength="10" required/>

                <input type="hidden" name="country" value="Việt Nam"/>

                <label for="provinceSelect">Tỉnh / Thành phố:<span style="color:red;">*</span></label>
                <select id="provinceSelect" name="state" autocomplete="state" required>
                    <option value="">-- Chọn Tỉnh / Thành phố --</option>
                </select>

                <label for="districtSelect">Quận / Huyện:<span style="color:red;">*</span></label>
                <select id="districtSelect" name="city" autocomplete="city" required disabled>
                    <option value="">-- Chọn Quận / Huyện --</option>
                </select>

                <label for="wardSelect">Phường / Xã:<span style="color:red;">*</span></label>
                <select id="wardSelect" name="ward" autocomplete="ward" required disabled>
                    <option value="">-- Chọn Phường / Xã --</option>
                </select>

                <label for="detail">Địa chỉ chi tiết:<span style="color:red;">*</span></label>
                <input type="text" id="detail" name="detail" autocomplete="detail"
                       placeholder="Số nhà, tên đường..." required/>

                <div class="radio-group">
                    <label>
                        <input type="radio" name="addressType" value="shipping" checked/> Địa chỉ giao hàng
                    </label>
                    <label>
                        <input type="radio" name="addressType" value="billing"/> Địa chỉ thanh toán
                    </label>
                </div>

                <button type="submit" class="submit-btn">Xác nhận</button>
            </form>
        </div>

        <div id="card_body">
            <c:choose>

                <c:when test="${not empty user and not empty addresses}">
                    <c:forEach var="address" items="${addresses}">
                        <div class="address_item row"
                             data-id="${address.id}"
                             data-full-name="${fn:escapeXml(address.fullName)}"
                             data-phone-number="${fn:escapeXml(address.phoneNumber)}"
                             data-street="${fn:escapeXml(address.street)}"
                             data-city="${fn:escapeXml(address.city)}"
                             data-state="${fn:escapeXml(address.state)}"
                             data-country="${fn:escapeXml(address.country)}"
                             data-address-type="${fn:escapeXml(address.addressType)}">

                            <div class="icon mid_align">
                                <c:choose>
                                    <c:when test="${address.addressType eq 'shipping'}">
                                        <i class="fa-solid fa-house"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa-solid fa-building"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="infor">
                                <div class="item_header row mid_align">
                                    <span class="name">${fn:escapeXml(address.fullName)}</span>
                                    <div class="rec_vertical"></div>
                                    <span class="phone">${fn:escapeXml(address.phoneNumber)}</span>

                                    <c:if test="${address.isDefault}">
                                        <div class="default">Mặc định</div>
                                    </c:if>
                                </div>

                                <div class="item_body">
                                    <div class="address_detail">
                                        <span>${fn:escapeXml(address.street)}</span>
                                    </div>
                                    <div class="location">
                                        <span>${fn:escapeXml(address.state)}, ${fn:escapeXml(address.city)}</span>
                                    </div>
                                </div>
                            </div>

                            <div class="manage mid_align col">
                                <button type="button" class="update_btn">Thay đổi</button>

                                <c:choose>
                                    <c:when test="${address.isDefault}">
                                        <div class="default">Mặc định</div>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="set_default_btn"
                                                onclick="setDefault('${address.id}')">
                                            Đặt làm mặc định
                                        </button>

                                        <button type="button" class="delete_btn"
                                                onclick="deleteAddress('${address.id}')">
                                            Xóa
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <div class="empty_address_box">
                        <p class="no_address">Bạn chưa có địa chỉ nào. Hãy thêm địa chỉ mới!</p>
                    </div>
                </c:otherwise>

            </c:choose>
        </div>

    </div>
</div>

<script>
    window.contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/static/style-component/style-user_profile/Address.js"></script>

</body>
</html>
