package hcmuaf.fit.mombabyecommerce.model;

import hcmuaf.fit.mombabyecommerce.contant.ERole;
import java.io.Serializable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private ERole roleType;
    private String name;
    private String description;
    private Boolean isActive;
    private Integer memberCount;
    private Integer permissionCount;
    private String permissions;

    public Role() {
    }

    @JdbiConstructor
    public Role(
            @ColumnName("id") Integer id,
            @ColumnName("roleType") ERole roleType,
            @ColumnName("name") String name,
            @ColumnName("description") String description,
            @ColumnName("isActive") Boolean isActive,
            @ColumnName("memberCount") Integer memberCount,
            @ColumnName("permissionCount") Integer permissionCount,
            @ColumnName("permissions") String permissions
    ) {
        this.id = id;
        this.roleType = roleType;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.memberCount = memberCount;
        this.permissionCount = permissionCount;
        this.permissions = permissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ERole getRoleType() {
        return roleType;
    }

    public void setRoleType(ERole roleType) {
        this.roleType = roleType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Integer getPermissionCount() {
        return permissionCount;
    }

    public void setPermissionCount(Integer permissionCount) {
        this.permissionCount = permissionCount;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleType=" + roleType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", memberCount=" + memberCount +
                ", permissionCount=" + permissionCount +
                ", permissions='" + permissions + '\'' +
                '}';
    }
}
