package hcmuaf.fit.mombabyecommerce.controller.admin.brand;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.service.BrandService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cloudinary.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "AddBrandController", value = "/admin/add-brand")
public class AddBrandController extends HttpServlet {
    BrandService brandService = new BrandService(DBConnection.getJdbi());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                jsonString.append(line);
            }

            if (jsonString.toString().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Dữ liệu trống\"}");
                return;
            }

            JSONObject jsonRequest = new JSONObject(jsonString.toString());
            String brandName = jsonRequest.getString("name").trim();

            if (brandName.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Tên nhà sản xuất không được để trống\"}");
                return;
            }

            brandService.createBrand(brandName);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true, \"message\": \"Thêm nhà sản xuất thành công\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Lỗi: " + e.getMessage() + "\"}");
        }
    }



}
