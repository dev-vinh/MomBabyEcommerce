package hcmuaf.fit.mombabyecommerce.controller.buy;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Address;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.cart.ProductCart;
import hcmuaf.fit.mombabyecommerce.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hcmuaf.fit.mombabyecommerce.model.Card;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@WebServlet(name = "BuyNowController", value = "/buy-now")
public class BuyNowController extends HttpServlet {
    ProductService productService = new ProductService(DBConnection.getJdbi());
    CardService cardService = new CardService(DBConnection.getJdbi());
    AddressService addressService = new AddressService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<ProductCart> buyNowList = (List<ProductCart>) session.getAttribute("buyNowList");
        if (buyNowList == null || buyNowList.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        List<Address> addressList = new ArrayList<>();
        if (userId != null) {
            addressList = addressService.findByUserId(userId);
        }

        request.setAttribute("productList", buyNowList);
        request.setAttribute("addressList", addressList);
        request.getRequestDispatcher("/Checkout/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            // read json
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {sb.append(line);}
            JSONObject json = new JSONObject(sb.toString());
            int productId = json.getInt("productId");
            int optionId = json.getInt("optionId");
            int quantity = json.getInt("quantity");

            // lấy sản phẩm
            Product product = productService.getProductByIdAndOptionId(productId, optionId);
            if (product == null) {
                JSONObject error = new JSONObject();
                error.put("success", false);

                error.put(
                        "message",
                        "Sản phẩm không tồn tại");
                response.getWriter().write(error.toString());
                return;
            }

            // CHECK STOCK
            if (quantity > product.getStock()) {
                JSONObject error = new JSONObject();
                error.put("success", false);

                error.put("message", "Số lượng vượt quá tồn kho");
                response.getWriter().write(error.toString());
                return;
            }
            // LƯU BUY NOW SESSION
            ProductCart productCart = new ProductCart(product);
            productCart.setQuantity(quantity);
            List<ProductCart> buyNowList = new ArrayList<>();
            buyNowList.add(productCart);
            HttpSession session = request.getSession();

            session.setAttribute("buyNowList", buyNowList);

            JSONObject success = new JSONObject();
            success.put("success", true);
            success.put("redirectUrl", request.getContextPath() + "/buy-now");
            response.getWriter().write(success.toString());
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("success", false);
            error.put("message", "Có lỗi xảy ra");
            response.getWriter().write(error.toString());
        }
    }

}
