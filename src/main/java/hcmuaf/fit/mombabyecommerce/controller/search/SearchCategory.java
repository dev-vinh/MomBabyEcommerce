package hcmuaf.fit.mombabyecommerce.controller.search;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Brand;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.BrandService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchCategory", value = "/search-category")
public class SearchCategory extends HttpServlet {

    private final ProductService productService =
            new ProductService(DBConnection.getJdbi());
    private final BrandService brandService =
            new BrandService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String categoryIdRaw = request.getParameter("categoryId");

        int categoryId;
        try {
            categoryId = Integer.parseInt(categoryIdRaw);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid categoryId");
            return;
        }
        List<Brand> brands = brandService.getAllBrands();

        List<Product> products = productService.getProductsByCategory(categoryId);
        List<Product> topProducts = productService.getTopProductsByCategory(categoryId, 4);


        //phân trang
        String pageRaw = request.getParameter("page");
        String sizeRaw = request.getParameter("size");

        int page = (pageRaw != null) ? Integer.parseInt(pageRaw) : 1;
        int size = (sizeRaw != null) ? Integer.parseInt(sizeRaw) : 16;

        request.setAttribute("products", products);
        request.setAttribute("topProducts", topProducts);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("brands", brands);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", size);

        request.getRequestDispatcher("search/list-product.jsp")
                .forward(request, response);
    }
}
