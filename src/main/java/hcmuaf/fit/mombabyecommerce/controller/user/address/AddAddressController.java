package hcmuaf.fit.mombabyecommerce.controller.user.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "AddAddressController", value = "/AddAddressController")
public class AddAddressController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get userId from session instead of request parameter
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            System.out.println("[AddAddress] userId from session: " + userId);

            if (userId == null) {
                System.err.println("[AddAddress] ERROR: User not logged in");
                response.sendRedirect("login");
                return;
            }

            String addressType = request.getParameter("addressType");
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String street = request.getParameter("street");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String country = request.getParameter("country");
            Boolean isDefault = Boolean.valueOf(request.getParameter("isDefault"));

            // Default addressType if not provided
            if (addressType == null || addressType.isEmpty()) {
                addressType = "shipping";
            }

            System.out.println("[AddAddress] Creating address for user: " + userId);
            System.out.println("[AddAddress] FullName: " + fullName + ", Phone: " + phoneNumber);

            // Khởi tạo Address
            Address newAddress = new Address(
                    null, userId, addressType, fullName, phoneNumber, street, city, state, country, isDefault);

            // Thêm vào cơ sở dữ liệu
            AddressService addressService = new AddressService(DBConnection.getJdbi());
            int resultId = addressService.addAddress(newAddress);

            System.out.println("[AddAddress] Result ID: " + resultId);

            if (resultId > 0) {
                System.out.println("[AddAddress] SUCCESS: Address added with ID: " + resultId);
                response.sendRedirect("user-address"); // Điều hướng về trang danh sách địa chỉ
            } else {
                System.err.println("[AddAddress] ERROR: Failed to add address");
                response.getWriter().println("Thêm địa chỉ thất bại.");
            }
        } catch (Exception e) {
            System.err.println("[AddAddress] EXCEPTION: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().println("Lỗi khi thêm địa chỉ: " + e.getMessage());
        }
    }
}