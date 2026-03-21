package hcmuaf.fit.mombabyecommerce.controller.user.address;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Address;
import hcmuaf.fit.mombabyecommerce.service.AddressService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "FindUserAddressController", value = "/address")
public class FindUserAddressController extends HttpServlet {
    private AddressService addressService = new AddressService(DBConnection.getJdbi());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        Integer userId = Integer.parseInt(request.getParameter("userId"));

        List<Address> addresses = addressService.findByUserId(userId);

        jsonResponse.put("success", true);
        jsonResponse.put("data", addresses);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(jsonResponse.toString());
    }

}
