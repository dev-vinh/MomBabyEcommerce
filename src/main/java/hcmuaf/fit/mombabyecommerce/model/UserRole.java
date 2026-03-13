package hcmuaf.fit.mombabyecommerce.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class UserRole {
    Integer id;
    Integer userId;
    Integer roleId;

    public UserRole(
            @ColumnName("id") Integer id,
            @ColumnName("userId") Integer userId,
            @ColumnName("roleId") Integer roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}' +"\n";
    }
}
