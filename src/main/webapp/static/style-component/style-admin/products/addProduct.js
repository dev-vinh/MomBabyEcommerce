const APP_CONTEXT = window.location.pathname.split('/admin/')[0];
const ADMIN_BASE = `${APP_CONTEXT}/admin`;
const API_BASE = `${APP_CONTEXT}/api`;

let currentProductId = null;
let currentOptionId = null;
let currentImageId = null;
let currentImageUrl = null;
let allVariants = [];

function $(id) {
    return document.getElementById(id);
}

function notify(message, type = 'info') {
    if (typeof window.showToast === 'function') {
        window.showToast(message, type);
    } else {
        alert(message);
    }
}

async function fetchJson(url, options = {}) {
    const response = await fetch(url, options);
    const data = await response.json().catch(() => ({}));

    if (!response.ok || (data.status && data.status === 'error')) {
        throw new Error(data.message || 'Có lỗi xảy ra khi gọi API.');
    }

    return data;
}

function parsePositiveInteger(value, fieldName) {
    const number = Number(value);
    if (!Number.isInteger(number) || number <= 0) {
        throw new Error(`${fieldName} phải là số nguyên lớn hơn 0.`);
    }
    return number;
}

function parseNonNegativeInteger(value, fieldName) {
    const number = Number(value);
    if (!Number.isInteger(number) || number < 0) {
        throw new Error(`${fieldName} không được âm.`);
    }
    return number;
}

function getProductIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    return id ? Number(id) : null;
}

function getSelectedVariantIds() {
    const ids = [];
    document.querySelectorAll('.variant-group').forEach(group => {
        group.querySelectorAll('.option-group').forEach(optionGroup => {
            const valueSelect = optionGroup.querySelector('.variant-value-select, #variant-value-select');
            if (valueSelect && valueSelect.value) {
                ids.push(Number(valueSelect.value));
            }
        });
    });
    return [...new Set(ids)].filter(id => Number.isInteger(id) && id > 0);
}

function gatherProductData() {
    const name = $('productName').value.trim();
    const sku = $('sku').value.trim();
    const categoryId = $('categoryDropdown').value;
    const brandId = $('vendor').value;
    const description = $('description').value.trim();
    const price = $('price').value.trim();
    const stock = $('total').value.trim();
    const isActive = $('statusSelect') ? $('statusSelect').value === 'true' : true;

    if (!name) throw new Error('Tên sản phẩm không được để trống.');
    if (!sku) throw new Error('SKU không được để trống.');
    if (!categoryId) throw new Error('Vui lòng chọn danh mục.');
    if (!brandId) throw new Error('Vui lòng chọn thương hiệu.');

    return {
        name,
        sku,
        description,
        categoryId: parsePositiveInteger(categoryId, 'Danh mục'),
        brandId: parsePositiveInteger(brandId, 'Thương hiệu'),
        price: parsePositiveInteger(price, 'Giá bán'),
        stock: parseNonNegativeInteger(stock, 'Số lượng tồn kho'),
        isActive,
        optionId: currentOptionId,
        primaryImage: currentImageId,
        variantIds: getSelectedVariantIds()
    };
}

function validateSaveButton() {
    try {
        const data = gatherProductData();
        const hasImage = currentProductId ? true : Boolean(data.primaryImage || $('fileInput').files.length > 0);
        $('saveButton').disabled = !hasImage;
    } catch (error) {
        $('saveButton').disabled = true;
    }
}

async function loadCategories() {
    const data = await fetchJson(`${ADMIN_BASE}/api/categories`);
    const dropdown = $('categoryDropdown');
    dropdown.innerHTML = '<option value="">Chọn danh mục</option>';

    (data.data || []).forEach(category => {
        const option = document.createElement('option');
        option.value = category.id;
        option.textContent = category.name;
        dropdown.appendChild(option);
    });
}

async function loadBrands() {
    const data = await fetchJson(`${ADMIN_BASE}/api/brand`);
    const dropdown = $('vendor');
    dropdown.innerHTML = '<option value="">Chọn nhà cung cấp</option>';

    (data.data || []).forEach(brand => {
        const option = document.createElement('option');
        option.value = brand.id;
        option.textContent = brand.name;
        dropdown.appendChild(option);
    });
}

async function loadVariantsByCategory(categoryId) {
    const variantSelects = document.querySelectorAll('.variant-select, #variant-select');
    const url = categoryId ? `${ADMIN_BASE}/api/variants?categoryId=${categoryId}` : `${ADMIN_BASE}/api/variants`;

    try {
        const data = await fetchJson(url);
        allVariants = data.data || [];
        variantSelects.forEach(select => populateVariantSelect(select, allVariants));
    } catch (error) {
        console.error(error);
        variantSelects.forEach(select => {
            select.innerHTML = '<option value="">Không tải được biến thể</option>';
        });
    }
}

function populateVariantSelect(select, variants) {
    const selectedValue = select.value;
    select.innerHTML = '<option value="">Chọn biến thể</option>';

    variants.forEach(variant => {
        const option = document.createElement('option');
        option.value = variant.id;
        option.textContent = variant.name;
        select.appendChild(option);
    });

    if (selectedValue) {
        select.value = selectedValue;
    }
}

async function loadVariantValues(variantSelect, selectedValueId = null, selectedValueText = null) {
    const optionGroup = variantSelect.closest('.option-group');
    const valueSelect = optionGroup.querySelector('.variant-value-select, #variant-value-select');

    if (!valueSelect) return;

    valueSelect.innerHTML = '<option value="">Chọn giá trị</option>';

    if (!variantSelect.value) return;

    try {
        const data = await fetchJson(`${ADMIN_BASE}/api/variants/${variantSelect.value}`);
        (data.data || []).forEach(value => {
            const option = document.createElement('option');
            option.value = value.id;
            option.textContent = value.value || value.name;
            valueSelect.appendChild(option);
        });

        if (selectedValueId) {
            valueSelect.value = String(selectedValueId);
        }
        if (selectedValueText && !valueSelect.value) {
            const matchedOption = Array.from(valueSelect.options)
                .find(option => option.textContent.trim() === String(selectedValueText).trim());
            if (matchedOption) {
                valueSelect.value = matchedOption.value;
            }
        }
    } catch (error) {
        console.error(error);
        valueSelect.innerHTML = '<option value="">Không tải được giá trị</option>';
    }
}

function createOptionGroup(selectedVariantId = null, selectedValueId = null, selectedValueText = null) {
    const optionGroup = document.createElement('div');
    optionGroup.className = 'option-group';

    const variantSelect = document.createElement('select');
    variantSelect.className = 'option-select variant-select';
    populateVariantSelect(variantSelect, allVariants);

    const valueSelect = document.createElement('select');
    valueSelect.className = 'option-select variant-value-select';
    valueSelect.innerHTML = '<option value="">Chọn giá trị</option>';

    const removeButton = document.createElement('button');
    removeButton.type = 'button';
    removeButton.className = 'remove-option-button';
    removeButton.innerHTML = '×';
    removeButton.addEventListener('click', () => optionGroup.remove());

    variantSelect.addEventListener('change', () => loadVariantValues(variantSelect));

    optionGroup.appendChild(variantSelect);
    optionGroup.appendChild(valueSelect);
    optionGroup.appendChild(removeButton);

    if (selectedVariantId) {
        variantSelect.value = String(selectedVariantId);
        loadVariantValues(variantSelect, selectedValueId, selectedValueText);
    }

    return optionGroup;
}

function addOptionGroup(containerId = 'optionsContainer') {
    const container = $(containerId);
    if (!container) return;
    container.appendChild(createOptionGroup());
}

function addVariant() {
    const optionsContainer = $('optionsContainer1');
    const template = document.querySelector('.variant-group');
    if (!optionsContainer || !template) return;

    const clone = template.cloneNode(true);
    clone.querySelectorAll('input').forEach(input => input.value = '');
    clone.querySelectorAll('.option-group').forEach((group, index) => {
        if (index > 0) group.remove();
    });
    clone.querySelectorAll('.variant-select, #variant-select').forEach(select => {
        select.value = '';
        select.addEventListener('change', () => loadVariantValues(select));
    });
    clone.querySelectorAll('.variant-value-select, #variant-value-select').forEach(select => {
        select.innerHTML = '<option value="">Chọn giá trị</option>';
    });

    optionsContainer.appendChild(clone);
}

function removeOptionGroup(button) {
    const optionGroup = button.closest('.option-group');
    if (optionGroup) optionGroup.remove();
}

function renderImagePreview(src) {
    const previewImage = $('previewImage');
    const uploadIcon = $('uploadIcon');
    const dragDropText = $('dragDropText');
    const imagePreviewContainer = $('imagePreviewContainer');

    if (previewImage) previewImage.src = src;
    if (uploadIcon) uploadIcon.style.display = 'block';
    if (dragDropText) dragDropText.style.display = 'none';
    if (imagePreviewContainer) imagePreviewContainer.innerHTML = '';
}

function previewSelectedImages(files) {
    const imagePreviewContainer = $('imagePreviewContainer');
    const uploadIcon = $('uploadIcon');
    const dragDropText = $('dragDropText');

    if (!files.length) return;

    if (uploadIcon) uploadIcon.style.display = 'none';
    if (dragDropText) dragDropText.style.display = 'none';
    imagePreviewContainer.innerHTML = '';

    Array.from(files).slice(0, 10).forEach((file, index) => {
        const reader = new FileReader();
        reader.onload = event => {
            const wrapper = document.createElement('div');
            wrapper.className = 'image-wrapper';

            const img = document.createElement('img');
            img.src = event.target.result;
            img.className = 'preview-image';

            const overlay = document.createElement('div');
            overlay.className = 'image-overlay';
            const deleteIcon = document.createElement('span');
            deleteIcon.className = 'icon delete-icon fas fa-trash';
            deleteIcon.onclick = () => wrapper.remove();
            overlay.appendChild(deleteIcon);

            if (index === 0) {
                const primaryIcon = document.createElement('span');
                primaryIcon.className = 'primary-icon';
                primaryIcon.innerHTML = '★';
                primaryIcon.title = 'Ảnh chính';
                wrapper.appendChild(primaryIcon);
                $('previewImage').src = event.target.result;
            }

            wrapper.appendChild(img);
            wrapper.appendChild(overlay);
            imagePreviewContainer.appendChild(wrapper);
        };
        reader.readAsDataURL(file);
    });
}

async function uploadImagesIfNeeded() {
    const files = $('fileInput').files;

    if (!files || files.length === 0) {
        return null;
    }

    const formData = new FormData();
    Array.from(files).forEach(file => formData.append('file', file));

    const uploadData = await fetchJson(`${API_BASE}/uploadImage`, {
        method: 'POST',
        body: formData
    });

    if (!uploadData.data || uploadData.data.length === 0) {
        throw new Error('Upload ảnh thất bại.');
    }

    return uploadData.data;
}

async function assignProductImages(images, productId) {
    if (!images || images.length === 0) return;

    await Promise.all(images.map(image => {
        const body = new URLSearchParams();
        body.append('productId', productId);
        body.append('imageId', image.id);

        return fetchJson(`${ADMIN_BASE}/ImageDetailDao`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
            body
        });
    }));
}

async function createProduct(productData) {
    const created = await fetchJson(`${ADMIN_BASE}/products`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=UTF-8' },
        body: JSON.stringify(productData)
    });

    if (!created.data || !created.data.id) {
        throw new Error('Không thể thêm sản phẩm.');
    }

    return created.data;
}

async function createProductOptions(productId, productData) {
    const option = await fetchJson(`${ADMIN_BASE}/options/create`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=UTF-8' },
        body: JSON.stringify({
            productId,
            price: productData.price,
            stock: productData.stock
        })
    });

    const optionId = option.data?.id;
    if (!optionId) return;

    await Promise.all((productData.variantIds || []).map(variantId => {
        return fetchJson(`${ADMIN_BASE}/addOptionVariantValue`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json;charset=UTF-8' },
            body: JSON.stringify({ optionId, variantId })
        });
    }));
}

async function updateProduct(productData) {
    productData.id = currentProductId;

    const updated = await fetchJson(`${ADMIN_BASE}/editProduct`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json;charset=UTF-8' },
        body: JSON.stringify(productData)
    });

    return updated.data;
}

async function saveProduct() {
    try {
        $('saveButton').disabled = true;
        const productData = gatherProductData();
        const uploadedImages = await uploadImagesIfNeeded();

        if (uploadedImages && uploadedImages.length > 0) {
            productData.primaryImage = uploadedImages[0].id;
        }

        if (!currentProductId && !productData.primaryImage) {
            throw new Error('Vui lòng upload ảnh sản phẩm.');
        }

        if (currentProductId) {
            const updated = await updateProduct(productData);
            await assignProductImages(uploadedImages, currentProductId);
            notify('Cập nhật sản phẩm thành công.', 'success');
            window.location.href = `${ADMIN_BASE}/list-product`;
            return updated;
        }

        const created = await createProduct(productData);
        await assignProductImages(uploadedImages, created.id);
        await createProductOptions(created.id, productData);
        notify('Thêm sản phẩm thành công.', 'success');
        window.location.href = `${ADMIN_BASE}/list-product`;
        return created;
    } catch (error) {
        console.error(error);
        notify(error.message || 'Có lỗi xảy ra khi lưu sản phẩm.', 'error');
    } finally {
        validateSaveButton();
    }
}

async function fetchProductDetails(productId) {
    const data = await fetchJson(`${ADMIN_BASE}/editProduct?id=${productId}`);
    const product = data.data;

    $('productName').value = product.name || '';
    $('sku').value = product.sku || '';
    $('categoryDropdown').value = product.categoryId || '';
    $('description').value = product.description || '';
    $('price').value = product.price || '';
    $('total').value = product.stock ?? 0;
    $('vendor').value = product.brandId || '';
    if ($('statusSelect')) $('statusSelect').value = String(product.active !== false);

    currentOptionId = product.optionId || null;
    currentImageId = product.imageId || null;
    currentImageUrl = product.imageUrl || null;

    if (currentImageUrl) {
        renderImagePreview(currentImageUrl);
    }

    await loadVariantsByCategory(product.categoryId);
    renderExistingVariants(product.variants || []);
    validateSaveButton();
}

function renderExistingVariants(variants) {
    const optionGroups = document.querySelectorAll('.option-group');
    optionGroups.forEach((group, index) => {
        if (index > 0) group.remove();
    });

    const container = $('optionsContainer');
    if (!container) return;

    const firstGroup = container.querySelector('.option-group');
    if (!variants.length) {
        const firstSelect = firstGroup?.querySelector('.variant-select, #variant-select');
        if (firstSelect) {
            populateVariantSelect(firstSelect, allVariants);
            firstSelect.addEventListener('change', () => loadVariantValues(firstSelect));
        }
        return;
    }

    if (firstGroup) firstGroup.remove();

    variants.forEach(variant => {
        const variantType = allVariants.find(item => item.name === variant.name);
        const selectedVariantId = variantType ? variantType.id : variant.id;
        container.appendChild(createOptionGroup(selectedVariantId, variant.id, variant.value));
    });
}

window.addVariant = addVariant;
window.addOptionGroup = addOptionGroup;
window.removeOptionGroup = removeOptionGroup;
window.saveProductDetails = saveProduct;
window.fetchVariantValues = function () {
    const activeElement = document.activeElement;
    if (activeElement && activeElement.matches('.variant-select, #variant-select')) {
        loadVariantValues(activeElement);
    }
};

window.addEventListener('DOMContentLoaded', async () => {
    const uploadButton = $('uploadButton');
    const fileInput = $('fileInput');
    const saveButton = $('saveButton');
    const categoryDropdown = $('categoryDropdown');

    currentProductId = getProductIdFromUrl();

    if (uploadButton) uploadButton.addEventListener('click', () => fileInput.click());
    if (fileInput) fileInput.addEventListener('change', () => {
        previewSelectedImages(fileInput.files);
        validateSaveButton();
    });
    if (saveButton) saveButton.addEventListener('click', saveProduct);

    ['productName', 'sku', 'price', 'total', 'vendor', 'statusSelect'].forEach(id => {
        const element = $(id);
        if (element) element.addEventListener('input', validateSaveButton);
        if (element) element.addEventListener('change', validateSaveButton);
    });

    if (categoryDropdown) {
        categoryDropdown.addEventListener('change', async () => {
            await loadVariantsByCategory(categoryDropdown.value);
            validateSaveButton();
        });
    }

    document.querySelectorAll('.variant-select, #variant-select').forEach(select => {
        select.classList.add('variant-select');
        select.addEventListener('change', () => loadVariantValues(select));
    });
    document.querySelectorAll('#variant-value-select').forEach(select => {
        select.classList.add('variant-value-select');
    });

    try {
        await Promise.all([loadCategories(), loadBrands()]);

        if (currentProductId) {
            await fetchProductDetails(currentProductId);
        } else {
            if (!$('sku').value.trim()) {
                $('sku').value = `PRD-${Date.now()}`;
            }
            await loadVariantsByCategory(categoryDropdown?.value || null);
            validateSaveButton();
        }
    } catch (error) {
        console.error(error);
        notify(error.message || 'Không thể tải dữ liệu form sản phẩm.', 'error');
    }
});
