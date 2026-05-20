package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "listProduct", value = "/admin/list-product")
public class ListProductController extends HttpServlet {

    ProductService productService = new ProductService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int size = 10;
            int page = 1;

            String pageParam = request.getParameter("page");
            String sizeParam = request.getParameter("size");

            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
            if (sizeParam != null && !sizeParam.isEmpty()) {
                size = Integer.parseInt(sizeParam);
            }

            if (page < 1) page = 1;

            int totalProducts = productService.countAllProducts();
            int totalPages    = (int) Math.ceil((double) totalProducts / size);

            if (totalPages > 0 && page > totalPages) page = totalPages;

            List<Product> products = productService.getProductsPaged(page, size);

            request.setAttribute("products",    products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages",  totalPages);
            request.setAttribute("size",        size);

            RequestDispatcher dispatcher = request.getRequestDispatcher("listProduct.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("list-product");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi tải danh sách sản phẩm");
        }
    }
}