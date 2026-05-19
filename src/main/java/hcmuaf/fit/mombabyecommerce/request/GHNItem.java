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
        this.length = product.getLength();
        this.weight = product.getWeight();
        this.width = product.getWidth();
        this.height = product.getHeight();
        this.quantity = quantity;

    }

}
