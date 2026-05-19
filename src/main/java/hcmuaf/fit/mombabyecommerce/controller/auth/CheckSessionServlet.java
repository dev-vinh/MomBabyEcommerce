package hcmuaf.fit.mombabyecommerce.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/check-session")
public class CheckSessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(
                    new ResponseWrapper<>(401, "error", "Chưa đăng nhập", null)
            ));
            return;
        }

        User user = (User) session.getAttribute("user");

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("fullName", user.getFullName());
        userData.put("displayName", user.getDisplayName());
        userData.put("email", user.getEmail());
        userData.put("roleType", user.getRole().getRoleType().name());
        userData.put("status", user.getStatus());
        userData.put("permissions", session.getAttribute("permissions"));
        System.out.println("CHECK SESSION: " +
                (session == null ? "null" : session.getId()));
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(
                new ResponseWrapper<>(200, "success", "Đã đăng nhập", userData)
        ));
    }
}
