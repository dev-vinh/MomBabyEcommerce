let images = [];
let currentIndex = 0;

// Lấy danh sách hình ảnh từ các thumbnails khi trang được tải
document.addEventListener('DOMContentLoaded', () => {
    const mainImage = document.getElementById('mainImage');
    const contextPath = mainImage.dataset.contextPath; // Lấy contextPath từ data-attribute

    // Lấy tất cả các thumbnail và tạo đường dẫn đầy đủ cho từng hình ảnh
    const thumbnails = document.querySelectorAll('.thumbnail');
    images = Array.from(thumbnails).map((thumbnail) => {
        return `${contextPath}${thumbnail.getAttribute('src').replace(contextPath, '')}`; // Thêm contextPath nếu cần
    });

    // Hiển thị hình ảnh đầu tiên mặc định
    showImage(currentIndex);
});

function showImage(index) {
    const mainImage = document.getElementById('mainImage');
    const thumbnails = document.querySelectorAll('.thumbnail');

    if (index >= 0 && index < images.length) {
        // Cập nhật src của mainImage với đường dẫn đầy đủ từ mảng images
        mainImage.src = images[index];
        currentIndex = index;

        // Cập nhật trạng thái active cho thumbnail
        thumbnails.forEach((thumbnail, i) => {
            if (i === index) {
                thumbnail.classList.add('active');
            } else {
                thumbnail.classList.remove('active');
            }
        });
    } else {
        console.error(`Index ${index} is out of bounds`);
    }
}

function nextImage() {
    currentIndex = (currentIndex + 1) % images.length; // Lặp vòng nếu đến cuối
    showImage(currentIndex);
}

function prevImage() {
    currentIndex = (currentIndex - 1 + images.length) % images.length; // Lặp vòng nếu về đầu
    showImage(currentIndex);
}
