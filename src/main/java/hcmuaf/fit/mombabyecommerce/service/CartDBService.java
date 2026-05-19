package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.CartDao;
import hcmuaf.fit.mombabyecommerce.Dao.CartItemDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.CartDB;
import hcmuaf.fit.mombabyecommerce.model.CartItem;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import hcmuaf.fit.mombabyecommerce.model.cart.ProductCart;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class CartDBService {
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;
    private final ProductService productService =
            new ProductService(DBConnection.getJdbi());
    public CartDBService(Jdbi jdbi) {
        this.cartDao = jdbi.onDemand(CartDao.class);
        this.cartItemDao = jdbi.onDemand(CartItemDao.class);
    }

    public CartDB getOrCreateCart(Integer userId) {
        CartDB cart = cartDao.getActiveCartByUserId(userId);
        if (cart == null) {
            cartDao.createCart(userId, null);
            cart = cartDao.getActiveCartByUserId(userId);
        }
        return cart;
    }
    public void addToCart(
            Integer userId,
            Integer productId,
            Integer optionId,
            Integer quantity,
            Integer price
    ) {

        CartDB cart = getOrCreateCart(userId);

        CartItem existing = cartItemDao.getItem(
                cart.getId(),
                productId,
                optionId
        );

        if (existing != null) {

            int newQty = existing.getQuantity() + quantity;

            cartItemDao.updateQuantity(
                    cart.getId(),
                    productId,
                    optionId,
                    newQty
            );

        } else {

            cartItemDao.addItem(
                    cart.getId(),
                    productId,
                    optionId,
                    quantity,
                    price
            );
        }
    }

    public void updateQuantity(
            Integer userId,
            Integer productId,
            Integer optionId,
            Integer quantity
    ) {

        CartDB cart = cartDao.getActiveCartByUserId(userId);

        if (cart == null) return;

        cartItemDao.updateQuantity(
                cart.getId(),
                productId,
                optionId,
                quantity
        );
    }

    public void removeItem(
            Integer userId,
            Integer productId,
            Integer optionId
    ) {

        CartDB cart = cartDao.getActiveCartByUserId(userId);

        if (cart == null) return;

        cartItemDao.deleteItem(
                cart.getId(),
                productId,
                optionId
        );
    }

    public void clearCart(Integer userId) {

        CartDB cart = cartDao.getActiveCartByUserId(userId);

        if (cart == null) return;

        cartItemDao.clearCart(cart.getId());
    }
    public Cart loadCartSessionFromDB(Integer userId) {

        Cart cart = new Cart();
        CartDB dbCart =cartDao.getActiveCartByUserId(userId);
        if (dbCart == null) {
            return cart;
        }
        List<CartItem> dbItems =cartItemDao.getItemsByCartId(dbCart.getId());

        for (CartItem item : dbItems) {
            Product product =
                    productService.getProductByIdAndOptionId(
                            item.getProductId(),
                            item.getOptionId()
                    );

            if (product == null) {
                continue;
            }
            ProductCart productCart =new ProductCart(product);
            productCart.setQuantity(item.getQuantity());
            cart.getData().put(
                    item.getOptionId(),
                    productCart
            );
        }
        return cart;
    }
}
