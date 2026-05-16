package hcmuaf.fit.mombabyecommerce.controller.CartController;

import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "Remove", value = "/cart/remove")
public class Remove extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            HttpSession session = request.getSession(false);

            if (session == null) {
                writeJson(response, false, "Session không tồn tại");
                return;
            }

            Cart cart = (Cart) session.getAttribute("cart");

            if (cart == null) {
                writeJson(response, false, "Giỏ hàng không tồn tại");
                return;
            }

            String optionIdParam = request.getParameter("optionId");

            if (optionIdParam == null || optionIdParam.trim().isEmpty()) {
                writeJson(response, false, "Thiếu optionId");
                return;
            }

            Integer optionId = Integer.parseInt(optionIdParam);

            if (!cart.getData().containsKey(optionId)) {
                writeJson(response, false, "Sản phẩm không tồn tại trong giỏ hàng");
                return;
            }

            cart.getData().remove(optionId);
            session.setAttribute("cart", cart);

            writeJson(response, true, "Đã xóa sản phẩm khỏi giỏ hàng");

        } catch (NumberFormatException e) {
            writeJson(response, false, "optionId không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            writeJson(response, false, "Lỗi server khi xóa sản phẩm");
        }
    }

    private void writeJson(HttpServletResponse response, boolean success, String message)
            throws IOException {
        response.getWriter().write(
                "{\"success\":" + success + ",\"message\":\"" + message + "\"}"
        );
    }
}