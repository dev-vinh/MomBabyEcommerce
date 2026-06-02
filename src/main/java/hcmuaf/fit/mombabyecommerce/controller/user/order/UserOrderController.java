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
        List<Order> orders = new ArrayList<>(); // 1. Khai báo sẵn danh sách rỗng
        int roundedSavings = 0;

        Object userIdObj = session.getAttribute("userId");

        if (userIdObj != null) {
            try {
                Integer userId = Integer.parseInt(userIdObj.toString());

                String status = request.getParameter("status");
                if (status != null && !status.isEmpty()) {
                    orders = orderService.getOrdersByUserIdAndStatus(userId, status);
                } else {
                    orders = orderService.getOrdersByUserId(userId);
                }
                System.out.println("orders"+orders);

                int savings = orders.stream().mapToInt(Order::getTotal).sum();
                roundedSavings = Math.round(savings / 1_000_000f);

                System.out.println("DEBUG: Đã lấy được đơn hàng cho User ID: " + userId + " - Số lượng: " + orders.size());
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: userId trong session không phải là số hợp lệ!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Lỗi: userId trong session bị NULL (chưa đăng nhập?)");
        }

        request.setAttribute("count", orders.size());
        request.setAttribute("orders", orders);
        request.setAttribute("savings", roundedSavings);

        request.getRequestDispatcher("user/user-order.jsp").forward(request, response);
    }
    }

