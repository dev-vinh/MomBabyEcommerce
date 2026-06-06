package hcmuaf.fit.mombabyecommerce.controller;

import hcmuaf.fit.mombabyecommerce.Dao.ImageDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Banner;
import hcmuaf.fit.mombabyecommerce.service.BannerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "HomeController",  value = "/home")
public class HomeController extends HttpServlet {
    private final BannerService bannerService = new BannerService(DBConnection.getJdbi());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Banner> banners = bannerService.getAllBanners();
        ImageDao imageDao = DBConnection.getJdbi().onDemand(ImageDao.class);

        Map<String, String> imageMap = new HashMap<>();
        for (Banner b : banners) {
            String url = imageDao.getImageUrlById(Integer.parseInt(b.getImageId()));
            imageMap.put(b.getImageId(), url);
        }

        request.setAttribute("banners", banners);
        request.setAttribute("imageMap", imageMap);
        request.getRequestDispatcher("home/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
