
const mainToggleButton = document.getElementById("toggle-specs-btn");
const bottomToggleButton = document.getElementById("toggle-specs-btn-bottom");
const specsSection = document.getElementById("specification-section");

let currentIndex = 0;

// Hiển thị slide hiện tại
function showSlide(index) {
    currentIndex = index;
    const carouselImage = document.getElementById("carousel-image");
    carouselImage.src = images[currentIndex];
    updateDots();
}

// Chuyển đến slide tiếp theo
function nextSlide() {
    currentIndex = (currentIndex + 1) % images.length;
    showSlide(currentIndex);
}

// Quay lại slide trước đó
function prevSlide() {
    currentIndex = (currentIndex - 1 + images.length) % images.length;
    showSlide(currentIndex);
}

// Cập nhật trạng thái của các chấm điều hướng
function updateDots() {
    const dots = document.querySelectorAll(".dot");
    dots.forEach((dot, index) => {
        dot.classList.toggle("active", index === currentIndex);
    });
}

// Trạng thái mở rộng thông số kỹ thuật
let isExpanded = false;

// Điều chỉnh chiều cao iframe
function resizeIframe(iframe) {
    const initialHeight = 500; // Chiều cao ban đầu của iframe
    const expandedHeight = iframe.contentWindow.document.body.scrollHeight; // Chiều cao mở rộng

    // Cập nhật chiều cao dựa trên trạng thái
    iframe.style.height = isExpanded ? expandedHeight + 'px' : initialHeight + 'px';
}

// Mở rộng hoặc thu gọn phần thông số kỹ thuật
function toggleSpecification() {
    const iframe = document.querySelector('.specification .iframe');
    isExpanded = !isExpanded; // Thay đổi trạng thái mở rộng

    resizeIframe(iframe); // Điều chỉnh chiều cao iframe

    document.querySelector('.show-more-btn').textContent = isExpanded ? "Thu gọn" : "Xem thêm";
}

// Hiển thị hoặc ẩn phần thông số kỹ thuật
function toggleSpecifications() {
    if (specsSection.style.display === "none") {
        specsSection.style.display = "block";
        mainToggleButton.style.display = "none"; // Ẩn nút chính
    } else {
        specsSection.style.display = "none";
        mainToggleButton.style.display = "inline-block"; // Hiển thị nút chính
    }
}

// Gắn sự kiện cho các nút hiển thị/ẩn thông số kỹ thuật
mainToggleButton.addEventListener("click", toggleSpecifications);
bottomToggleButton.addEventListener("click", toggleSpecifications);

// Thêm sản phẩm vào giỏ hàng với thông báo
document.addEventListener("DOMContentLoaded", function () {
    const addToCartButtons = document.querySelectorAll(".btn.add");
    const notification = document.getElementById("cart-notification");

    addToCartButtons.forEach(button => {
        button.addEventListener("click", function () {
            notification.classList.remove("hidden");
            notification.classList.add("show");

            setTimeout(() => {
                notification.classList.remove("show");
                notification.classList.add("hidden");
            }, 3000);
        });
    });
});

// Tự động tải slide đầu tiên và cập nhật trạng thái
document.addEventListener("DOMContentLoaded", function () {
    showSlide(0); // Hiển thị slide đầu tiên
    updateDots(); // Cập nhật trạng thái chấm điều hướng
});




