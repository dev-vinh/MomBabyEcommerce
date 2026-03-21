package hcmuaf.fit.mombabyecommerce.controller.user.card;

import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.model.Card;
import hcmuaf.fit.mombabyecommerce.service.CardService;
import hcmuaf.fit.mombabyecommerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class UserCardController extends HttpServlet {
    CardService cardService = new CardService(DBConnection.getJdbi());
    UserService userService = new UserService(DBConnection.getJdbi());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        List<Card> cards= cardService.getCartByUserId(userId);
        request.setAttribute("cards", cards);
        request.setAttribute("user", user);

        request.getRequestDispatcher("user/user-card.jsp").forward(request, response);

    }

}
