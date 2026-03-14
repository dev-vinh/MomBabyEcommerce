package hcmuaf.fit.mombabyecommerce.model.cart;

import hcmuaf.fit.mombabyecommerce.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    Map<Integer, ProductCart> data = new HashMap<>();

    public boolean addProduct(Product product) {
        if (product == null) {
            System.err.println("[Cart] ERROR: Cannot add null product");
            return false;
        }

        if (product.getOptionId() == null) {
            System.err.println("[Cart] ERROR: Product optionId is null for product: " + product.getName());
            return false;
        }

        System.out.println("[Cart] Adding product with optionId: " + product.getOptionId());

        if (data.containsKey(product.getOptionId())) {
            System.out.println("[Cart] Product already exists, updating quantity");
            return update(product, data.get(product.getOptionId()).getQuantity() + 1);
        } else {
            data.put(product.getOptionId(), new ProductCart(product));
            System.out.println("[Cart] Product added successfully. Total items in cart: " + data.size());
            return true;
        }
    }

    public List<ProductCart> getProducts() {
        return new ArrayList<>(data.values());
    }

    public boolean update(Product product, int quantity) {
        if (product.getOptionId() == null) {
            System.err.println("[Cart] ERROR: Cannot update - product optionId is null");
            return false;
        }

        if (data.containsKey(product.getOptionId())) {
            ProductCart productCart = data.get(product.getOptionId());
            productCart.setQuantity(quantity);
            data.put(product.getOptionId(), productCart);
            System.out.println("[Cart] Updated quantity for optionId " + product.getOptionId() + " to " + quantity);
            return true;
        }
        System.err.println("[Cart] ERROR: Product with optionId " + product.getOptionId() + " not found in cart");
        return false;
    }

    public void delete(Integer optionId) {
        if (data.containsKey(optionId)) {
            data.remove(optionId);
            System.out.println("[Cart] Removed product with optionId: " + optionId);
        } else {
            System.err.println("[Cart] ERROR: Cannot remove - optionId " + optionId + " not found in cart");
        }
    }

    public Map<Integer, ProductCart> getData() {
        return data;
    }

    public void setData(Map<Integer, ProductCart> data) {
        this.data = data;
    }

    public void clearById(Integer optionId) {
        data.remove(optionId);
    }
}