package hcmuaf.fit.mombabyecommerce.controller.user.address;

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

@WebServlet(name = "DeleteAddressController", value = "/address/delete")
public class DeleteAddressController extends HttpServlet {
    AddressService addressService = new AddressService(DBConnection.getJdbi());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }
        BufferedReader reader = request.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        JSONObject jsonObject = new JSONObject(json.toString());

        int addressId = jsonObject.getInt("addressId");
        Address address = addressService.findById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("Address not found");
        }

        boolean success=  addressService.updateStatus(addressId, "DELETED");
        if (success) {
            response.getWriter().write("{\"status\":\"success\", \"message\":\"Address deleted\"}");
        }
        else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Delete failed\"}");

        }


    }
}
