package hcmuaf.fit.mombabyecommerce.controller.product;


import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.OptionVariant;
import hcmuaf.fit.mombabyecommerce.model.Product;
import hcmuaf.fit.mombabyecommerce.model.ProductOptionDTO;
import hcmuaf.fit.mombabyecommerce.service.ImageService;
import hcmuaf.fit.mombabyecommerce.service.OptionService;
import hcmuaf.fit.mombabyecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

        Map<Integer, ProductOptionDTO> optionMap = new LinkedHashMap<>();

        for (OptionVariant op : optionVariant) {
            ProductOptionDTO dto = optionMap.get(op.getId());

            if (dto == null) {
                dto = new ProductOptionDTO(
                        op.getId(),
                        op.getProductId(),
                        op.getPrice(),
                        op.getStock()
                );
                dto.setVariantText("");
                optionMap.put(op.getId(), dto);
            }

            if (op.getVariantName() != null && op.getVariantValue() != null) {
                String text = op.getVariantName() + ": " + op.getVariantValue();

                if (dto.getVariantText() == null || dto.getVariantText().isEmpty()) {
                    dto.setVariantText(text);
                } else {
                    dto.setVariantText(dto.getVariantText() + " | " + text);
                }
            }
        }

        List<ProductOptionDTO> productOptions = new ArrayList<>(optionMap.values());

        for (ProductOptionDTO dto : productOptions) {
            if (dto.getVariantText() == null || dto.getVariantText().isEmpty()) {
                dto.setVariantText("Mặc định");
            }
        }

        request.setAttribute("images", images);
        request.setAttribute("primaryImageUrl", primaryImageUrl);
        request.setAttribute("product", product);
        request.setAttribute("descriptions", descriptions);
        request.setAttribute("productPrice", productPrice);
        request.setAttribute("productOptions", productOptions);


        productService.increaseNoOfViews(productId);
        request.getRequestDispatcher("product_detail/product-detail.jsp").forward(request, response);
    }

    // lỗi
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}

