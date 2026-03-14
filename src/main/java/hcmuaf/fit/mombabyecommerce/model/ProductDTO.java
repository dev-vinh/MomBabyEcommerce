package hcmuaf.fit.mombabyecommerce.model;

import java.util.List;

public class ProductDTO {
    private Integer id;
    private  String name;
    private String sku;
    private String description;
    private  Boolean isActive;
    private Integer categoryId;
    private Integer brandId;
    private  Integer noOfViews;
    private Integer noOfSold;
    private Integer imageId;
    private Integer price;  // option
    private Integer stock;  //option
    private Integer optionId;
    private  String categoryName;
    private  String imageUrl;
    private List<Variant> variants;

    public ProductDTO(Product product, List<Variant> variants) {
        this.id = product.getId();
        this.name = product.getName();
        this.sku = product.getSku();
        this.description = product.getDescription();
        this.isActive = product.getActive();
        this.categoryId = product.getCategoryId();
        this.brandId = product.getBrandId();
        this.noOfViews = product.getNoOfViews();
        this.noOfSold = product.getNoOfSold();
        this.imageId = product.getImageId();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.optionId = product.getOptionId();
        this.categoryName = product.getCategoryName();
        this.imageUrl = product.getImageUrl();
        this.variants = variants;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getNoOfViews() {
        return noOfViews;
    }

    public void setNoOfViews(Integer noOfViews) {
        this.noOfViews = noOfViews;
    }

    public Integer getNoOfSold() {
        return noOfSold;
    }

    public void setNoOfSold(Integer noOfSold) {
        this.noOfSold = noOfSold;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }
}

