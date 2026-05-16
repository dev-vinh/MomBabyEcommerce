<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="review-wrapper">
    <div class="review-title-row">
        <h2>Đánh giá sản phẩm</h2>

        <form method="get"
              action="${pageContext.request.contextPath}/product-detail"
              class="review-sort-form">

            <input type="hidden" name="id" value="${product.id}">

            <select name="sort" onchange="this.form.submit()">
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
            <div class="review-form-box">
                <h3>
                    <c:choose>
                        <c:when test="${not empty myReview}">
                            Sửa đánh giá của bạn
                        </c:when>
                        <c:otherwise>
                            Viết đánh giá của bạn
                        </c:otherwise>
                    </c:choose>
                </h3>

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

                    <div class="review-upload-row">
                        <label for="review-photo">
                            Thêm ảnh thực tế, tối đa 5 ảnh
                        </label>

                        <input type="file"
                               id="review-photo"
                               name="images"
                               accept="image/*"
                               multiple>
                    </div>

                    <div id="photo-preview" class="photo-preview"></div>

                    <button type="submit" class="review-submit-btn">
                        <c:choose>
                            <c:when test="${not empty myReview}">
                                Cập nhật đánh giá
                            </c:when>
                            <c:otherwise>
                                Gửi đánh giá
                            </c:otherwise>
                        </c:choose>
                    </button>
                </form>
            </div>
        </c:when>

        <c:otherwise>
            <div class="review-note">
                Bạn cần mua sản phẩm và đơn hàng phải hoàn thành thì mới có thể đánh giá.
            </div>
        </c:otherwise>
    </c:choose>

    <div class="review-list">
        <c:choose>
            <c:when test="${empty reviews}">
                <div class="empty-review">
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
                                    Đã mua hàng
                                </span>
                            </c:if>
                        </div>

                        <div class="review-content">
                            <c:out value="${review.description}"/>
                        </div>

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
                                <strong>Phản hồi từ shop</strong>
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