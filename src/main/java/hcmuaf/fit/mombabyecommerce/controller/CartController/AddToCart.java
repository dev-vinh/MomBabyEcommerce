package hcmuaf.fit.mombabyecommerce.controller.CartController;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AddToCart", value = "/add-cart")
public class AddToCart extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý yêu cầu GET ở đây
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        try {
            Integer productId = Integer.parseInt(request.getParameter("productId"));
            Integer optionId = Integer.parseInt(request.getParameter("optionId"));

            System.out.println("[AddToCart] Received request - productId: " + productId + ", optionId: " + optionId);

            Product product = productService.getProductByIdAndOptionId(productId, optionId);

            if (product == null) {
                System.err.println(
                        "[AddToCart] ERROR: Product not found for productId: " + productId + ", optionId: " + optionId);
                response.getWriter().write("{\"success\": false, \"message\": \"Product not found\"}");
                return;
            }

            System.out.println("[AddToCart] Product retrieved: " + product.getName() +
                    ", optionId in product: " + product.getOptionId());

            if (product.getOptionId() == null) {
                System.err.println("[AddToCart] ERROR: Product optionId is NULL!");
                response.getWriter().write("{\"success\": false, \"message\": \"Product optionId is null\"}");
                return;
            }

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);
                System.out.println("[AddToCart] Created new cart in session");
            } else {
                System.out.println(
                        "[AddToCart] Retrieved existing cart from session with " + cart.getData().size() + " items");
            }

            boolean added = cart.addProduct(product);

            if (added) {
                // Explicitly save cart back to session to ensure persistence
                session.setAttribute("cart", cart);
                System.out.println("[AddToCart] SUCCESS: Product added to cart. Cart now has " +
                        cart.getData().size() + " items");
                System.out.println("[AddToCart] Cart saved to session. Session ID: " + session.getId());
                System.out.println("[AddToCart] Cart contents (optionIds): " + cart.getData().keySet());
                response.getWriter().write("{\"success\": true}");
            } else {
                System.err.println("[AddToCart] ERROR: Failed to add product to cart");
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to add to cart\"}");
            }

        } catch (Exception e) {
            System.err.println("[AddToCart] EXCEPTION: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
