package hcmuaf.fit.mombabyecommerce.controller.product;


import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.OptionVariant;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.service.ImageService;
import hcmuaf.fit.mombabyecommerce.service.OptionService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "ProductDetailController", value = "/product-detail")
public class ProductDetailController extends HttpServlet {
    // chưa có service
    ProductService productService = new ProductService(DBConnection.getJdbi());
    ImageService imageService = new ImageService(DBConnection.getJdbi());
    OptionService optionService = new OptionService(DBConnection.getJdbi());

    @Override   
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = productService.getProductById(productId);

        Integer productPrice = productService.getMinimumPriceForProduct(productId);
        if (product.getOptionId() != null) {
            productPrice = productService.getPriceForOption(product.getOptionId());
        }

        List<String> images = imageService.getAllImagesByProductId(product.getId());
        String primaryImageUrl = imageService.getImageUrlById(product.getImageId());
        List<String> descriptions = List.of(product.getDescription().split("\\n"));


        List<OptionVariant> optionVariant = optionService.getOptionDetailsByProductId(product.getId());
        request.setAttribute("images", images);
        request.setAttribute("primaryImageUrl", primaryImageUrl);
        request.setAttribute("product", product);
        request.setAttribute("descriptions", descriptions);
        request.setAttribute("productPrice", productPrice);
        request.setAttribute("optionVariant", optionVariant);


        productService.increaseNoOfViews(productId);
        request.getRequestDispatcher("product_detail/product-detail.jsp").forward(request, response);
    }

    // lỗi
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}

