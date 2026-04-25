package hcmuaf.fit.mombabyecommerce.controller.search;

import hcmuaf.fit.mombabyecommerce.util.ResponseWrapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.ProductService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/home/products/search")
public class SearchProductController extends HttpServlet{
    private final ProductService productService = new ProductService(DBConnection.getJdbi());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String limitParam = request.getParameter("limit");

        try {
            if (name == null || name.trim().isEmpty()) {
                response.getWriter().write(
                        new ResponseWrapper<>(200, "success", "Empty keyword", new ArrayList<>()).toJson()
                );
                return;
            }

            // Lấy danh sách sản phẩm từ service
            List<Product> products = productService.searchProducts("%" + name + "%");

            if (products == null) {
                products = new ArrayList<>();
            }


            // Xử lý giới hạn số lượng sản phẩm trả về
            int limit = 5; // Giá trị mặc định
            try {
                if (limitParam != null) {
                    limit = Math.max(1, Integer.parseInt(limitParam)); // Tránh giá trị âm hoặc 0
                }
            } catch (Exception ignored) {
            }


            List<Product> result = products.stream()
                    .limit(limit)
                    .map(p -> new Product(
                            p.getId(),
                            p.getName(),
                            null, null, null,
                            null, null, null, null,
                            p.getImageId(),
                            p.getPrice(),
                            p.getStock(),
                            p.getOptionId(),
                            null,
                            p.getImageUrl()
                    ))
                    .collect(Collectors.toList());

            response.getWriter().write(
                    new ResponseWrapper<>(200, "success", "OK", result).toJson()
            );


        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(
                    new ResponseWrapper<>(500, "error", "Server error", new ArrayList<>()).toJson()
            );
        }
    }
}
