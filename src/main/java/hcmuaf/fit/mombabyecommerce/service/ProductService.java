package hcmuaf.fit.mombabyecommerce.service;


import hcmuaf.fit.mombabyecommerce.Dao.ProductDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.ProductDTO;
import hcmuaf.fit.mombabyecommerce.model.Variant;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ProductService {
    Jdbi jdbi;
    private ProductDao productDao;

    public ProductService(Jdbi jdbi) {
        this.jdbi = jdbi;
            this.productDao = jdbi.onDemand(ProductDao.class);
    }

    public Product getProductById(int productId) {
        return jdbi.withExtension(ProductDao.class, dao -> dao.getProductById(productId));
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return jdbi.withExtension(ProductDao.class, dao -> dao.getProductsByCategory(categoryId));
    }

    public Product getProductByIdAndOptionId(int productId, int optionId) {
        return jdbi.withExtension(ProductDao.class, dao -> dao.getProductByIdAndOptionId(productId, optionId));
    }

    public List<Product> getAllProducts() {
        List<Product> products = jdbi.withExtension(ProductDao.class, dao -> dao.getAllProducts());

        return products;
    }

    public Integer getMinimumPriceForProduct(int productId) {
        return productDao.getMinimumPriceForProduct(productId);
    }

    public Integer getPriceForOption(int optionId) {
        return productDao.getPriceForOption(optionId);
    }

    public Product addProduct(Product product) {

        String generatedSku = "PRD-" + System.currentTimeMillis();
        product.setSku(generatedSku);

        int productId = productDao.addProduct(
                product.getName(), product.getDescription(),
                product.getActive(), product.getCategoryId(),
                product.getBrandId(), product.getImageId(), product.getSku());

        if (productId > 0) {
            product.setId(productId);
            return product;
        }
        // if (rowsAffected > 0) {
        // return productDao.getProductById(product.getId()); // Trả về sản phẩm đã được
        // thêm vào
        // }
        return null;
    }

    public List<Product> searchProducts(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be empty");
        }
        return productDao.searchProducts(name);
    }

    public List<Product> getTopProductsByCategory(Integer categoryId, Integer limit) {
        if (categoryId <= 0 || limit <= 0) {
            throw new IllegalArgumentException("Bad request");
        } else {
            return productDao.getTopProductsByCategoryId(categoryId, limit);
        }
    }

    public Boolean increaseNoOfViews(Integer productId) {
        return productDao.increaseNoOfViews(productId);
    }

    public Boolean increaseNoOfSold(Integer productId, Integer quantity) {
        return productDao.increaseNoOfSold(productId, quantity);
    }

    public List<Product> getTop10() {
        return productDao.getTopProducts();
    }

    public List<Product> filterProducts(int categoryId, Integer minPrice, Integer maxPrice) {
        return productDao.filterProducts(categoryId, minPrice, maxPrice);
    }

    // mới thêm vô bởi NV
    public ProductDTO editProductById(int id) {
        Product product = productDao.editProduct(id);
        List<Variant> variants = productDao.getVariants(id);
        return new ProductDTO(product, variants);
    }
    public boolean updateProduct(Integer id, String name, String description, String sku,
                                 Integer categoryId, Integer brandId, Integer primaryImage,
                                 Integer height, Integer length, Integer width, Integer weight) {
        return jdbi.withExtension(ProductDao.class, dao ->
                dao.updateProduct(id, name, description, sku, categoryId, brandId, primaryImage,
                        height, length, width, weight));
    }

    public static void main(String[] args) {
        ProductService productService = new ProductService(DBConnection.getJdbi());
        // System.out.println(productService.suggestProducts( ).size());
        // System.out.println(productService.getProductByIdAndOptionId(211, 85 ));
        System.out.println(productService.getProductById(1));

    }
}

