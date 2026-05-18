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
    public int addOptionVariantValue(Integer optionId, Integer variantId) {
        return variantDao.addOptionVariantValue(optionId, variantId);
    }

    public int deleteOptionVariants(Integer optionId) {
        return variantDao.deleteOptionVariants(optionId);
    }
}