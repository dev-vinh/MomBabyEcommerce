package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.RoleDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.contant.ERole;
import hcmuaf.fit.mombabyecommerce.model.Role;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class RoleService {
    private final RoleDao roleDAO;

    public RoleService(Jdbi jdbi) {
        roleDAO = jdbi.onDemand(RoleDao.class);
    }

    public List<Role> getAllRoles() {
        return roleDAO.getRoles();
    }

    public int addRole(Role role ) {
        return roleDAO.addRole(role);
    }



    public static void main(String[] args) {
        RoleService roleService = new RoleService(DBConnection.getJdbi());
        Role  role = new Role(null, ERole.CUSTOM, "test2 ", "", true);
        System.out.println(roleService.getAllRoles());
    }
}
