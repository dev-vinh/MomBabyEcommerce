package hcmuaf.fit.mombabyecommerce.model;

public class ProductOptionDTO {
    private Integer optionId;
    private Integer productId;
    private Integer price;
    private Integer stock;
    private String variantText;

    public ProductOptionDTO() {
    }

    public ProductOptionDTO(Integer optionId, Integer productId, Integer price, Integer stock) {
        this.optionId = optionId;
        this.productId = productId;
        this.price = price;
        this.stock = stock;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public String getVariantText() {
        return variantText;
    }

    public void setVariantText(String variantText) {
        this.variantText = variantText;
    }
}
