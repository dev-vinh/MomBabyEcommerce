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

@JsonIgnoreProperties(ignoreUnknown = true)
@WebServlet(name = "AddAddressController", value = "/add-address")
public class AddAddressController extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(AddAddressController.class);
    AddressService addressService = new AddressService(DBConnection.getJdbi());

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }
        try {
            StringBuilder jsonBuffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
            String jsonData = jsonBuffer.toString();
            ObjectMapper mapper = new ObjectMapper();

            Address newAddress = mapper.readValue(jsonData, Address.class);
            Address addressDefault = addressService.findDefautlByUserId(userId);

            if (addressDefault == null) {
                newAddress.setDefault(true);
            }
            int resultId = addressService.addAddress(newAddress);
            if (resultId > 0) {
                newAddress.setId(resultId);

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Thêm địa chỉ thành công!");
                String jsonAddress = mapper.writeValueAsString(newAddress);
                jsonResponse.put("address", new JSONObject(jsonAddress));
                response.getWriter().write(jsonResponse.toString());

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Thêm địa chỉ thất bại.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            response.getWriter().println("Lỗi khi thêm địa chỉ.");
        }
    }

}
