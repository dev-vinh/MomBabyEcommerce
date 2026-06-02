document.addEventListener('DOMContentLoaded', () => {
    initReviewEditFlow();
    initReviewStars();
    initReviewImagePreview();
    initReviewSubmit();
    initReviewLike();
});

function initReviewEditFlow() {
    const summary = document.getElementById('my-review-summary');
    const formPanel = document.getElementById('review-form-panel');
    const editButton = document.getElementById('review-edit-toggle');
    const cancelButton = document.getElementById('review-cancel-edit');
    const form = document.getElementById('review-form');
    const ratingInput = document.getElementById('rating-value');
    const descriptionInput = document.getElementById('review-description');
    const starBox = document.getElementById('user-rating');
    const photoInput = document.getElementById('review-photo');
    const photoPreview = document.getElementById('photo-preview');

    if (!summary || !formPanel || !editButton || !form) return;

    const initialRating = ratingInput ? ratingInput.value : '0';
    const initialDescription = descriptionInput ? descriptionInput.value : '';

    editButton.addEventListener('click', () => {
        summary.classList.add('is-hidden');
        formPanel.classList.remove('is-hidden');

        if (descriptionInput) {
            descriptionInput.focus();
        }
    });

    if (cancelButton) {
        cancelButton.addEventListener('click', () => {
            if (ratingInput) {
                ratingInput.value = initialRating;
            }

            if (descriptionInput) {
                descriptionInput.value = initialDescription;
            }

            if (photoInput) {
                photoInput.value = '';
            }

            if (photoPreview) {
                photoPreview.innerHTML = '';
            }

            if (starBox) {
                starBox.dispatchEvent(new CustomEvent('review:set-rating', {
                    detail: {rating: initialRating}
                }));
            }

            formPanel.classList.add('is-hidden');
            summary.classList.remove('is-hidden');
        });
    }
}

function initReviewStars() {
    const starBox = document.getElementById('user-rating');
    const ratingInput = document.getElementById('rating-value');

    if (!starBox || !ratingInput) return;

    const stars = starBox.querySelectorAll('span');
    let currentRating = parseInt(starBox.dataset.currentRating || ratingInput.value || '0');

    paintStars(stars, currentRating);

    stars.forEach(star => {
        star.addEventListener('click', () => {
            currentRating = parseInt(star.dataset.value);
            ratingInput.value = currentRating;
            paintStars(stars, currentRating);
        });

        star.addEventListener('mouseenter', () => {
            paintStars(stars, parseInt(star.dataset.value));
        });
    });

    starBox.addEventListener('mouseleave', () => {
        paintStars(stars, currentRating);
    });

    starBox.addEventListener('review:set-rating', event => {
        currentRating = parseInt(event.detail.rating || '0');
        ratingInput.value = currentRating;
        paintStars(stars, currentRating);
    });
}

function paintStars(stars, rating) {
    stars.forEach(star => {
        const value = parseInt(star.dataset.value);
        star.textContent = value <= rating ? '★' : '☆';
        star.classList.toggle('selected', value <= rating);
    });
}

function initReviewImagePreview() {
    const photoInput = document.getElementById('review-photo');
    const photoPreview = document.getElementById('photo-preview');

    if (!photoInput || !photoPreview) return;

    photoInput.addEventListener('change', () => {
        let files = Array.from(photoInput.files);

        if (files.length > 5) {
            showReviewToast('Chỉ được upload tối đa 5 ảnh', 'error');
            files = files.slice(0, 5);

            const dataTransfer = new DataTransfer();
            files.forEach(file => dataTransfer.items.add(file));
            photoInput.files = dataTransfer.files;
        }

        photoPreview.innerHTML = '';

        files.forEach(file => {
            if (!file.type.startsWith('image/')) {
                return;
            }

            const reader = new FileReader();

            reader.onload = e => {
                const img = document.createElement('img');
                img.src = e.target.result;
                photoPreview.appendChild(img);
            };

            reader.readAsDataURL(file);
        });
    });
}

function initReviewSubmit() {
    const form = document.getElementById('review-form');

    if (!form) return;

    form.addEventListener('submit', async e => {
        e.preventDefault();

        const submitButton = form.querySelector('.review-submit-btn');
        const productId = form.dataset.productId;
        const rating = document.getElementById('rating-value').value;
        const description = document.getElementById('review-description').value.trim();
        const photoInput = document.getElementById('review-photo');

        if (!rating || parseInt(rating) < 1) {
            showReviewToast('Vui lòng chọn số sao đánh giá', 'error');
            return;
        }

        if (!description) {
            showReviewToast('Vui lòng nhập nội dung đánh giá', 'error');
            return;
        }

        if (description.length > 1000) {
            showReviewToast('Nội dung đánh giá tối đa 1000 ký tự', 'error');
            return;
        }

        const formData = new FormData();
        formData.append('productId', productId);
        formData.append('rating', rating);
        formData.append('description', description);

        if (photoInput && photoInput.files.length > 0) {
            Array.from(photoInput.files).forEach(file => {
                formData.append('images', file);
            });
        }

        try {
            if (submitButton) {
                submitButton.disabled = true;
            }

            const response = await fetch(getContextPath() + '/add-review', {
                method: 'POST',
                body: formData
            });

            const result = await response.json();

            if (!response.ok || result.status !== 'success') {
                showReviewToast(result.message || 'Không thể gửi đánh giá', 'error');

                if (submitButton) {
                    submitButton.disabled = false;
                }

                return;
            }

            showReviewToast(result.message || 'Gửi đánh giá thành công', 'success');

            setTimeout(() => {
                window.location.reload();
            }, 500);

        } catch (error) {
            console.error(error);
            showReviewToast('Có lỗi xảy ra khi gửi đánh giá', 'error');

            if (submitButton) {
                submitButton.disabled = false;
            }
        }
    });
}

function initReviewLike() {
    const buttons = document.querySelectorAll('.review-like-btn');

    buttons.forEach(button => {
        button.addEventListener('click', async () => {
            const reviewId = button.dataset.reviewId;

            const formData = new FormData();
            formData.append('reviewId', reviewId);

            try {
                const response = await fetch(getContextPath() + '/review-like', {
                    method: 'POST',
                    body: formData
                });

                const result = await response.json();

                if (!response.ok || result.status !== 'success') {
                    showReviewToast(result.message || 'Không thể thích đánh giá', 'error');
                    return;
                }

                button.classList.toggle('liked', result.liked);

                const likeText = button.querySelector('.like-text');
                const likeCount = button.querySelector('.like-count');

                if (likeText) {
                    likeText.textContent = result.liked ? 'Đã thích' : 'Thích';
                }

                if (likeCount) {
                    likeCount.textContent = result.likeCount;
                }

            } catch (error) {
                console.error(error);
                showReviewToast('Có lỗi xảy ra khi thích đánh giá', 'error');
            }
        });
    });
}

function getContextPath() {
    const path = window.location.pathname;
    const firstSlash = path.indexOf('/', 1);

    if (firstSlash === -1) {
        return '';
    }

    return path.substring(0, firstSlash);
}

function showReviewToast(message, type) {
    if (typeof window.showToast === 'function') {
        window.showToast(message, type);
    } else {
        alert(message);
    }
}
