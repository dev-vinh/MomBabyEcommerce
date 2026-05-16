package hcmuaf.fit.mombabyecommerce.controller.review;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.ProductReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "ToggleReviewLikeController", value = "/review-like")
public class ToggleReviewLikeController extends HttpServlet {
    private final ProductReviewService productReviewService =
            new ProductReviewService(DBConnection.getJdbi());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject json = new JSONObject();

        try {
            HttpSession session = request.getSession(false);
            Integer userId = session == null ? null : (Integer) session.getAttribute("userId");

            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                json.put("status", "error");
                json.put("message", "Bạn cần đăng nhập để thích đánh giá.");
                response.getWriter().write(json.toString());
                return;
            }

            int reviewId = Integer.parseInt(request.getParameter("reviewId"));

            ProductReviewService.LikeResult result =
                    productReviewService.toggleLike(reviewId, userId);

            json.put("status", "success");
            json.put("liked", result.isLiked());
            json.put("likeCount", result.getLikeCount());

            response.getWriter().write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json.put("status", "error");
            json.put("message", "Không thể xử lý lượt thích.");
            response.getWriter().write(json.toString());
        }
    }
}