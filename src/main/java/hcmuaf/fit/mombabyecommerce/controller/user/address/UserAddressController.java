package hcmuaf.fit.mombabyecommerce.controller.user.address;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Address;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.AddressService;
import hcmuaf.fit.mombabyecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserAddressController", value = "/user-address")
public class UserAddressController extends HttpServlet {
    UserService userService = new UserService(DBConnection.getJdbi());
    AddressService addressSevice = new AddressService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        System.out.println("=== UserAddressController ===");
        System.out.println("userId from session: " + userId);
        if (userId != null) {
            try {
                User user = userService.getUserById(userId);
                System.out.println("user: " + user);

                List<Address> addresses = addressSevice.findByUserId(userId);
                System.out.println("addresses: " + addresses);

                request.setAttribute("user", user);
                request.setAttribute("addresses", addresses);
            } catch (Exception e) {
                // In ra lỗi cụ thể để biết chỗ nào fail
                System.err.println("=== LỖI trong UserAddressController ===");
                e.printStackTrace();
            }
        }

        request.getRequestDispatcher("user/user-address.jsp").forward(request, response);
    }
}