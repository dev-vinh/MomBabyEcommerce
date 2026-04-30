package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/confirm")
public class ConfirmAccountController extends HttpServlet {
    private final AuthService authService = new AuthService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            response.sendRedirect("login?error=invalid_token");
            return;
        }

        try {
            boolean success = authService.confirmAccount(token.trim());
            response.setContentType("text/html;charset=UTF-8");
            if (success) {
                response.sendRedirect("login?success=confirmed");
            } else {
                response.sendRedirect("login?error=invalid_or_used");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login?error=system");
        }
    }
}

