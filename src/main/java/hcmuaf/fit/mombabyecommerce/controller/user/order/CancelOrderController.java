package hcmuaf.fit.mombabyecommerce.controller.user.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.contant.OrderStatus;
import hcmuaf.fit.mombabyecommerce.controller.GHNApiCaller;
import hcmuaf.fit.mombabyecommerce.model.Order;
import hcmuaf.fit.mombabyecommerce.request.GHNCancelOrderRequest;
import hcmuaf.fit.mombabyecommerce.response.APIResponse;
import hcmuaf.fit.mombabyecommerce.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
@WebServlet(name = "CancelOrderController", value = "/cancel-order")
public class CancelOrderController extends HttpServlet {
    OrderService orderService = new OrderService(DBConnection.getJdbi());
    GHNApiCaller apiCaller = new GHNApiCaller();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        Integer orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderService.getOrderById(orderId);

        if (order == null || !order.getUserId().equals(userId)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }

        if (order.getShippingId() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Shipping ID not found");
            return;
        }
        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Delivered order cannot be cancelled");
            return;
        }

        try {

            Gson gson = new Gson();

            String json = gson.toJson(
                    new GHNCancelOrderRequest(List.of(order.getShippingId()))
            );

            String ghnResponse = apiCaller.cancelOrder(json);

            ObjectMapper mapper = new ObjectMapper();

            APIResponse apiResponse =
                    mapper.readValue(ghnResponse, APIResponse.class);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (apiResponse.getCode() == 200) {

                orderService.updateStatus(orderId, OrderStatus.CANCELLED);

                response.setStatus(HttpServletResponse.SC_OK);

                response.getWriter().write("""
                        {
                            "success": true,
                            "message": "Order cancelled successfully"
                        }
                        """);
                return;
            }

            orderService.updateStatus(orderId, OrderStatus.CANCEL_ERROR);

            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);

            response.getWriter().write("""
                    {
                        "success": false,
                        "message": "Order cancel error. Please try again later."
                    }
                    """);

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error"
            );
        }
    }

}
