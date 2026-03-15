package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.ProductDao;
import org.jdbi.v3.core.Jdbi;

public class DeleteProductService {
    private final ProductDao productDao;

    public DeleteProductService(Jdbi jdbi) {
        this.productDao = jdbi.onDemand(ProductDao.class);
    }

    public boolean  deactivateProduct(int productId) {
        return productDao.deactivateProduct(productId);
    }
}

