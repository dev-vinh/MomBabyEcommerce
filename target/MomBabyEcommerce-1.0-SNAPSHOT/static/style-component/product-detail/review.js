document.addEventListener('DOMContentLoaded', () => {
    const stars = document.querySelectorAll('#user-rating .stars span');
    const ratingInput = document.getElementById('rating-value');

    stars.forEach(star => {
        star.addEventListener('click', () => {
            ratingInput.value = star.dataset.value;
            stars.forEach(s => s.classList.remove('selected'));
            star.classList.add('selected');
        });

        star.addEventListener('mouseover', () => {
            stars.forEach(s => s.classList.remove('hover'));
            star.classList.add('hover');
            star.previousElementSibling && star.previousElementSibling.classList.add('hover');
        });

        star.addEventListener('mouseout', () => {
            stars.forEach(s => s.classList.remove('hover'));
        });
    });

    const reviewForm = document.getElementById('review-form');
    reviewForm.addEventListener('submit', e => {
        e.preventDefault();
        alert('Cảm ơn bạn đã gửi đánh giá!');

        reviewForm.reset();
        stars.forEach(s => s.classList.remove('selected'));
        ratingInput.value = 0;
    });
});


document.addEventListener('DOMContentLoaded', () => {
    const photoInput = document.getElementById('review-photo');
    const photoPreview = document.getElementById('photo-preview');

    photoInput.addEventListener('change', () => {
        const files = Array.from(photoInput.files);

        photoPreview.innerHTML = '';

        files.forEach((file, index) => {
            const reader = new FileReader();
            reader.onload = (e) => {
                const imgContainer = document.createElement('div');
                imgContainer.className = 'remove-photo';

                const img = document.createElement('img');
                img.src = e.target.result;
                img.alt = `Image ${index + 1}`;

                imgContainer.appendChild(img);
                photoPreview.appendChild(imgContainer);

                imgContainer.addEventListener('click', () => {
                    const updatedFiles = Array.from(photoInput.files).filter((_, i) => i !== index);
                    const dataTransfer = new DataTransfer();
                    updatedFiles.forEach(f => dataTransfer.items.add(f));
                    photoInput.files = dataTransfer.files;
                    imgContainer.remove();
                });
            };
            reader.readAsDataURL(file);
        });
    });
});

