<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 14/03/2026
  Time: 2:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
      <li>
        <a href="${pageContext.request.contextPath}/search-category?categoryId=1">
          Combo Đồ sơ sinh
        </a>
      </li>
      <c:choose>
        <c:when test="${not empty categories}">
          <c:forEach var="c" items="${categories}">
            <li>
              <a href="${pageContext.request.contextPath}/search-category?categoryId=${c.id}">
                  ${c.name}
              </a>
            </li>
          </c:forEach>
        </c:when>

        <c:otherwise>

          <li>
            <a href="${pageContext.request.contextPath}/search-category?categoryId=1">
              Combo Đồ sơ sinh
            </a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/search-category?categoryId=8">
              Máy móc thiết yếu
            </a>
          </li>

          <li>
            <a href="${pageContext.request.contextPath}/search-category?categoryId=1">
              Sữa & Bình Sữa
            </a>
          </li>

          <li>
            <a href="${pageContext.request.contextPath}/search-category?categoryId=3">
              Ăn dặm
            </a>
          </li>

          <li>
            <a href="${pageContext.request.contextPath}/search-category?categoryId=2">
              Bỉm tã & vệ sinh
            </a>
          </li>

          <li>
            <a href="${pageContext.request.contextPath}/search-category?categoryId=5">
              Thời trang cho bé
            </a>
          </li>

          <li>
            <a href="${pageContext.request.contextPath}/search-category?categoryId=7">
              Đồ cho mẹ
            </a>
          </li>
        </c:otherwise>

      </c:choose>

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
                <c:choose>

                    <c:when test="${not empty sessionScope.user}">
                        <a class="nav_item" href="user-profile" >
                            Xin chào ${sessionScope.user.displayName}
                        </a>
                        <a class="nav_item" href="user-profile">Trang của tôi</a>
                        <a class="nav_item" href="logout">Đăng xuất</a>
                    </c:when>

                    <c:otherwise>
                        <a href="login" id="login-link">Đăng nhập/Đăng ký</a>
                    </c:otherwise>

                </c:choose>
            </div>
      </div>
    </div>



    <div id="search-overlay">
      <button id="close-search-overlay" class="close-btn">
        <i class="fa-solid fa-xmark"></i>
      </button>

      <div class="search-container">

        <div class="search-bar">
          <i class="fa-solid fa-magnifying-glass search-bar-icon"></i>
          <input type="text"
                 id="search-input"
                 placeholder="Nhập tên sản phẩm..."
                 autocomplete="off" />
          <button class="search-submit-btn" onclick="performSearch()">Tìm kiếm</button>
        </div>
        <div class="keyword-suggestions">
          <span class="keyword-label">Gợi ý:</span>

          <button class="keyword-tag" onclick="searchKeyword('Bỉm tã')">Bỉm tã</button>

          <button class="keyword-tag" onclick="searchKeyword('Máy hâm sữa')">Máy hâm sữa</button>
          <button class="keyword-tag" onclick="searchKeyword('Sữa bột')">Sữa bột</button>
          <button class="keyword-tag" onclick="searchKeyword('Quần áo')">Quần áo</button>
          <button class="keyword-tag" onclick="searchKeyword('Bình sữa')">Bình sữa</button>
          <button class="keyword-tag" onclick="searchKeyword('Khăn ướt')">Khăn ướt</button>
        </div>

        <div id="suggestion-box"  style="display:none;">
          <ul id="suggestion-list"></ul>
          <div id="no-result" style="display:none;" class="no-result">
            <p>Không tìm thấy sản phẩm</p>
          </div>
        </div>

        <div id="suggestion-footer" style="display:none">
          <span class="footer-count"></span>
          <a class="footer-link" href="#">
            Xem tất cả
            <i class="fa-solid fa-arrow-right"></i>
          </a>
        </div>

        <div id="search-content"></div>

      </div>
    </div>


  </nav>

</div>


  <script>  window.contextPath = "${pageContext.request.contextPath}"; </script>
  <script src="${pageContext.request.contextPath}/static/style-page/home/home.js"></script>
<script src="${pageContext.request.contextPath}/static/style-component/style-home/search.js"></script>


</body>

</html>