package hcmuaf.fit.mombabyecommerce.controller.CartController;

import hcmuaf.fit.mombabyecommerce.Dao.CartDao;
import hcmuaf.fit.mombabyecommerce.Dao.CartItemDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.CartDB;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import hcmuaf.fit.mombabyecommerce.model.cart.ProductCart;
import hcmuaf.fit.mombabyecommerce.service.CartDBService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "IncreaseQuantity", value = "/cart/update-quantity")
public class UpdateQuantity extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());
    CartDBService cartDBService = new CartDBService(DBConnection.getJdbi());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý yêu cầu GET ở đây
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.getWriter().write("{\"success\":false,\"message\":\"No session\"}");
                return;
            }

            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                response.getWriter().write("{\"success\":false,\"message\":\"Cart not found\"}");
                return;
            }

            String productIdParam =request.getParameter("productId");
            String optionIdParam =request.getParameter("optionId");
            String quantityParam =request.getParameter("quantity");

            if (productIdParam == null
                    || optionIdParam == null
                    || quantityParam == null) {

                response.getWriter().write("""
                        {"success":false,"message":"Missing params"}
                        """);
                return;
            }

            Integer productId =Integer.parseInt(productIdParam);
            Integer optionId =Integer.parseInt(optionIdParam);
            int quantity =Integer.parseInt(quantityParam);

            if (quantity < 1) {
                response.getWriter().write("""
                        {"success":false,"message":"Số lượng không hợp lệ"}
                        """);
                return;
            }

            if (!cart.getData().containsKey(optionId)) {
                response.getWriter().write("""
                        {"success":false,"message":"Item not found"}
                        """);
                return;
            }
            Product product =productService.getProductByIdAndOptionId(productId, optionId);
            if (product == null) {
                response.getWriter().write("""
                        {"success":false,"message":"Product not found"}
                        """);
                return;
            }

            int currentStock =product.getStock() == null? 0: product.getStock();
            if (currentStock <= 0) {
                response.getWriter().write("""
                        {"success":false,"message":"Sản phẩm đã hết hàng"}
                        """);
                return;
            }

            if (quantity > currentStock) {
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Số lượng vượt quá tồn kho. Trong kho chỉ còn "
                                + currentStock + " sản phẩm\"}"
                );
            response.getWriter().write("{\"success\":true,\"stock\":" + currentStock + ",\"quantity\":" + quantity + "}");
                return;
            }
            ProductCart productCart = cart.getData().get(optionId);
            productCart.setQuantity(quantity);
            productCart.setStock(currentStock);
            cart.getData().put(optionId, productCart);
            session.setAttribute("cart", cart);
            Integer userId =
                    (Integer) session.getAttribute("userId");
            if (userId != null) {
                cartDBService.updateQuantity(
                        userId,
                        productId,
                        optionId,
                        quantity
                );
            }
            response.getWriter().write("{\"success\":true,\"stock\":"+ currentStock + ",\"quantity\":" + quantity + "}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        }

    }
}