package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.ProductDao;
import org.jdbi.v3.core.Jdbi;

public class ProductService {
    Jdbi jdbi;
    private ProductDao productDao;

    public ProductService(Jdbi jdbi) {
        this.jdbi = jdbi;
        this.productDao = jdbi.onDemand(ProductDao.class);
    }

}
