package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.ImageDao;
import hcmuaf.fit.mombabyecommerce.Dao.OptionVariantDao;
import hcmuaf.fit.mombabyecommerce.Dao.ProductDao;
import hcmuaf.fit.mombabyecommerce.Dao.VariantDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Image;
import hcmuaf.fit.mombabyecommerce.model.OptionVariant;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.ProductDTO;
import hcmuaf.fit.mombabyecommerce.model.Variant;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<Product> getProductsPaged(int page, int size) {
        int offset = (page - 1) * size;
        return jdbi.withExtension(ProductDao.class, dao -> dao.getProductsPaged(size, offset));
    }

    public int countAllProducts() {
        return jdbi.withExtension(ProductDao.class, dao -> dao.countAllProducts());
    }

    public boolean deactivateProduct(int productId) {
        return productDao.deactivateProduct(productId);
    }

    public List<Product> getProductsFiltered(
            Integer categoryId, Boolean isActive, String keyword,
            int page, int size) {
        int offset = (page - 1) * size;
        int hasCat = categoryId != null ? 1 : 0;
        int hasActive = isActive != null ? 1 : 0;
        int hasKw = (keyword != null && !keyword.isBlank()) ? 1 : 0;
        return productDao.getProductsFiltered(
                categoryId, hasCat, isActive, hasActive, keyword, hasKw, size, offset);
    }

    public int countProductsFiltered(Integer categoryId, Boolean isActive, String keyword) {
        int hasCat = categoryId != null ? 1 : 0;
        int hasActive = isActive != null ? 1 : 0;
        int hasKw = (keyword != null && !keyword.isBlank()) ? 1 : 0;
        return productDao.countProductsFiltered(categoryId, hasCat, isActive, hasActive, keyword, hasKw);
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

    public ProductDTO createProductForAdmin(String name,
                                            String sku,
                                            String description,
                                            Boolean isActive,
                                            Integer categoryId,
                                            Integer brandId,
                                            Integer imageId,
                                            List<Integer> imageIds,
                                            List<Map<String, Object>> options) {
        final int[] createdProductId = new int[1];

        jdbi.useTransaction(handle -> {
            ProductDao pDao = handle.attach(ProductDao.class);
            ImageDao imageDao = handle.attach(ImageDao.class);
            OptionVariantDao optionDao = handle.attach(OptionVariantDao.class);
            VariantDao variantDao = handle.attach(VariantDao.class);

            int productId = pDao.addProduct(
                    name,
                    description,
                    isActive,
                    categoryId,
                    brandId,
                    imageId,
                    sku
            );

            if (productId <= 0) {
                throw new IllegalArgumentException("Không thể thêm sản phẩm.");
            }

            createdProductId[0] = productId;
            replaceProductImages(imageDao, productId, imageIds);
            saveOptionsForProduct(optionDao, variantDao, productId, options, false);
        });

        return editProductById(createdProductId[0]);
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
                                        List<Integer> brandIds,
                                        String sort,
                                        Integer page,
                                        Integer size) {

        if (page == null || page < 1) page = 1;
        if (size == null || size <= 0) size = 16;

        int offset = (page - 1) * size;
        int brandIdsSize = (brandIds != null && !brandIds.isEmpty()) ? brandIds.size() : 0;

        if (brandIdsSize == 0) brandIds = null;
        String brandIdsComma = (brandIds != null && !brandIds.isEmpty())
                ? brandIds.stream().map(String::valueOf).collect(Collectors.joining(","))
                : null;

        return productDao.filterProducts(categoryId, minPrice, maxPrice, brandIds, brandIdsSize, brandIdsComma, sort, size, offset);
    }

    // mới thêm vô bởi NV
    public ProductDTO editProductById(int id) {
        Product product = productDao.editProduct(id);

        if (product == null) {
            return null;
        }

        List<Variant> variants = productDao.getVariants(id);
        List<OptionVariant> options = jdbi.withExtension(OptionVariantDao.class, dao -> dao.getOptionDetailsByProductId(id));
        List<Image> images = jdbi.withExtension(ImageDao.class, dao -> dao.getImagesByProductId(id));

        return new ProductDTO(product, variants, options, images);
    }

    public int countProducts(int categoryId,
                             Integer minPrice,
                             Integer maxPrice,
                             List<Integer> brandIds) {
        int brandIdsSize = (brandIds != null && !brandIds.isEmpty()) ? brandIds.size() : 0;
        if (brandIdsSize == 0) brandIds = null;
        String brandIdsComma = (brandIds != null && !brandIds.isEmpty())
                ? brandIds.stream().map(String::valueOf).collect(Collectors.joining(","))
                : null;

        return productDao.countProducts(categoryId, minPrice, maxPrice, brandIds, brandIdsSize, brandIdsComma);
    }

    public ProductDTO updateProductForAdmin(Integer productId,
                                            String name,
                                            String sku,
                                            String description,
                                            Boolean isActive,
                                            Integer categoryId,
                                            Integer brandId,
                                            Integer imageId,
                                            List<Integer> imageIds,
                                            List<Map<String, Object>> options) {
        jdbi.useTransaction(handle -> {
            ProductDao pDao = handle.attach(ProductDao.class);
            OptionVariantDao optionDao = handle.attach(OptionVariantDao.class);
            VariantDao variantDao = handle.attach(VariantDao.class);
            ImageDao imageDao = handle.attach(ImageDao.class);

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

            replaceProductImages(imageDao, productId, imageIds);
            saveOptionsForProduct(optionDao, variantDao, productId, options, true);
        });

        return editProductById(productId);
    }

    private void replaceProductImages(ImageDao imageDao, Integer productId, List<Integer> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất 1 ảnh sản phẩm.");
        }

        imageDao.deleteImagesByProductId(productId);
        Set<Integer> inserted = new HashSet<>();
        for (Integer imgId : imageIds) {
            if (imgId != null && imgId > 0 && inserted.add(imgId)) {
                imageDao.addImageToProduct(productId, imgId);
            }
        }
    }

    private void saveOptionsForProduct(OptionVariantDao optionDao,
                                       VariantDao variantDao,
                                       Integer productId,
                                       List<Map<String, Object>> options,
                                       boolean deactivateMissingOptions) {
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("Sản phẩm cần ít nhất 1 phiên bản bán.");
        }

        List<Integer> submittedExistingOptionIds = new ArrayList<>();
        for (Map<String, Object> optionPayload : options) {
            Integer optionId = getIntegerFromMap(optionPayload, "optionId");
            if (optionId != null && optionId > 0) {
                submittedExistingOptionIds.add(optionId);
            }
        }

        if (deactivateMissingOptions) {
            if (submittedExistingOptionIds.isEmpty()) {
                optionDao.deactivateAllOptionsByProductId(productId);
            } else {
                optionDao.deactivateOptionsNotIn(productId, submittedExistingOptionIds);
            }
        }

        Set<String> seenCombinations = new HashSet<>();
        for (int index = 0; index < options.size(); index++) {
            Map<String, Object> optionPayload = options.get(index);

            Integer price = getIntegerFromMap(optionPayload, "price");
            if (price == null || price <= 0) {
                throw new IllegalArgumentException("Giá bán của phiên bản " + (index + 1) + " phải lớn hơn 0.");
            }

            List<Integer> variantIds = getIntegerListFromMap(optionPayload.get("variantIds"));
            if (variantIds == null || variantIds.isEmpty()) {
                throw new IllegalArgumentException("Phiên bản " + (index + 1) + " cần ít nhất 1 thuộc tính.");
            }

            List<Integer> distinctVariantIds = variantIds.stream()
                    .filter(id -> id != null && id > 0)
                    .distinct()
                    .sorted()
                    .toList();

            if (distinctVariantIds.isEmpty()) {
                throw new IllegalArgumentException("Phiên bản " + (index + 1) + " cần ít nhất 1 thuộc tính hợp lệ.");
            }

            String combinationKey = distinctVariantIds.toString();
            if (!seenCombinations.add(combinationKey)) {
                throw new IllegalArgumentException("Có phiên bản bị trùng thuộc tính. Vui lòng kiểm tra lại dòng " + (index + 1) + ".");
            }

            Integer optionId = getIntegerFromMap(optionPayload, "optionId");
            Integer finalOptionId;

            if (optionId == null || optionId <= 0) {
                finalOptionId = optionDao.createOption(productId, price);
                optionDao.createInventory(finalOptionId, 0);
            } else {
                if (optionDao.countOptionBelongsToProduct(productId, optionId) == 0) {
                    throw new IllegalArgumentException("Phiên bản " + optionId + " không thuộc sản phẩm hiện tại.");
                }

                finalOptionId = optionId;
                boolean optionUpdated = optionDao.updateOption(finalOptionId, price);
                if (!optionUpdated) {
                    throw new IllegalArgumentException("Không tìm thấy phiên bản cần cập nhật.");
                }
            }

            variantDao.deleteOptionVariants(finalOptionId);

            for (Integer variantId : distinctVariantIds) {
                variantDao.addOptionVariantValue(finalOptionId, variantId);
            }
        }
    }

    private Integer getIntegerFromMap(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString().trim());
    }

    private List<Integer> getIntegerListFromMap(Object value) {
        if (!(value instanceof List<?> list)) {
            return null;
        }

        return list.stream()
                .filter(item -> item != null && !item.toString().trim().isEmpty())
                .map(item -> item instanceof Number ? ((Number) item).intValue() : Integer.parseInt(item.toString().trim()))
                .toList();
    }
    public boolean updateStock(Integer optionId, int quantity) {
        return productDao.updateStock(optionId, quantity) > 0;
    }




    public static void main(String[] args) {
        ProductService productService = new ProductService(DBConnection.getJdbi());
        System.out.println(productService.getProductById(1));

    }
}