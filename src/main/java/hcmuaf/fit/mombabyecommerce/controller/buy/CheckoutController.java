package hcmuaf.fit.mombabyecommerce.controller.buy;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Address;
import hcmuaf.fit.mombabyecommerce.model.Card;
import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import hcmuaf.fit.mombabyecommerce.model.cart.ProductCart;
import hcmuaf.fit.mombabyecommerce.request.GHNItem;
import hcmuaf.fit.mombabyecommerce.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hcmuaf.fit.mombabyecommerce.connection.DBConnection.jdbi;

@WebServlet(name = "Checkout", value = "/checkout")
public class CheckoutController extends HttpServlet {
    OrderService orderService = new OrderService(DBConnection.getJdbi());
    OrderDetailService orderDetailService = new OrderDetailService(DBConnection.getJdbi());
    CardService cardService = new CardService(DBConnection.getJdbi());
    AddressService addressService = new AddressService(DBConnection.getJdbi());
    ProductService productService = new ProductService(DBConnection.getJdbi());
    UserService userService = new UserService(DBConnection.getJdbi());

    private int codAmount;
    private StringBuilder content = new StringBuilder();
    private List<GHNItem>items = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        List<ProductCart> productList = new ArrayList<>();
        List<Address> addressList = new ArrayList<>();
        List<Card> cardList = new ArrayList<>();

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }
        String productParam = request.getParameter("productIds");
        if (productParam != null) {

            String[] arrProducts = productParam.split(",");
            for (String product : arrProducts) {
                if (cart.getData().containsKey(Integer.parseInt(product))) {
                    productList.add(cart.getData().get(Integer.parseInt(product)));
                }
            }

        }
        addressList = addressService.findByUserId(userId);
        if (addressList == null || addressList.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user-address?requireAddress=true");
            return;
        }
        cardList = cardService.getCartByUserId(userId);
        request.setAttribute("productList", productList);
        request.setAttribute("addressList", addressList);
        request.setAttribute("cardList", cardList);
        request.getRequestDispatcher("Checkout/Checkout.jsp").forward(request, response);
    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();
    Gson gson = new Gson();
    Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // 1. Đọc dữ liệu JSON từ Client
            BufferedReader reader = request.getReader();
            JsonObject inputData = gson.fromJson(reader, JsonObject.class);

            int addressId = inputData.get("address_id").getAsInt();
            String paymentMethod = inputData.get("payment_method").getAsString();
            JsonArray products = inputData.getAsJsonArray("products");

            int userId = 1;

            jdbi.useTransaction(handle -> {

                int orderId = handle.createUpdate("INSERT INTO orders (user_id, address_id, payment_method, status, created_at) VALUES (:userId, :addressId, :payment, 'PENDING', NOW())")
                        .bind("userId", userId)
                        .bind("addressId", addressId)
                        .bind("payment", paymentMethod)
                        .executeAndReturnGeneratedKeys()
                        .mapTo(Integer.class)
                        .one();

                for (JsonElement item : products) {
                    JsonObject p = item.getAsJsonObject();
                    int productId = p.get("id").getAsInt();
                    int quantity = p.get("quantity").getAsInt();
                    double price = p.get("price").getAsDouble();

                    JsonElement optionIdElem = p.get("optionId");
                    Integer optionId = (optionIdElem != null && !optionIdElem.isJsonNull() && !optionIdElem.getAsString().isEmpty()) ? optionIdElem.getAsInt() : null;

                    int targetId = (optionId != null) ? optionId : productId;

                    int rowsUpdated = handle.createUpdate("UPDATE products SET stock = stock - :quantity WHERE id = :targetId AND stock >= :quantity")
                            .bind("quantity", quantity)
                            .bind("targetId", targetId)
                            .execute();

                    if (rowsUpdated == 0) {
                        String productName = handle.createQuery("SELECT name FROM products WHERE id = :id")
                                .bind("id", productId)
                                .mapTo(String.class)
                                .findFirst()
                                .orElse("Sản phẩm");

                        throw new RuntimeException("Sản phẩm '" + productName + "' đã hết hàng hoặc không đủ số lượng.");
                    }

                    handle.createUpdate("INSERT INTO order_details (order_id, product_id, option_id, quantity, price) VALUES (:orderId, :productId, :optionId, :quantity, :price)")
                            .bind("orderId", orderId)
                            .bind("productId", productId)
                            .bind("optionId", optionId)
                            .bind("quantity", quantity)
                            .bind("price", price)
                            .execute();
                }

            });

            jsonResponse.put("success", true);
            jsonResponse.put("message", "Thanh toán thành công!");
            out.print(gson.toJson(jsonResponse));

        } catch (RuntimeException e) {
            // Bắt lỗi Hết hàng để thông báo cho khách
            jsonResponse.put("success", false);
            jsonResponse.put("message", e.getMessage());
            out.print(gson.toJson(jsonResponse));
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Đã xảy ra lỗi hệ thống.");
            out.print(gson.toJson(jsonResponse));
        }
    }
}
