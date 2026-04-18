package hcmuaf.fit.mombabyecommerce.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer userId;
    private Integer roleId;

    public UserRole() {
    }

    @JdbiConstructor
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
                '}';
    }
}
