package hcmuaf.fit.mombabyecommerce.controller.image;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "add-product-image", value = "/admin/ImageDetailDao")
public class ProductImageController extends HttpServlet {
    private ImageService imageService = new ImageService(DBConnection.getJdbi());


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int imageId = Integer.parseInt(req.getParameter("imageId"));

            boolean isSuccess = imageService.addImageToProduct(productId, imageId);

            String jsonResponse = buildJsonResponse(
                    isSuccess ? 200 : 500,
                    isSuccess ? "success" : "error",
                    isSuccess ? "Image added to product successfully." : "Failed to add image to product.",
                    null
            );

            resp.getWriter().write(jsonResponse);

        } catch (NumberFormatException e) {
            String errorResponse = buildJsonResponse(400, "error", "Invalid input data.", null);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(errorResponse);
        }
    }
    private String buildJsonResponse(int statusCode, String status, String message, Object data) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"statusCode\":").append(statusCode).append(",");
        jsonBuilder.append("\"status\":\"").append(status).append("\",");
        jsonBuilder.append("\"message\":\"").append(message).append("\",");
        jsonBuilder.append("\"data\":").append(data == null ? "null" : "\"" + data.toString() + "\"");
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
