package hcmuaf.fit.mombabyecommerce.util;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

public class CustomResponse {
    public static void getResponse(HttpServletResponse response , int statusCode, String body, boolean success) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("data", body);
        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());

    }
}
