package hcmuaf.fit.mombabyecommerce.controller.user.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.UserService;
import hcmuaf.fit.mombabyecommerce.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UpdateUserInfor", value = "/updateUser")
public class UpdateUserInforController extends HttpServlet {
    UserService userService = new UserService(DBConnection.getJdbi());


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý yêu cầu GET ở đây
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> requestData =
                objectMapper.readValue(request.getInputStream(), Map.class);

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String fullName = requestData.get("fullName");
        String displayName = requestData.get("displayName");
        String gender = requestData.get("gender");
        String phoneNumber = requestData.get("phoneNumber");

        User user = new User();
        user.setId(userId);
        user.setFullName(fullName);
        user.setDisplayName(displayName);
        user.setGender(gender);
        user.setPhoneNumber(phoneNumber);

        boolean success = userService.updateUser(user);

        Map<String, Object> res = new HashMap<>();

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            res.put("success", true);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.put("success", false);
        }

        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), res);
    }
}
