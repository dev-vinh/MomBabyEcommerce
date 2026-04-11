$(document).ready(function () {
    const upload_avatar = $('#upload_avatar');
    const btn_upload= $('#btn_upload');
    const avatar = $('#avatar');
    const save = $('#save');

    btn_upload.on('click', function () {
        upload_avatar.click();
    })

    upload_avatar.on('change', function (event) {
        const file = event.target.files[0];
        if (file){

            const formData = new FormData();
            formData.append("file", file);
            fetch(`admin/uploadImage`, {
                method: "POST",
                body: formData,
            })
                .then(
                    response => response.json()
                ).then(data => {
                if (data.statusCode === 200) {
                    const imageId = data.data[0].id;

                    fetch(`update-avatar`,{
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({imageId: imageId}),
                    })
                }
            })

            const reader = new FileReader();
            reader.onload = function (e) {
                avatar.attr('src', e.target.result);
            }
            reader.readAsDataURL(file);


        }
    })




    // $('.update_btn').click(function() {
    //     var field = $(this).data('field');
    //     var currentValue = $('#' + field).text().trim();
    //
    //
    //     console.log(field);
    //     console.log(currentValue);
    //
    //     var inputField = $('<input>', {
    //         type: 'text',
    //         id: field + '-input',
    //         value: currentValue,
    //         class: 'update-input'
    //     });
    //
    //     $('#' + field).replaceWith(inputField);
    //     inputField.focus();
    //
    //
    //     inputField.on('blur', function() {
    //         var newValue = inputField.val().trim();
    //         if (newValue !== currentValue) {
    //              var newSpan = $('<span>', {
    //                 id: field,
    //                 class: 'item_text',
    //                 text: newValue
    //             });
    //
    //             inputField.replaceWith(newSpan);
    //         } else {
    //              var currentSpan = $('<span>', {
    //                 id: field,
    //                 class: 'item_text',
    //                 text: currentValue
    //             });
    //
    //             inputField.replaceWith(currentSpan);
    //         }
    //     });
    // });


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

    const name = $('#name');
    const displayName = $('#displayName');
    const gender = $('input[name="gender"]:checked');
    const birthYear =$('#year')
    const birthMonth =$('#month')
    const birthDay =$('#day')
    const email = $('#email')
    const phone = $('#phone')

    const birth =(`${birthYear.val()}-${birthMonth.val().padStart(2, '0')}-${birthDay.val().padStart(2, '0')}`);

    const formData = new FormData();
    formData.append("fullName", name.val());
    formData.append("displayName", displayName.val());
    formData.append("gender", gender.val());
    formData.append("birth", birth);
    formData.append("email", email.text());
    formData.append("phone", phone.text());


    for (let [key, value] of formData.entries()) {
        console.log(`${key}: ${value}`);
    }


    const jsonObject = {};
    formData.forEach((value, key) => {
        jsonObject[key] = value;
    });


    fetch(`updateUser`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonObject),
    }).then(response => {
        return  response.json()
    })
}