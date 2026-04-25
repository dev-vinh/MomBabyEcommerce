package hcmuaf.fit.mombabyecommerce.controller.admin.category;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Category;
import hcmuaf.fit.mombabyecommerce.service.CategoryManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cloudinary.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AddCategoryController", value = "/admin/add-category")
public class AddCategoryController extends HttpServlet {
    CategoryManager categoryManager = new CategoryManager(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        try {
            // Đọc dữ liệu từ request body
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                jsonString.append(line);
            }

            // Parse dữ liệu JSON
            JSONObject jsonRequest = new JSONObject(jsonString.toString());
            String categoryName = jsonRequest.getString("name");

            // Kiểm tra dữ liệu
            if (categoryName == null || categoryName.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(new JSONObject().put("message", "Tên danh mục không được để trống").toString());
                return;
            }
            categoryManager.addCategory(new Category(null, categoryName,true));

            // Phản hồi thành công
            response.setStatus(HttpServletResponse.SC_OK);
            out.write(new JSONObject().put("message", "Danh mục được thêm thành công").toString());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write(new JSONObject().put("message", "Có lỗi xảy ra khi thêm danh mục").toString());
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
