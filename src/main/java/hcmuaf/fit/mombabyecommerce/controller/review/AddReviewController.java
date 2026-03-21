package hcmuaf.fit.mombabyecommerce.controller.review;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Review;
import hcmuaf.fit.mombabyecommerce.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "AddReviewController", value = "/add-review")
public class AddReviewController extends HttpServlet {
    ReviewService reviewService = new ReviewService(DBConnection.getJdbi());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        boolean flag = true;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

        HttpSession session = request.getSession();
        Integer user_id = (Integer) session.getAttribute("userId");

        if (user_id == null) {
            if (jsonObject.has("user_id")) {
                user_id = jsonObject.getInt("user_id");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                JSONObject errorJson = new JSONObject();
                errorJson.put("status", "error");
                errorJson.put("message", "Chưa đăng nhập và không có user_id trong request.");
                response.getWriter().write(errorJson.toString());
                return;
            }
        }

        Integer order_id = jsonObject.getInt("order_id");
        Integer rating = jsonObject.getInt("rating");
        String description = jsonObject.getString("description");
        JSONArray productIds = jsonObject.getJSONArray("productIds");

        if (!reviewService.isOrderDelivered(order_id, user_id)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            JSONObject errorJson = new JSONObject();
            errorJson.put("status", "error");
            errorJson.put("message", "Chỉ có thể đánh giá sau khi đơn hàng đã giao thành công.");
            response.getWriter().write(errorJson.toString());
            return;
        }

        Review review = new Review();
        review.setDescription(description);
        review.setRating(rating);
        review.setOrderId(order_id);
        review.setUserId(user_id);

        for (int i = 0; i < productIds.length(); i++) {
            Integer id = productIds.getInt(i);
            review.setProductId(id);
            if (!reviewService.addReview(review)) {
                flag = false;
            }
        }

        JSONObject responseJson = new JSONObject();
        if (flag) {
            responseJson.put("status", "success");
            responseJson.put("message", "All reviews added successfully.");
        } else {
            responseJson.put("status", "error");
            responseJson.put("message", "Failed to add all reviews.");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseJson.toString());
    }
}
