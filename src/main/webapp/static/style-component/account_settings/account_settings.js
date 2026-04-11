// Toggle Password Visibility
document.addEventListener('DOMContentLoaded', () => {
    // Toggle password visibility for all password fields
    document.querySelectorAll('.toggle_pass').forEach(icon => {
        icon.addEventListener('click', () => {
            // Get the input element associated with the icon
            const input = icon.previousElementSibling;

            if (input.type === 'password') {
                input.type = 'text'; // Show password
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                input.type = 'password'; // Hide password
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    });
});

//Toggle-switch
document.addEventListener("DOMContentLoaded", () => {
    // Lấy tất cả các phần tử switch
    const switchElements = document.querySelectorAll(".switch");

    // Lặp qua từng phần tử và gắn sự kiện click
    switchElements.forEach(switchElement => {
        switchElement.addEventListener("click", () => {
            // Toggle lớp active cho phần tử được nhấn
            switchElement.classList.toggle("active");
        });
    });
});

//Basic-information
document.addEventListener('DOMContentLoaded', function() {
    const selectedOption = document.getElementById('selected-option');
    const optionsContainer = document.getElementById('options-container');

    // Hiển thị và ẩn danh sách tùy chọn khi click vào selected-option
    selectedOption.addEventListener('click', () => {
        optionsContainer.style.display = optionsContainer.style.display === 'block' ? 'none' : 'block';
    });

    // Khi chọn một tùy chọn
    document.querySelectorAll('.option').forEach(option => {
        option.addEventListener('click', function() {
            // Cập nhật selected option với cờ và tên quốc gia đã chọn
            const selectedFlag = this.querySelector('.flag-icon').src;
            const selectedText = this.textContent.trim();
            selectedOption.innerHTML = `<img src="${selectedFlag}" alt="Flag Icon" class="flag-icon"> ${selectedText}`;

            // Ẩn danh sách tùy chọn sau khi chọn
            optionsContainer.style.display = 'none';
        });
    });

    // Ẩn danh sách tùy chọn nếu click ra ngoài dropdown
    document.addEventListener('click', function(event) {
        if (!event.target.closest('#custom-select') && !event.target.closest('.location-select-container')) {
            optionsContainer.style.display = 'none';
        }
    });
});

