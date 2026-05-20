package hcmuaf.fit.mombabyecommerce.controller.buy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hcmuaf.fit.mombabyecommerce.Dao.CartDao;
import hcmuaf.fit.mombabyecommerce.Dao.CartItemDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.contant.OrderStatus;
import hcmuaf.fit.mombabyecommerce.contant.PaymentStatus;
import hcmuaf.fit.mombabyecommerce.controller.GHNApiCaller;
import hcmuaf.fit.mombabyecommerce.model.*;
import hcmuaf.fit.mombabyecommerce.model.cart.Cart;
import hcmuaf.fit.mombabyecommerce.model.cart.ProductCart;
import hcmuaf.fit.mombabyecommerce.request.GHNCreateOrderRequest;
import hcmuaf.fit.mombabyecommerce.request.GHNItem;
import hcmuaf.fit.mombabyecommerce.response.APIResponse;
import hcmuaf.fit.mombabyecommerce.response.CreateOrderResponse;
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
import java.time.LocalDate;
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
    int codAmount = 0;
    List<GHNItem>items = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }
        Cart cart =(Cart) session.getAttribute("cart");
        if (cart == null || cart.getProducts().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        String optionIdsParam = request.getParameter("optionIds");
        List<ProductCart> productList = new ArrayList<>();
        if (optionIdsParam != null && !optionIdsParam.trim().isEmpty()) {
            String[] optionIds = optionIdsParam.split(",");
            for (String optionIdStr : optionIds) {
                try {
                    Integer optionId = Integer.parseInt(optionIdStr.trim());
                    ProductCart item = cart.getData().get(optionId);
                    if (item != null) {
                        productList.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (productList.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        List<Address> addressList =addressService.findByUserId(userId);
        if (addressList == null || addressList.isEmpty()) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/user-address?requireAddress=true"
            );
            return;
        }
        List<Card> cardList =cardService.getCartByUserId(userId);
        request.setAttribute("productList", productList);
        request.setAttribute("addressList", addressList);
        request.setAttribute("cardList", cardList);
        request.getRequestDispatcher("Checkout/checkout.jsp").forward(request, response);
    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();
    Map<String, Object> jsonResponse = new HashMap<>();

        try {
            HttpSession session =request.getSession();
            Integer userId =(Integer) session.getAttribute("userId");

            if (userId == null) {
                response.getWriter().write("""
                        {"success":false,"message":"Vui lòng đăng nhập"}
                        """);
                return;
            }
            Cart cart =(Cart) session.getAttribute("cart");
            if (cart == null || cart.getProducts().isEmpty()) {
                response.getWriter().write("""
                        {"success":false,"message":"Giỏ hàng trống"}
                        """);

                return;
            }
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            int addressId =jsonObject.get("address_id").getAsInt();
            String paymentMethod = jsonObject.get("payment_method").getAsString();
            int shippingFee = jsonObject.get("ship_fee").getAsInt();
            JsonArray products = jsonObject.getAsJsonArray("products");
            User user = userService.getUserById(userId);

            if (user == null) {
                throw new RuntimeException("User not found");
            }
            Address address = addressService.findById(addressId);

            if (address == null || !address.getUserId().equals(userId)) {
                throw new RuntimeException("Địa chỉ không hợp lệ");
            }
            Order order = new Order();
            order.setCreateAt(LocalDate.now());
            order.setOrderStatus(OrderStatus.PENDING);
            order.setShippingFee(shippingFee);
            order.setUserId(userId);
            order.setAddressId(addressId);

            if (paymentMethod.equals("COD")) {
                order.setCOD(true);
                order.setPaymentStatus(PaymentStatus.PENDING);
            } else {
                order.setCOD(false);
                order.setPaymentStatus(PaymentStatus.PAID);
            }
            Integer orderId =orderService.addOrder(order);
            if (orderId == null) {
                throw new RuntimeException("Không thể tạo đơn hàng");
            }
            order.setId(orderId);
            codAmount = shippingFee;
            boolean flag = false;
            for (JsonElement element : products) {
                JsonObject p =element.getAsJsonObject();
                int productId =p.get("id").getAsInt();
                int optionId =p.get("optionId").getAsInt();
                int quantity =p.get("quantity").getAsInt();
                Product product =productService.getProductByIdAndOptionId(productId, optionId);

                if (product == null) {
                    throw new RuntimeException("Sản phẩm không tồn tại");
                }

                if (product.getStock() == null || product.getStock() <= 0) {

                    throw new RuntimeException(
                            product.getName()
                                    + " đã hết hàng"
                    );
                }

                if (quantity > product.getStock()) {

                    throw new RuntimeException(
                            product.getName()
                                    + " không đủ tồn kho"
                    );
                }
                boolean updated =productService.updateStock(optionId, quantity);

                if (!updated) {
                    throw new RuntimeException(
                            "Không thể cập nhật tồn kho"
                    );
                }
                codAmount +=product.getPrice() * quantity;
                GHNItem item =new GHNItem(product, quantity);
                items.add(item);
                OrderDetail od =new OrderDetail();
                od.setOrderId(orderId);
                od.setProductId(productId);
                od.setOptionId(optionId);
                od.setQuantity(quantity);
                od.setTotal(product.getPrice() * quantity);

                flag =orderDetailService.addOrderDetail(od);
                cart.getData().remove(optionId);
            }
            session.setAttribute("cart", cart);
            if (codAmount > 30000000) {
                codAmount = 29999999;
            }
            GHNCreateOrderRequest ghnRequest =
                    new GHNCreateOrderRequest(
                            address,
                            user,
                            "Đơn hàng MomBaby",
                            paymentMethod.equals("COD")
                                    ? codAmount
                                    : 0,
                            items
                    );
            String ghnResponse = GHNCreateOrder(ghnRequest);
            ObjectMapper mapper =
                    new ObjectMapper();

            APIResponse<CreateOrderResponse> apiResponse =
                    mapper.readValue(
                            ghnResponse,
                            new TypeReference<>() {}
                    );
            if (apiResponse.getCode() == 200) {
                orderService.updateShippingId(
                        orderId,
                        apiResponse.getData().getOrder_code()
                );

            } else {

                orderService.updateStatus(
                        orderId,
                        OrderStatus.ORDER_CREATE_ERROR
                );
            }

            if (flag) {

                response.getWriter().write("""
                        {"success":true,"message":"Đặt hàng thành công"}
                        """);

            } else {

                response.getWriter().write("""
                        {"success":false,"message":"Đặt hàng thất bại"}
                        """);
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().write(
                    "{\"success\":false,\"message\":\""
                            + e.getMessage()
                            + "\"}"
            );
        }
}

    private String GHNCreateOrder(
            GHNCreateOrderRequest request
    ) throws IOException {

        GHNApiCaller apiCaller =
                new GHNApiCaller();

        Gson gson =
                new Gson();

        String json =
                gson.toJson(request);

        return apiCaller.createOrder(json);
    }

}
