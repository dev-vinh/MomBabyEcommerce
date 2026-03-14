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

        Integer productPrice = productService.getMinimumPriceForProduct(productId); // Default to minimum price
        if (product.getOptionId() != null) {
            productPrice = productService.getPriceForOption(product.getOptionId());
        }

        List<String> images = imageService.getAllImagesByProductId(product.getId());
        String primaryImageUrl = imageService.getImageUrlById(product.getImageId());
        List<String> descriptions = List.of(product.getDescription().split("\\n"));

        // lỗi
        List<OptionVariant> options = optionService.getOptionsByProductId(product.getId());
        List<Integer> optionIds = options.stream().map(OptionVariant::getId).collect(Collectors.toList());

        List<OptionVariant> optionVariant = optionService.getVariantByOptionId(optionIds);
        List<String> variants = optionVariant.stream().map(OptionVariant::getVariantName).distinct()
                .collect(Collectors.toList());

        // nếu lấy chi tieets sản phẩm ra không được xem lại chỗ này

        request.setAttribute("images", images);
        request.setAttribute("primaryImageUrl", primaryImageUrl); // Add primary image URL
        request.setAttribute("product", product);
        request.setAttribute("descriptions", descriptions);
        request.setAttribute("productPrice", productPrice);
        request.setAttribute("optionVariant", optionVariant);
        request.setAttribute("variants", variants);

        productService.increaseNoOfViews(productId);

        request.getRequestDispatcher("product_detail/product_detail.jsp").forward(request, response);
    }

    // lỗi
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}

