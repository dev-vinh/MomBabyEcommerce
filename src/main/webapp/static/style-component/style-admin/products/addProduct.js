const variantValueSelect = document.getElementById("variant-value-select");

document.getElementById('fileInput').addEventListener('change', function (event) {
    const files = Array.from(event.target.files);
    const imagePreviewContainer = document.getElementById('imagePreviewContainer');
    const uploadIcon = document.getElementById('uploadIcon');
    const dragDropText = document.getElementById('dragDropText');


    uploadIcon.style.display = 'none';
    dragDropText.style.display = 'none';


    let currentImages = Array.from(imagePreviewContainer.getElementsByTagName('img'));
    let totalImages = currentImages.length + files.length;

    if (totalImages > 10) {
        files.splice(10 - currentImages.length);
    }

    files.forEach((file) => {
        const reader = new FileReader();
        reader.onload = function (e) {
            const wrapper = document.createElement('div');
            wrapper.classList.add('image-wrapper');

            const img = document.createElement('img');
            img.src = e.target.result;
            img.classList.add('preview-image');
            img.onclick = function () {
                showPreview(
                    [...Array.from(imagePreviewContainer.getElementsByTagName('img')).map(img => img.src)],
                    currentImages.length
                );
            };

            wrapper.appendChild(img);


            if (imagePreviewContainer.getElementsByClassName('image-wrapper').length === 0) {
                const primaryIcon = document.createElement('span');
                primaryIcon.className = 'primary-icon';
                primaryIcon.innerHTML = '★';
                primaryIcon.title = 'Primary Image';
                wrapper.appendChild(primaryIcon);
            }

            imagePreviewContainer.appendChild(wrapper);


            wrapper.classList.add('image-wrapper');

            img.src = e.target.result;
            img.classList.add('preview-image');

            const overlay = document.createElement('div');
            overlay.classList.add('image-overlay');

            // Icon View
            const viewIcon = document.createElement('span');
            viewIcon.className = 'icon view-icon fas fa-eye';
            viewIcon.onclick = function () {
                showPreview(
                    [...Array.from(imagePreviewContainer.getElementsByTagName('img')).map(img => img.src)],
                    currentImages.length
                );
            };

            // Icon Delete
            const deleteIcon = document.createElement('span');
            deleteIcon.className = 'icon delete-icon fas fa-trash';
            deleteIcon.onclick = function () {
                const confirmation = confirm('Bạn có chắc chắn muốn xóa hình ảnh này?');
                if (confirmation) {
                    wrapper.remove();
                }
            };

            overlay.appendChild(viewIcon);
            overlay.appendChild(deleteIcon);
            wrapper.appendChild(img);
            wrapper.appendChild(overlay);

            imagePreviewContainer.appendChild(wrapper);
        };
        reader.readAsDataURL(file);
    });


    setTimeout(() => {

        const moreImagesDiv = document.querySelector('.more-images');
        if (moreImagesDiv) {
            moreImagesDiv.remove();
        }


        const maxVisibleImages = 3;
        const totalChildren = imagePreviewContainer.querySelectorAll('.image-wrapper').length;

        if (totalChildren > maxVisibleImages) {
            const excessImages = totalChildren - maxVisibleImages;
            const moreImagesDiv = document.createElement('div');
            moreImagesDiv.className = 'more-images';
            moreImagesDiv.innerText = `+${excessImages}`;
            moreImagesDiv.onclick = function () {
                showPreview(
                    [...Array.from(imagePreviewContainer.getElementsByTagName('img')).map(img => img.src)]
                );
            };


            const lastVisibleImage = imagePreviewContainer.querySelectorAll('.image-wrapper')[maxVisibleImages - 1];
            lastVisibleImage.appendChild(moreImagesDiv);
        }
    }, 0);
});


function showPreview(images, startIndex = 0) {
    const previewModal = document.createElement('div');
    previewModal.className = 'preview-modal';

    const closeButton = document.createElement('span');
    closeButton.className = 'close-preview';
    closeButton.innerHTML = '×';
    closeButton.onclick = function () {
        document.body.removeChild(previewModal);
    };

    const carousel = document.createElement('div');
    carousel.className = 'carousel';

    images.forEach((imageSrc, index) => {
        const img = document.createElement('img');
        img.src = imageSrc;
        img.classList.add('carousel-image');
        if (index === startIndex) {
            img.style.display = 'block';
        } else {
            img.style.display = 'none';
        }

        img.addEventListener('wheel', function (event) {
            event.preventDefault();
            const scaleStep = 0.1;
            let scale = parseFloat(img.getAttribute('data-scale')) || 1;
            scale += event.deltaY < 0 ? scaleStep : -scaleStep;
            scale = Math.min(Math.max(0.5, scale), 3);
            img.style.transform = `scale(${scale})`;
            img.setAttribute('data-scale', scale);
        });
        img.addEventListener('dblclick', function () {
            img.style.transform = 'scale(1)';
            img.setAttribute('data-scale', 1);
        });

        carousel.appendChild(img);
    });

    let currentIndex = startIndex;

    const prevButton = document.createElement('button');
    prevButton.className = 'carousel-button prev';
    prevButton.innerHTML = '◀';
    prevButton.onclick = function () {
        const images = carousel.querySelectorAll('.carousel-image');
        images[currentIndex].style.display = 'none';
        currentIndex = (currentIndex - 1 + images.length) % images.length;
        images[currentIndex].style.display = 'block';
    };

    const nextButton = document.createElement('button');
    nextButton.className = 'carousel-button next';
    nextButton.innerHTML = '▶';
    nextButton.onclick = function () {
        const images = carousel.querySelectorAll('.carousel-image');
        images[currentIndex].style.display = 'none';
        currentIndex = (currentIndex + 1) % images.length;
        images[currentIndex].style.display = 'block';
    };

    previewModal.appendChild(closeButton);
    previewModal.appendChild(prevButton);
    previewModal.appendChild(carousel);
    previewModal.appendChild(nextButton);
    document.body.appendChild(previewModal);
}



function addVariant() {
    const optionsContainer = document.getElementById('optionsContainer1');
    const variantGroup = document.querySelector('.variant-group'); // Lấy phần tử mẫu
    const newVariant = variantGroup.cloneNode(true); // Tạo bản sao

    // Sao chép giá trị của các input
    const inputs = newVariant.querySelectorAll('input');
    inputs.forEach(input => input.value = ''); // Xóa giá trị input

    // Kiểm tra số lượng biến thể trong tất cả các nhóm
    const variantSelects = newVariant.querySelectorAll('.option-select');
    const numVariants = variantSelects.length;  // Lấy số lượng variant

    // Đảm bảo rằng số lượng biến thể giống nhau ở tất cả các nhóm
    const allVariantGroups = optionsContainer.querySelectorAll('.variant-group');
    allVariantGroups.forEach(group => {
        const currentVariantSelects = group.querySelectorAll('.option-select');
        while (currentVariantSelects.length < numVariants) {
            addOptionGroup(group.id);
        }
    });

    optionsContainer.appendChild(newVariant);
    console.log(optionsContainer, variantGroup);
}

window.addVariant = addVariant;

function removeVariant(button) {
    const variantGroup = button.closest('.variant-group'); // Tìm phần tử cha gần nhất
    if (variantGroup) {
        variantGroup.remove();
    }
}




async function addOptionGroup(containerId) {
    const optionGroup = document.createElement('div');
    const select = document.createElement('select');
    const secondSelect = document.createElement('select');
    const removeButton = document.createElement('button');

    // Thêm class và nội dung cho các phần tử
    optionGroup.classList.add('option-group');
    select.classList.add('option-select');
    secondSelect.classList.add('option-select');
    removeButton.classList.add('remove-option-button');
    removeButton.innerHTML = '×';
    removeButton.onclick = function () {
        removeOptionGroup(removeButton);
    };

    select.id = "variant-select";
    select.setAttribute("onchange", "fetchVariantValues(this.value, this.nextElementSibling)");

    secondSelect.id = "variant-value-select";

    const categoryId = getSelectedCategoryId();
    if (categoryId) {
        try {
            const variants = await loadVariantsByCategory(categoryId); // Chờ đợi kết quả
            populateSelect(select, variants);
        } catch (error) {
            console.error("Lỗi khi tải biến thể:", error);
        }
    } else {
        console.warn("Chưa chọn danh mục.");
    }

    // Thêm các phần tử con vào nhóm tùy chọn
    optionGroup.appendChild(select);
    optionGroup.appendChild(secondSelect);
    optionGroup.appendChild(removeButton);

    // Lấy container cần thao tác và thêm nhóm tùy chọn
    const container = document.getElementById(containerId);
    container.appendChild(optionGroup);
}

// Hàm xóa nhóm tùy chọn
function removeOptionGroup(button) {
    const optionGroup = button.parentElement; // Lấy nhóm tùy chọn
    const container = optionGroup.parentElement; // Xác định container hiện tại
    container.removeChild(optionGroup); // Xóa nhóm tùy chọn khỏi container
}

// Hàm lấy categoryId đã chọn (ví dụ: từ dropdown)
function getSelectedCategoryId() {
    const categorySelect = document.getElementById("categoryDropdown");
    return categorySelect ? categorySelect.value : null;
}

// Hàm tải danh sách biến thể dựa trên categoryId
async function loadVariantsByCategory(categoryId) {
    const variantSelect = document.getElementById('variant-select'); // Hoặc chọn select khác nếu cần
    const url = categoryId ? `api/variants?categoryId=${categoryId}` : `api/variants`;
    const response = await fetch(url);
    const data = await response.json();
    if (data.statusCode === 200) {
        return data.data; // Trả về danh sách các biến thể
    } else {
        throw new Error("Không thể tải biến thể");
    }
}

// Hàm gọi API lấy giá trị cho variant và cập nhật select "variant-value-select"
async function fetchVariantValues(variantId) {
    // Lấy dropdown "variant-value-select" trong cùng nhóm tùy chọn
    const variantGroup = event.target.closest('.option-group');
    const variantValueSelect = variantGroup.querySelector('#variant-value-select');

    if (!variantValueSelect) {
        console.error("Không tìm thấy 'variant-value-select'!");
        return;
    }

    if (!variantId) {
        variantValueSelect.innerHTML = '<option value="">Chọn giá trị</option>';
        return;
    }

    try {
        const response = await fetch(`api/variants/${variantId}`);
        const data = await response.json();

        if (data.statusCode === 200) {
            variantValueSelect.innerHTML = '<option value="">Chọn giá trị</option>';
            if (data.data && data.data.length > 0) {
                data.data.forEach(value => {
                    const option = document.createElement("option");
                    option.value = value.id;
                    option.textContent = value.value; // Gán giá trị hiển thị
                    variantValueSelect.appendChild(option);
                });
            } else {
                console.warn("Không tìm thấy giá trị cho biến thể đã chọn.");
            }
        } else {
            console.error("Lỗi khi lấy giá trị cho biến thể:", data.message);
        }
    } catch (error) {
        console.error("Lỗi khi gọi API:", error.message);
    }
}



// Hàm hiển thị dữ liệu vào một select element
function populateSelect(selectElement, variants) {
    selectElement.innerHTML = ''; // Xóa các option cũ
    variants.forEach(variant => {
        const option = document.createElement('option');
        option.value = variant.id;
        option.textContent = variant.name; // Hiển thị tên biến thể
        selectElement.appendChild(option);
    });
}








//get category
document.addEventListener("DOMContentLoaded", function () {
    // Lấy phần tử dropdown
    const categoryDropdown = document.getElementById("categoryDropdown");

    // Kiểm tra phần tử dropdown có tồn tại
    if (!categoryDropdown) {
        console.error("Phần tử dropdown không tồn tại");
        return;
    }

    // Gọi API để lấy danh mục
    fetch('api/categories')
        .then((response) => {
            if (!response.ok) {
                throw new Error('Không thể lấy danh mục');
            }
            return response.json();
        })
        .then((data) => {
            if (data.statusCode === 200 && data.data) {
                // Xóa các option cũ (nếu cần)
                categoryDropdown.innerHTML = '<option>Chọn danh mục</option>';

                // Thêm các danh mục mới
                data.data.forEach((category) => {
                    const option = document.createElement('option');
                    option.value = category.id;
                    option.textContent = category.name;
                    categoryDropdown.appendChild(option);
                });
            } else {
                console.error("Dữ liệu danh mục không hợp lệ");
            }
        })
        .catch((err) => {
            console.error("Error fetching categories:", err);
        });
});

// nha cung cap

document.addEventListener("DOMContentLoaded", function () {
    // Lấy phần tử dropdown
    const vendor = document.getElementById("vendor");

    // Kiểm tra phần tử dropdown có tồn tại
    if (!vendor) {
        console.error("Phần tử dropdown không tồn tại");
        return;
    }

    // Gọi API để lấy danh mục
    fetch(' api/brand')
        .then((response) => {
            if (!response.ok) {
                throw new Error('Không thể lấy nhà cung cấp');
            }
            return response.json();
        })
        .then((data) => {
            if (data.statusCode === 200 && data.data) {
                // Xóa các option cũ (nếu cần)
                vendor.innerHTML = '<option>Chọn nhà cung cấp</option>';

                // Thêm các danh mục mới
                data.data.forEach((brand) => {
                    const option = document.createElement('option');
                    option.value = brand.id;
                    option.textContent = brand.name;
                    vendor.appendChild(option);
                });
            } else {
                console.error("Dữ liệu nhà cung cấp không hợp lệ");
            }
        })
        .catch((err) => {
            console.error("Error fetching brand:", err);
        });
});


document.addEventListener("DOMContentLoaded", () => {
    const categorySelect = document.getElementById("categoryDropdown");
    const variantSelect = document.getElementById("variant-select");
    const variantValueSelect = document.getElementById("variant-value-select");
    const uploadButton = document.getElementById("uploadButton");
    const fileInput = document.getElementById("fileInput");
    const previewImage = document.getElementById("previewImage");
    const saveButton = document.getElementById("saveButton");

    // Khai báo biến lưu trữ ID variant và variantValue
    let selectedVariantId = null;
    let selectedVariantValueId = null;

    if (!variantSelect || !variantValueSelect) {
        console.error("Các phần tử dropdown không tồn tại");
        return;
    }

    // Xử lý sự kiện thay đổi của category
    if (categorySelect) {
        categorySelect.addEventListener("change", () => {
            const categoryId = categorySelect.value;
            loadVariantsByCategory(categoryId || null);
        });
    }

    // Lắng nghe sự kiện thay đổi của variant
    variantSelect.addEventListener("change", () => {
        selectedVariantId = variantSelect.value;  // Lưu ID của variant đã chọn
        if (selectedVariantId) {
            fetchVariantValues(selectedVariantId);
        } else {
            resetVariantValues();
        }
    });

    // Lắng nghe sự kiện thay đổi của variantValue
    variantValueSelect.addEventListener("change", () => {
        selectedVariantValueId = Number(variantValueSelect.value); // Lưu ID variantValue được chọn
        console.log("Selected VariantValue ID:", selectedVariantValueId);
    });

    // Mở hộp thoại chọn file khi bấm nút "Tải ảnh lên"
    uploadButton.addEventListener("click", () => fileInput.click());

    // Hiển thị ảnh xem trước khi chọn file
    fileInput.addEventListener("change", () => {
        const file = fileInput.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => previewImage.src = e.target.result;
            reader.readAsDataURL(file);
        }
    });

    // Xử lý sự kiện lưu thông tin sản phẩm
    saveButton.addEventListener("click", async (event) => {
        event.preventDefault();
        const formData = gatherFormData();
        if (!formData) return alert("Vui lòng điền đầy đủ các trường bắt buộc!");

        try {
            // Upload ảnh sản phẩm
            const uploadData = await uploadImages(fileInput.files);
            const productData = await createProduct(formData, uploadData);
            await assignAdditionalImages(uploadData, productData.id);

            // Thêm các tùy chọn sản phẩm
            await addProductOptions(productData.id);
            alert("Thêm sản phẩm và tùy chọn thành công!");
            window.location.href = "list-product";
        } catch (error) {
            console.error("Lỗi:", error);
            alert(`Đã xảy ra lỗi: ${error.message}`);
        }
    });

    // Hàm tải variants theo category
    async function loadVariantsByCategory(categoryId) {
        try {
            variantSelect.innerHTML = '<option>Đang tải...</option>';
            const url = categoryId ? `api/variants?categoryId=${categoryId}` : `api/variants`;
            const response = await fetch(url);
            const data = await response.json();

            if (data.statusCode === 200) {
                populateVariants(data.data);
            } else {
                alert("Không thể tải danh sách biến thể!");
                resetVariantsAndValues();
            }
        } catch (error) {
            console.error("Lỗi khi tải variants:", error);
            resetVariantsAndValues();
        }
    }

    // Hàm hiển thị danh sách variants
    function populateVariants(variants) {
        variantSelect.innerHTML = '<option value="">Chọn biến thể</option>';
        resetVariantValues();
        variants.forEach(variant => {
            const option = document.createElement("option");
            option.value = variant.id;
            option.textContent = variant.name;
            variantSelect.appendChild(option);
        });
    }

    // Hàm tải giá trị của variant
    async function fetchVariantValues(variantId) {
        try {
            variantValueSelect.innerHTML = '<option>Đang tải...</option>';
            const response = await fetch(`api/variants/${variantId}`);
            const data = await response.json();

            if (data.statusCode === 200) {
                populateVariantValues(data.data);
            } else {
                alert("Không thể tải danh sách giá trị biến thể!");
                resetVariantValues();
            }
        } catch (error) {
            console.error("Lỗi khi tải variant values:", error);
            resetVariantValues();
        }
    }

    // Hàm hiển thị danh sách variant values
    function populateVariantValues(values) {
        variantValueSelect.innerHTML = '<option value="">Chọn giá trị</option>';
        if (values.length > 0) {
            values.forEach(value => {
                const option = document.createElement("option");
                option.value = value.id;
                option.textContent = value.value;
                variantValueSelect.appendChild(option);
            });
        } else {
            console.warn("Không tìm thấy giá trị cho biến thể đã chọn.");
        }
    }

    // Hàm reset variant values
    function resetVariantValues() {
        variantValueSelect.innerHTML = '<option value="">Chọn giá trị</option>';
        selectedVariantValueId = null; // Đặt lại selectedVariantValueId về null
    }

    // Hàm reset variants và variant-values
    function resetVariantsAndValues() {
        variantSelect.innerHTML = '<option value="">Chọn biến thể</option>';
        resetVariantValues();
    }

    // Hàm kiểm tra các trường bắt buộc
    function validateForm() {
        const productName = document.getElementById("productName").value.trim();
        const category = document.getElementById("categoryDropdown").value;
        const price = document.getElementById("price").value.trim();
        const stock = document.getElementById("total").value.trim();
        const imageUpload = fileInput.files;

        // Kiểm tra các trường bắt buộc
        if (productName && category && price && stock && imageUpload.length > 0) {
            document.getElementById("saveButton").disabled = false;  // Bật nút Lưu
        } else {
            document.getElementById("saveButton").disabled = true;  // Tắt nút Lưu
        }
    }

// Gọi hàm validate khi người dùng thay đổi giá trị trong các trường
    document.getElementById("productName").addEventListener("input", validateForm);
    document.getElementById("categoryDropdown").addEventListener("change", validateForm);
    document.getElementById("price").addEventListener("input", validateForm);
    document.getElementById("total").addEventListener("input", validateForm);
    document.getElementById("fileInput").addEventListener("change", validateForm);

// Hàm thu thập dữ liệu từ form
    function gatherFormData() {
        const productName = document.getElementById("productName").value.trim();
        const sku = document.getElementById("sku").value.trim();
        const category = document.getElementById("categoryDropdown").value;
        const brand = document.getElementById("vendor").value;
        const description = document.getElementById("description").value.trim();
        const price = document.getElementById("price").value.trim();
        const stock = document.getElementById("total").value.trim();
        const tags = document.getElementById("tags").value.trim();
        const imageUpload = fileInput.files;

        if (!productName || !category || !price || !stock || imageUpload.length === 0 || !brand) {
            return null;
        }

        return {
            productName,
            sku,
            category,
            brand,
            description,
            price,
            stock,
            tags,
            imageUpload
        };
    }


    // Hàm upload ảnh
    async function uploadImages(files) {
        const formData = new FormData();
        Array.from(files).forEach(file => formData.append("file", file));
        const uploadResponse = await fetch("uploadImage", { method: "POST", body: formData });
        const uploadData = await uploadResponse.json();

        if (uploadData.statusCode !== 200 || !uploadData.data || !uploadData.data.length) {
            throw new Error("Không thể upload ảnh.");
        }
        return uploadData;
    }

    // Hàm tạo sản phẩm
    async function createProduct(formData, uploadData) {
        const productData = {
            name: formData.productName,
            sku: formData.sku,
            categoryId: formData.category,
            description: formData.description,
            price: parseFloat(formData.price),
            stock: parseInt(formData.stock),
            brandId: formData.brand,
            tags: formData.tags.split(",").map(tag => tag.trim()),
            isActive: true,
            primaryImage: uploadData.data[0].id,
        };

        const response = await fetch("products", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(productData),
        });
        const product = await response.json();

        if (!product.data || !product.data.id) {
            throw new Error("Không thể thêm sản phẩm.");
        }

        return product.data;
    }

    // Hàm gán ảnh bổ sung cho sản phẩm
    async function assignAdditionalImages(uploadData, productId) {
        const additionalImages = uploadData.data.slice(1).map(image => image.id);
        await Promise.all(additionalImages.map(imageId => {
            return fetch("ImageDetailDao", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: `productId=${productId}&imageId=${imageId}`,
            });
        }));
    }

    // Hàm thêm tùy chọn sản phẩm
    async function addProductOptions(productId) {
        const optionGroups = document.querySelectorAll(".variant-group");
        const optionPromises = Array.from(optionGroups).map(group => {
            const priceInput = group.querySelector("input[id='price']");
            const stockInput = group.querySelector("input[id='total']");
            const priceValue = priceInput?.value.trim();
            const stockValue = stockInput?.value.trim();

            if (priceValue && stockValue) {
                const optionData = {
                    productId,
                    price: parseFloat(priceValue),
                    stock: parseInt(stockValue),
                };

                return fetch("options/create", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(optionData),
                }).then(async (optionResponse) => {
                    const option = await optionResponse.json();
                    if (option.data?.id) {
                        const optionId = option.data.id;
                        const variantValueId = selectedVariantValueId;
                        const variantValueApiUrl = "addOptionVariantValue";
                        const variantValueData = {
                            optionId: optionId,
                            variantValueId: variantValueId
                        };

                        await fetch(variantValueApiUrl, {
                            method: "POST",
                            headers: { "Content-Type": "application/json" },
                            body: JSON.stringify(variantValueData), // Gửi dưới dạng JSON
                        });
                    }
                });
            }
            return Promise.resolve();
        });

        await Promise.all(optionPromises);
    }
});

document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');

    if (productId) {
        // Gọi API để lấy dữ liệu sản phẩm và điền vào form
        fetchProductDetails(productId);
    }
});
function fetchProductDetails(productId) {
    fetch(`editProduct?id=${productId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.statusCode === 200 && data.data) {
                const product = data.data;

                // Điền thông tin sản phẩm vào các input
                document.getElementById('productName').value = product.name;
                document.getElementById('sku').value = product.sku;
                document.getElementById('categoryDropdown').value = product.categoryId;
                document.getElementById('description').value = product.description;
                document.getElementById('previewImage').src = product.imageUrl;
                document.getElementById('price').value = product.price;
                document.getElementById('total').value = product.stock;
                document.getElementById('vendor').value = product.brandId || '';
                document.getElementById('tags').value = product.tags || '';
                document.getElementById('optionsContainer1').value = product.optionId;

                // Điền các variants vào dropdown
                const variantSelect = document.getElementById('variant-select');
                const variantValueSelect = document.getElementById('variant-value-select');

                // Đảm bảo dropdown variant và variant-value được reset trước khi thêm mới
                variantSelect.innerHTML = '<option value="">Chọn danh mục</option>'; // Reset dropdown
                variantValueSelect.innerHTML = '<option value="">Chọn giá trị</option>'; // Reset dropdown

                // Thêm các variants vào dropdown variant-select
                if (product.variants && product.variants.length > 0) {
                   //todo
                }

            } else {
                alert('Không tìm thấy thông tin sản phẩm.');
            }
        })
        .catch(error => {
            console.error('Có lỗi xảy ra khi gọi API:', error);
            alert('Có lỗi xảy ra khi lấy thông tin sản phẩm.');
        });
}


