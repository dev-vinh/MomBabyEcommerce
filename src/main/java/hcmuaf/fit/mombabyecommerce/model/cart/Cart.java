package hcmuaf.fit.mombabyecommerce.model.cart;

import hcmuaf.fit.mombabyecommerce.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements Serializable {
    Map<Integer, ProductCart> data = new HashMap<>();

    public Cart() {
    }

    public boolean addProduct(Product product) {
        return addProduct(product, 1);
    }
    public boolean addProduct(Product product, int quantity) {
        if (product == null) {
            System.err.println("[Cart] ERROR: Cannot add null product");
            return false;
        }

        if (product.getOptionId() == null) {
            System.err.println("[Cart] ERROR: Product optionId is null for product: " + product.getName());
            return false;
        }

        System.out.println("[Cart] Adding product with optionId: " + product.getOptionId() + ", quantity: " + quantity);

        if (data.containsKey(product.getOptionId())) {
            System.out.println("[Cart] Product already exists, updating quantity");
            // Lấy số lượng cũ + số lượng khách vừa chọn (thay vì + 1)
            int currentQuantity = data.get(product.getOptionId()).getQuantity();
            return update(product, currentQuantity + quantity);
        } else {
            // Nếu sản phẩm chưa có trong giỏ, tạo mới và set đúng số lượng khách chọn
            ProductCart newProductCart = new ProductCart(product);
            newProductCart.setQuantity(quantity);

            data.put(product.getOptionId(), newProductCart);
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

        if (quantity < 1) {
            return false;
        }

        if (product.getStock() != null && quantity > product.getStock()) {
            return false;
        }

        if (data.containsKey(product.getOptionId())) {
            ProductCart productCart = data.get(product.getOptionId());
            productCart.setQuantity(quantity);
            data.put(product.getOptionId(), productCart);
            return true;
        }
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
    public void add(ProductCart productCart) {

        data.put(
                productCart.getOptionId(),
                productCart
        );
    }

    public void setData(Map<Integer, ProductCart> data) {
        this.data = data;
    }

    public void clearById(Integer optionId) {
        data.remove(optionId);
    }
}