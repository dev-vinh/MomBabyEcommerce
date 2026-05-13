package hcmuaf.fit.mombabyecommerce.controller.CartController;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import hcmuaf.fit.mombabyecommerce.model.cart.ProductCart;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartDetail", value = "/cart")
public class CartDetail extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        for (ProductCart item : cart.getProducts()) {
            Product dbProduct = productService.getProductByIdAndOptionId(item.getProductId(), item.getOptionId());
            int currentStock = dbProduct == null || dbProduct.getStock() == null ? 0 : dbProduct.getStock();

            item.setStock(currentStock);

            if (currentStock > 0 && item.getQuantity() != null && item.getQuantity() > currentStock) {
                item.setQuantity(currentStock);
            }

            cart.getData().put(item.getOptionId(), item);
        }

        session.setAttribute("cart", cart);

        List<ProductCart> productCarts = cart.getProducts();
        request.setAttribute("productCarts", productCarts);

        request.getRequestDispatcher("/cart/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý yêu cầu POST ở đây
    }
}