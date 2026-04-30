package hcmuaf.fit.mombabyecommerce.controller.admin.banner;

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

@WebServlet(name = "AdminBannerController", urlPatterns = {"/admin/banner"})
public class AdminBannerController extends HttpServlet {
    private final BannerService bannerService = new BannerService(DBConnection.getJdbi());
    private final ImageDao imageDao = DBConnection.getJdbi().onDemand(ImageDao.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Banner> banners = bannerService.getAllBanners();

        Map<String, String> imageMap = new HashMap<>();
        for (Banner b : banners) {
            try {
                String url = imageDao.getImageUrlById(Integer.parseInt(b.getImageId()));
                imageMap.put(b.getImageId(), url);
            } catch (NumberFormatException e) {
                imageMap.put(b.getImageId(), ""); // fallback nếu imageId lỗi
            }

            request.setAttribute("banners", banners);
            request.setAttribute("imageMap", imageMap);

            request.getRequestDispatcher("/admin/banner.jsp").forward(request, response);
        }
    }
}
