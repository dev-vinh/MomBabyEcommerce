package hcmuaf.fit.mombabyecommerce.filter;


import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Category;
import hcmuaf.fit.mombabyecommerce.service.CategoryService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.util.List;

@WebFilter("/*")
public class CategoryFilter implements Filter {

    private CategoryService categoryService;

    @Override
    public void init(FilterConfig filterConfig) {
        categoryService = new CategoryService(DBConnection.getJdbi());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        List<Category> categories = categoryService.getTop7Categories();

        request.setAttribute("categories", categories);
        System.out.println("CategoryFilter running...");
        chain.doFilter(request, response);
    }


}
