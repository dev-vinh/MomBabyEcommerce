$(document).ready(function () {
    const upload_avatar = $('#upload_avatar');
    const btn_upload= $('#btn_upload');
    const avatar = $('#avatar');
    const save = $('#save');

    btn_upload.on('click', function () {
        upload_avatar.click();
    })

    upload_avatar.on('change', function (event) {

        console.log("change triggered");

        const file = event.target.files[0];

        console.log(file);

        if (file){

            const formData = new FormData();
            formData.append("file", file);

            fetch(`${window.contextPath}/api/uploadImage`, {
                method: "POST",
                body: formData,
            })
                .then(response => response.json())
                .then(data => {

                    console.log("upload response:", data);

                    if (data.statusCode === 200) {

                        const imageId = data.data[0].id;

                        console.log("imageId:", imageId);

                        return fetch(`update-avatar`,{
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify({imageId: imageId}),
                        });
                    }
                })
                .then(res => res.json())
                .then(data => {
                    console.log("update avatar:", data);
                })
                .catch(err => {
                    console.error(err);
                });
        }
    })

    $('.update_btn').click(function() {
        var field = $(this).data('field');
        var spanElement = $('#' + field); // Lấy phần tử span
        var currentValue = spanElement.length ? spanElement.text().trim() : ''; // Kiểm tra xem phần tử tồn tại

        console.log(field);
        console.log("Current Value:", currentValue);

        // Tạo ô input thay thế
        var inputField = $('<input>', {
            type: 'text',
            id: field + '-input',
            value: currentValue, // Nếu trống thì vẫn hiển thị input rỗng
            class: 'update-input'
        });

        // Thay thế span bằng input
        if (spanElement.length) {
            spanElement.replaceWith(inputField);
        } else {
            console.error("Element with ID '" + field + "' does not exist.");
        }

        inputField.focus();

        // Xử lý khi người dùng nhấn blur (thoát ô input)
        inputField.on('blur', function() {
            var newValue = inputField.val().trim();
            var newSpan = $('<span>', {
                id: field,
                class: 'item_text',
                text: newValue // Trả lại nội dung mới hoặc trống
            });

            inputField.replaceWith(newSpan);
        });
    });

    save.on('click', function (event) {
        update_profile();
    })

})

function update_profile() {
    const name = $('#name').val().trim();
    const displayName = $('#displayName').val().trim();

    const gender = $('input[name="gender"]:checked');
    const genderValue = gender.length > 0 ? gender.val() : null;

    const phone = $('#phone').val().trim();

    if (phone && !isValidPhoneNumber(phone)) {
        alert("Số điện thoại không hợp lệ.");
        return;
    }

    const jsonObject = {
        fullName: name,
        displayName: displayName,
        gender: genderValue,
        phoneNumber: phone
    };

    fetch(`updateUser`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonObject),
    })
        .then(response => response.json())
        .then(data => {

            console.log(data);

            if (data.success) {

                $('#name').val(name);
                $('#displayName').val(displayName);
                $('#phone').val(phone);
                $('.nav_item').text("Xin chào " + displayName);

                alert("Update success!");
            } else {
                alert("Update fail!");
            }
        })
        .catch(error => {
            console.error(error);
        });
}

function isValidPhoneNumber(phone) {
    const phoneRegex = /^(03|05|07|08|09)\d{8}$/;
    return phoneRegex.test(phone);
}