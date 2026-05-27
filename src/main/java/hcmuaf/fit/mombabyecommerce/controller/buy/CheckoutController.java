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
import hcmuaf.fit.mombabyecommerce.util.VNPAYConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static hcmuaf.fit.mombabyecommerce.connection.DBConnection.jdbi;

@WebServlet(name = "Checkout", value = "/checkout")
public class CheckoutController extends HttpServlet {
    OrderService orderService = new OrderService(DBConnection.getJdbi());
    OrderDetailService orderDetailService = new OrderDetailService(DBConnection.getJdbi());
    CardService cardService = new CardService(DBConnection.getJdbi());
    AddressService addressService = new AddressService(DBConnection.getJdbi());
    ProductService productService = new ProductService(DBConnection.getJdbi());
    UserService userService = new UserService(DBConnection.getJdbi());
    private int codAmount = 0;
    private StringBuilder content = new StringBuilder();
    private List<GHNItem> items = new ArrayList<>();


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
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getProducts().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        List<ProductCart> buyNowList = (List<ProductCart>)
                session.getAttribute("buyNowList");
        if (buyNowList == null || buyNowList.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
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
        List<Address> addressList = addressService.findByUserId(userId);
        if (addressList == null || addressList.isEmpty()) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/user-address?requireAddress=true"
            );
            return;
        }
        List<Card> cardList = cardService.getCartByUserId(userId);
        request.setAttribute("productList", productList);
        request.setAttribute("addressList", addressList);
        request.setAttribute("cardList", cardList);
        request.getRequestDispatcher("/Checkout/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            if (userId == null) {
                response.getWriter().write("""
                        {"success":false,"message":"Vui lòng đăng nhập"}
                        """);
                return;
            }
            Cart cart = (Cart) session.getAttribute("cart");
//            if (cart == null || cart.getData().isEmpty()) {
//                response.getWriter().write("""
//                        {"success":false,"message":"Giỏ hàng trống"}
//                        """);
//
//                return;
//            }
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            int addressId = jsonObject.get("address_id").getAsInt();
            String paymentMethod = jsonObject.get("paymentMethod").getAsString();
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
                order.setPaymentStatus(PaymentStatus.PENDING
                );
            } else {
                // VNPAY
                order.setCOD(false);
                order.setPaymentStatus(PaymentStatus.PENDING);
            }
            Integer orderId = orderService.addOrder(order);
            if (orderId == null) {
                throw new RuntimeException("Không thể tạo đơn hàng");
            }
            order.setId(orderId);
            codAmount = shippingFee;
            boolean flag = true;
            for (JsonElement element : products) {
                JsonObject p = element.getAsJsonObject();
                int productId = p.get("id").getAsInt();
                int optionId = p.get("optionId").getAsInt();
                int quantity = p.get("quantity").getAsInt();
                Product product = productService.getProductByIdAndOptionId(productId, optionId);

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
                boolean updated = productService.updateStock(optionId, quantity);

                if (!updated) {
                    throw new RuntimeException(
                            "Không thể cập nhật tồn kho"
                    );
                }
                codAmount += product.getPrice() * quantity;
                GHNItem item = new GHNItem(product, quantity);
                items.add(item);
                OrderDetail od = new OrderDetail();
                od.setOrderId(orderId);
                od.setProductId(productId);
                od.setOptionId(optionId);
                od.setQuantity(quantity);
                od.setTotal(product.getPrice() * quantity);

                flag &= orderDetailService.addOrderDetail(od);
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
                            new TypeReference<>() {
                            }
                    );
            if (apiResponse.getCode() == 200 && apiResponse.getData() != null
                    && apiResponse.getData().getOrder_code() != null
                    && !apiResponse.getData().getOrder_code().isBlank()) {

                orderService.updateShippingId(
                        orderId,
                        apiResponse.getData().getOrder_code()
                );
                orderService.updateStatus(
                        orderId,
                        OrderStatus.PENDING
                );
            }else {
                orderService.updateShippingId(
                        orderId,
                        "DHM_" + System.currentTimeMillis()
                );
                orderService.updateStatus(
                        orderId,
                        OrderStatus.PENDING
                );
            }

            if (flag) {
                if (paymentMethod.equals("COD")) {
                    response.getWriter().write("""
                                {
                                    "success":true,
                                    "message":"Đặt hàng thành công"
                                }
                            """);
                } else {
                    long amount =
                            (long) codAmount * 100;
                    Map<String, String> vnp_Params =
                            new HashMap<>();

                    vnp_Params.put("vnp_Version", "2.1.0");

                    vnp_Params.put("vnp_Command", "pay");

                    vnp_Params.put("vnp_TmnCode", VNPAYConfig.vnp_TmnCode);

                    vnp_Params.put("vnp_Amount", String.valueOf(amount));

                    vnp_Params.put(
                            "vnp_CurrCode",
                            "VND"
                    );

                    String txnRef =
                            String.valueOf(orderId);

                    vnp_Params.put(
                            "vnp_TxnRef",
                            txnRef
                    );

                    vnp_Params.put(
                            "vnp_OrderInfo",
                            "Thanh toan don hang "
                                    + orderId
                    );

                    vnp_Params.put(
                            "vnp_OrderType",
                            "other"
                    );

                    vnp_Params.put(
                            "vnp_Locale",
                            "vn"
                    );

                    vnp_Params.put(
                            "vnp_ReturnUrl",
                            VNPAYConfig.vnp_ReturnUrl
                    );

                    vnp_Params.put(
                            "vnp_IpAddr",
                            VNPAYConfig.getIpAddress(request)
                    );

                    Calendar cld =
                            Calendar.getInstance(
                                    TimeZone.getTimeZone(
                                            "Etc/GMT+7"
                                    )
                            );

                    SimpleDateFormat formatter =
                            new SimpleDateFormat(
                                    "yyyyMMddHHmmss"
                            );

                    String createDate =
                            formatter.format(
                                    cld.getTime()
                            );

                    vnp_Params.put(
                            "vnp_CreateDate",
                            createDate
                    );

                    List<String> fieldNames =
                            new ArrayList<>(
                                    vnp_Params.keySet()
                            );

                    Collections.sort(fieldNames);

                    StringBuilder hashData =
                            new StringBuilder();

                    StringBuilder query =
                            new StringBuilder();

                    for (String fieldName : fieldNames) {
                        String fieldValue =
                                vnp_Params.get(fieldName);

                        if (fieldValue != null
                                && fieldValue.length() > 0) {

                            hashData.append(fieldName);
                            hashData.append('=');

                            hashData.append(
                                    URLEncoder.encode(
                                            fieldValue,
                                            StandardCharsets.UTF_8
                                    )
                            );

                            query.append(
                                    URLEncoder.encode(
                                            fieldName,
                                            StandardCharsets.UTF_8
                                    )
                            );

                            query.append('=');

                            query.append(
                                    URLEncoder.encode(
                                            fieldValue,
                                            StandardCharsets.UTF_8
                                    )
                            );
                            query.append('&');
                            hashData.append('&');
                        }
                    }
                    hashData.setLength(hashData.length() - 1);
                    query.setLength(query.length() - 1);
                    String queryUrl = query.toString();

                    String secureHash = VNPAYConfig.hmacSHA512(
                            VNPAYConfig.secretKey,
                            hashData.toString());

                    queryUrl += "&vnp_SecureHash=" + secureHash;

                    String paymentUrl =
                            VNPAYConfig.vnp_PayUrl
                                    + "?"
                                    + queryUrl;

                    JsonObject result =
                            new JsonObject();

                    result.addProperty(
                            "success",
                            true
                    );

                    result.addProperty(
                            "paymentUrl",
                            paymentUrl
                    );

                    response.getWriter().write(
                            result.toString()
                    );
                }

            } else {

                response.getWriter().write("""
                            {
                                "success":false,
                                "message":"Đặt hàng thất bại"
                            }
                        """);
            }

        } catch (Exception e) {

            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("success", false);

            error.addProperty("message", e.getMessage());
            response.getWriter().write(error.toString());
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
