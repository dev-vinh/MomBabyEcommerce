package hcmuaf.fit.mombabyecommerce.model;

import hcmuaf.fit.mombabyecommerce.contant.EPermission;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.io.Serializable;

public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private EPermission type;

    public Permission() {
    }

    @JdbiConstructor
    public Permission(
            @ColumnName("id") Integer id,
            @ColumnName("name") String name,
            @ColumnName("type")  EPermission type
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EPermission getType() {
        return type;
    }

    public void setType(EPermission type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                "}\n";
    }
}
