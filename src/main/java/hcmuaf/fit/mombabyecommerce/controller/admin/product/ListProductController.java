package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Category;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.CategoryService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "listProduct", value = "/admin/list-product")
public class ListProductController extends HttpServlet {

    ProductService productService = new ProductService(DBConnection.getJdbi());
    CategoryService categoryService = new CategoryService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Đọc pagination
            int size = 10;
            int page = 1;
            String pageParam = request.getParameter("page");
            String sizeParam = request.getParameter("size");
            if (pageParam != null && !pageParam.isEmpty()) page = Integer.parseInt(pageParam);
            if (sizeParam != null && !sizeParam.isEmpty()) size = Integer.parseInt(sizeParam);
            if (page < 1) page = 1;

            // Đọc filter params
            String categoryParam = request.getParameter("categoryId");
            String statusParam = request.getParameter("status");
            String keyword = request.getParameter("keyword");

            Integer categoryId = (categoryParam != null && !categoryParam.isEmpty())
                    ? Integer.parseInt(categoryParam) : null;
            Boolean isActive = (statusParam != null && !statusParam.isEmpty())
                    ? "active".equals(statusParam) : null;

            // Kiểm tra có filter không
            boolean hasFilter = (categoryId != null) || (isActive != null)
                    || (keyword != null && !keyword.isBlank());

            List<Product> products;
            int totalProducts;
            if (hasFilter) {
                products = productService.getProductsFiltered(categoryId, isActive, keyword, page, size);
                totalProducts = productService.countProductsFiltered(categoryId, isActive, keyword);
            } else {
                products = productService.getProductsPaged(page, size);
                totalProducts = productService.countAllProducts();
            }

            int totalPages = (totalProducts > 0) ? (int) Math.ceil((double) totalProducts / size) : 1;
            if (page > totalPages) page = totalPages;

            List<Category> categories = categoryService.getAllCategories();

            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("size", size);
            request.setAttribute("categories", categories);
            request.setAttribute("filterCategoryId", categoryId);
            request.setAttribute("filterStatus", statusParam);
            request.setAttribute("filterKeyword", keyword);

            RequestDispatcher dispatcher = request.getRequestDispatcher("listProduct.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi tải danh sách sản phẩm");
        }
    }
}
