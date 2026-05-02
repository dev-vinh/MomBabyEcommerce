package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.UserDao;
import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(Jdbi jdbi) {
        this.userDao = jdbi.onDemand(UserDao.class);
    }



    public User getUserById(Integer id) {
        return enrichUserWithRole(userDao.getUserWithRole(id));
    }

    private User enrichUserWithRole(User user) {
        if (user == null) return null;
        List<Role> roles = userDao.getUserRoleObjects(user.getId());
        if (roles != null && !roles.isEmpty()) {
            user.setRole(roles.get(0));
        }
        return user;
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public String getAvatarUrlById(Integer avatarId) {
        return userDao.getAvatarUrlById(avatarId);
    }
    public boolean updateAvatar(Integer userId, Integer avatarId) {
        return userDao.updateAvatar(userId, avatarId) > 0;
    }

    public boolean updateUser(User user) {
        return userDao.updateUser(
                user.getId(),
                user.getFullName(),
                user.getDisplayName(),
                user.getdOB(),
                user.getGender(),
                user.getPhoneNumber()
        ) > 0;
    }
    public boolean updateNeedRefresh(Integer userId, Boolean needRefresh) {
        return userDao.updateNeedRefresh(userId, needRefresh) > 0;
    }

    public List<User> getCustomers() {
        return userDao.getCustomers();
    }
}



