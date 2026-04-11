package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.UserDao;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.util.HashUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.UUID;

public class AuthService {
    private UserDao userDAO;

    public AuthService(Jdbi jdbi) {
        this.userDAO = jdbi.onDemand(UserDao.class);
    }

    public boolean register(String fullName, String displayName, String email, String password) {
        if (userDAO.getUserByEmail(email) != null) {
            return false;
        }
        String salt = HashUtils.generateSalt();
        String hashedPassword = HashUtils.hashWithSalt(password, salt);
        String userId = userDAO.createUser(fullName, displayName, email, hashedPassword, salt);
        return userId != null;
    }


    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            String storedSalt = user.getSalt();
            String storedHashedPassword = user.getPasswordUsername();
            String hashedPassword = HashUtils.hashWithSalt(password, storedSalt);
            if (hashedPassword.equals(storedHashedPassword) && "ACTIVE".equalsIgnoreCase(user.getStatus())) {
                return user;
            }
        }
        return null;
    }


    public boolean changePassword(Integer userId, String oldPassword, String newPassword, boolean verifyOldPassword) {
        User user = userDAO.getPasswordByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (verifyOldPassword) {
            String storedSalt = user.getSalt();
            String storedHashedPassword = user.getPasswordUsername();

            String hashedPassword = HashUtils.hashWithSalt(oldPassword, storedSalt);

            if (!hashedPassword.equals(storedHashedPassword)) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
        }

        String newSalt = HashUtils.generateSalt();
        String hashedNewPassword = HashUtils.hashWithSalt(newPassword, newSalt);

        return userDAO.updatePassword(userId, hashedNewPassword, newSalt) > 0;
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public User getUserById(Integer userId) {
        return userDAO.getUserById(userId);
    }

    public boolean verifySession(HttpServletRequest request, String sessionId) {
        HttpSession session = request.getSession(false);  // Lấy session hiện tại, nếu không có thì trả về null
        if (session != null) {
            String storedSessionId = (String) session.getAttribute("sessionId");
            return storedSessionId != null && storedSessionId.equals(sessionId);
        }
        return false;
    }


    public void activateUserAccount(HttpServletRequest request, String sessionId) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String storedSessionId = (String) session.getAttribute("sessionId");
            if (storedSessionId != null && storedSessionId.equals(sessionId)) {
                String email = (String) session.getAttribute("email");

                if (email != null) {
                    userDAO.updateStatusByEmail(email, "ACTIVE");
                }

                System.out.println("Tài khoản với email " + email + " đã được xác nhận và kích hoạt.");

                session.removeAttribute("sessionId");
                session.removeAttribute("email");
            }
        }
    }



    public void saveSessionId(HttpServletRequest request, String email, String sessionId) {
        HttpSession session = request.getSession();
        session.setAttribute("sessionId", sessionId);
        session.setAttribute("email", email);  // Lưu email vào session nếu cần thiết
    }

}