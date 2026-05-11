package hcmuaf.fit.mombabyecommerce.controller.user.order;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Order;
import hcmuaf.fit.mombabyecommerce.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserOrderController", value = "/user-order")
public class UserOrderController extends HttpServlet {
    OrderService orderService = new OrderService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        List<Order> orders = new ArrayList<>();
        try {

            orders = orderService.getOrdersByUserId(userId);
            int savings = orders.stream().mapToInt(Order::getTotal).sum();
            int roundedSavings = Math.round(savings / 1_000_000f);


            request.setAttribute("count", orders.size());
            request.setAttribute("orders", orders);
            request.setAttribute("savings", roundedSavings);

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("user/user-order.jsp").forward(request, response);
    }
    }

