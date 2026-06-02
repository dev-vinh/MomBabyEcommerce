<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="review-wrapper">
    <div class="review-title-row">
        <div class="review-heading">
            <div>
                <h2>Đánh giá sản phẩm</h2>
                <p>${reviewStats.totalReviews} đánh giá</p>
            </div>
        </div>

        <form method="get"
              action="${pageContext.request.contextPath}/product-detail"
              class="review-sort-form">

            <input type="hidden" name="id" value="${product.id}">

            <label for="review-sort">Sắp xếp</label>
            <select id="review-sort" name="sort" onchange="this.form.submit()">
                <option value="newest" ${reviewSort == 'newest' ? 'selected' : ''}>
                    Mới nhất
                </option>

                <option value="oldest" ${reviewSort == 'oldest' ? 'selected' : ''}>
                    Cũ nhất
                </option>

                <option value="rating_desc" ${reviewSort == 'rating_desc' ? 'selected' : ''}>
                    Sao cao nhất
                </option>

                <option value="rating_asc" ${reviewSort == 'rating_asc' ? 'selected' : ''}>
                    Sao thấp nhất
                </option>
            </select>
        </form>
    </div>

    <div class="rating-overview">
        <div class="rating-score-box">
            <span class="rating-score-label">Điểm trung bình</span>
            <div class="rating-score">
                <fmt:formatNumber value="${reviewStats.averageRating}" pattern="0.0"/>
            </div>

            <div class="rating-stars">★★★★★</div>

            <div class="rating-count">
                ${reviewStats.totalReviews} đánh giá
            </div>
        </div>

        <div class="rating-bars">
            <c:set var="total"
                   value="${reviewStats.totalReviews == 0 ? 1 : reviewStats.totalReviews}"/>

            <div class="rating-bar-row">
                <span>5 sao</span>
                <div class="bar">
                    <span style="width:${reviewStats.fiveStar * 100 / total}%"></span>
                </div>
                <b>${reviewStats.fiveStar}</b>
            </div>

            <div class="rating-bar-row">
                <span>4 sao</span>
                <div class="bar">
                    <span style="width:${reviewStats.fourStar * 100 / total}%"></span>
                </div>
                <b>${reviewStats.fourStar}</b>
            </div>

            <div class="rating-bar-row">
                <span>3 sao</span>
                <div class="bar">
                    <span style="width:${reviewStats.threeStar * 100 / total}%"></span>
                </div>
                <b>${reviewStats.threeStar}</b>
            </div>

            <div class="rating-bar-row">
                <span>2 sao</span>
                <div class="bar">
                    <span style="width:${reviewStats.twoStar * 100 / total}%"></span>
                </div>
                <b>${reviewStats.twoStar}</b>
            </div>

            <div class="rating-bar-row">
                <span>1 sao</span>
                <div class="bar">
                    <span style="width:${reviewStats.oneStar * 100 / total}%"></span>
                </div>
                <b>${reviewStats.oneStar}</b>
            </div>
        </div>
    </div>

    <c:choose>
        <c:when test="${canReview}">
            <c:if test="${not empty myReview}">
                <div id="my-review-summary" class="my-review-summary">
                    <div class="my-review-header">
                        <div class="my-review-title">
                            <span class="my-review-icon">
                                <i class="fa-solid fa-check"></i>
                            </span>
                            <div>
                                <h3>Đánh giá của bạn</h3>
                                <p>Đánh giá đã được ghi nhận. Bạn có thể chỉnh sửa khi cần.</p>
                            </div>
                        </div>

                        <button type="button" id="review-edit-toggle" class="review-edit-toggle">
                            <i class="fa-regular fa-pen-to-square"></i>
                        </button>
                    </div>

                    <div class="my-review-stars">
                        <c:forEach begin="1" end="5" var="i">
                            <c:choose>
                                <c:when test="${i <= myReview.rating}">
                                    ★
                                </c:when>
                                <c:otherwise>
                                    ☆
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>

                    <div class="my-review-content"><c:out value="${myReview.description}"/></div>

                    <c:if test="${not empty myReview.imageUrls}">
                        <div class="my-review-images">
                            <c:forEach var="img" items="${myReview.imageUrls}">
                                <img src="${img}" alt="Ảnh đánh giá của bạn">
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </c:if>

            <div id="review-form-panel"
                 class="review-form-box ${not empty myReview ? 'is-hidden' : ''}">
                <div class="review-form-header">
                    <span class="review-form-icon">
                        <i class="fa-regular fa-pen-to-square"></i>
                    </span>
                    <div>
                        <h3>
                            <c:choose>
                                <c:when test="${not empty myReview}">
                                    Chỉnh sửa đánh giá
                                </c:when>
                                <c:otherwise>
                                    Viết đánh giá của bạn
                                </c:otherwise>
                            </c:choose>
                        </h3>
                        <p>
                            <c:choose>
                                <c:when test="${not empty myReview}">
                                    Cập nhật sao, nội dung hoặc chọn ảnh mới nếu bạn muốn thay ảnh cũ.
                                </c:when>
                                <c:otherwise>
                                    Chia sẻ cảm nhận thực tế để giúp khách hàng khác chọn đúng sản phẩm.
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>

                <form id="review-form"
                      enctype="multipart/form-data"
                      data-product-id="${product.id}">

                    <input type="hidden"
                           id="rating-value"
                           name="rating"
                           value="${empty myReview ? 0 : myReview.rating}">

                    <div class="user-rating-row">
                        <span>Chọn số sao:</span>

                        <div id="user-rating"
                             class="user-rating-stars"
                             data-current-rating="${empty myReview ? 0 : myReview.rating}">

                            <span data-value="1">☆</span>
                            <span data-value="2">☆</span>
                            <span data-value="3">☆</span>
                            <span data-value="4">☆</span>
                            <span data-value="5">☆</span>
                        </div>
                    </div>

                    <textarea id="review-description"
                              name="description"
                              rows="4"
                              maxlength="1000"
                              placeholder="Chia sẻ cảm nhận thật của bạn về sản phẩm..."><c:out value="${myReview.description}"/></textarea>

                    <c:if test="${not empty myReview.imageUrls}">
                        <div class="review-current-images">
                            <span>Ảnh hiện tại</span>
                            <div>
                                <c:forEach var="img" items="${myReview.imageUrls}">
                                    <img src="${img}" alt="Ảnh đánh giá hiện tại">
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>

                    <div class="review-upload-row">
                        <label for="review-photo">
                            <i class="fa-regular fa-images"></i>
                            <c:choose>
                                <c:when test="${not empty myReview}">
                                    Chọn ảnh mới nếu muốn thay ảnh cũ
                                </c:when>
                                <c:otherwise>
                                    Thêm ảnh thực tế, tối đa 5 ảnh
                                </c:otherwise>
                            </c:choose>
                        </label>

                        <input type="file"
                               id="review-photo"
                               name="images"
                               accept="image/*"
                               multiple>
                    </div>

                    <div id="photo-preview" class="photo-preview"></div>

                    <div class="review-form-actions">
                        <button type="submit" class="review-submit-btn">
                            <i class="fa-solid fa-paper-plane"></i>
                            <c:choose>
                                <c:when test="${not empty myReview}">
                                    Lưu thay đổi
                                </c:when>
                                <c:otherwise>
                                    Gửi đánh giá
                                </c:otherwise>
                            </c:choose>
                        </button>

                        <c:if test="${not empty myReview}">
                            <button type="button" id="review-cancel-edit" class="review-cancel-btn">
                                Hủy chỉnh sửa
                            </button>
                        </c:if>
                    </div>
                </form>
            </div>
        </c:when>

        <c:otherwise>
            <div class="review-note">
                <i class="fa-solid fa-circle-info"></i>
                Bạn cần mua sản phẩm và đơn hàng phải hoàn thành thì mới có thể đánh giá.
            </div>
        </c:otherwise>
    </c:choose>

    <div class="review-list">
        <c:choose>
            <c:when test="${empty reviews}">
                <div class="empty-review">
                    <i class="fa-regular fa-comment-dots"></i>
                    Chưa có đánh giá nào cho sản phẩm này.
                </div>
            </c:when>

            <c:otherwise>
                <c:forEach var="review" items="${reviews}">
                    <div class="review-item">
                        <div class="review-user-row">
                            <div class="review-avatar">
                                <c:choose>
                                    <c:when test="${not empty review.avatarUrl}">
                                        <img src="${review.avatarUrl}" alt="avatar">
                                    </c:when>

                                    <c:otherwise>
                                        <div class="avatar-placeholder">
                                            <i class="fa-solid fa-user"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="review-user-info">
                                <strong>
                                    <c:out value="${review.userName}"/>
                                </strong>

                                <div class="review-stars">
                                    <c:forEach begin="1" end="5" var="i">
                                        <c:choose>
                                            <c:when test="${i <= review.rating}">
                                                ★
                                            </c:when>
                                            <c:otherwise>
                                                ☆
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </div>

                                <div class="review-date">
                                        ${review.createdAt}
                                </div>
                            </div>

                            <c:if test="${review.verifiedPurchase}">
                                <span class="verified-badge">
                                    <i class="fa-solid fa-check"></i>
                                    Đã mua hàng
                                </span>
                            </c:if>
                        </div>

                        <div class="review-content"><c:out value="${review.description}"/></div>

                        <c:if test="${not empty review.imageUrls}">
                            <div class="review-images">
                                <c:forEach var="img" items="${review.imageUrls}">
                                    <img src="${img}" alt="review image">
                                </c:forEach>
                            </div>
                        </c:if>

                        <div class="review-action-row">
                            <button type="button"
                                    class="review-like-btn ${review.likedByCurrentUser ? 'liked' : ''}"
                                    data-review-id="${review.id}">

                                <i class="fa-regular fa-thumbs-up"></i>

                                <span class="like-text">
                                        ${review.likedByCurrentUser ? 'Đã thích' : 'Thích'}
                                </span>

                                <span class="like-count">
                                        ${review.likeCount}
                                </span>
                            </button>
                        </div>

                        <c:if test="${not empty review.adminReply}">
                            <div class="shop-reply">
                                <strong>
                                    <i class="fa-solid fa-store"></i>
                                    Phản hồi từ shop
                                </strong>
                                <p>
                                    <c:out value="${review.adminReply}"/>
                                </p>
                                <small>${review.repliedAt}</small>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${reviewTotalPages > 1}">
        <div class="review-pagination">
            <c:forEach begin="1" end="${reviewTotalPages}" var="p">
                <a class="${p == reviewPage ? 'active' : ''}"
                   href="${pageContext.request.contextPath}/product-detail?id=${product.id}&sort=${reviewSort}&reviewPage=${p}">
                        ${p}
                </a>
            </c:forEach>
        </div>
    </c:if>
</div>
