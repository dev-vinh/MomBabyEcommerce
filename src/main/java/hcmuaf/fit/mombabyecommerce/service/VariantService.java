package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.VariantDao;
import hcmuaf.fit.mombabyecommerce.model.Variant;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

import java.util.List;

public class VariantService {

    private final VariantDao variantDao;
    public VariantService(Jdbi jdbi) {
        this.variantDao = jdbi.onDemand(VariantDao.class);
        jdbi.registerRowMapper(ConstructorMapper.factory(Variant.class));
    }


    public Variant createVariant(String name, Integer categoryId) {
        int id = variantDao.createVariant(name, categoryId);
        return variantDao.getVariantById(id);
    }
    public int addOptionVariantValue(Integer optionId, Integer variantId) {
        return variantDao.addOptionVariantValue(optionId, variantId);
    }



}

