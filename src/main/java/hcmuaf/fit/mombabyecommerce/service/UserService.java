package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.UserDao;
import hcmuaf.fit.mombabyecommerce.model.User;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class UserService {
    UserDao userDao;

    public UserService(Jdbi jdbi) {
        this.userDao = jdbi.onDemand(UserDao.class);
    }


    public User getUserById(Integer id) {
        User user = userDao.getUserById(id);
        return user;
    }

    public User getUserByEmail(String email) {
        User user = userDao.getUserByEmail(email);
        return user;
    }


    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }


    public String getAvatarUrlById(Integer avatarId) {
        return userDao.getAvatarUrlById(avatarId);
    }


    public Boolean updateAvatar(Integer userId, Integer avatarId) {
        return userDao.updateAvatar(userId, avatarId);
    }


    public Boolean updateUser(User user) {
        return userDao.updateUser(
                user.getId(),
                user.getFullName(),
                user.getDisplayName(),
                user.getdOB(),
                user.getGender(),
                user.getPhoneNumber()
        );
    }

}



