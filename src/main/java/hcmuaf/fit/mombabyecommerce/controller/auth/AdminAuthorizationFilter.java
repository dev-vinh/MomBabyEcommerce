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
import java.util.EnumSet;

@WebFilter("/admin/*")
public class AdminAuthorizationFilter implements Filter{
    private final AuthService authService = new AuthService(DBConnection.getJdbi());
    private final EnumSet<ERole> ADMIN_ROLES = EnumSet.of(
            ERole.SUPER_ADMIN,
            ERole.PRODUCT_MANAGER,
            ERole.ORDER_MANAGER,
            ERole.CUSTOMER_SUPPORT,
            ERole.CONTENT_EDITOR,
            ERole.INVENTORY_MANAGER,
            ERole.MARKET_SPECIALIST,
            ERole.CUSTOM
    );
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;


        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            redirectToLoginWithMessage(request, response, "Bạn chưa đăng nhập.");
            return;
        }


        User user = authService.getUserById(userId);

        if (user == null || user.getRole() == null) {
            session.invalidate();
            redirectToLoginWithMessage(request, response, "Không xác định được quyền.");
            return;
        }
        String roleStr = (String) session.getAttribute("roleType");

        if (roleStr == null) {
            session.invalidate();
            redirectToLoginWithMessage(request, response, "Bạn không có quyền truy cập.");
            return;
        }
        try{
            ERole roleType = ERole.valueOf(roleStr);
            if (!ADMIN_ROLES.contains(roleType)) {
                session.invalidate();
                redirectToLoginWithMessage(request, response, "Bạn không có quyền truy cập vùng quản trị.");
                return;
            }
            String uri = request.getRequestURI();
            if(roleType != ERole.SUPER_ADMIN) {
                if (uri.contains("/admin/list-product") && uri.contains("/admin/add-product") && uri.contains("/admin/brand") && uri.contains("/admin/category")&& roleType != ERole.PRODUCT_MANAGER) {
                    redirectToLoginWithMessage(request, response, "Bạn không có quyền quản lý sản phẩm.");
                    return;
                }
                if (uri.contains("/admin/orders") && roleType != ERole.ORDER_MANAGER) {
                    redirectToLoginWithMessage(request, response, "Bạn không có quyền quản lý đơn hàng.");
                    return;
                }
                if (uri.contains("/admin/inventory") && uri.contains("/admin/inventory-log") && roleType != ERole.INVENTORY_MANAGER){
                    redirectToLoginWithMessage(request, response, "Bạn không có quyền quản lý kho hàng.");
                    return;
                }
            }

        }catch (IllegalArgumentException e) {
            session.invalidate();
            redirectToLoginWithMessage(request, response, "Quyền truy cập không hợp lệ.");
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
    public void destroy() {}
    //todo
}


