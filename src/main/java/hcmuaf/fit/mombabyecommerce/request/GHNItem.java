package hcmuaf.fit.mombabyecommerce.request;

import hcmuaf.fit.mombabyecommerce.model.Product;

public class GHNItem {
    String name;
    String code;
    int quantity;
    int price;
    int length;
    int weight;
    int width;
    int height;

    public GHNItem(Product product, int quantity) {
        this.name = product.getName();
        this.code = String.valueOf(product.getId());
        this.price = product.getPrice();
        this.length = product.getLength() != null ? product.getLength() : 20;

        this.weight = product.getWeight() != null
                        ? product.getWeight()
                        : 500;

        this.width = product.getWidth() != null
                        ? product.getWidth()
                        : 15;

        this.height = product.getHeight() != null
                        ? product.getHeight()
                        : 10;

        this.quantity = quantity;
    }

}
