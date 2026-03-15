package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.UserDao;
import hcmuaf.fit.mombabyecommerce.model.User;
import org.jdbi.v3.core.Jdbi;

public class UserService {
    UserDao userDao;
    public UserService(Jdbi jdbi) {
        this.userDao= jdbi.onDemand(UserDao.class);
    }

    public Boolean updateNeedRefresh (Integer userId, Boolean needRefresh ) {
        return userDao.updateNeedRefresh(userId, needRefresh);
    }

    public User getUserById(Integer id) {
        User user = userDao.getUserById(id);
        return user;
    }
}
