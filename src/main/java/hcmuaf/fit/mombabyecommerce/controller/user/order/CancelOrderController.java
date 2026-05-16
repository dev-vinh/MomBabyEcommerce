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
    OrderService orderSerivce = new OrderService(DBConnection.getJdbi());
    GHNApiCaller apiCaller = new GHNApiCaller();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Integer userId = (Integer) session.getAttribute("userId");
        Integer orderId = Integer.parseInt(request.getParameter("orderId"));

        if (userId == null) {
            throw new ServletException("User not found");
        }

        Order order = orderSerivce.getOrderById(orderId);
        if (order == null || order.getUserId() != userId) {
            throw new ServletException("Order not found");
        }

        Gson gson = new Gson();
        String json = gson.toJson(new GHNCancelOrderRequest(List.of(order.getShippingId())));
        String GHNResponse = apiCaller.cancelOrder(json);

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        APIResponse apiResponse = mapper.readValue(GHNResponse, APIResponse.class);

        if (apiResponse.getCode() == 200) {
            orderSerivce.updateStatus(orderId, OrderStatus.CANCELLED);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true, \"message\": \"Order cancelled successfully\"}");

        }

        if (apiResponse.getCode() != 200) {
            orderSerivce.updateStatus(order.getId(), OrderStatus.CANCEL_ERROR);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Order cancel error. Please try again later.\"}");

        }




    }

}
