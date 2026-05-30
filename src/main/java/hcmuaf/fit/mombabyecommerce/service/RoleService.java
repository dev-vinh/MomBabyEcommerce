package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.RoleDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.contant.ERole;
import hcmuaf.fit.mombabyecommerce.model.Role;
import hcmuaf.fit.mombabyecommerce.model.User;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class RoleService {
    private final RoleDao roleDAO;

    public RoleService(Jdbi jdbi) {
        roleDAO = jdbi.onDemand(RoleDao.class);
    }

    public int addRole(Role role ) {
        return roleDAO.addRole(role);
    }
    public List<Role> getAllRoles() {
        return roleDAO.getRoles();
    }
    public List<User> getUsersByRoleId(Integer roleId) {
        return roleDAO.getUsersByRoleId(roleId);
    }
}
