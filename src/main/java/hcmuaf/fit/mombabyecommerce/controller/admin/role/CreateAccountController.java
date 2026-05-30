package hcmuaf.fit.mombabyecommerce.controller.admin.role;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/create-account")
public class CreateAccountController extends HttpServlet {
    AuthService authService = new AuthService(DBConnection.getJdbi());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String displayName = request.getParameter("displayName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Integer roleId =
                Integer.parseInt(
                        request.getParameter("roleId")
                );

        boolean success =
                authService.createStaffAccount(
                        fullName,
                        displayName,
                        email,
                        password,
                        roleId
                );
        response.sendRedirect(
                request.getContextPath()
                        + "/admin/manage-role"
        );
    }
}
