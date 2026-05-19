package hcmuaf.fit.mombabyecommerce.controller.CartController;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import hcmuaf.fit.mombabyecommerce.service.CartDBService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AddToCart", value = "/add-cart")
public class AddToCart extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());
    CartDBService cartDBService = new CartDBService(DBConnection.getJdbi());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            Integer productId = Integer.parseInt(request.getParameter("productId"));
            String optionParam = request.getParameter("optionId");
            if (optionParam == null || optionParam.isEmpty()) {
                response.getWriter().write("{\"success\": false, \"message\": \"OptionId required\"}");
                return;
            }
            Integer optionId = Integer.parseInt(optionParam);
            String qtyParam = request.getParameter("quantity");
            int quantity = 1;

            if (qtyParam != null && !qtyParam.isEmpty()) {
                try {
                    quantity = Integer.parseInt(qtyParam);
                } catch (NumberFormatException e) {
                    quantity = 1;
                }
            }

            Product product = productService.getProductByIdAndOptionId(productId, optionId);

            if (product == null) {
                System.err.println(
                        "[AddToCart] ERROR: Product not found for productId: " + productId + ", optionId: " + optionId);
                response.getWriter().write("{\"success\": false, \"message\": \"Product not found\"}");
                return;
            }
            if (product.getStock() == null || product.getStock() <= 0) {
                response.getWriter().write("{\"success\": false, \"message\": \"Sản phẩm đã hết hàng\"}");
                return;
            }

            if (quantity > product.getStock()) {
                response.getWriter().write("{\"success\": false, \"message\": \"Số lượng vượt quá tồn kho. Trong kho chỉ còn "
                        + product.getStock() + " sản phẩm\"}");
                return;
            }
            System.out.println("[AddToCart] Product retrieved: " + product.getName() +
                    ", optionId in product: " + product.getOptionId());

            if (product.getOptionId() == null) {
                System.err.println("[AddToCart] ERROR: Product optionId is NULL!");
                response.getWriter().write("{\"success\": false, \"message\": \"Product optionId is null\"}");
                return;
            }
            if (quantity < 1) {
                response.getWriter().write("{\"success\": false, \"message\": \"Số lượng không hợp lệ\"}");
                return;
            }

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);
            }


            if (cart.getData().containsKey(optionId)) {
                int currentQty = cart.getData().get(optionId).getQuantity();
                if (currentQty + quantity > product.getStock()) {
                    response.getWriter().write("{\"success\": false, \"message\": \"Số lượng trong giỏ vượt quá tồn kho. Trong kho chỉ còn "
                            + product.getStock() + " sản phẩm\"}");
                    return;
                }
            }
            boolean added = cart.addProduct(product, quantity);

            if (!added) {
                response.getWriter().write("{\"success\": false, \"message\": \"Add failed\"}");
                return;
            }
            session.setAttribute("cart", cart);

            Integer userId = (Integer) session.getAttribute("userId");

            if (userId != null) {
                cartDBService.addToCart(
                        userId,
                        productId,
                        optionId,
                        quantity,
                        product.getPrice()
                );
            }
            response.getWriter().write("{\"success\": true}");
        } catch (Exception e) {
            System.err.println("[AddToCart] EXCEPTION: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
