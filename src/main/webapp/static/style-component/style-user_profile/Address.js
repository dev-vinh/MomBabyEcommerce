/* ================================================================
   Address.js  –  phù hợp với Address.jsp (JSTL version)
   API địa chỉ: GHN (Giao Hàng Nhanh)
================================================================ */

const GHN_TOKEN = "b5610ffa-4b5b-11f1-a973-aee5264794df";
const GHN_BASE  = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data";

/* ----------------------------------------------------------------
   Helpers
---------------------------------------------------------------- */
function resetSelect(selectEl, placeholder) {
    selectEl.innerHTML = `<option value="">${placeholder}</option>`;
    selectEl.disabled = true;
}

function setLoading(selectEl, text = "Đang tải...") {
    selectEl.innerHTML = `<option value="">${text}</option>`;
    selectEl.disabled = true;
}

/* ================================================================
   DOMContentLoaded – toàn bộ logic chính
================================================================ */
document.addEventListener("DOMContentLoaded", function () {

    /* --- Popup / Overlay --- */
    const addBtn        = document.querySelector(".add_btn");
    const formContainer = document.getElementById("addAddressFormContainer");
    const overlay       = document.querySelector(".overlay");
    const closeIcon     = document.querySelector(".close-icon");
    const addressItems  = document.querySelectorAll(".address_item");

    function openForm()  { formContainer.style.display = "block"; overlay.style.display = "block"; }
    function closeForm() { formContainer.style.display = "none";  overlay.style.display = "none";  }

    if (addBtn)    addBtn.addEventListener("click", openForm);
    if (overlay)   overlay.addEventListener("click", closeForm);
    if (closeIcon) closeIcon.addEventListener("click", closeForm);

    // Nút "Thay đổi" mở lại form
    addressItems.forEach(item => {
        const btn = item.querySelector(".update_btn");
        if (btn) btn.addEventListener("click", openForm);
    });

    /* --- Selects --- */
    const provinceSelect = document.getElementById("provinceSelect");
    const districtSelect = document.getElementById("districtSelect");
    const wardSelect     = document.getElementById("wardSelect");

    if (!provinceSelect) return;

    /* ---- Load Tỉnh/Thành ---- */
    (async function loadProvinces() {
        try {
            const res  = await fetch(`${GHN_BASE}/province`, {
                method: "GET",
                headers: { "Content-Type": "application/json", "token": GHN_TOKEN }
            });
            const json = await res.json();

            provinceSelect.innerHTML = '<option value="">-- Chọn Tỉnh / Thành phố --</option>';
            (json.data || []).forEach(p => {
                const opt = document.createElement("option");
                opt.value = p.ProvinceID;
                opt.textContent = p.ProvinceName;
                opt.dataset.name = p.ProvinceName;
                provinceSelect.appendChild(opt);
            });
            provinceSelect.disabled = false;
        } catch (err) {
            console.error("Lỗi tải tỉnh/thành:", err);
        }
    })();

    /* ---- Chọn Tỉnh → load Quận/Huyện ---- */
    provinceSelect.addEventListener("change", async function () {
        const provinceId = this.value;
        this.dataset.id   = provinceId;
        this.dataset.name = this.selectedOptions[0]?.dataset.name || "";

        resetSelect(districtSelect, "-- Chọn Quận / Huyện --");
        resetSelect(wardSelect, "-- Chọn Phường / Xã --");
        districtSelect.dataset.id = districtSelect.dataset.name = "";
        wardSelect.dataset.id     = wardSelect.dataset.name     = "";

        if (!provinceId) return;
        setLoading(districtSelect);

        try {
            const res  = await fetch(`${GHN_BASE}/district`, {
                method: "POST",
                headers: { "Content-Type": "application/json", "token": GHN_TOKEN },
                body: JSON.stringify({ province_id: parseInt(provinceId) })
            });
            const json = await res.json();

            districtSelect.innerHTML = '<option value="">-- Chọn Quận / Huyện --</option>';
            (json.data || []).forEach(d => {
                const opt = document.createElement("option");
                opt.value = d.DistrictID;
                opt.textContent = d.DistrictName;
                opt.dataset.name = d.DistrictName;
                districtSelect.appendChild(opt);
            });
            districtSelect.disabled = false;
        } catch (err) {
            console.error("Lỗi tải quận/huyện:", err);
            resetSelect(districtSelect, "-- Lỗi tải dữ liệu --");
        }
    });

    /* ---- Chọn Quận → load Phường/Xã ---- */
    districtSelect.addEventListener("change", async function () {
        const districtId = this.value;
        this.dataset.id   = districtId;
        this.dataset.name = this.selectedOptions[0]?.dataset.name || "";

        resetSelect(wardSelect, "-- Chọn Phường / Xã --");
        wardSelect.dataset.id = wardSelect.dataset.name = "";

        if (!districtId) return;
        setLoading(wardSelect);

        try {
            const res  = await fetch(`${GHN_BASE}/ward?district_id`, {
                method: "POST",
                headers: { "Content-Type": "application/json", "token": GHN_TOKEN },
                body: JSON.stringify({ district_id: parseInt(districtId) })
            });
            const json = await res.json();

            wardSelect.innerHTML = '<option value="">-- Chọn Phường / Xã --</option>';
            (json.data || []).forEach(w => {
                const opt = document.createElement("option");
                opt.value = w.WardCode;
                opt.textContent = w.WardName;
                opt.dataset.name = w.WardName;
                wardSelect.appendChild(opt);
            });
            wardSelect.disabled = false;
        } catch (err) {
            console.error("Lỗi tải phường/xã:", err);
            resetSelect(wardSelect, "-- Lỗi tải dữ liệu --");
        }
    });

    /* ---- Chọn Phường/Xã → ghi data-attribute ---- */
    wardSelect.addEventListener("change", function () {
        this.dataset.id   = this.value;
        this.dataset.name = this.selectedOptions[0]?.dataset.name || "";
    });

    /* ---- Validate số điện thoại ---- */
    const phoneInput = document.getElementById("phone");
    if (phoneInput) {
        phoneInput.addEventListener("input", function () {
            this.value = this.value.replace(/\D/g, "").slice(0, 10);
        });
    }

    /* ---- Submit form (AJAX → JSON) ---- */
    const addForm = document.getElementById("addAddressForm");
    if (addForm) {
        addForm.addEventListener("submit", function (e) {
            e.preventDefault();

            const formData = {
                userId:     sessionStorage.getItem("userId"),
                province:   provinceSelect.dataset.name  || "",
                provinceId: provinceSelect.dataset.id    || "",
                district:   districtSelect.dataset.name || "",
                districtId: districtSelect.dataset.id   || "",
                commune:    wardSelect.dataset.name     || "",
                communeId:  wardSelect.dataset.id       || "",
                detail:     (document.getElementById("detail")?.value || "").trim(),
                name:       (document.getElementById("name")?.value   || "").trim(),
                phone:      (document.getElementById("phone")?.value  || "").trim(),
                type:       (document.querySelector('input[name="addressType"]:checked')?.value || "Home"),
                isDefault:  false
            };

            fetch(addForm.getAttribute("action"), {
                method:  "POST",
                headers: { "Content-Type": "application/json" },
                body:    JSON.stringify(formData)
            })
                .then(res => res.json())
                .then(data => {
                    if (data.status === "success") {
                        alert("Địa chỉ đã được thêm thành công!");
                        location.reload();
                    } else {
                        alert("Lỗi: " + data.message);
                    }
                })
                .catch(err => console.error("Lỗi gửi form:", err));
        });
    }

}); // end DOMContentLoaded

/* ================================================================
   Xóa địa chỉ
================================================================ */
function deleteAddress(addressId) {
    if (!confirm("Bạn có chắc muốn xóa địa chỉ này?")) return;
    fetch("address/delete", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ addressId })
    })
        .then(res => res.json())
        .then(data => {
            alert(data.status === "success" ? "Xóa thành công!" : "Đã xảy ra lỗi, vui lòng thử lại.");
            if (data.status === "success") location.reload();
        })
        .catch(err => console.error("Lỗi xóa địa chỉ:", err));
}

/* ================================================================
   Đặt địa chỉ mặc định
================================================================ */
function setDefault(addressId) {
    fetch("address/default", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ addressId })
    })
        .then(res => res.json())
        .then(data => {
            alert(data.status === "success" ? "Đặt mặc định thành công!" : "Đã xảy ra lỗi, vui lòng thử lại.");
            if (data.status === "success") location.reload();
        })
        .catch(err => console.error("Lỗi đặt mặc định:", err));
}