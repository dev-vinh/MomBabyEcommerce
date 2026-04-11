<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 12/25/2025
  Time: 12:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>home-body</title>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-home/homeBody.css">
</head>
<body>
<section class="body">
    <h2>Chăm sóc yêu thương – Đồng hành cùng mẹ và bé</h2>
    <p>
        Hành trình làm mẹ là hành trình của yêu thương, hi sinh và niềm hạnh phúc vô bờ. Me&Be tự hào được đồng hành
        cùng mẹ trong từng giai đoạn trưởng thành của bé – từ những ngày đầu chào đời đến lúc bé tự tin khám phá thế
        giới. Chúng tôi mang đến sản phẩm chất lượng, dịch vụ tận tâm và trải nghiệm mua sắm ấm áp như chính vòng tay
        của mẹ.
    </p>
    <div class="body-image">
        <img src="${pageContext.request.contextPath}/static/image/banner-body.jpg" alt="banner1">
    </div>
</section>

<section class="product-showcase">
    <div class="product-grid">
        <div class="product-item">
            <img src="${pageContext.request.contextPath}/static/image/sua&dinhduong.png" alt="dinhduong">
            <h3>Sữa & Dinh Dưỡng</h3>
            <a href="Search_Sua" class="buy-button">Tìm hiểu thêm</a>
        </div>
        <div class="product-item">
            <img src="${pageContext.request.contextPath}/static/image/dodungchobe.png" alt=dodungchobe>
            <h3>Đồ dùng cho bé</h3>
            <a href="Search_mayphasua" class="buy-button">Tìm hiểu thêm</a>
        </div>
        <div class="product-item">
            <img src="https://dongangia.com/files/common/uong-sua-dac-khi-mang-bau-co-tot-khong-xg8qh.jpg" alt="mebau">
            <h3>Chăm sóc mẹ bầu</h3>
            <a href="Search_Me" class="buy-button">Tìm hiểu thêm</a>
        </div>
    </div>
</section>

<section class="body">
    <h2>Tính năng nổi bật</h2>
</section>
<div class="container1">
    <div class="feature-item">
        <video controls class="item-video">
            <source src="https://www.pexels.com/vi-vn/download/video/4158917/" type="video/mp4">
            Trình duyệt của bạn không hỗ trợ thẻ video.
        </video>
        <div class="feature-content">
            <h2>Sản phẩm an toàn – Chất lượng được kiểm chứng</h2>
            <div class="feature-text">Tất cả sản phẩm tại Me&Be đều được chọn lọc kỹ lưỡng, có nguồn gốc rõ ràng và đạt
                tiêu chuẩn an toàn dành cho mẹ và bé. Chúng tôi luôn ưu tiên chất lượng để mang lại sự an tâm tuyệt đối
                trong từng lựa chọn.
            </div>
        </div>

    </div>

    <div class="feature-item">
        <img src="/static/image/item_giaodien.jpg" alt="Item2">
        <div class="feature-content">
            <h2 class="feature-title">Tiện lợi – Dễ dàng cho mẹ, thoải mái cho bé</h2>
            <div class="feature-text">Giao diện thân thiện, dễ tìm kiếm, cùng các danh mục sản phẩm rõ ràng giúp mẹ tiết
                kiệm thời gian mua sắm. Chỉ với vài cú click, mẹ đã có thể tìm thấy mọi thứ cho bé yêu của mình.
            </div>
        </div>
    </div>
    <div class="feature-item">
        <img src="http://file.hstatic.net/200000472237/article/shipper-ghn-giao-hang-cho-khach_fc14a6d13f6f4d7dbbf19e88eed8862b.png"
             alt="Item3">
        <div class="feature-content">
            <h2 class="feature-title">Dịch vụ tận tâm – Giao hàng nhanh chóng</h2>
            <div class="feature-text">Me&Be cam kết giao hàng tận nơi trong thời gian ngắn nhất, hỗ trợ đổi trả linh
                hoạt và luôn sẵn sàng tư vấn để mẹ có trải nghiệm mua sắm trọn vẹn, tiện lợi và vui vẻ.
            </div>
        </div>
    </div>
    <div class="feature-item">
        <img src="https://www.bigc.vn/files/banners/2022/july-nh/sep-nh/resize-cover-blog-5.jpg" alt="Item4">
        <div class="feature-content">
            <h2 class="feature-title">Đa dạng sản phẩm – Đáp ứng mọi nhu cầu của mẹ và bé</h2>
            <div class="feature-text">Từ sữa, tã, đồ chơi, quần áo đến các thiết bị hỗ trợ chăm sóc bé, MeOi Baby mang
                đến hàng nghìn lựa chọn phong phú. Mẹ có thể dễ dàng tìm thấy mọi sản phẩm cần thiết ở cùng một nơi.
            </div>
        </div>
    </div>
    <div class="feature-item">
        <img src="https://tse1.mm.bing.net/th/id/OIP.Fj3afZBocGGf4r1JBNM8VAHaFX?pid=Api&P=0&h=180" alt="Item5">
        <div class="feature-content">
            <h2 class="feature-title">Ưu đãi hấp dẫn – Mua sắm tiết kiệm hơn mỗi ngày</h2>
            <div class="feature-text">Thường xuyên cập nhật chương trình khuyến mãi, quà tặng và voucher giảm giá giúp
                mẹ mua sắm thông minh và tiết kiệm. Mỗi đơn hàng đều là cơ hội để nhận thêm niềm vui bất ngờ.
            </div>

        </div>

    </div>

    <div class="feature-item">
        <img src="https://thuonghieusanpham.vn/stores/news_dataimages/2023/082023/27/15/in_article/anh-3a20230827153741.png?rt=20230827153754"
             alt="Item6">
        <div class="feature-content">
            <h2 class="feature-title">Cộng đồng gắn kết – Nơi chia sẻ kinh nghiệm nuôi dạy con</h2>
            <div class="feature-text">Không chỉ là nơi mua sắm, MeOi Baby còn là cộng đồng yêu thương, nơi các mẹ có thể
                học hỏi, chia sẻ kinh nghiệm và lan tỏa tình yêu thương dành cho con trẻ.
            </div>
        </div>
    </div>
</div>
<%--<section class="body">--%>
<%--    <div class="content">--%>
<%--        <div class="container2">--%>
<%--            <header>--%>
<%--                <div class="text">Sản Phẩm Nổi Bật</div>--%>
<%--                <nav>--%>
<%--                    <a data-target="SP1" class="nav-link active">Sữa Aptamil</a>--%>
<%--                    <a data-target="SP2" class="nav-link">Tã quần Bobby</a>--%>
<%--                    <a data-target="SP3" class="nav-link">Máy hâm sữa GluckBaby</a>--%>
<%--                    <a data-target="SP4" class="nav-link">Sữa cho mẹ bầu Enfa</a>--%>
<%--                </nav>--%>
<%--            </header>--%>

<%--            <div id="SP1" class="product-showcase active">--%>
<%--                <div class="product-image">--%>
<%--                    <img src="https://concung.com/img/news/2023/2129-1691567165-cover.png" alt="Sữa Aptamil">--%>
<%--                </div>--%>
<%--                <h2 class="product-title">Sữa Aptamil</h2>--%>
<%--                <p class="product-description">Dinh dưỡng vàng cho bé</p>--%>
<%--                <a href="search-category?category_id=3" class="buy-button">Mua ngay</a>--%>
<%--            </div>--%>

<%--            <div id="SP2" class="product-showcase">--%>
<%--                <div class="product-image">--%>
<%--                    <img src="https://concung.com/img/news/2024/2713-1709801834-cover.png" alt="Tã quần Bobby">--%>
<%--                </div>--%>
<%--                <h2 class="product-title">Tã quần Bobby</h2>--%>
<%--                <p class="product-description">Siêu mềm mại, thấm hút tốt – cho bé luôn khô thoáng</p>--%>
<%--                <a href="search-category?category_id=4" class="buy-button">Mua ngay</a>--%>
<%--            </div>--%>

<%--            <div id="SP3" class="product-showcase">--%>
<%--                <div class="product-image">--%>
<%--                    <img src="https://media.bibomart.net/images/2025/2/11/0/origin/may-ham-sua-1739261305.jpg" alt="Máy hâm sữa GluckBaby">--%>
<%--                </div>--%>
<%--                <h2 class="product-title">Máy hâm sữa GluckBaby</h2>--%>
<%--                <p class="product-description">Giữ ấm sữa nhanh, tiện lợi – hỗ trợ mẹ chăm bé dễ dàng hơn</p>--%>
<%--                <a href="search-category?category_id=2" class="buy-button">Mua ngay</a>--%>
<%--            </div>--%>

<%--            <div id="SP4" class="product-showcase">--%>
<%--                <div class="product-image">--%>
<%--                    <img src="https://cdn.tgdd.vn/Products/Images/2382/260218/Slider/tong-quan-1020x570.jpg" alt="Sữa cho mẹ bầu Enfa">--%>
<%--                </div>--%>
<%--                <h2 class="product-title">Sữa cho mẹ bầu Enfa</h2>--%>
<%--                <p class="product-description">Bổ sung dưỡng chất thiết yếu cho mẹ bầu và thai nhi khỏe mạnh</p>--%>
<%--                <a href="search-category?category_id=7" class="buy-button">Mua ngay</a>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</section>--%>
<section class="body">
    <h2>Sản phẩm nổi bật</h2>
</section>
<section class="body">
    <div class="content">

        <div class="navbar-light">
            <a onclick="showImage('image1')" data-text="Sữa Aptamil">Sữa Aptamil</a>
            <a onclick="showImage('image2')" data-text="Tã quần Bobby">Tã quần Bobby</a>
            <a onclick="showImage('image3')" data-text="Máy hâm sữa GluckBaby">Máy hâm sữa GluckBaby</a>
            <a onclick="showImage('image4')" data-text="Sữa cho mẹ bầu Enfa">Sữa cho mẹ bầu Enfa</a>
        </div>


        <div class="image-container">
            <img id="image1" src="${pageContext.request.contextPath}/static/image/suaAptamil.png" alt="Sữa Aptamil" class="active">
            <img id="image2" src=" ${pageContext.request.contextPath}/static/image/taBoby.png" alt="Tã quần Bobby">
            <img id="image3" src="${pageContext.request.contextPath}/static/image/MayHamSua.jpg" alt="Máy hâm sữa GluckBaby">
            <img id="image4" src="${pageContext.request.contextPath}/static/image/sua-enfa-20.jpg" alt="Sữa cho mẹ bầu Enfa">

            <div class="text-overlay">
                <h1 id="product-description">Sữa Aptamil</h1>
                <a href="Search_Sua" class="link">Tìm hiểu thêm</a>
                <!--                <a href="../../component/Checkout/Checkout.html" class="link">Mua ngay</a>-->
            </div>
        </div>
    </div>
</section>

<footer>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-home/footer.css">
    <div class="footer-container">
        <div class="footer-column">
            <h3>Sản Phẩm </h3>
            <ul>
                <li>Sữa & Bình Sữa</li>
                <li>Máy móc thiết yếu</li>
                <li>Combo đồ sơ sinh</li>
                <li>Ăn dặm</li>
                <li>Bỉm tã & vệ sinh</li>
                <li>Thời trang cho bé</li>
                <li>Đồ chơi cho bé</li>
                <li>Xe đẩy & đồ chơi</li>
                <li>Đồ cho mẹ bầu</li>
                <li>Thuốc & vitamin</li>
                <li>Quần áo bé trai</li>
                <li>Quần áo bé gái</li>
            </ul>
        </div>
        <div class="footer-column">
            <h3>Mua Sắm Trực Tuyến</h3>
            <ul>
                <li>Ưu Đãi Độc Quyền</li>
                <li>Khuyến mãi & ưu đãi</li>
                <li>Hướng dẫn đặt hàng</li>
                <li>Phương thức thanh toán</li>
                <li>Chính sách giao hàng</li>
                <li>Chính sách đổi trả</li>
                <li>Liên hệ hỗ trợ</li>
                <li>Điều Khoản & Điều Kiện</li>
            </ul>
        </div>
        <div class="footer-column">
            <h3>Chương trình đặc biệt</h3>
            <ul>
                <li>Ưu đãi thành viên</li>
                <li>Combo tiết kieemh cho mẹ</li>
                <li>Chương trình quà tặng bé</li>
                <li>Tích điểm đổi quà</li>
            </ul>
        </div>
        <div class="footer-column">
            <h3>Bạn Cần Hỗ Trợ?</h3>
            <ul>
                <li>Hỏi đáp thường gặp</li>
                <li>Hướng dẫn bảo quản sản phẩm</li>
                <li>Chính sách đảm bảo sản phẩm</li>
                <li>Chính sách bảo hành</li>
                <li>Gửi phản hồi cho Me&Be</li>
                <li>Liên hệ chăm sóc khách hàng</li>
            </ul>
        </div>
        <div class="footer-column">
            <h3>Sự bền vững</h3>
            <ul>
                <li>Môi trường</li>
                <li>Bảo mật & Quyền riêng tư</li>
                <li>Trợ năng</li>
                <li>Công dân Doanh nghiệp</li>
                <li>Tính bền vững của Doanh nghiệp</li>
            </ul>
            <h3>Giới thiệu về Me&Be</h3>
            <ul>
                <li>Thông tin về Công ty</li>
                <li>Lĩnh vực kinh doanh</li>
                <li>Nhận diện thương hiệu</li>
                <li>Tuyển dụng</li>
                <li>Tin tức & chia sẻ</li>
                <li>Liên hệ hợp tác</li>
                <li>Đạo đức</li>
                <li>Chính sách bảo mật</li>
            </ul>
        </div>
    </div>
    <div class="footer-bottom">
        <p>Bản quyền © 2018. Công ty cổ phần Thế Giới Di Động.</p>
        <p>Công ty cổ phần Thế Giới Di Động</p>
        <p>Giấy CNĐK: 0303217354 do sở KH & ĐT TP.HCM cấp ngày 02/01/2007.</p>
        <p>Địa chỉ: 128 Trần Quang Khải, P.Tân Định, TP.Hồ Chí Minh</p>
        <p>Điện thoại: +84-2839157310</p>
        <p>Email: hotrotmdt@thegioididong.com</p>
    </div>
</footer>
<%--<script src="${pageContext.request.contextPath}/static/style-component/style-home/homeBody.js" defer></script>--%>
<%--&lt;%&ndash;xem js lại&ndash;%&gt;--%>
<%--<script>--%>
<%--    document.addEventListener('DOMContentLoaded', function() {--%>
<%--        // Fetch top products--%>
<%--        fetch('api/top-products')--%>
<%--            .then(response => response.json())--%>
<%--            .then(products => {--%>
<%--                const sliderTrack = document.getElementById('slider-track');--%>
<%--                const sliderDots = document.getElementById('slider-dots');--%>

<%--                // Clear existing content--%>
<%--                sliderTrack.innerHTML = '';--%>
<%--                sliderDots.innerHTML = '';--%>

<%--                // Calculate number of pages (3 products per page)--%>
<%--                const productsPerPage = 3;--%>
<%--                const numPages = Math.ceil(products.length / productsPerPage);--%>

<%--                console.log('Total products:', products.length);--%>
<%--                console.log('Products per page:', productsPerPage);--%>
<%--                console.log('Number of pages:', numPages);--%>

<%--                // Create product cards--%>
<%--                products.forEach((product, index) => {--%>
<%--                    const productCard = document.createElement('div');--%>
<%--                    productCard.className = 'slider-item';--%>
<%--                    productCard.innerHTML = `--%>
<%--                    <div class="product-card">--%>
<%--                        <div class="product-image" onclick="window.location.href='product-detail?id=\${product.id}'" style="cursor: pointer;">--%>
<%--                            <img src="\${product.imageUrl}" alt="\${product.name}">--%>
<%--                        </div>--%>
<%--                        <h3 class="product-name" onclick="window.location.href='product-detail?id=\${product.id}'" style="cursor: pointer;">\${product.name}</h3>--%>
<%--                        <p class="product-price">\${product.price.toLocaleString('vi-VN')} VND</p>--%>
<%--                        <button class="buy-now" onclick="window.location.href='product-detail?id=\${product.id}'">Mua ngay</button>--%>
<%--                    </div>--%>
<%--                `;--%>
<%--                    sliderTrack.appendChild(productCard);--%>
<%--                });--%>

<%--                // Create dots--%>
<%--                for (let i = 0; i < numPages; i++) {--%>
<%--                    const dot = document.createElement('span');--%>
<%--                    dot.className = 'dot' + (i === 0 ? ' active' : '');--%>
<%--                    dot.setAttribute('data-index', i);--%>
<%--                    sliderDots.appendChild(dot);--%>
<%--                }--%>

<%--                // Initialize slider--%>
<%--                let currentPage = 0;--%>
<%--                const items = document.querySelectorAll('.slider-item');--%>
<%--                const dots = document.querySelectorAll('.dot');--%>

<%--                console.log('Total slider items created:', items.length);--%>

<%--                // FIXED: Correct updateSlider function--%>
<%--                function updateSlider() {--%>
<%--                    console.log('Current page:', currentPage);--%>

<%--                    // Calculate the translate value for the entire track--%>
<%--                    // Move the entire track to show the current page--%>
<%--                    const translateValue = -currentPage * 100; // Move left by 100% per page--%>

<%--                    // Apply transform to the slider track, not individual items--%>
<%--                    sliderTrack.style.transform = `translateX(${translateValue}%)`;--%>

<%--                    // Alternative method: Hide/show items based on current page--%>
<%--                    items.forEach((item, index) => {--%>
<%--                        const startIndex = currentPage * productsPerPage;--%>
<%--                        const endIndex = startIndex + productsPerPage;--%>

<%--                        if (index >= startIndex && index < endIndex) {--%>
<%--                            item.style.display = 'block';--%>
<%--                            item.style.opacity = '1';--%>
<%--                        } else {--%>
<%--                            item.style.display = 'none';--%>
<%--                            item.style.opacity = '0';--%>
<%--                        }--%>
<%--                    });--%>

<%--                    // Update dots--%>
<%--                    dots.forEach((dot, index) => {--%>
<%--                        dot.classList.toggle('active', index === currentPage);--%>
<%--                    });--%>
<%--                }--%>

<%--                // Add click handlers for dots--%>
<%--                dots.forEach((dot, index) => {--%>
<%--                    dot.addEventListener('click', () => {--%>
<%--                        console.log('Dot clicked:', index);--%>
<%--                        currentPage = index;--%>
<%--                        updateSlider();--%>
<%--                    });--%>
<%--                });--%>

<%--                // Add click handlers for arrows--%>
<%--                document.getElementById('prev-btn').addEventListener('click', () => {--%>
<%--                    if (currentPage > 0) {--%>
<%--                        console.log('Previous button clicked');--%>
<%--                        currentPage--;--%>
<%--                        updateSlider();--%>
<%--                    }--%>
<%--                });--%>

<%--                document.getElementById('next-btn').addEventListener('click', () => {--%>
<%--                    if (currentPage < numPages - 1) {--%>
<%--                        console.log('Next button clicked');--%>
<%--                        currentPage++;--%>
<%--                        updateSlider();--%>
<%--                    }--%>
<%--                });--%>

<%--                // Initial setup--%>
<%--                updateSlider();--%>
<%--            })--%>
<%--            .catch(error => console.error('Error loading products:', error));--%>
<%--    });--%>
<%--</script>--%>
<script src="${pageContext.request.contextPath}/static/style-component/style-home/homeBody.js"></script>
</body>
