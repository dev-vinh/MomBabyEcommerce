package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.RolePermission;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;

import java.util.List;

@RegisterConstructorMapper(RolePermission.class)
public interface RolePermissionDao {
    @SqlBatch("""
    INSERT INTO role_permission (roleId, permissionId)
    VALUES (:roleId, :permissionId)
""")
    void addRolePermissions(@BindBean List<RolePermission> rolePermissions);
}
