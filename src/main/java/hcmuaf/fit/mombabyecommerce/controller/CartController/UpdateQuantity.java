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
        HttpSession session = request.getSession();
        if (session != null) {
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart != null) {
                Integer optionId = Integer.parseInt(request.getParameter("optionId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                if (cart.getData().containsKey(optionId)) {
                    ProductCart productCart = cart.getData().get(optionId);
                    productCart.setQuantity(quantity);
                    cart.getData().put(optionId, productCart);
                }
            }

        }

    }
}