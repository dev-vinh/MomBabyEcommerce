<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 1/7/2026
  Time: 12:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang hồ sơ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/myProfile.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <script src="${pageContext.request.contextPath}/static/style-component/style-admin/myProfile.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
</head>
<body>

<div class="header">
    <jsp:include page="header.jsp"/>
</div>

<div class="container">


    <div class="side_bar">
        <jsp:include page="sidebar.jsp"/>
    </div>

    <div class="content">
        <div class="profile-container">
            <!-- Header -->
            <div class="profile-header">
                <img id="cover" src="${pageContext.request.contextPath}/static/image/background_adm_profile.jpg" alt="Header Image" class="header-image">
                <input type="file"  id="file_cover" accept="image/*" style="display: none;">
                <button id="upload_cover" class="upload-header-btn">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M6.827 6.175A2.31 2.31 0 0 1 5.186 7.23c-.38.054-.757.112-1.134.175C2.999 7.58 2.25 8.507 2.25 9.574V18a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18V9.574c0-1.067-.75-1.994-1.802-2.169a47.865 47.865 0 0 0-1.134-.175 2.31 2.31 0 0 1-1.64-1.055l-.822-1.316a2.192 2.192 0 0 0-1.736-1.039 48.774 48.774 0 0 0-5.232 0 2.192 2.192 0 0 0-1.736 1.039l-.821 1.316Z"></path>
                        <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 12.75a4.5 4.5 0 1 1-9 0 4.5 4.5 0 0 1 9 0ZM18.75 10.5h.008v.008h-.008V10.5Z"></path>
                    </svg>

                    </svg> Thay đổi ảnh bìa
                </button>
            </div>

            <!-- Avatar và thông tin -->
            <div class="profile-info">
                <div class="avatar">
                    <img id="avatar" src="https://htmlstream.com/front-dashboard/assets/img/160x160/img6.jpg" alt="Avatar" class="avatar-image">
                    <input type="file"  id="file_avatar" accept="image/*" style="display: none;">

                    <button id="upload_avatar" class="edit-avatar-btn">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                            <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L6.832 19.82a4.5 4.5 0 0 1-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 0 1 1.13-1.897L16.863 4.487Zm0 0L19.5 7.125" />
                        </svg>
                    </button>
                </div>

                <h1 class="name">Quang Tuấn <span class="verified">✔</span></h1>
                <p class="details">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 21h19.5m-18-18v18m10.5-18v18m6-13.5V21M6.75 6.75h.75m-.75 3h.75m-.75 3h.75m3-6h.75m-.75 3h.75m-.75 3h.75M6.75 21v-3.375c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21M3 3h12m-.75 4.5H21m-3.75 3.75h.008v.008h-.008v-.008Zm0 3h.008v.008h-.008v-.008Zm0 3h.008v.008h-.008v-.008Z" />
                    </svg>Bến Tre &nbsp;&nbsp;

                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
                        <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1 1 15 0Z" />
                    </svg> VietNam. &nbsp;&nbsp;

                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
                    </svg> Tham gia tháng 6 năm 2025
                </p>


            </div>

            <!-- Thanh menu -->
            <div class="profile-menu">
                <a href="#" class="menu-item active">Hồ sơ</a>
                <a href="${pageContext.request.contextPath}/admin/account_settings.jsp"> <button class="action-btn">Chỉnh sửa hồ sơ</button></a>
            </div>
            <div class="container">
                <!-- Phần About Me -->
                <div class="col-xl-6 col-lg-12 col-md-12 col-12 mb-5">
                    <div class="card h-100">
                        <div class="card-header">
                            <h4 class="mb-0">Giới Thiệu</h4>
                        </div>
                        <div class="card-body">
                            <h5 class="text-uppercase">Tiểu sử</h5>
                            <p class="mt-2 mb-6">
                                what will be, will be!
                            </p>
                            <div class="col">
                                <div class="col-12 mb-5">
                                    <h5 class="text-uppercase">Vị trí</h5>
                                    <p class="mb-0">Doanh Nhân kinh doanh</p>
                                </div>
                                <div class="col-6 mb-5">
                                    <h5 class="text-uppercase">Điện thoại</h5>
                                    <p class="mb-0">+32112345689</p>
                                </div>
                                <div class="col-6 mb-5">
                                    <h5 class="text-uppercase">Ngày sinh</h5>
                                    <p class="mb-0">01.10.2005</p>
                                </div>
                                <div class="col-6">
                                    <h5 class="text-uppercase">Email</h5>
                                    <p class="mb-0">tuan112233@gmail.com</p>
                                </div>
                                <div class="col-6">
                                    <h5 class="text-uppercase">Địa điểm</h5>
                                    <p class="mb-0">Quận 2, Thủ Đức</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Phần Share Your Profile -->
                <div class="col-xl-6 col-lg-12 col-md-12 col-12 mb-5">
                    <div class="card h-100 share-profile-card">
                        <div class="card-header">
                            <h4 class="mb-0">Chia sẻ hồ sơ của bạn</h4>
                        </div>
                        <div class="card-body text-center">
                            <p>Hãy chia sẻ hồ sơ của bạn để bắt đầu tạo khách hàng tiềm năng.</p>
                            <img src="https://techzaa.in/larkon/admin/assets/images/qr-code.png" alt="QR Code" class="qr-code">
                            <div class="social-icons mt-3">
                                <i class="fab fa-facebook"></i>
                                <i class="fab fa-instagram"></i>
                                <i class="fab fa-twitter"></i>
                                <i class="fab fa-whatsapp"></i>
                                <i class="fas fa-envelope"></i>
                            </div>
                            <p class="mt-3">Sao chép đường dẫn và chia sẻ với bạn bè:</p>
                            <div class="copy-url">
                                <input type="text" value="https://larkon-mileage.com" readonly>
                                <button class="copy-button">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h3.75M9 15h3.75M9 18h3.75m3 .75H18a2.25 2.25 0 0 0 2.25-2.25V6.108c0-1.135-.845-2.098-1.976-2.192a48.424 48.424 0 0 0-1.123-.08m-5.801 0c-.065.21-.1.433-.1.664 0 .414.336.75.75.75h4.5a.75.75 0 0 0 .75-.75 2.25 2.25 0 0 0-.1-.664m-5.8 0A2.251 2.251 0 0 1 13.5 2.25H15c1.012 0 1.867.668 2.15 1.586m-5.8 0c-.376.023-.75.05-1.124.08C9.095 4.01 8.25 4.973 8.25 6.108V8.25m0 0H4.875c-.621 0-1.125.504-1.125 1.125v11.25c0 .621.504 1.125 1.125 1.125h9.75c.621 0 1.125-.504 1.125-1.125V9.375c0-.621-.504-1.125-1.125-1.125H8.25ZM6.75 12h.008v.008H6.75V12Zm0 3h.008v.008H6.75V15Zm0 3h.008v.008H6.75V18Z" />
                                    </svg>
                                </button>
                            </div>

                        </div>
                    </div>
                </div>
            </div>


        </div>
    </div>


</div>




</body>
</html>
