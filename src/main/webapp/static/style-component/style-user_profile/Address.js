
const addBtn = document.querySelector(".add_btn");
const addressList = document.querySelectorAll(".address_item");
const formContainer = document.getElementById("addAddressFormContainer");
const overlay = document.querySelector(".overlay");


document.addEventListener("DOMContentLoaded", function () {

    // Hiển thị form thêm địa chỉ và overlay
    addBtn.addEventListener("click", function () {
        formContainer.style.display = "block";
        overlay.style.display = "block";
    });
    // Ẩn popup nếu bấm ra ngoài overlay
    overlay.addEventListener("click", function () {
        console.log("overlay");
        formContainer.style.display = "none";
        overlay.style.display = "none";
    });


    addressList.forEach((address) => {
        const update_btn = address.querySelector(".update_btn");
        if (update_btn) {
            update_btn.addEventListener("click", () => {
                formContainer.style.display = "block";


                overlay.style.display = "block";
            })
        }

    })





});


// Add event to close btn
document.addEventListener("DOMContentLoaded", function () {
    const formContainer = document.getElementById("addAddressFormContainer");
    const closeIcon = document.querySelector(".close-icon");

    closeIcon.addEventListener("click", function () {
        formContainer.style.display = "none";
        overlay.style.display = "none";
    });
});


$(document).ready(function () {
    $("#province, #district, #commune").select({ width: '100%' });

    fetch("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "token": "676e7671-116a-11f0-95d0-0a92b8726859"
        }
    })
        .then(response => response.json())
        .then(data => {
            $("#province").append(new Option("Chọn tỉnh", ""));
            data.data.forEach(province => {
                $("#province").append(new Option(province.ProvinceName, province.ProvinceID));
            });
        })
        .catch(error => console.error("Lỗi khi gọi API:", error));


    // Khi chọn tỉnh -> Load huyện
    $("#province").on("change", function () {
        let provinceId = $(this).val();
        let provinceName = $("#province option:selected").text();
        $(this).attr("data-name", provinceName);
        $(this).attr("data-id", provinceId);


        $("#district").empty().append(new Option("Chọn huyện", ""));

        if (provinceId) {
            fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "token": "676e7671-116a-11f0-95d0-0a92b8726859",

                },
                body: JSON.stringify({
                    province_id: parseInt(provinceId),
                })

            })
                .then(response => response.json())
                .then(data => {
                    data.data.forEach(district => {
                        $("#district").append(new Option(district.DistrictName, district.DistrictID));
                    });
                });
        }
    });

    // Khi chọn huyện -> Load xã
    $("#district").on("change", function () {
        let districtId = $(this).val();
        let districtName = $("#district option:selected").text();
        $(this).attr("data-name", districtName);
        $(this).attr("data-id", districtId);

        $("#commune").empty().append(new Option("Chọn xã", ""));

        if (districtId ) {
            fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id`,{
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "token": "676e7671-116a-11f0-95d0-0a92b8726859",
                },
                body: JSON.stringify({
                    district_id: parseInt( districtId )
                })
            })
                .then(response => response.json())
                .then(data => {
                    data.data.forEach(commune => {
                        $("#commune").append(new Option(commune.WardName, commune.WardCode));
                    });
                });
        }
    });

    // Khi chọn xã -> Lưu tên xã
    $("#commune").on("change", function () {
        let communeId = $(this).val();
        let communeName = $("#commune option:selected").text();
        $(this).attr("data-name", communeName);
        $(this).attr("data-id", communeId);
    });



// Kiểm tra số điện thoại
    $("#phone").on("input", function () {
        this.value = this.value.replace(/\D/g, "").slice(0, 10);
    });

});




// Add address
document.querySelector("form").addEventListener("submit", function(event) {
    event.preventDefault(); // Chặn chuyển trang
    let userId = sessionStorage.getItem("userId");
    let formData = {
        userId: userId,
        province: document.getElementById("province").getAttribute("data-name"),
        provinceId: $("#province").attr("data-id"),

        district: document.getElementById("district").getAttribute("data-name"),
        districtId: $("#district").attr("data-id"),

        commune: document.getElementById("commune").getAttribute("data-name"),
        communeId:  $("#commune").attr("data-id"),

        detail: document.getElementById("detail").value,
        name: document.getElementById("name").value,
        phone: document.getElementById("phone").value,
        type: document.querySelector('input[name="addressType"]:checked').value,
        // isDefault: document.getElementById("default").checked
        isDefault: false
    };

    console.log(formData);

    const form = document.getElementById("addAddressForm");
    const formAction = form.getAttribute("action");
    fetch(formAction, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === "success") {
                alert("Địa chỉ đã được thêm thành công!");
                location.reload();
            } else {
                alert("Lỗi: " + data.message);
            }
        })
        .catch(error => console.error("Lỗi:", error));
});


function deleteAddress ( addressId) {
    fetch('address/delete', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({"addressId": addressId})
    }).then(response => response.json())
        .then(data => {
            if (data.status === "success") {
                alert("Xóa thành công");
                location.reload();
            }else {
                alert("Đã xảy ra lỗi, vui lòng thử lại.")
            }
        })

}



function setDefault ( addressId) {
    fetch('address/default', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({"addressId": addressId})
    }).then(response => response.json())
        .then(data => {
            if (data.status === "success") {
                alert("Thay đổi thành công");
                location.reload();
            }else {
                alert("Đã xảy ra lỗi, vui lòng thử lại.")
            }
        })

}