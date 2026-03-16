package hcmuaf.fit.mombabyecommerce.controller.admin.category;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.CategoriesWithStock;
import hcmuaf.fit.mombabyecommerce.service.CategoryCustomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListCategoryController", value = "/admin/category")
public class ListCategoryController extends HttpServlet {
    CategoryCustomService categoryCustomService =  new CategoryCustomService(DBConnection.getJdbi()); ;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CategoriesWithStock> categoriesWithStock = categoryCustomService.getCustomCategoriesWithTotalStock();
            request.setAttribute("categoriesWithStock", categoriesWithStock);
            request.getRequestDispatcher("categories.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải danh mục.");
        }
    }
}