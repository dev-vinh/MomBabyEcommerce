package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.PermissionDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class PermissionService {
    private final PermissionDao permissionDAO;

    public PermissionService(Jdbi jdbi) {
        this.permissionDAO = jdbi.onDemand(PermissionDao.class);
    }

    public List<Permission> getAllPermissions() {
        return permissionDAO.getAllPermissions();
    }

    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        return permissionDAO.getPermissionsByRoleId(roleId);
    }

    public static void main(String[] args) {
        PermissionService permissionService =
                new PermissionService(DBConnection.getJdbi());

        List<Permission> permissions =
                permissionService.getAllPermissions();

        System.out.println(permissions);
    }
}
