package hcmuaf.fit.mombabyecommerce.controller.admin.product;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.ExcelImportService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/admin/api/products/import-excel")
@MultipartConfig
public class ImportProductController extends HttpServlet {
    private final ProductService productService = new ProductService(DBConnection.getJdbi());
    private final ExcelImportService excelImportService = new ExcelImportService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Part filePart = request.getPart("excelFile");

            if (filePart == null || filePart.getSize() == 0) {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Vui lòng chọn một file Excel hợp lệ.\"}");
                return;
            }
            InputStream inputStream = filePart.getInputStream();
            List<Product> productList = excelImportService.readProductsFromExcel(inputStream);

            if (productList.isEmpty()) {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"File Excel trống hoặc không có dữ liệu hợp lệ.\"}");
                return;
            }
            int successCount = productService.importExcelProducts(productList);
            if (successCount > 0) {
                response.getWriter().write("{\"status\":\"success\",\"message\":\"Đã import thành công " + successCount + "/" + productList.size() + " sản phẩm.\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Import thất bại. Vui lòng kiểm tra lại định dạng dữ liệu hoặc mã SKU có thể đã tồn tại.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
        }
    }

}

