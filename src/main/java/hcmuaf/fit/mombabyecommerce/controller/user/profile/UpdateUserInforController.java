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
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);

        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestData = objectMapper.readValue(stringBuilder.toString(), Map.class);
        HttpSession session = request.getSession();



        Integer userId = (Integer) session.getAttribute("userId");
        User user = new User();
        user.setId(userId);
        user.setFullName(requestData.get("fullName").toString());
        user.setDisplayName(requestData.get("displayName").toString());
        user.setGender(requestData.get("gender").toString());
        user.setdOB(LocalDate.parse(requestData.get("dOB").toString()));
        user.setEmail(requestData.get("email").toString());
        user.setPhoneNumber(requestData.get("phoneNumber").toString());

        Boolean success = userService.updateUser(user);




        Map<String, Object> responseBody = new HashMap<>();
        if (success){
            response.setStatus(HttpServletResponse.SC_OK);
            responseBody.put("status", "success");
            responseBody.put("message", "User updated successfully!");
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseBody.put("status", "failed");
            responseBody.put("message", "Update user failed!");
        }

        objectMapper.writeValue(response.getWriter(), responseBody);
    }

}
