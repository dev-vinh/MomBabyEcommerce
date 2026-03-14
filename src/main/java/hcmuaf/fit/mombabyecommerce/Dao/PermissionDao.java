package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Permission;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;
@RegisterConstructorMapper(Permission.class)
public interface PermissionDao {
    @SqlQuery("SELECT p.id, p.name, p.type " +
            "FROM permission p " +
            "INNER JOIN role_permission rp ON p.id = rp.permissionId " +
            "WHERE rp.roleId = :roleId")
    List<Permission> getPermissionsByRoleId(@Bind("roleId") Integer roleId);
}
