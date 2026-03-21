package hcmuaf.fit.mombabyecommerce.controller.user.profile;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UserDetailController", value = "/user-profile")
public class UserDetailController extends HttpServlet {
    UserService userService = new UserService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        User user= null;
        if (userId != null) {
            user = userService.getUserById(userId);
            request.setAttribute("user", user);

            System.out.println(user.toString());
        }
        request.getRequestDispatcher("user/user-profile.jsp").forward(request, response);
    }
}
