package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.PermissionDao;
import hcmuaf.fit.mombabyecommerce.Dao.UserDao;
import hcmuaf.fit.mombabyecommerce.Dao.UserRoleDao;
import hcmuaf.fit.mombabyecommerce.config.ConfigLoader;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.util.HashUtils;
import org.jdbi.v3.core.Jdbi;

import java.util.List;
import java.util.UUID;

public class AuthService {
    private UserDao userDao;
    private UserRoleDao userRoleDao;
    String facebookId = null;
    private EmailService emailService;
    private PermissionDao permissionDao;

    public AuthService(Jdbi jdbi) {
        this.userDao = jdbi.onDemand(UserDao.class);
        this.userRoleDao = jdbi.onDemand(UserRoleDao.class);
        this.emailService = new EmailService();
        this.permissionDao = jdbi.onDemand(PermissionDao.class);
    }

    public User getUserByEmail(String email) {

        return userDao.getUserByEmail(email);
    }

    public boolean register(String fullName, String displayName, String email, String password) {
        if(userDao.getUserByEmail(email) != null) {
            return false;
        }
        String salt = HashUtils.generateSalt();
        String hashedPassword = HashUtils.hashWithSalt(password, salt);
        String confirmationToken = UUID.randomUUID().toString();
        Integer userId = userDao.createUser(fullName, displayName, email, hashedPassword, confirmationToken,salt, facebookId);
        if (userId != null) {
            Role defaultRole = userDao.getDefaultUserRole();
            if (defaultRole != null) {
                userRoleDao.addUserRole(userId, defaultRole.getId());
            }
            String hostProduct = ConfigLoader.get("host.product");

            String confirmationLink = hostProduct + "/confirm?token=" + confirmationToken;
            String emailContent = "Xin chào " + fullName + ",\n\n" +
                    "Cảm ơn bạn đã đăng ký tài khoản. Vui lòng nhấp vào liên kết sau để xác nhận tài khoản của bạn:\n" +
                    confirmationLink + "\n\n" +
                    "Trân trọng,\n" +
                    "Đội ngũ hỗ trợ";

            emailService.sendEmail(email, "Xác nhận tài khoản", emailContent);
            return true;
        }
        return false;
    }

    public User login(String email, String password) {
        User user = userDao.getUserByEmail(email.trim());
        if (user != null) {
            if ("PENDING".equals(user.getStatus())) {
                throw new RuntimeException("Tài khoản chưa được xác nhận. Vui lòng kiểm tra email của bạn.");
            }
            if ("DEACTIVE".equals(user.getStatus())) {
                throw new RuntimeException("Tài khoản của bạn đã bị vô hiệu hóa.");
            }
            String storedSalt = user.getSalt();
            String storedHashedPassword = user.getPasswordUsername();

            String hashedPassword = HashUtils.hashWithSalt(password.trim(), storedSalt);

            if (hashedPassword.equals(storedHashedPassword)) {
                return user;
            }
        }
        return null;
    }
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        return permissionDao.getPermissionsByRoleId(roleId);
    }
    }




