package hcmuaf.fit.mombabyecommerce.model;

import java.util.List;

public class InventoryDTO {
    private Integer productId;
    private String productName;
    private String productImage;
    private List<OptionVariant> options;

    public InventoryDTO() {}

    public InventoryDTO(Integer productId, String productName, String productImage, List<OptionVariant> options) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.options = options;
    }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }

    public List<OptionVariant> getOptions() { return options; }
    public void setOptions(List<OptionVariant> options) { this.options = options; }
}