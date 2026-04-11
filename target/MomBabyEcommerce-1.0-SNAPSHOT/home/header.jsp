<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 14/03/2026
  Time: 2:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html lang="en">

<head>
  <title>Header</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-page/home/Home.css" />
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/static/style-component/style-home/search.css" />

</head>

<body>

<div id="main_header">


  <a class="logo nav_item" id="logo" href="home">
    <img class="logo" src="${pageContext.request.contextPath}/static/image/logo_web.jpg" />
  </a>


  <nav class="navbar">
    <ul>
      <li class="active">
        <a href="#">Combo Đồ sơ sinh

        </a>
        <div class="submenu submenu-do-so-sinh">
          <div class="submenu-column">

            <%-- <a class="nav_item" href="search-do-so-sinh">Tất cả combo đồ sơ sinh</a>--%>
            <a class="nav_item" href="Search_Clothes">Tất cả combo đồ
              sơ sinh</a>


            <a class="nav_item" href="#">Quần áo sơ sinh <span class="highlight">HẤP
                                                DẪN</span></a>
            <a href="">Bình sữa sơ sinh</a>
            <a href="#">Bao tay, bao chân</a>
            <a href="#">Bỉm sơ sinh</a>
            <a href="#">Khăn quấn, túi ngủ</a>
            <a href="#">Gối chặn, gối ôm
              <span class="highlight">HẤP DẪN</span></a>
            <a href="#">Khăn Tắm</a>
            <a href="#">Chậu tắm</a>
          </div>
        </div>
      </li>
      <li>
        <a href="#">Máy móc thiết yếu</a>
        <div class="submenu submenu-may-moc-thiet-yeu">
          <div class="submenu-column">
            <%-- <a class="nav_item" href="search-tat-ca-loai-may">Tất cả các loại máy</a>--%>
            <a class="nav_item" href="Search_mayphasua">Tất cả các loại máy</a>

            <a href="#">Máy hút sữa</a>
            <a href="#">Nồi nấu<span class="new">MỚI</span></a>
            <a href="#">Máy làm ấm khăn<span class="highlight">HẤP DẪN</span></a>
            <a href="#">Máy pha sữa</a>
            <a href="#">Máy tiệt trùng</a>
            <a href="#">Máy hâm sữa</a>
            <a href="#">Máy ép-máy xay</a>
          </div>
        </div>
      </li>
      <li>
        <a href="#">Sữa & Bình Sữa</a>
        <div class="submenu submenu-sua-&-binh-sua">
          <div class="submenu-column">
            <a class="nav_item" href="Search_Sua">Tất cả các loại sữa</a>
            <a href="#">Sữa bột</a>
            <a href="#">Sữa pha sẵn <span class="new">MỚI</span></a>
            <a href="#">Sữa bầu</a>
            <a href="#">Sữa tươi</a>
            <a href="#">Bình sữa</a>
            <a href="#">Cốc tập uống</a>
            <a href="#">Ti giả</a>
            <a href="#">Bình đựng sữa bột</a>
            <a href="#">Vệ sinh bình sữa</a>
          </div>
        </div>
      </li>
      <li>
        <a href="#">Ăn dặm</a>
        <div class="submenu submenu-an-dam">
          <div class="submenu-column">
            <a class="nav_item" href="Search_Andam">Tất cả món ăn dặm</a>
            <a href="#">Bánh ăn dặm <span class="new">MỚI</span></a>
            <a href="#">Bột ăn dặm</a>
            <a href="#">Gia vị ăn dặm</a>
            <a href="#">Dầu ăn dặm</a>
            <a href="#">Yến ăn dặm<span class="new">MỚI</span></a>
            <a href="#">Đồ chế biến</a>
          </div>
        </div>
      </li>
      <li class="active">
        <a href="#">Bỉm tã & vệ sinh</a>
        <div class="submenu submenu-bim-ta-&-ve-sinh">
          <div class="submenu-column">
            <a class="nav_item" href="Search_Bimta">Tất cả các loại bỉm tã</a>
            <a href="#">Bĩm tã</a>
            <a href="#">Quần đóng bĩm</a>
            <a href="#">Quần bỏ bĩm</a>
            <a href="#">Bỉm người lớn</a>
            <a href="#">Sữa tắm gội</a>
            <a href="#">Chậu rữa mặt</a>
            <a href="#">Khăn vải</a>
          </div>
        </div>
      </li>
      <li>
        <a href="#">Thời trang cho bé</a>
        <div class="submenu submenu-thoi-trang-cho-be">
          <div class="submenu-column">
            <a class="nav_item" href="Search_TTBe">Tất cả mẫu quần áo</a>
            <a href="#">Quần áo</a>
            <a href="#">Balo, túi xách</a>
            <a href="#">Giày dép</a>
            <a href="#">Nước hoa</a>
            <a href="#">Phụ kiện</a>
            <a href="#">Đồ bơi</a>
          </div>
        </div>
      </li>
      <li>
        <a href="#">Đồ cho mẹ</a>
        <div class="submenu submenu-do-cho-me">
          <div class="submenu-column">
            <a class="nav_item" href="Search_Me">Tất cả mẫu đồ cho
              mẹ</a>
            <a href="#">Sữa bầu</a>
            <a href="#">Vitamin cho mẹ</a>
            <a href="#">Gối bầu</a>
            <a href="#">Quần áo cho mẹ</a>
            <a href="#">Phụ kiện cho mẹ</a>
          </div>
        </div>
      </li>
      <li>
        <a href="#">Hỗ Trợ</a>
        <div class="submenu submenu-ho-tro">
          <div class="submenu-column">
            <a href="#">Trung Tâm Hỗ Trợ</a>
            <a href="#">Bảo Hành</a>
            <a href="#">Hướng dẫn sử dụng</a>
            <a href="#">Câu Hỏi Thường Gặp</a>
          </div>
        </div>
      </li>
    </ul>
    <!-- User Login Icon and Popup -->
    <div class="icons">
      <a href="#" class="icon" id="search-icon" onclick="showSearchOverlay()">
        <%-- đổi version 6.5.1 <i class="fas fa-search"></i>--%>
        <i class="fa-solid fa-magnifying-glass"></i>
      </a>


      <a class="nav_item icon" href="cart" id="cart-link">
        <i class="fa-solid fa-cart-shopping"></i>
        <%-- <i class="fas fa-shopping-cart"></i>--%>
        <%-- <span class="cart-count" id="cart-count">0</span>--%>
      </a>

      <div class="icon user-login" target="_top">
        <%-- <i class="fas fa-user"></i>--%>
        <i class="fa-solid fa-user"></i>
        <div class="user-popup">
          <!-- cần tạo user-profile.jsp  login  -->
          <a class="nav_item" href="user-profile" id="my-page-link">Trang của tôi</a>
          <a href="login" id="login-link">Đăng nhập/Đăng ký</a>
        </div>
      </div>
    </div>



    <div id="search-overlay" class="layer">


      <div class="search-container">
        <h2 class="search-container-title">Chúng tôi có thể giúp bạn tìm kiếm?</h2>
        <div class="search-bar">
          <input type="text" id="search-input" placeholder="Nhập từ khoá ..." />
          <button class="search-icon" onclick="performSearch()">
            <i class="fa-solid fa-magnifying-glass"></i>
          </button>
        </div>

        <!-- Dropdown gợi ý -->
        <div class="suggestion-box" id="suggestion-box">
          <ul id="suggestion-list"></ul>


          <%-- <h3 class="suggestion-title">Gợi ý dành cho bạn</h3>--%>
          <div class="product-suggestions">
          </div>
        </div>


        <div id="search-content"></div>


        <button id="close-search-overlay" class="close-btn">
          <%-- <i class="fas fa-times"></i>--%>
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>
    </div>


  </nav>

</div>


<script> const contextPath = "${pageContext.request.contextPath}"; </script>
<script src="${pageContext.request.contextPath}/static/style-page/home/home.js"></script>
<script src="${pageContext.request.contextPath}/static/style-component/style-home/search.js"></script>


</body>

</html>