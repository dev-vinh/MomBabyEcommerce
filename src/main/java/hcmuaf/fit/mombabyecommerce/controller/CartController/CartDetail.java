package hcmuaf.fit.mombabyecommerce.controller.CartController;

import hcmuaf.fit.mombabyecommerce.Dao.CartDao;
import hcmuaf.fit.mombabyecommerce.Dao.CartItemDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.CartDB;
import hcmuaf.fit.mombabyecommerce.model.CartItem;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import hcmuaf.fit.mombabyecommerce.model.cart.ProductCart;
import hcmuaf.fit.mombabyecommerce.service.CartDBService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartDetail", value = "/cart")
public class CartDetail extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());
    CartDBService cartDBService = new CartDBService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId =(Integer) session.getAttribute("userId");
        Cart cart =(Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        if (userId != null) {
            cart = cartDBService.loadCartSessionFromDB(userId);
            session.setAttribute("cart", cart);
        }
        for (ProductCart item : cart.getProducts()) {
            Product dbProduct =
                    productService.getProductByIdAndOptionId(
                            item.getProductId(),
                            item.getOptionId()
                    );
            int currentStock =
                    dbProduct == null || dbProduct.getStock() == null
                            ? 0
                            : dbProduct.getStock();

            item.setStock(currentStock);
            if (dbProduct != null) {

                item.setPrice(dbProduct.getPrice());
                item.setName(dbProduct.getName());
                item.setImageUrl(dbProduct.getImageUrl());
                item.setVariantText(dbProduct.getVariantText());
            }
            if (currentStock > 0
                    && item.getQuantity() != null
                    && item.getQuantity() > currentStock) {

                item.setQuantity(currentStock);
                if (userId != null) {

                    cartDBService.updateQuantity(
                            userId,
                            item.getProductId(),
                            item.getOptionId(),
                            currentStock
                    );
                }
            }
            cart.getData().put(
                    item.getOptionId(),
                    item
            );
        }

        session.setAttribute("cart", cart);

        List<ProductCart> productCarts =
                cart.getProducts();

        request.setAttribute(
                "productCarts",
                productCarts
        );
        request.getRequestDispatcher("/cart/cart.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}