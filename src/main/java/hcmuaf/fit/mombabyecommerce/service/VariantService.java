package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.VariantDao;
import hcmuaf.fit.mombabyecommerce.model.Variant;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class VariantService {

    private final VariantDao variantDao;

    public VariantService(Jdbi jdbi) {
        this.variantDao = jdbi.onDemand(VariantDao.class);
    }

    public List<Variant> getVariantsByCategoryId(Integer categoryId) {
        return variantDao.getVariantsByCategoryId(categoryId);
    }

    public List<Variant> getVariantValuesByAttributeId(Integer attributeId) {
        return variantDao.getVariantValuesByAttributeId(attributeId);
    }

    public Variant createTemplateValue(Integer attributeId, String value) {
        validateTemplateInput(attributeId, value);
        String normalizedValue = value.trim();

        if (variantDao.countTemplateValueExists(attributeId, normalizedValue, null) > 0) {
            throw new IllegalArgumentException("Giá trị biến thể này đã tồn tại trong dropdown.");
        }

        int id = variantDao.createTemplateValue(attributeId, normalizedValue);
        Variant created = variantDao.getTemplateVariantById(id);

        if (created == null) {
            throw new IllegalStateException("Không thể tạo giá trị biến thể.");
        }

        return created;
    }

    public Variant updateTemplateValue(Integer id, String value) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Mã giá trị biến thể không hợp lệ.");
        }

        String normalizedValue = normalizeTemplateValue(value);

        Variant current = variantDao.getTemplateVariantById(id);
        if (current == null) {
            throw new IllegalArgumentException("Chỉ được sửa giá trị dropdown mẫu, không được sửa biến thể thật của sản phẩm.");
        }

        Integer attributeId = findAttributeIdByTemplateVariant(current);

        if (variantDao.countTemplateValueExists(attributeId, normalizedValue, id) > 0) {
            throw new IllegalArgumentException("Giá trị biến thể này đã tồn tại trong dropdown.");
        }

        int updated = variantDao.updateTemplateValue(id, normalizedValue);

        if (updated == 0) {
            throw new IllegalStateException("Không thể cập nhật giá trị biến thể.");
        }

        return variantDao.getTemplateVariantById(id);
    }

    public void deleteTemplateValue(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Mã giá trị biến thể không hợp lệ.");
        }

        int deleted = variantDao.deleteTemplateValue(id);

        if (deleted == 0) {
            throw new IllegalArgumentException("Chỉ được xóa giá trị dropdown mẫu, không được xóa biến thể thật của sản phẩm.");
        }
    }

    public int addOptionVariantValue(Integer optionId, Integer variantId) {
        return variantDao.addOptionVariantValue(optionId, variantId);
    }

    public int deleteOptionVariants(Integer optionId) {
        return variantDao.deleteOptionVariants(optionId);
    }

    private void validateTemplateInput(Integer attributeId, String value) {
        if (attributeId == null || attributeId <= 0) {
            throw new IllegalArgumentException("Vui lòng chọn thuộc tính trước khi thêm giá trị.");
        }

        normalizeTemplateValue(value);
    }

    private String normalizeTemplateValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Giá trị biến thể không được để trống.");
        }

        String normalizedValue = value.trim();

        if (normalizedValue.length() > 100) {
            throw new IllegalArgumentException("Giá trị biến thể không được vượt quá 100 ký tự.");
        }

        return normalizedValue;
    }

    private Integer findAttributeIdByTemplateVariant(Variant templateVariant) {
        return getVariantsByCategoryId(templateVariant.getCategoryId()).stream()
                .filter(attribute -> attribute.getName() != null
                        && attribute.getName().equals(templateVariant.getName()))
                .map(Variant::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy thuộc tính tương ứng của giá trị biến thể."));
    }
}