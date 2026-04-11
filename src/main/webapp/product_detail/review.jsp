<%--
  Created by IntelliJ IDEA.
  User: vinhp
  Date: 12/27/2025
  Time: 10:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đánh giá sản phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/product-detail/review.css"/>
</head>
<body>

<div class="write-review-section">
    <h2>Viết đánh giá của bạn</h2>
    <form id="review-form" method="post" action="<%=request.getContextPath()%>/submitReview">
        <div class="input-group">
            <label for="rating-value">Đánh giá:</label>
            <div id="user-rating" class="stars">
                <span data-value="1">☆</span>
                <span data-value="2">☆</span>
                <span data-value="3">☆</span>
                <span data-value="4">☆</span>
                <span data-value="5">☆</span>
            </div>
            <input type="hidden" id="rating-value" name="rating" value="0">
        </div>

        <div class="input-group">
            <label for="review-text">Viết đánh giá:</label>
            <textarea id="review-text" name="review" rows="4" placeholder="Chia sẻ suy nghĩ của bạn" required></textarea>
        </div>
        <div class="custom-file-upload">
            <label for="review-photo" class="file-label">+ Chọn hình ảnh</label>
            <input type="file" id="review-photo" name="photo" accept="image/*" multiple>
        </div>
        <div id="photo-preview" class="photo-preview"></div>

        <button type="submit">Gửi đánh giá</button>
    </form>
</div>

<div class="rating-section">
    <div class="rating-details">
        <h1>Đánh giá</h1>
        <p>Kết xuất nhanh xếp loại</p>
        <div class="rating-bar">
            <span>5 sao</span>
            <div><span style="width: 10%;"></span></div>
            <span>1</span>
        </div>
        <div class="rating-bar">
            <span>4 sao</span>
            <div><span style="width: 0%;"></span></div>
            <span>0</span>
        </div>
        <div class="rating-bar">
            <span>3 sao</span>
            <div><span style="width: 0%;"></span></div>
            <span>0</span>
        </div>
        <div class="rating-bar">
            <span>2 sao</span>
            <div><span style="width: 0%;"></span></div>
            <span>0</span>
        </div>
        <div class="rating-bar">
            <span>1 sao</span>
            <div><span style="width: 90%;"></span></div>
            <span>6</span>
        </div>
    </div>
    <div class="rating-summary">
        <div class="overall-rating">Xếp hạng tổng thể</div>
        <div class="stars">4.6 ★★★★☆</div>
        <a href="#">7 đánh giá</a>
        <p>0 trong số 6 (0%) người đánh giá giới thiệu sản phẩm này</p>
    </div>
    <div class="rating-input">
        <p>Đánh giá sản phẩm này</p>
        <div class="stars">
            <span>☆</span>
            <span>☆</span>
            <span>☆</span>
            <span>☆</span>
            <span>☆</span>
        </div>
        <p>Thêm đánh giá sẽ yêu cầu một email hợp lệ để xác minh</p>
    </div>
</div>

<div class="detailed-rating-section">
    <h2>Xếp loại khách hàng trung bình</h2>
    <div class="criteria">
        <div class="criteria-item">
            <span>Tính năng</span>
            <div class="criteria-bar">
                <span style="width: 10%;"></span>
            </div>
        </div>
        <div class="criteria-item">
            <span>Hiệu năng</span>
            <div class="criteria-bar">
                <span style="width: 20%;"></span>
            </div>
        </div>
        <div class="criteria-item">
            <span>Thiết kế</span>
            <div class="criteria-bar">
                <span style="width: 30%;"></span>
            </div>
        </div>
    </div>
</div>

<h2>Hình ảnh và video của khách hàng</h2>
<div class="customer-images">
    <img src="<%=request.getContextPath()%>/static/image/img-detail/img_slide1.jpg" alt="Ảnh khách hàng">
    <img src="<%=request.getContextPath()%>/static/image/img-detail/img_slide1.jpg" alt="Ảnh khách hàng">
    <img src="<%=request.getContextPath()%>/static/image/img-detail/img_slide1.jpg" alt="Ảnh khách hàng">
    <img src="<%=request.getContextPath()%>/static/image/img-detail/img_slide1.jpg" alt="Ảnh khách hàng">
    <img src="<%=request.getContextPath()%>/static/image/img-detail/img_slide1.jpg" alt="Ảnh khách hàng">
</div>
<div class="filter-section">
    <h2>Lọc Đánh Giá</h2>
    <input type="text" placeholder="Tìm kiếm chủ đề và đánh giá">
    <div class="filter-controls">
        <select>
            <option>Xếp hạng</option>
            <option>1 sao</option>
            <option>2 sao</option>
            <option>3 sao</option>
            <option>4 sao</option>
            <option>5 sao</option>
        </select>
        <select>
            <option>Bản địa</option>
            <option>Tiếng Việt</option>
            <option>Tiếng Anh</option>
        </select>
    </div>
</div>

<div class="review-header">
    <p>1 - 6 / 7 Đánh giá</p>
    <div class="sort-section">
        <span>Đánh giá theo khu vực</span>
        <select>
            <option>Sắp xếp theo</option>
            <option>Khu vực</option>
            <option>Thời gian</option>
            <option>Đánh giá cao nhất</option>
            <option>Đánh giá thấp nhất</option>
        </select>
    </div>
</div>

<div class="review-container">
    <h2>Đánh giá theo khu vực</h2>
    <!-- Ví dụ Đánh giá -->
    <div class="review-item">
        <div class="review">
<%--            sua anh cho nay--%>
            <img class="avatar" src="<%=request.getContextPath()%>/resource/image/medium%20(1).png" alt="User Avatar">
            <div class="review-info">
                <div class="star-rating">★★★★</div>
                <span class="reviewer-name">Ngoc Vinh</span>
                <span class="time">24 ngày trước</span>
            </div>
        </div>
        <p>Sữa này rất ngon và giao hàng nhanh chống</p>
        <div class="image">
            <img src="<%=request.getContextPath()%>/resource/image/1440x640_disclaimer.webp" alt="Product issue photo">
        </div>
        <div class="review-footer">
            <button>Hữu ích? (0)</button>
            <button>Không (0)</button>
            <button>Báo cáo</button>
        </div>
    </div>
</div>

</body>
</html>