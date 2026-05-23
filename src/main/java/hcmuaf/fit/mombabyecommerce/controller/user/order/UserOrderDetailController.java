package hcmuaf.fit.mombabyecommerce.controller.user.order;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.*;
import hcmuaf.fit.mombabyecommerce.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserOrderDetailController", value = "/user-order-detail")
public class UserOrderDetailController extends HttpServlet {
    OrderDetailService orderDetailService = new OrderDetailService(DBConnection.getJdbi());
    OrderService orderService = new OrderService(DBConnection.getJdbi());
    UserService userService = new UserService(DBConnection.getJdbi());
    CardService cardService = new CardService(DBConnection.getJdbi());
    AddressService addressService = new AddressService(DBConnection.getJdbi());
    ReviewService reviewService = new ReviewService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer orderId = Integer.parseInt(request.getParameter("orderId"));
        HttpSession session = request.getSession();
        Integer userId = Integer.parseInt(session.getAttribute("userId").toString());

        User user = userService.getUserById(userId);
        if (user != null) {
            request.setAttribute("user", user);
        }

        Order order = orderService.getOrderByIdAndUserId(orderId,userId);
        if (order != null) {
            request.setAttribute("order", order);

            if (!order.getCOD()){
                Card card = cardService.getCardById(order.getCardId());
                if (card != null) {
                    request.setAttribute("card", card);
                }
            }


            Address address = addressService.findById(order.getAddressId());
            System.out.println("address" +address);
            if (address != null) {
                request.setAttribute("address", address);
            }


        }

        List<OrderDetail> orderDetails = null;
        if (order != null && order.getId() != null) {
            orderDetails = orderDetailService.getOrderDetailByOrderId(orderId);
            request.setAttribute("orderDetails", orderDetails);
        }

        Map<Integer, ProductReview> reviewMap = new HashMap<>();
        boolean allReviewed = true;
        if (orderDetails != null) {
            for (OrderDetail od : orderDetails) {
                ProductReview review = reviewService.getReview(userId, orderId, od.getProductId());
                if (review != null) {
                    reviewMap.put(od.getProductId(), review);
                }
            }
        }
        request.setAttribute("reviewMap", reviewMap);
        request.setAttribute("allReviewed", allReviewed);


        request.getRequestDispatcher("user/user-order-detail.jsp").forward(request, response);
  }
}
