package hcmuaf.fit.mombabyecommerce.controller.user.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Address;
import hcmuaf.fit.mombabyecommerce.service.AddressService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "UpdateAddressController", value = "/address/update")
public class UpdateAddressController extends HttpServlet {
    private final AddressService addressService = new AddressService(DBConnection.getJdbi());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new JSONObject()
                    .put("status", "error")
                    .put("message", "Bạn chưa đăng nhập.")
                    .toString());
            return;
        }

        try {
            StringBuilder jsonBuffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }

            ObjectMapper mapper = new ObjectMapper();
            Address address = mapper.readValue(jsonBuffer.toString(), Address.class);

            String validationError = validateAddress(address);
            if (validationError != null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(new JSONObject()
                        .put("status", "error")
                        .put("message", validationError)
                        .toString());
                return;
            }

            boolean success = addressService.updateAddress(userId, address);

            if (success) {
                response.getWriter().write(new JSONObject()
                        .put("status", "success")
                        .put("message", "Cập nhật địa chỉ thành công.")
                        .toString());
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(new JSONObject()
                        .put("status", "error")
                        .put("message", "Không tìm thấy địa chỉ hoặc bạn không có quyền sửa địa chỉ này.")
                        .toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new JSONObject()
                    .put("status", "error")
                    .put("message", "Lỗi khi cập nhật địa chỉ.")
                    .toString());
        }
    }

    private String validateAddress(Address address) {
        if (address.getId() == null) {
            return "Thiếu id địa chỉ cần cập nhật.";
        }

        if (address.getFullName() == null || address.getFullName().isBlank()) {
            return "Tên người nhận không được để trống.";
        }

        if (address.getPhoneNumber() == null || !address.getPhoneNumber().matches("0\\d{9}")) {
            return "Số điện thoại phải gồm 10 số và bắt đầu bằng 0.";
        }

        if (address.getStreet() == null || address.getStreet().isBlank()) {
            return "Địa chỉ chi tiết không được để trống.";
        }

        if (address.getCity() == null || address.getCity().isBlank()) {
            return "Quận/Huyện không được để trống.";
        }

        if (address.getState() == null || address.getState().isBlank()) {
            return "Tỉnh/Thành phố không được để trống.";
        }

        if (address.getCountry() == null || address.getCountry().isBlank()) {
            address.setCountry("Việt Nam");
        }

        if (address.getAddressType() == null || address.getAddressType().isBlank()) {
            address.setAddressType("shipping");
        }

        return null;
    }
}
