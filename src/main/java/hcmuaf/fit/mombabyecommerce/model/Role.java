package hcmuaf.fit.mombabyecommerce.model;

import hcmuaf.fit.mombabyecommerce.contant.ERole;
import jakarta.annotation.Nullable;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private ERole roleType;
    private String name;
    private String description;
    private Boolean isActive;

    public Role() {
    }

    public Role(Integer id, ERole roleType, String name, String description, Boolean isActive) {
        this.id = id;
        this.roleType = roleType;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
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

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleType=" + roleType +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                "}\n";
    }
}
