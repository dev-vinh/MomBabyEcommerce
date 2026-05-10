package hcmuaf.fit.mombabyecommerce.controller.CartController;

import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import hcmuaf.fit.mombabyecommerce.model.cart.ProductCart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "IncreaseQuantity", value = "/cart/update-quantity")
public class UpdateQuantity extends HttpServlet {

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

            if (!cart.getData().containsKey(optionId)) {
                response.getWriter().write("{\"success\":false,\"message\":\"Item not found\"}");
                return;
            }

            ProductCart productCart = cart.getData().get(optionId);

            if (quantity < 1) {
                response.getWriter().write("{\"success\":false,\"message\":\"Quantity invalid\"}");
                return;
            }

            if (productCart.getStock() != null && quantity > productCart.getStock()) {
                response.getWriter().write("{\"success\":false,\"message\":\"Quantity exceeds stock\"}");
                return;
            }

            productCart.setQuantity(quantity);
            cart.getData().put(optionId, productCart);
            session.setAttribute("cart", cart);

            response.getWriter().write("{\"success\":true}");

        } catch (Exception e) {
            response.getWriter().write("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        }

    }
}