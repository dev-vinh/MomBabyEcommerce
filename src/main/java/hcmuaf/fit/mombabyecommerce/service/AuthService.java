package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.UserDao;
import hcmuaf.fit.mombabyecommerce.model.User;
import hcmuaf.fit.mombabyecommerce.util.HashUtils;
import org.jdbi.v3.core.Jdbi;

import java.util.UUID;

public class AuthService {
    private UserDao userDao;

    public AuthService(Jdbi jdbi) {
        this.userDao = jdbi.onDemand(UserDao.class);
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
        String userId = userDao.createUser(fullName, displayName, email, hashedPassword, salt);
        if (userId != null) {

        }
    }
    }

