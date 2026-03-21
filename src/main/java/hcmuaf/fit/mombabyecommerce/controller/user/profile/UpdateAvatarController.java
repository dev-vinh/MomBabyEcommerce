package hcmuaf.fit.mombabyecommerce.controller.user.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UpdateAvatarController", value = "/update-avatar")
public class UpdateAvatarController extends HttpServlet {
    UserService userService = new UserService(DBConnection.getJdbi());
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

        Integer imageId = (Integer) requestData.get("imageId");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        Boolean success = userService.updateAvatar(userId, imageId);
        Map<String, Object> responseBody = new HashMap<>();
        if (success){
            response.setStatus(HttpServletResponse.SC_OK);
            responseBody.put("status", "success");
            responseBody.put("message", "Avatar updated successfully!");
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseBody.put("status", "failed");
            responseBody.put("message", "Update avatar failed!");
        }

        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}
