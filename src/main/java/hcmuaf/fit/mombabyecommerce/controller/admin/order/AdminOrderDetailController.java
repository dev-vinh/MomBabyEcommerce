package hcmuaf.fit.mombabyecommerce.controller.admin.order;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Order;
import hcmuaf.fit.mombabyecommerce.model.OrderDetail;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.OrderDetailService;
import hcmuaf.fit.mombabyecommerce.service.OrderService;
import hcmuaf.fit.mombabyecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminOrderDetailController", value = "/admin/order-detail")
public class AdminOrderDetailController extends HttpServlet {
    OrderDetailService orderDetailService = new OrderDetailService(DBConnection.getJdbi());
    OrderService orderService = new OrderService(DBConnection.getJdbi());
    UserService userService = new UserService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderService.getOrderById(orderId);

        if (order.getId() != null) {
            request.setAttribute("order", order);
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailByOrderId(order.getId());
            request.setAttribute("orderDetails", orderDetails);
            User user = userService.getUserById(order.getUserId());
            request.setAttribute("user", user);


        }

        request.getRequestDispatcher("orderDetail.jsp").forward(request, response);
    }

}
