package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Permission;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;
@RegisterConstructorMapper(Permission.class)
public interface PermissionDao {

    @SqlQuery("SELECT id, name, type FROM permissions")
    List<Permission> getAllPermissions();

    @SqlQuery("""
        SELECT p.id, p.name, p.type
        FROM permissions p
        JOIN role_permission rp ON p.id = rp.permissionId
        JOIN roles r ON r.id = rp.roleId
        WHERE r.id = :roleId
    """)
    List<Permission> getPermissionsByRoleId(
            @Bind("roleId") Integer roleId
    );

    @SqlQuery("""
        SELECT * FROM permissions
        WHERE id = :id
    """)
    Permission getPermissionById(
            @Bind("id") Integer id
    );


}