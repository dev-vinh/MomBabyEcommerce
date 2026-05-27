<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 12/03/2026
  Time: 7:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/sidebar/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">
</head>
<body>
<div id="body" class="row">
    <c:set var="permissions" value="${sessionScope.permissions}"/>
    <nav id="sidebar" class="col">
        <ul>
            <!-- Bảng điều khiển -->
            <c:if test="${permissions != null or
             (permissions.contains('VIEW_DASHBOARD'))}">
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-house"></i>
                    <a href="dashboard">Tổng quan</a>
                </div>
            </li>
            </c:if>
            <!-- Sản phẩm -->
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-box"></i>
                    <span>Sản phẩm</span>
                    <i class="fa-solid fa-chevron-down toggle-arrow"></i>
                </div>
                <ul class="submenu">
                    <c:if test="${permissions != null and
                        (permissions.contains('VIEW_PRODUCTS'))}">
                    <li class="submenu_item">
                        <a href="list-product">Danh sách sản phẩm</a>
                    </li>
                    </c:if>
                    <c:if test="${permissions != null and
                        (permissions.contains('CREATE_PRODUCTS'))}">
                    <li class="submenu_item">
                        <a href="add-product">Thêm sản phẩm</a>
                    </li>
                    </c:if>
                </ul>
            </li>

            <!-- Đơn hàng -->
            <c:if test="${permissions != null and
                        (permissions.contains('VIEW_ORDERS') or
                        permissions.contains('UPDATE_ORDERS_STATUS')
                        )}">
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-cart-shopping"></i>
                    <a href="orders">Đơn hàng</a>
                </div>
            </li>
            </c:if>

            <!-- Khách hàng -->
            <c:if test="${permissions != null and
                        (permissions.contains('VIEW_CUSTOMERS')
                        )}">
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-users"></i>
                    <a href="customers">Khách hàng</a>
                </div>
            </li>
            </c:if>

            <!-- Danh mục -->
            <c:if test="${permissions != null and
                        (permissions.contains('MANAGE_CATEGORIES') or
                         permissions.contains('MANAGE_INVENTORY')
                        )}">
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-layer-group"></i>
                    <a href="category">Danh mục</a>
                </div>
            </li>
            </c:if>
            <!-- Nhà sản xuất -->
            <c:if test="${permissions != null and
                        (permissions.contains('MANAGE_BRAND') or
                         permissions.contains('MANAGE_INVENTORY')
                        )}">
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-industry"></i>
                    <a href="brand">Nhà sản xuất</a>
                </div>
            </li>
            </c:if>
            <!-- Quản lý kho -->
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-box"></i>
                    <span>Quản lý kho</span>
                    <i class="fa-solid fa-chevron-down toggle-arrow"></i>
                </div>
                <ul class="submenu">
                    <c:if test="${permissions != null and
                        (permissions.contains('MANAGE_INVENTORY')
                        )}">
                    <li class="submenu_item">
                        <a href="inventory">Tồn kho</a>
                    </li>
                    </c:if>
                    <c:if test="${permissions != null and
                        (permissions.contains('MANAGE_INVENTORY')
                        )}">
                    <li class="submenu_item">
                        <a href="inventory-log">Lịch sử kho</a>
                    </li>
                    </c:if>
                </ul>
            </li>
            <c:if test="${permissions != null and
                        (permissions.contains('MANAGE_MEMBER')
                        )}">
            <li class="menu_item">
                <div class="wrap_menu_item">
                    <i class="fa-solid fa-calendar"></i>
                    <a href="/admin/manage-role">Quản lý Vai Trò</a>
                </div>
            </li>
            </c:if>
            <c:if test="${permissions != null and
                        (permissions.contains('MANAGE_BANNER')
                        )}">
                <li class="menu_item">
                    <div class="wrap_menu_item">
                        <i class="fa-solid fa-users"></i>
                        <a href="/admin/banner">Quản lý Banner</a>
                    </div>
                </li>
            </c:if>

        </ul>
    </nav>
</div>
<script src="${pageContext.request.contextPath}/static/style-component/style-admin/sidebar/Admin.js"></script>
</body>
</html>
