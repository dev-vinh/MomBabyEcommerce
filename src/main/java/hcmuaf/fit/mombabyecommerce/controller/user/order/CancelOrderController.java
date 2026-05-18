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

    private final OrderService orderService =
            new OrderService(DBConnection.getJdbi());

    private final GHNApiCaller apiCaller = new GHNApiCaller();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            HttpSession session = request.getSession();

            Integer userId = (Integer) session.getAttribute("userId");

        try {
            if (order.getShippingId().startsWith("GHN")) {

                orderService.updateStatus(orderId, OrderStatus.CANCELLED);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write("""
        {
            "success": true,
            "message": "Mock cancel success"
        }
        """);

                return;
            }

            Gson gson = new Gson();
            String json = gson.toJson(
                    new GHNCancelOrderRequest(
                            List.of(order.getShippingId())
                    )
            );
            String ghnResponse = apiCaller.cancelOrder(json);

            ObjectMapper mapper = new ObjectMapper();

            APIResponse apiResponse =
                    mapper.readValue(ghnResponse, APIResponse.class);

            if (apiResponse.getCode() == 200) {

                orderService.updateStatus(orderId,
                        OrderStatus.CANCELLED);

                sendSuccess(response,
                        "Order cancelled successfully");

                response.getWriter().write("""
                {
                    "success": true,
                    "message": "Order cancelled successfully"
                }
                """);

                return;
            }

            orderService.updateStatus(orderId,
                    OrderStatus.CANCEL_ERROR);

            sendError(response,
                    HttpServletResponse.SC_BAD_GATEWAY,
                    "Order cancel error. Please try again later.");

        } catch (NumberFormatException e) {

            response.getWriter().write("""
            {
                "success": false,
                "message": "Order cancel error. Please try again later."
            }
            """);

        } catch (Exception e) {

            e.printStackTrace();

            sendError(response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error");
        }
    }

    private boolean cannotCancel(OrderStatus status) {

        return status == OrderStatus.DELIVERED
                || status == OrderStatus.CANCELLED
                || status == OrderStatus.CANCEL_ERROR
                || status == OrderStatus.SHIPPING
                || status == OrderStatus.DELIVERY;
    }

    private void sendSuccess(HttpServletResponse response,
                             String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().write("""
                {
                    "success": true,
                    "message": "%s"
                }
                """.formatted(message));
    }

    private void sendError(HttpServletResponse response,
                           int status,
                           String message) throws IOException {

        response.setStatus(status);

        response.getWriter().write("""
                {
                    "success": false,
                    "message": "%s"
                }
                """.formatted(message));
    }
}