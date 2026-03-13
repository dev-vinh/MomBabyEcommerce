<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 12/03/2026
  Time: 7:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Header</title>
<%--  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/header/header.css">--%>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"/>

  <style>

    #header {
      background-color: white;
      height: 60px;
      border-bottom: 1px solid #e5e7eb;
      display: flex;
      align-items: center;
      padding: 0 20px;
      position: relative;
      /*top: 0;*/
      /*position: fixed;*/
      /*z-index: 1000;*/
      /*width: 100%;*/
    }

    #header .logo {
      height: 100%;
      width: 200px;
      display: flex;
      align-items: center;
    }

    #header .logo img {
      object-fit: contain;
      width: 70%;
      height: 80%;
      cursor: pointer;
    }

    .search-bar {
      display: flex;
      align-items: center;
      margin-left: 30px;
      width: 250px;
    }

    .search-bar input {
      width: 100%;
      padding: 8px 10px;
      border: 1px solid #e5e7eb;
      border-radius: 5px 0 0 5px;
      outline: none;
    }

    .search-bar button {
      padding: 8px 10px;
      border: 1px solid #e5e7eb;
      border-left: none;
      background-color: #f3f4f6;
      border-radius: 0 5px 5px 0;
      cursor: pointer;
    }

    .search-bar button i {
      color: #6b7280;
    }

    #header .menu {
      margin-left: auto;
      font-size: 24px;
      display: flex;
      align-items: center;
    }

    #header .menu i {
      margin: 0 15px;
      cursor: pointer;
    }

    #header .menu i:hover {
      color: #3461E9;
    }

    #header .menu .avatar {
      height: 35px;
      width: 35px;
      border-radius: 50%;
      cursor: pointer;
      overflow: hidden;
    }

    .avatar img {
      height: 100%;
      width: 100%;
      object-fit: cover;
      border-radius: 50%;
      position: relative;

    }
    .user-popup {
      display: none;
      position: absolute;
      top: 100%;
      right: 0;
      background-color: white;
      border: 1px solid #ddd;
      border-radius: 4px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
      padding: 10px;
      width: 120px;
      z-index: 9999;
      transform: translate(-10%, -15px);

    }

    .user-popup a {
      display: block;
      padding: 8px 10px;
      color: #333;
      text-decoration: none;
      font-size: 13px;
    }

    .user-popup a:hover {
      background-color: #f0f0f0;
    }
  </style>
</head>

<body>

<div id="header" class="row">
  <div class="logo">
    <img class="logo" src="${pageContext.request.contextPath}/static/image/logo_web.jpg" alt="Logo">
  </div>
  <div class="search-bar">
    <input type="text" placeholder="Tìm kiếm...">
    <button type="submit"><i class="fa-solid fa-search"></i></button>
  </div>
  <div class="menu row mid_align">
    <i class="fa-regular fa-bell"></i>
    <i class="fa-regular fa-envelope"></i>
    <div class="avatar" id="avatar">
      <img src="${pageContext.request.contextPath}/static/image/avatar.jpg" alt="Avatar" height="1024"
           width="1024"/>
      <div class="user-popup">
        <a
                class="nav_item"
                href="profile">Trang của tôi</a
        >
        <a href="Trang_Web_ban_hang_san_pham_cho_me_va_be_war_exploded/login">Đăng xuất</a>
        <%--                <a href="Trang_Web_ban_hang_san_pham_cho_me_va_be_war/login">Đăng xuất</a>--%>
      </div>
    </div>
  </div>
</div>



<script>
  const userPopup = document.querySelector(".user-popup");
  const avatar = document.querySelector(".avatar");


  avatar.addEventListener("mouseenter", () => {
    userPopup.style.display = "block";
  });

  avatar.addEventListener("mouseleave", () => {
    setTimeout(() => {
      userPopup.style.display = "none";
    }, 200);
  });

  userPopup.addEventListener("mouseenter", () => {
    userPopup.style.display = "block";
  });

  userPopup.addEventListener("mouseleave", () => {
    userPopup.style.display = "none";
  }); </script>

</body>