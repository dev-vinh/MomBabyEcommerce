<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 1/7/2026
  Time: 11:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/customerDetail.css">

</head>
<body>
<div class="container">
    <!-- Header -->
    <div class="header">
        <h1>Chi tiết khách hàng</h1>
        <div class="actions">
            <button class="btn delete">Xóa</button>

        </div>
    </div>

    <!-- Nội dung chính -->
    <div class="content">
        <!-- Bảng bên trái -->
        <div class="left-panel">
            <div class="profile-card">
                <div class ="info">
                    <img src="https://dashui.codescandy.com/dashuipro/assets/images/avatar/avatar-11.jpg" alt="Jessica Moore" class="profile-pic">
                    <h3>Phan Ngọc Vĩnh</h3>
                    <p><a href="mailto:jessica-moore@example.com">phanngocvinh@gmail.com</a></p>
                    <p class="phone">012388888</p>
                </div>
                <hr>
                <p><strong>Đơn hàng gần nhất:</strong>  <a href="#">#80294</a></p>
                <p><strong>Giá trị đơn hàng trung bình:</strong> 574.000 VND </p>
                <p><strong>Đăng ký:</strong> 2 tháng trước</p>
                <p><strong>Email Marketing:</strong> Đã đăng ký</p>
            </div>
        </div>

        <!-- Bảng bên phải -->
        <div class="right-panel">
            <!-- Phần Ghi chú -->
            <div class="notes">
                <textarea placeholder="Ghi chú về khách hàng"></textarea>
            </div>

            <!-- Phần Đơn hàng -->
            <div class="orders">
                <h4>Đơn hàng</h4>
                <table>
                    <thead>
                    <tr>
                        <th>Đơn hàng</th>
                        <th>Ngày</th>
                        <th>Tình trạng</th>
                        <th>Số mặt hàng</th>
                        <th>Tổng cộng</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><a href="#">#80294</a></td>
                        <td>Hôm nay lúc 6:10 PM</td>
                        <td>Chờ xử lý</td>
                        <td>4</td>
                        <td>320.000 VND</td>
                    </tr>
                    <tr>
                        <td><a href="#">#63736</a></td>
                        <td>15 Tháng 5, 2025</td>
                        <td>Hoàn thành</td>
                        <td>7</td>
                        <td>2,574.311 VND</td>
                    </tr>
                    <tr>
                        <td><a href="#">#63501</a></td>
                        <td>7 Tháng 4, 2025</td>
                        <td>Hoàn thành</td>
                        <td>1</td>
                        <td>34.000 VND</td>
                    </tr>
                    <tr>
                        <td><a href="#">#40278</a></td>
                        <td>19 Tháng 3, 2025</td>
                        <td>Hoàn thành</td>
                        <td>2</td>
                        <td>704.000 VND</td>
                    </tr>
                    </tbody>
                </table>
                <p><a href="#">Xem tất cả 7 đơn hàng</a></p>
            </div>

            <!-- Phần Địa chỉ -->
            <div class="addresses">
                <h4>Địa chỉ</h4>
                <p><strong> Quận 1</strong><br>Thành phố Hồ Chí Minh</p>
                <p><strong>Quận 2</strong><br>Thành phố Hồ Chí Minh</p>
                <button class="btn new-address">Thêm Địa chỉ Mới</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
