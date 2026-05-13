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

@WebServlet(name = "IncreaseQuantity", value = "/cart/update-quantity")
public class UpdateQuantity extends HttpServlet {
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

            Integer optionId = Integer.parseInt(request.getParameter("optionId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            if (quantity < 1) {
                response.getWriter().write("{\"success\":false,\"message\":\"Số lượng không hợp lệ\"}");
                return;
            }
            if (!cart.getData().containsKey(optionId)) {
                response.getWriter().write("{\"success\":false,\"message\":\"Item not found\"}");
                return;
            }

            ProductCart productCart = cart.getData().get(optionId);
            Product product = productService.getProductByIdAndOptionId(productCart.getProductId(), optionId);
            int currentStock = product == null || product.getStock() == null ? 0 : product.getStock();
            if (currentStock <= 0) {
                response.getWriter().write("{\"success\":false,\"message\":\"Sản phẩm đã hết hàng\"}");
                return;
            }
            if (quantity > currentStock) {
                productCart.setStock(currentStock);
                response.getWriter().write("{\"success\":false,\"message\":\"Số lượng vượt quá tồn kho. Trong kho chỉ còn "
                        + currentStock + " sản phẩm\"}");
                return;
            }

            productCart.setQuantity(quantity);
            productCart.setStock(currentStock);
            cart.getData().put(optionId, productCart);
            session.setAttribute("cart", cart);

            response.getWriter().write("{\"success\":true,\"stock\":" + currentStock + ",\"quantity\":" + quantity + "}");

        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        }

    }
}