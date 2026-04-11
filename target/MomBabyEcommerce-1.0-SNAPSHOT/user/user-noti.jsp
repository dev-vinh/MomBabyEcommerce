<%--
  Created by IntelliJ IDEA.
  User: fileh
  Date: 1/10/2026
  Time: 11:57 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông báo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/notifications.css">
</head>
<body>

<div class="header">
    <jsp:include page="/home/header.jsp"/>
</div>


<div class="container">

    <div class="sidebar">
        <jsp:include page="user-sidebar.jsp"/>
    </div>

    <div class="warp_content">
        <div class="content notifications" id="notifications">
            <div class="card-header">
                <h3>Thông báo</h3>
            </div>
            <div class="alert">
                Chúng tôi cần quyền từ trình duyệt của bạn để hiển thị thông báo.
                <a href="#" class="alert-link">Yêu cầu quyền</a>
            </div>

            <form>
                <div class="table-responsive">
                    <table class="table table-thead-bordered table-nowrap table-align-middle">
                        <thead class="thead-light">
                        <tr>
                            <th>Loại thông báo</th>
                            <th class="text-center">
                                <div class="mb-1">
                                    <img class="avatar avatar-xs"
                                         src="${pageContext.request.contextPath}/static/image/icon-user-settings/oc-email-at.svg"
                                         alt="Email Icon">
                                </div>
                                Email
                            </th>
                            <th class="text-center">
                                <div class="mb-1">
                                    <img class="avatar avatar-xs"
                                         src="${pageContext.request.contextPath}/static/image/icon-user-settings/oc-globe.svg"
                                         alt="Browser Icon">
                                </div>
                                Trình duyệt
                            </th>
                            <th class="text-center">
                                <div class="mb-1">
                                    <img class="avatar avatar-xs"
                                         src="${pageContext.request.contextPath}/static/image/icon-user-settings/oc-phone.svg"
                                         alt="App Icon">
                                </div>
                                Ứng dụng
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>Thông báo mới</td>
                            <td class="text-center"><input type="checkbox"></td>
                            <td class="text-center"><input type="checkbox"></td>
                            <td class="text-center"><input type="checkbox"></td>
                        </tr>
                        <tr>
                            <td>Hoạt động tài khoản</td>
                            <td class="text-center"><input type="checkbox"></td>
                            <td class="text-center"><input type="checkbox"></td>
                            <td class="text-center"><input type="checkbox"></td>
                        </tr>
                        <tr>
                            <td>Trình duyệt mới đăng nhập</td>
                            <td class="text-center"><input type="checkbox" checked></td>
                            <td class="text-center"><input type="checkbox" checked></td>
                            <td class="text-center"><input type="checkbox" checked></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="actions">
                    <label>Khi nào bạn muốn nhận thông báo?</label>
                    <select class="form-select">
                        <option>Luôn luôn</option>
                        <option>Chỉ trong các ngày làm việc</option>
                    </select>
                    <button class="btn btn-primary">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>

</div>




</body>
</html>

