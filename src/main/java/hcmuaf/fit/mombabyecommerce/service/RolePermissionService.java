package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.PermissionDao;
import hcmuaf.fit.mombabyecommerce.Dao.RolePermissionDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.Permission;
import hcmuaf.fit.mombabyecommerce.model.RolePermission;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class RolePermissionService {
    private final RolePermissionDao rolePermissionDAO;

    public RolePermissionService(Jdbi jdbi) {
        this.rolePermissionDAO = jdbi.onDemand(RolePermissionDao.class);
    }


    public void addRolePermission( List<RolePermission> rolePermissions) {
        rolePermissionDAO.addRolePermissions(rolePermissions);
    }


    public static void main(String[] args) {
        RolePermission rolePermission1 = new RolePermission(null, 11, 2);
        RolePermission rolePermission2 = new RolePermission(null, 11, 3);
        RolePermission rolePermission3 = new RolePermission(null, 11, 4);
        RolePermission rolePermission4 = new RolePermission(null, 11, 5);

        RolePermissionService service = new RolePermissionService(DBConnection.getJdbi());
        service.addRolePermission(List.of(rolePermission1, rolePermission2, rolePermission3, rolePermission4));
    }
}
