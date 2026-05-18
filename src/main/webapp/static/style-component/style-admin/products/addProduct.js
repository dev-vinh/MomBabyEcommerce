console.log('addProduct.js product-options-images-fixed loaded');

const APP_CONTEXT = window.location.pathname.split('/admin/')[0];
const ADMIN_BASE = `${APP_CONTEXT}/admin`;
const API_BASE = `${APP_CONTEXT}/api`;

let currentProductId = null;
let allVariants = [];
let productImages = [];
let tempImageCounter = 1;

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

function setInputValue(id, value) {
    const element = $(id);
    if (!element) {
        console.warn(`Không tìm thấy element có id="${id}" trong addProduct.jsp`);
        return;
    }
    element.value = value ?? '';
}

function getInputValue(id) {
    const element = $(id);
    return element ? element.value : '';
}

async function fetchJson(url, options = {}) {
    const response = await fetch(url, options);
    const data = await response.json().catch(() => ({}));

    if (!response.ok || data.status === 'error') {
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

function getProductIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    return id ? Number(id) : null;
}

function getActiveImages() {
    return productImages.filter(image => !image.removed);
}

function getPrimaryImage() {
    return getActiveImages()[0] || null;
}

function getOptionCards() {
    return Array.from(document.querySelectorAll('.variant-card'));
}

function validateSaveButton() {
    const saveButton = $('saveButton');
    if (!saveButton) return;

    try {
        gatherProductData(false);
        saveButton.disabled = false;
    } catch (error) {
        saveButton.disabled = true;
    }
}

async function loadCategories() {
    const data = await fetchJson(`${ADMIN_BASE}/api/categories`);
    const dropdown = $('categoryDropdown');
    if (!dropdown) return;

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
    if (!dropdown) return;

    dropdown.innerHTML = '<option value="">Chọn nhà cung cấp</option>';
    (data.data || []).forEach(brand => {
        const option = document.createElement('option');
        option.value = brand.id;
        option.textContent = brand.name;
        dropdown.appendChild(option);
    });
}

async function loadVariantsByCategory(categoryId) {
    const url = categoryId ? `${ADMIN_BASE}/api/variants?categoryId=${categoryId}` : `${ADMIN_BASE}/api/variants`;
    const data = await fetchJson(url);
    allVariants = data.data || [];

    document.querySelectorAll('.variant-type-select').forEach(select => {
        const selected = select.value;
        populateVariantTypeSelect(select);
        if (selected) select.value = selected;
    });
}

function populateVariantTypeSelect(select) {
    select.innerHTML = '<option value="">Chọn thuộc tính</option>';
    allVariants.forEach(variant => {
        const option = document.createElement('option');
        option.value = variant.id;
        option.dataset.name = variant.name;
        option.textContent = variant.name;
        select.appendChild(option);
    });
}

async function loadVariantValues(typeSelect, selectedValueId = null, selectedValueText = null) {
    const attributeRow = typeSelect.closest('.attribute-row');
    if (!attributeRow) return;

    const valueSelect = attributeRow.querySelector('.variant-value-select');
    if (!valueSelect) return;

    valueSelect.innerHTML = '<option value="">Chọn giá trị</option>';

    if (!typeSelect.value) {
        validateSaveButton();
        return;
    }

    try {
        const data = await fetchJson(`${ADMIN_BASE}/api/variants/${typeSelect.value}`);
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
            if (matchedOption) valueSelect.value = matchedOption.value;
        }
    } catch (error) {
        console.error(error);
        valueSelect.innerHTML = '<option value="">Không tải được giá trị</option>';
    }

    validateSaveButton();
}

function createAttributeRow(card, selectedTypeId = null, selectedValueId = null, selectedValueText = null) {
    const row = document.createElement('div');
    row.className = 'attribute-row';

    const typeSelect = document.createElement('select');
    typeSelect.className = 'option-select variant-type-select';
    populateVariantTypeSelect(typeSelect);

    const valueSelect = document.createElement('select');
    valueSelect.className = 'option-select variant-value-select';
    valueSelect.innerHTML = '<option value="">Chọn giá trị</option>';

    const removeButton = document.createElement('button');
    removeButton.type = 'button';
    removeButton.className = 'remove-attribute-button';
    removeButton.innerHTML = '×';
    removeButton.title = 'Xóa thuộc tính';
    removeButton.addEventListener('click', () => {
        row.remove();
        validateSaveButton();
    });

    typeSelect.addEventListener('change', () => loadVariantValues(typeSelect));
    valueSelect.addEventListener('change', validateSaveButton);

    row.appendChild(typeSelect);
    row.appendChild(valueSelect);
    row.appendChild(removeButton);

    card.querySelector('.attributes-list').appendChild(row);

    if (selectedTypeId) {
        typeSelect.value = String(selectedTypeId);
        loadVariantValues(typeSelect, selectedValueId, selectedValueText);
    }

    return row;
}

function createOptionCard(option = {}) {
    const card = document.createElement('div');
    card.className = 'variant-card';
    if (option.optionId) card.dataset.optionId = option.optionId;

    const stockText = option.optionId ? `Tồn kho hiện tại: ${option.stock ?? 0}` : 'Tồn kho hiện tại: 0';

    card.innerHTML = `
        <div class="variant-card-header">
            <div>
                <strong>Phiên bản bán</strong>
                <div class="stock-readonly">${stockText}</div>
            </div>
            <button type="button" class="remove-variant-button">Xóa dòng</button>
        </div>

        <div class="variant-price-row">
            <label>Giá bán <span class="required">*</span></label>
            <input type="number" min="1" step="1000" class="variant-price-input" placeholder="VD: 450000" value="${option.price ?? ''}">
        </div>

        <div class="attributes-list"></div>

        <button type="button" class="add-attribute-button">+ Thêm thuộc tính cho phiên bản này</button>
    `;

    card.querySelector('.variant-price-input').addEventListener('input', validateSaveButton);

    card.querySelector('.remove-variant-button').addEventListener('click', () => {
        if (getOptionCards().length <= 1) {
            notify('Sản phẩm cần ít nhất 1 phiên bản bán.', 'error');
            return;
        }
        card.remove();
        validateSaveButton();
    });

    card.querySelector('.add-attribute-button').addEventListener('click', () => {
        createAttributeRow(card);
        validateSaveButton();
    });

    if (option.attributes && option.attributes.length > 0) {
        option.attributes.forEach(attr => createAttributeRow(card, attr.typeId, attr.valueId, attr.valueText));
    } else {
        createAttributeRow(card);
    }

    return card;
}

function addOptionRow(option = {}) {
    const container = $('optionRows');
    if (!container) return;
    const card = createOptionCard(option);
    container.appendChild(card);
    validateSaveButton();
}

function groupOptionsFromApi(options) {
    const grouped = new Map();

    (options || []).forEach(row => {
        const optionId = row.id;
        if (!optionId) return;

        if (!grouped.has(optionId)) {
            grouped.set(optionId, {
                optionId,
                price: row.price,
                stock: row.stock ?? 0,
                attributes: []
            });
        }

        if (row.variantId) {
            const type = allVariants.find(item => item.name === row.variantName);
            grouped.get(optionId).attributes.push({
                typeId: type ? type.id : null,
                valueId: row.variantId,
                valueText: row.variantValue
            });
        }
    });

    return Array.from(grouped.values());
}

function renderOptionRows(options) {
    const container = $('optionRows');
    if (!container) return;
    container.innerHTML = '';

    const groupedOptions = groupOptionsFromApi(options);
    if (groupedOptions.length === 0) {
        addOptionRow();
        return;
    }

    groupedOptions.forEach(option => addOptionRow(option));
}

function getOptionRowsData(strict = true) {
    const cards = getOptionCards();
    if (!cards.length) {
        if (strict) throw new Error('Vui lòng thêm ít nhất 1 phiên bản bán.');
        return [];
    }

    return cards.map((card, index) => {
        const price = card.querySelector('.variant-price-input')?.value?.trim();
        const variantIds = Array.from(card.querySelectorAll('.variant-value-select'))
            .map(select => select.value)
            .filter(Boolean)
            .map(value => Number(value));

        if (strict) {
            parsePositiveInteger(price, `Giá bán của phiên bản ${index + 1}`);
            if (variantIds.length === 0) {
                throw new Error(`Phiên bản ${index + 1} cần ít nhất 1 thuộc tính.`);
            }
        }

        return {
            optionId: card.dataset.optionId ? Number(card.dataset.optionId) : null,
            price: Number(price),
            variantIds: [...new Set(variantIds)]
        };
    });
}

function gatherProductData(strict = true) {
    const name = getInputValue('productName').trim();
    const sku = getInputValue('sku').trim();
    const categoryId = getInputValue('categoryDropdown');
    const brandId = getInputValue('vendor');
    const description = getInputValue('description').trim();
    const isActive = $('statusSelect') ? $('statusSelect').value === 'true' : true;
    const primaryImage = getPrimaryImage();

    if (strict) {
        if (!name) throw new Error('Tên sản phẩm không được để trống.');
        if (!sku) throw new Error('SKU không được để trống.');
        if (!categoryId) throw new Error('Vui lòng chọn danh mục.');
        if (!brandId) throw new Error('Vui lòng chọn thương hiệu.');
        if (!primaryImage) throw new Error('Vui lòng upload ít nhất 1 ảnh sản phẩm.');
    }

    return {
        name,
        sku,
        description,
        categoryId: categoryId ? Number(categoryId) : null,
        brandId: brandId ? Number(brandId) : null,
        isActive,
        primaryImage: primaryImage?.id || null,
        imageIds: getActiveImages().filter(image => image.id).map(image => image.id),
        options: getOptionRowsData(strict)
    };
}

function renderImagePreview() {
    const container = $('imagePreviewContainer');
    const uploadIcon = $('uploadIcon');
    const dragDropText = $('dragDropText');
    const previewImage = $('previewImage');

    if (!container) return;
    container.innerHTML = '';

    const activeImages = getActiveImages();

    if (uploadIcon) uploadIcon.style.display = activeImages.length ? 'none' : 'block';
    if (dragDropText) dragDropText.style.display = activeImages.length ? 'none' : 'block';

    if (previewImage && activeImages[0]?.url) {
        previewImage.src = activeImages[0].url;
    }

    activeImages.forEach((image, index) => {
        const wrapper = document.createElement('div');
        wrapper.className = 'image-wrapper';

        const img = document.createElement('img');
        img.src = image.url;
        img.className = 'preview-image';
        img.alt = `Ảnh sản phẩm ${index + 1}`;

        const badge = document.createElement('span');
        badge.className = index === 0 ? 'primary-icon' : 'image-order-badge';
        badge.textContent = index === 0 ? '★' : String(index + 1);
        badge.title = index === 0 ? 'Ảnh chính' : 'Bấm để chọn làm ảnh chính';
        badge.addEventListener('click', () => setPrimaryImage(image.tempId || image.id));

        const deleteButton = document.createElement('button');
        deleteButton.type = 'button';
        deleteButton.className = 'image-delete-button';
        deleteButton.innerHTML = '×';
        deleteButton.title = 'Xóa ảnh';
        deleteButton.addEventListener('click', () => removeImage(image.tempId || image.id));

        wrapper.appendChild(img);
        wrapper.appendChild(badge);
        wrapper.appendChild(deleteButton);
        container.appendChild(wrapper);
    });

    validateSaveButton();
}

function setPrimaryImage(identifier) {
    const index = productImages.findIndex(image => !image.removed && (image.tempId === identifier || image.id === identifier));
    if (index <= 0) return;
    const [selected] = productImages.splice(index, 1);
    productImages.unshift(selected);
    renderImagePreview();
}

function removeImage(identifier) {
    const image = productImages.find(item => !item.removed && (item.tempId === identifier || item.id === identifier));
    if (!image) return;
    image.removed = true;
    renderImagePreview();
}

function previewSelectedImages(files) {
    Array.from(files || []).slice(0, 10).forEach(file => {
        if (!file.type.startsWith('image/')) return;
        const tempId = `new-${tempImageCounter++}`;
        productImages.push({
            tempId,
            file,
            url: URL.createObjectURL(file),
            isNew: true,
            removed: false
        });
    });

    if ($('fileInput')) $('fileInput').value = '';
    renderImagePreview();
}

async function uploadNewImagesIfNeeded() {
    const newImages = getActiveImages().filter(image => image.isNew && !image.id);
    if (!newImages.length) return [];

    const formData = new FormData();
    newImages.forEach(image => formData.append('file', image.file));

    const uploadData = await fetchJson(`${API_BASE}/uploadImage`, {
        method: 'POST',
        body: formData
    });

    const uploadedImages = uploadData.data || [];
    if (uploadedImages.length !== newImages.length) {
        throw new Error('Số ảnh upload thành công không khớp với số ảnh đã chọn.');
    }

    newImages.forEach((image, index) => {
        image.id = uploadedImages[index].id;
        image.url = uploadedImages[index].url;
        image.isNew = false;
        delete image.file;
    });

    return uploadedImages;
}

async function assignProductImages(imageIds, productId) {
    if (!imageIds || imageIds.length === 0) return;

    await Promise.all(imageIds.map(imageId => {
        const body = new URLSearchParams();
        body.append('productId', productId);
        body.append('imageId', imageId);

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
    for (const optionRow of productData.options || []) {
        const option = await fetchJson(`${ADMIN_BASE}/options/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json;charset=UTF-8' },
            body: JSON.stringify({
                productId,
                price: optionRow.price
            })
        });

        const optionId = option.data?.id;
        if (!optionId) continue;

        for (const variantId of optionRow.variantIds || []) {
            await fetchJson(`${ADMIN_BASE}/addOptionVariantValue`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                body: JSON.stringify({ optionId, variantId })
            });
        }
    }
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
    const saveButton = $('saveButton');

    try {
        if (saveButton) saveButton.disabled = true;

        await uploadNewImagesIfNeeded();
        const productData = gatherProductData(true);
        productData.primaryImage = getPrimaryImage()?.id || null;
        productData.imageIds = getActiveImages().map(image => image.id).filter(Boolean);

        if (currentProductId) {
            const updated = await updateProduct(productData);
            notify('Cập nhật sản phẩm thành công.', 'success');
            window.location.href = `${ADMIN_BASE}/list-product`;
            return updated;
        }

        const created = await createProduct(productData);
        await assignProductImages(productData.imageIds, created.id);
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

    if (!product) throw new Error('Không tìm thấy dữ liệu sản phẩm.');

    setInputValue('productName', product.name || '');
    setInputValue('sku', product.sku || '');
    setInputValue('categoryDropdown', product.categoryId || '');
    setInputValue('description', product.description || '');
    setInputValue('vendor', product.brandId || '');
    setInputValue('statusSelect', String((product.active ?? product.isActive) !== false));

    productImages = [];
    if (product.images && product.images.length > 0) {
        productImages = product.images.map(image => ({
            id: image.id,
            url: image.url,
            isNew: false,
            removed: false
        }));
    } else if (product.imageId && product.imageUrl) {
        productImages = [{
            id: product.imageId,
            url: product.imageUrl,
            isNew: false,
            removed: false
        }];
    }
    renderImagePreview();

    await loadVariantsByCategory(product.categoryId);
    renderOptionRows(product.options || []);
    validateSaveButton();
}

window.addVariant = () => addOptionRow();
window.addOptionGroup = () => {};
window.removeOptionGroup = button => button?.closest('.attribute-row')?.remove();
window.saveProductDetails = saveProduct;
window.fetchVariantValues = function () {
    const activeElement = document.activeElement;
    if (activeElement && activeElement.matches('.variant-type-select')) {
        loadVariantValues(activeElement);
    }
};

window.addEventListener('DOMContentLoaded', async () => {
    const uploadButton = $('uploadButton');
    const fileInput = $('fileInput');
    const saveButton = $('saveButton');
    const categoryDropdown = $('categoryDropdown');
    const addOptionRowButton = $('addOptionRowButton');
    const mediaUploadBox = $('mediaUploadBox');

    currentProductId = getProductIdFromUrl();

    if ($('pageTitle')) {
        $('pageTitle').textContent = currentProductId ? 'Chỉnh sửa sản phẩm' : 'Thêm sản phẩm';
    }

    if (uploadButton && fileInput) uploadButton.addEventListener('click', () => fileInput.click());
    if (fileInput) fileInput.addEventListener('change', () => previewSelectedImages(fileInput.files));
    if (saveButton) saveButton.addEventListener('click', saveProduct);
    if (addOptionRowButton) addOptionRowButton.addEventListener('click', () => addOptionRow());

    if (mediaUploadBox) {
        mediaUploadBox.addEventListener('dragover', event => {
            event.preventDefault();
            mediaUploadBox.classList.add('drag-over');
        });
        mediaUploadBox.addEventListener('dragleave', () => mediaUploadBox.classList.remove('drag-over'));
        mediaUploadBox.addEventListener('drop', event => {
            event.preventDefault();
            mediaUploadBox.classList.remove('drag-over');
            previewSelectedImages(event.dataTransfer.files);
        });
    }

    ['productName', 'sku', 'vendor', 'statusSelect'].forEach(id => {
        const element = $(id);
        if (element) {
            element.addEventListener('input', validateSaveButton);
            element.addEventListener('change', validateSaveButton);
        }
    });

    if (categoryDropdown) {
        categoryDropdown.addEventListener('change', async () => {
            await loadVariantsByCategory(categoryDropdown.value);
            renderOptionRows([]);
            validateSaveButton();
        });
    }

    try {
        await Promise.all([loadCategories(), loadBrands()]);

        if (currentProductId) {
            await fetchProductDetails(currentProductId);
        } else {
            if (!getInputValue('sku').trim()) {
                setInputValue('sku', `PRD-${Date.now()}`);
            }
            await loadVariantsByCategory(categoryDropdown?.value || null);
            addOptionRow();
            validateSaveButton();
        }
    } catch (error) {
        console.error(error);
        notify(error.message || 'Không thể tải dữ liệu form sản phẩm.', 'error');
    }
});
