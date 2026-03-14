package hcmuaf.fit.mombabyecommerce.model.cart;

import hcmuaf.fit.mombabyecommerce.model.Product;

public class ProductCart {
    Integer productId;
    Integer optionId;
    String name;
    String imageUrl;
    Integer quantity;
    Integer price;
    Integer stock;
    Integer height;
    Integer length;
    Integer width;
    Integer weight;




    public ProductCart(Product product) {
        this.productId = product.getId();
        this.optionId = product.getOptionId();
        this.name = product.getName();
        this.imageUrl = product.getImageUrl();
        this.quantity = 1;
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.height = product.getHeight();
        this.length = product.getLength();
        this.width = product.getWidth();
        this.weight = product.getWeight();


    }


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "ProductCart{" +
                "productId=" + productId +
                ", optionId=" + optionId +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", stock=" + stock +
                ", height=" + height +
                ", length=" + length +
                ", width=" + width +
                ", weight=" + weight +
                '}';
    }
}
