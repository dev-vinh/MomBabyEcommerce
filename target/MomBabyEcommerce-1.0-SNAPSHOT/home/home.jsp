<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 12/25/2025
  Time: 12:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<head>
    <title>Moder Home</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/image/logo_web.jpg">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-home/banner.css">

    <style>
        #container{
            display: flex;
            flex-direction: column;
            padding-top: 85px;
        }

        #home_header{
            height: 70px;
            width: 100%;
            margin-bottom: 20px;
            top: 0;
            position: fixed;
            z-index: 1000;
        }

    </style>

</head>
<body id="container">

<div id="home_header">
    <jsp:include page="/home/header.jsp"/>
</div>

<%--<div id="home_banner">--%>
<%--    <button class="pause-play-btn" id="pausePlayBtn">--%>
<%--        <i class="fa-solid fa-pause"></i>--%>
<%--    </button>--%>

<%--    <div class="indicator" id="indicator1">--%>
<%--        <div class="progress-loader" >--%>

<%--            <div class="progress"></div>--%>
<%--        </div>--%>
<%--        <div class="progress-loader" >--%>

<%--            <div class="progress"></div>--%>
<%--        </div>--%>
<%--        <div class="progress-loader" >--%>

<%--            <div class="progress"></div>--%>
<%--        </div>--%>
<%--        <div class="progress-loader" >--%>

<%--            <div class="progress"></div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--&lt;%&ndash;chưa xử lí đoạn này trong banner----------------------------------------------------------&ndash;%&gt;--%>
<%--    <div class="banner-container">--%>
<%--        <c:forEach var="b" items="${banners}" varStatus="status">--%>
<%--            <c:if test="${status.index < 4}">--%>
<%--                <div class="banner-slide ${status.index == 0 ? 'active' : ''}"--%>
<%--                     style="background-image: url('${imageMap[b.image_id]}');">--%>
<%--                    <div class="overlay"></div>--%>
<%--                    <div class="banner-content">--%>
<%--                        <h2>${b.title}</h2>--%>
<%--                        <p>${b.description}</p>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </c:if>--%>
<%--        </c:forEach>--%>

<%--        <button class="nav-btn prev-btn">❮</button>--%>
<%--        <button class="nav-btn next-btn">❯</button>--%>
<%--    </div>--%>
<%--    &lt;%&ndash;chưa xử lí đoạn này trong banner----------------------------------------------------------&ndash;%&gt;--%>
<%--</div>--%>

<div id="home_body">
    <jsp:include page="/home/body-home.jsp"/>
</div>


<script src="${pageContext.request.contextPath}/static/style-page/home/home.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<%--<script> const contextPath = "${pageContext.request.contextPath}"; </script>--%>
<%--<script src="${pageContext.request.contextPath}/static/style-component/style-home/banner.js"></script>--%>

</body>