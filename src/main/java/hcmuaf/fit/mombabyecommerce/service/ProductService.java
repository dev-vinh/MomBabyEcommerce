package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.OptionVariantDao;
import hcmuaf.fit.mombabyecommerce.Dao.ProductDao;
import hcmuaf.fit.mombabyecommerce.Dao.VariantDao;
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

        if (product.getSku() == null || product.getSku().trim().isEmpty()) {
            String generatedSku = "PRD-" + System.currentTimeMillis();
            product.setSku(generatedSku);
        }

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

    public List<Product> filterProducts(int categoryId,
                                        Integer minPrice,
                                        Integer maxPrice,
                                        Integer brandId,
                                        String sort,
                                        Integer page,
                                        Integer size) {

        if (page == null || page < 1) page = 1;
        if (size == null || size <= 0) size = 16;

        int offset = (page - 1) * size;

        return productDao.filterProducts(categoryId, minPrice, maxPrice, brandId, sort, size, offset);
    }

    // mới thêm vô bởi NV
    public ProductDTO editProductById(int id) {
        Product product = productDao.editProduct(id);
        List<Variant> variants = productDao.getVariants(id);
        return new ProductDTO(product, variants);
    }
    public int countProducts(int categoryId,
                              Integer minPrice,
                              Integer maxPrice,
                              Integer brandId) {
        return productDao.countProducts(categoryId, minPrice, maxPrice, brandId);
    }
    public ProductDTO updateProductForAdmin(Integer productId,
                                            String name,
                                            String sku,
                                            String description,
                                            Boolean isActive,
                                            Integer categoryId,
                                            Integer brandId,
                                            Integer imageId,
                                            Integer optionId,
                                            Integer price,
                                            Integer stock,
                                            List<Integer> variantIds) {
        jdbi.useTransaction(handle -> {
            ProductDao pDao = handle.attach(ProductDao.class);
            OptionVariantDao optionDao = handle.attach(OptionVariantDao.class);
            VariantDao variantDao = handle.attach(VariantDao.class);

            boolean updated = pDao.updateProduct(
                    productId,
                    name,
                    sku,
                    description,
                    isActive,
                    categoryId,
                    brandId,
                    imageId
            );

            if (!updated) {
                throw new IllegalArgumentException("Không tìm thấy sản phẩm cần cập nhật.");
            }

            Integer finalOptionId = optionId;
            if (finalOptionId == null || finalOptionId <= 0) {
                finalOptionId = optionDao.createOption(productId, price);
                optionDao.createInventory(finalOptionId, stock);
            } else {
                boolean optionUpdated = optionDao.updateOption(finalOptionId, price);
                if (!optionUpdated) {
                    throw new IllegalArgumentException("Không tìm thấy biến thể giá cần cập nhật.");
                }

                boolean stockUpdated = optionDao.updateOptionStock(finalOptionId, stock);
                if (!stockUpdated) {
                    optionDao.createInventory(finalOptionId, stock);
                }
            }

            if (variantIds != null) {
                variantDao.deleteOptionVariants(finalOptionId);
                for (Integer variantId : variantIds) {
                    if (variantId != null && variantId > 0) {
                        variantDao.addOptionVariantValue(finalOptionId, variantId);
                    }
                }
            }
        });

        return editProductById(productId);
    }
    public static void main(String[] args) {
        ProductService productService = new ProductService(DBConnection.getJdbi());
        // System.out.println(productService.suggestProducts( ).size());
        // System.out.println(productService.getProductByIdAndOptionId(211, 85 ));
        System.out.println(productService.getProductById(1));

    }
}
