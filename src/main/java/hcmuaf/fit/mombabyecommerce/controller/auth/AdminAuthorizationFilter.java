package hcmuaf.fit.mombabyecommerce.controller.auth;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.contant.ERole;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.service.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebFilter("/admin/*")
public class AdminAuthorizationFilter implements Filter {
    private final AuthService authService = new AuthService(DBConnection.getJdbi());
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        ERole role = (ERole) session.getAttribute("role");

        if (userId == null) {
            redirectToLoginWithMessage(request, response, "Bạn chưa đăng nhập.");
            return;
        }

        User user = authService.getUserById(userId);
        if (user == null || role == ERole.USER || user.getStatus().equals("BANNED")) {
            session.invalidate();
            redirectToLoginWithMessage(request, response, "Bạn không có quyền truy cập vào trang này.");
            return;
        }
        if (user.getNeedRefresh()) {
            session.invalidate();
            redirectToLoginWithMessage(request, response, "");
            return;
        }
        chain.doFilter(req, res);
    }
    private void redirectToLoginWithMessage(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException {
        request.getSession().setAttribute("errorMessage", message);
        response.sendRedirect(request.getContextPath() + "/login");
    }
    @Override
    public void destroy() {
        // to do
    }
}

