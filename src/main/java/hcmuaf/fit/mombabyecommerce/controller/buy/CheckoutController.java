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
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("[CheckOut] UserId from session: " + userId);

        List<ProductCart> productList = new ArrayList<>();
        List<Address> addressList = new ArrayList<>();
        List<Card> cardList = new ArrayList<>();

        Cart cart = (Cart) session.getAttribute("cart");
        System.out.println("[CheckOut] Cart from session: " + (cart != null ? "Found" : "NULL"));

        if (cart == null) {
            cart = new Cart();
            System.out.println("[CheckOut] Created new empty cart");
        } else {
            System.out.println("[CheckOut] Cart has " + cart.getData().size() + " items");
            System.out.println("[CheckOut] Cart keys (optionIds): " + cart.getData().keySet());
        }

        String optionIdParam = request.getParameter("optionIds");
        System.out.println("[CheckOut] Received optionIds parameter: " + optionIdParam);

        if (optionIdParam != null && !optionIdParam.trim().isEmpty()) {
            String[] arrOptions = optionIdParam.split(",");
            System.out.println("[CheckOut] Split into " + arrOptions.length + " option IDs");

            for (String optionIdStr : arrOptions) {
                try {
                    int optionId = Integer.parseInt(optionIdStr.trim());
                    System.out.println("[CheckOut] Looking for optionId: " + optionId);

                    if (cart.getData().containsKey(optionId)) {
                        ProductCart pc = cart.getData().get(optionId);
                        productList.add(pc);
                        System.out
                                .println("[CheckOut] Added product: " + pc.getName() + " (optionId: " + optionId + ")");
                    } else {
                        System.err.println("[CheckOut] ERROR: optionId " + optionId + " NOT FOUND in cart!");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("[CheckOut] ERROR: Invalid optionId format: " + optionIdStr);
                }
            }
        } else {
            System.out.println("[CheckOut] No optionIds parameter provided");
        }

        System.out.println("[CheckOut] Final productList size: " + productList.size());
        if (productList.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        // Only fetch addresses and cards if user is logged in
        if (userId != null) {
            try {
                addressList = addressService.findByUserId(userId);
                System.out.println("[CheckOut] Found " + (addressList != null ? addressList.size() : 0)
                        + " addresses for userId: " + userId);
            } catch (Exception e) {
                System.err.println("[CheckOut] ERROR fetching addresses: " + e.getMessage());
                e.printStackTrace();
                addressList = new ArrayList<>();
            }

            try {
                cardList = cardService.getCartByUserId(userId);
                System.out.println("[CheckOut] Found " + (cardList != null ? cardList.size() : 0)
                        + " cards for userId: " + userId);
            } catch (Exception e) {
                System.err.println("[CheckOut] ERROR fetching cards: " + e.getMessage());
                e.printStackTrace();
                cardList = new ArrayList<>();
            }
        } else {
            System.out.println(
                    "[CheckOut] WARNING: userId is NULL - user not logged in. Addresses and cards will be empty.");
        }

        request.setAttribute("productList", productList);
        request.setAttribute("addressList", addressList);
        request.setAttribute("cardList", cardList);

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("checkout/checkout.jsp").forward(request, response);

    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        String line;
//        BufferedReader reader = request.getReader();
//        Boolean flag = false;
//
//        while ((line = reader.readLine()) != null) {
//            stringBuilder.append(line);
//
//        }
//
//        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
//        String address = jsonObject.getString("address_id");
//        String card = jsonObject.getString("card");
//        JSONArray products = jsonObject.getJSONArray("products");
//        HttpSession session = request.getSession();
//        Integer userId = (Integer) session.getAttribute("userId");
//        Cart cartFromSession = (Cart) session.getAttribute("cart");
//
//        Order order = new Order();
//        order.setCreateAt(LocalDate.now());
//        order.setPaymentStatus(PaymentStatus.PAID);
//        order.setOrderStatus(DELIVERY);
//        order.setUserId(userId);
//        try {
//            order.setAddressId(Integer.parseInt(address));
//            if (card.equals("COD")) {
//                order.setCOD(true);
//            } else {
//                order.setCardId(Integer.parseInt(card));
//                order.setCOD(false);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Integer orderid = orderService.addOrder(order);
//
//        if (orderid != null) {
//            for (int i = 0; i < products.length(); i++) {
//                JSONObject product = products.getJSONObject(i);
//
//                int productId = product.getInt("id");
//                int quantity = product.getInt("quantity");
//                int total = product.getInt("total");
//                int optionId = product.getInt("optionId");
//
//                OrderDetail od = new OrderDetail();
//                od.setOrderId(orderid);
//                od.setProductId(productId);
//                od.setQuantity(quantity);
//                od.setTotal(total);
//                od.setOptionId(optionId);
//
//                flag = orderDetailService.addOrderDetail(od);
//                if (flag) {
//                    productService.increaseNoOfSold(productId, quantity);
//                    // Clear from cart if present. Cart key is now optionId.
//                    if (cartFromSession != null) {
//                        cartFromSession.delete(optionId);
//                    }
//                }
//            }
//        }
//
//        if (flag) {
//            JSONObject jsonResponse = new JSONObject();
//            jsonResponse.put("success", true);
//            response.getWriter().write(jsonResponse.toString());
//        } else {
//            JSONObject jsonResponse = new JSONObject();
//            jsonResponse.put("success", false);
//            response.getWriter().write(jsonResponse.toString());
//        }
//
//    }
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
