package hcmuaf.fit.mombabyecommerce.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class RolePermission implements Serializable {
    private Integer id;
    private Integer roleId;
    private Integer permissionId;

    @JdbiConstructor
    public RolePermission(
            @ColumnName("id") Integer id,
            @ColumnName("roleId") Integer roleId,
            @ColumnName("permissionId") Integer permissionId

    ) {
        this.id = id;
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public RolePermission() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
}
